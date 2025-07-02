package me.hsgamer.bettergui.betterdialogs.component.action;

import com.github.retrooper.packetevents.protocol.dialog.action.Action;
import com.github.retrooper.packetevents.protocol.dialog.button.ActionButton;
import com.github.retrooper.packetevents.protocol.dialog.button.CommonButtonData;
import me.hsgamer.bettergui.betterdialogs.builder.DialogComponentBuilder;
import me.hsgamer.bettergui.betterdialogs.component.DialogComponent;
import me.hsgamer.bettergui.betterdialogs.constructor.*;
import me.hsgamer.bettergui.betterdialogs.util.ComponentUtils;
import me.hsgamer.bettergui.util.StringReplacerApplier;
import me.hsgamer.hscore.common.Validate;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public abstract class ActionComponent extends DialogComponent {
    private final String label;
    private final @Nullable String tooltip;
    private final int width;
    private final @Nullable String assign;

    protected ActionComponent(DialogComponentBuilder.Input input) {
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
        assign = Optional.ofNullable(input.options().get("assign"))
                .map(Object::toString)
                .orElse(null);
    }

    protected abstract @Nullable Action getAction(Player player);

    private void apply(ActionButton button, DialogConstructor dialogConstructor) {
        switch (dialogConstructor) {
            case ConfirmationDialogConstructor confirmationDialogConstructor -> {
                if (assign == null) {
                    confirmationDialogConstructor.button(button);
                } else if (assign.equalsIgnoreCase("yes")) {
                    confirmationDialogConstructor.yesButton(button);
                } else if (assign.equalsIgnoreCase("no")) {
                    confirmationDialogConstructor.noButton(button);
                }
            }
            case MultiActionDialogConstructor multiActionDialogConstructor -> {
                if (assign == null || !assign.equalsIgnoreCase("exit")) {
                    multiActionDialogConstructor.button(button);
                } else {
                    multiActionDialogConstructor.exitButton(button);
                }
            }
            case null, default -> {
            }
        }
    }

    @Override
    public void apply(Player player, DialogDataConstructor dialogDataConstructor, DialogConstructor dialogConstructor) {
        CommonButtonData buttonData = ButtonDataConstructor.create()
                .label(ComponentUtils.convertLegacy(StringReplacerApplier.replace(label, player.getUniqueId(), this)))
                .tooltip(tooltip == null ? null : ComponentUtils.convertLegacy(StringReplacerApplier.replace(tooltip, player.getUniqueId(), this)))
                .width(width)
                .construct();
        Action action = getAction(player);
        ActionButton button = new ActionButton(buttonData, action);
        apply(button, dialogConstructor);
    }
}
