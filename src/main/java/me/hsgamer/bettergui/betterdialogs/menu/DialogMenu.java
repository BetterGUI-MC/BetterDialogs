package me.hsgamer.bettergui.betterdialogs.menu;

import io.github.projectunified.unidialog.core.dialog.Dialog;
import io.github.projectunified.unidialog.packetevents.dialog.PEDialog;
import me.hsgamer.bettergui.betterdialogs.BetterDialogs;
import me.hsgamer.bettergui.betterdialogs.builder.DialogComponentBuilder;
import me.hsgamer.bettergui.betterdialogs.component.DialogComponent;
import me.hsgamer.bettergui.betterdialogs.component.input.InputComponent;
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
import java.util.UUID;
import java.util.function.Consumer;

public abstract class DialogMenu extends BaseMenu {
    protected final BetterDialogs instance;
    private final Map<String, DialogComponent> componentMap = new LinkedHashMap<>();

    private final String title;
    private final @Nullable String externalTitle;
    private final boolean canCloseWithEscape;
    private final boolean pause;
    private final Dialog.AfterAction afterAction;

    DialogMenu(BetterDialogs instance, Config config) {
        super(config);
        this.instance = instance;

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
                        return Dialog.AfterAction.valueOf(s.toUpperCase());
                    } catch (Exception e) {
                        return null;
                    }
                })
                .orElse(Dialog.AfterAction.CLOSE);

        for (Map.Entry<String, Object> configEntry : configSettings.entrySet()) {
            String key = configEntry.getKey();
            MapUtils.castOptionalStringObjectMap(configEntry.getValue())
                    .map(CaseInsensitiveStringMap::new)
                    .map(map -> new DialogComponentBuilder.Input(this, key, map))
                    .flatMap(DialogComponentBuilder.INSTANCE::build)
                    .ifPresent(customFormComponent -> componentMap.put(key, customFormComponent));
        }

        variableManager.register("dialog_", StringReplacer.of((original, uuid) -> {
            String[] split = original.split(":", 2);
            String component = split[0];
            String key = split.length > 1 ? split[1] : "";
            return Optional.ofNullable(componentMap.get(component))
                    .filter(InputComponent.class::isInstance)
                    .map(InputComponent.class::cast)
                    .map(provider -> provider.getValue(uuid, key))
                    .orElse(null);
        }));
    }

    protected abstract PEDialog<?> createDialogConstructor(Player player);

    public PEDialog<?> createDialog(Player player) {
        PEDialog<?> dialog = createDialogConstructor(player);

        dialog
                .title(StringReplacerApplier.replace(title, player.getUniqueId(), this))
                .externalTitle(externalTitle != null ? StringReplacerApplier.replace(externalTitle, player.getUniqueId(), this) : null)
                .canCloseWithEscape(canCloseWithEscape)
                .pause(pause)
                .afterAction(afterAction);

        for (DialogComponent component : componentMap.values()) {
            component.apply(player, dialog);
        }

        return dialog;
    }

    private void applyInputs(UUID uuid, Map<String, String> map) {
        for (DialogComponent component : componentMap.values()) {
            if (component instanceof InputComponent<?> inputComponent) {
                inputComponent.applyValue(uuid, map);
            }
        }
    }

    public String registerAction(String actionName, Consumer<UUID> action) {
        String id = getName() + "_" + actionName;
        instance.dialogManager().registerCustomAction(id, (uuid, map) -> {
            applyInputs(uuid, map);
            action.accept(uuid);
        });
        return id;
    }

    @Override
    protected boolean createChecked(Player player, String[] args, boolean bypass) {
        PEDialog<?> dialog = createDialog(player);
        dialog.opener().open(player.getUniqueId());
        return true;
    }

    @Override
    public void update(Player player) {
        // EMPTY
    }

    @Override
    public void close(Player player) {
        instance.dialogManager().clearDialog(player.getUniqueId());
    }

    @Override
    public void closeAll() {
        // EMPTY
    }
}
