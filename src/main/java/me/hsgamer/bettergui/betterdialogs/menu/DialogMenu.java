package me.hsgamer.bettergui.betterdialogs.menu;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.dialog.CommonDialogData;
import com.github.retrooper.packetevents.protocol.dialog.Dialog;
import com.github.retrooper.packetevents.protocol.dialog.DialogAction;
import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerClearDialog;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerShowDialog;
import io.github.retrooper.packetevents.adventure.serializer.legacy.LegacyComponentSerializer;
import me.hsgamer.bettergui.betterdialogs.BetterDialogs;
import me.hsgamer.bettergui.betterdialogs.builder.DialogComponentBuilder;
import me.hsgamer.bettergui.betterdialogs.component.DialogComponent;
import me.hsgamer.bettergui.betterdialogs.constructor.DialogConstructor;
import me.hsgamer.bettergui.betterdialogs.constructor.DialogDataConstructor;
import me.hsgamer.bettergui.menu.BaseMenu;
import me.hsgamer.bettergui.util.StringReplacerApplier;
import me.hsgamer.hscore.collections.map.CaseInsensitiveStringMap;
import me.hsgamer.hscore.common.MapUtils;
import me.hsgamer.hscore.common.StringReplacer;
import me.hsgamer.hscore.config.Config;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class DialogMenu extends BaseMenu {
    private final BetterDialogs instance;
    private final Supplier<DialogConstructor> dialogConstructorSupplier;
    private final Map<String, DialogComponent> componentMap = new LinkedHashMap<>();

    private final String title;
    private final @Nullable String externalTitle;
    private final boolean canCloseWithEscape;
    private final boolean pause;
    private final DialogAction afterAction;

    public DialogMenu(BetterDialogs instance, Config config, Supplier<DialogConstructor> dialogConstructorSupplier) {
        super(config);
        this.instance = instance;
        this.dialogConstructorSupplier = dialogConstructorSupplier;

        title = Optional.ofNullable(menuSettings.get("title"))
                .map(Object::toString)
                .orElse("Dialog Menu");
        externalTitle = Optional.ofNullable(menuSettings.get("external-title"))
                .map(Object::toString)
                .orElse(null);
        canCloseWithEscape = Optional.ofNullable(menuSettings.get("can-close-with-escape"))
                .map(Object::toString)
                .map(Boolean::parseBoolean)
                .orElse(true);
        pause = Optional.ofNullable(menuSettings.get("pause"))
                .map(Object::toString)
                .map(Boolean::parseBoolean)
                .orElse(false);
        afterAction = Optional.ofNullable(menuSettings.get("after-action"))
                .map(Object::toString)
                .map(s -> {
                    try {
                        return DialogAction.valueOf(s.toUpperCase());
                    } catch (Exception e) {
                        return null;
                    }
                })
                .orElse(DialogAction.CLOSE);

        for (Map.Entry<String, Object> configEntry : configSettings.entrySet()) {
            String key = configEntry.getKey();
            MapUtils.castOptionalStringObjectMap(configEntry.getValue())
                    .map(CaseInsensitiveStringMap::new)
                    .map(map -> new DialogComponentBuilder.Input(this, key, map))
                    .flatMap(DialogComponentBuilder.INSTANCE::build)
                    .ifPresent(customFormComponent -> componentMap.put(key, customFormComponent));
        }

        variableManager.register("form_", StringReplacer.of((original, uuid) -> {
            String[] split = original.split(":", 2);
            String component = split[0];
            String key = split.length > 1 ? split[1] : "";
            return Optional.ofNullable(componentMap.get(component))
                    .map(provider -> provider.getValue(uuid, key))
                    .orElse(null);
        }));
    }

    public Dialog createDialog(Player player) {
        DialogDataConstructor dialogDataConstructor = DialogDataConstructor.create();
        DialogConstructor dialogConstructor = dialogConstructorSupplier.get();

        dialogDataConstructor
                .title(LegacyComponentSerializer.legacySection().deserialize(StringReplacerApplier.replace(title, player.getUniqueId(), this)))
                .externalTitle(externalTitle != null ? LegacyComponentSerializer.legacySection().deserialize(StringReplacerApplier.replace(externalTitle, player.getUniqueId(), this)) : null)
                .canCloseWithEscape(canCloseWithEscape)
                .pause(pause)
                .afterAction(afterAction);

        for (DialogComponent component : componentMap.values()) {
            component.apply(player, dialogDataConstructor, dialogConstructor);
        }

        CommonDialogData dialogData = dialogDataConstructor.construct();
        return dialogConstructor.construct(dialogData);
    }

    public ResourceLocation registerAction(String actionName, BiConsumer<Player, NBT> action) {
        return instance.dialogCustomClickListener().registerAction(getName() + "_" + actionName, action);
    }

    @Override
    protected boolean createChecked(Player player, String[] args, boolean bypass) {
        Dialog dialog = createDialog(player);
        WrapperPlayServerShowDialog packet = new WrapperPlayServerShowDialog(dialog);
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, packet);
        return true;
    }

    @Override
    public void update(Player player) {
        // EMPTY
    }

    @Override
    public void close(Player player) {
        WrapperPlayServerClearDialog packet = new WrapperPlayServerClearDialog();
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, packet);
    }

    @Override
    public void closeAll() {
        // EMPTY
    }
}
