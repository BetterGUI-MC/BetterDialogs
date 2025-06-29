package me.hsgamer.bettergui.betterdialogs.component.button;

import com.github.retrooper.packetevents.protocol.dialog.action.Action;
import com.github.retrooper.packetevents.protocol.dialog.button.ActionButton;
import com.github.retrooper.packetevents.protocol.dialog.button.CommonButtonData;
import io.github.retrooper.packetevents.adventure.serializer.legacy.LegacyComponentSerializer;
import me.hsgamer.bettergui.betterdialogs.builder.ActionComponentBuilder;
import me.hsgamer.bettergui.betterdialogs.builder.DialogComponentBuilder;
import me.hsgamer.bettergui.betterdialogs.component.DialogComponent;
import me.hsgamer.bettergui.betterdialogs.component.action.ActionComponent;
import me.hsgamer.bettergui.betterdialogs.constructor.ButtonDataConstructor;
import me.hsgamer.bettergui.betterdialogs.constructor.DialogConstructor;
import me.hsgamer.bettergui.betterdialogs.constructor.DialogDataConstructor;
import me.hsgamer.bettergui.util.StringReplacerApplier;
import me.hsgamer.hscore.common.Validate;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public abstract class ButtonComponent extends DialogComponent {
    private final String label;
    private final @Nullable String tooltip;
    private final int width;
    private final @Nullable ActionComponent actionComponent;

    protected ButtonComponent(DialogComponentBuilder.Input input) {
        super(input);
        label = Optional.ofNullable(input.options().get("label"))
                .map(Object::toString)
                .orElse("Action");
        tooltip = Optional.ofNullable(input.options().get("tooltip"))
                .map(Object::toString)
                .orElse(null);
        width = Optional.ofNullable(input.options().get("width"))
                .map(Object::toString)
                .flatMap(Validate::getNumber)
                .map(Number::intValue)
                .filter(width -> width > 0)
                .orElse(150);
        actionComponent = ActionComponentBuilder.INSTANCE
                .build(new ActionComponentBuilder.Input(this, input.options()))
                .orElse(null);
    }

    protected abstract void apply(Player player, ActionButton button, DialogConstructor dialogConstructor);

    @Override
    public void apply(Player player, DialogDataConstructor dialogDataConstructor, DialogConstructor dialogConstructor) {
        CommonButtonData buttonData = ButtonDataConstructor.create()
                .label(LegacyComponentSerializer.legacySection().deserialize(StringReplacerApplier.replace(label, player.getUniqueId(), this)))
                .tooltip(tooltip == null ? null : LegacyComponentSerializer.legacySection().deserialize(StringReplacerApplier.replace(tooltip, player.getUniqueId(), this)))
                .width(width)
                .construct();
        Action action = actionComponent == null ? null : actionComponent.getAction(player);
        ActionButton button = new ActionButton(buttonData, action);
        apply(player, button, dialogConstructor);
    }
}
