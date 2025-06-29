package me.hsgamer.bettergui.betterdialogs.component.button;

import com.github.retrooper.packetevents.protocol.dialog.button.ActionButton;
import me.hsgamer.bettergui.betterdialogs.builder.DialogComponentBuilder;
import me.hsgamer.bettergui.betterdialogs.constructor.ConfirmationDialogConstructor;
import me.hsgamer.bettergui.betterdialogs.constructor.DialogConstructor;
import org.bukkit.entity.Player;

public class ConfirmationButtonComponent extends ButtonComponent {
    private final boolean yes;

    public ConfirmationButtonComponent(DialogComponentBuilder.Input input, boolean yes) {
        super(input);
        this.yes = yes;
    }

    @Override
    protected void apply(Player player, ActionButton button, DialogConstructor dialogConstructor) {
        if (!(dialogConstructor instanceof ConfirmationDialogConstructor confirmationDialogConstructor)) return;
        if (yes) {
            confirmationDialogConstructor.yesButton(button);
        } else {
            confirmationDialogConstructor.noButton(button);
        }
    }
}
