package me.hsgamer.bettergui.betterdialogs.constructor;

import com.github.retrooper.packetevents.protocol.dialog.CommonDialogData;
import com.github.retrooper.packetevents.protocol.dialog.Dialog;
import com.github.retrooper.packetevents.protocol.dialog.NoticeDialog;
import com.github.retrooper.packetevents.protocol.dialog.button.ActionButton;

public class NoticeDialogConstructor implements DialogConstructor {
    private ActionButton button;

    private NoticeDialogConstructor() {
        // Private constructor to prevent instantiation
    }

    public static NoticeDialogConstructor create() {
        return new NoticeDialogConstructor();
    }

    public NoticeDialogConstructor button(ActionButton button) {
        this.button = button;
        return this;
    }

    @Override
    public Dialog construct(CommonDialogData data) {
        return new NoticeDialog(
                data,
                button != null ? button : NoticeDialog.DEFAULT_ACTION
        );
    }
}
