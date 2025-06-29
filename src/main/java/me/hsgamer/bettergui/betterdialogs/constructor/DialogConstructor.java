package me.hsgamer.bettergui.betterdialogs.constructor;

import com.github.retrooper.packetevents.protocol.dialog.CommonDialogData;
import com.github.retrooper.packetevents.protocol.dialog.Dialog;

public interface DialogConstructor {
    Dialog construct(CommonDialogData data);
}
