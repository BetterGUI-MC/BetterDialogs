package me.hsgamer.bettergui.betterdialogs.constructor;

import com.github.retrooper.packetevents.protocol.dialog.CommonDialogData;
import com.github.retrooper.packetevents.protocol.dialog.ConfirmationDialog;
import com.github.retrooper.packetevents.protocol.dialog.Dialog;
import com.github.retrooper.packetevents.protocol.dialog.button.ActionButton;
import com.github.retrooper.packetevents.protocol.dialog.button.CommonButtonData;
import net.kyori.adventure.text.Component;

public class ConfirmationDialogConstructor implements DialogConstructor {
    private static final ActionButton DEFAULT_YES_BUTTON = new ActionButton(
            new CommonButtonData(
                    Component.text("Yes"),
                    null,
                    150
            ),
            null
    );
    private static final ActionButton DEFAULT_NO_BUTTON = new ActionButton(
            new CommonButtonData(
                    Component.text("No"),
                    null,
                    151
            ),
            null
    );

    private ActionButton yesButton;
    private ActionButton noButton;

    private ConfirmationDialogConstructor() {
        // Private constructor to prevent instantiation
    }

    public static ConfirmationDialogConstructor create() {
        return new ConfirmationDialogConstructor();
    }

    public ConfirmationDialogConstructor yesButton(ActionButton yesButton) {
        this.yesButton = yesButton;
        return this;
    }

    public ConfirmationDialogConstructor noButton(ActionButton noButton) {
        this.noButton = noButton;
        return this;
    }

    public ConfirmationDialogConstructor button(ActionButton button) {
        if (yesButton == null) {
            yesButton = button;
        } else if (noButton == null) {
            noButton = button;
        }
        return this;
    }

    @Override
    public Dialog construct(CommonDialogData data) {
        return new ConfirmationDialog(
                data,
                yesButton != null ? yesButton : DEFAULT_YES_BUTTON,
                noButton != null ? noButton : DEFAULT_NO_BUTTON
        );
    }
}
