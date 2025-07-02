package me.hsgamer.bettergui.betterdialogs.constructor;

import com.github.retrooper.packetevents.protocol.dialog.CommonDialogData;
import com.github.retrooper.packetevents.protocol.dialog.Dialog;
import com.github.retrooper.packetevents.protocol.dialog.ServerLinksDialog;
import com.github.retrooper.packetevents.protocol.dialog.button.ActionButton;
import org.jetbrains.annotations.Nullable;

public class ServerLinksDialogConstructor implements DialogConstructor {
    private @Nullable ActionButton exitButton;
    private int columns;
    private int buttonWidth;

    private ServerLinksDialogConstructor() {
        // Private constructor to prevent instantiation
    }

    public static ServerLinksDialogConstructor create() {
        return new ServerLinksDialogConstructor();
    }

    public ServerLinksDialogConstructor exitButton(@Nullable ActionButton exitButton) {
        this.exitButton = exitButton;
        return this;
    }

    public ServerLinksDialogConstructor columns(int columns) {
        this.columns = columns;
        return this;
    }

    public ServerLinksDialogConstructor buttonWidth(int buttonWidth) {
        this.buttonWidth = buttonWidth;
        return this;
    }

    @Override
    public Dialog construct(CommonDialogData data) {
        return new ServerLinksDialog(
                data,
                exitButton,
                columns > 0 ? columns : 2,
                buttonWidth > 0 ? buttonWidth : 150
        );
    }
}
