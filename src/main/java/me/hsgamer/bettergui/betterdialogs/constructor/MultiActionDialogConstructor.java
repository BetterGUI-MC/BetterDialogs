package me.hsgamer.bettergui.betterdialogs.constructor;

import com.github.retrooper.packetevents.protocol.dialog.CommonDialogData;
import com.github.retrooper.packetevents.protocol.dialog.Dialog;
import com.github.retrooper.packetevents.protocol.dialog.MultiActionDialog;
import com.github.retrooper.packetevents.protocol.dialog.button.ActionButton;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MultiActionDialogConstructor implements DialogConstructor {
    private List<ActionButton> actionButtons;
    private @Nullable ActionButton exitButton;
    private int columns;

    private MultiActionDialogConstructor() {
        // Private constructor to prevent instantiation
    }

    public static MultiActionDialogConstructor create() {
        return new MultiActionDialogConstructor();
    }

    public MultiActionDialogConstructor button(ActionButton button) {
        if (actionButtons == null) {
            actionButtons = new ArrayList<>();
        }
        actionButtons.add(button);
        return this;
    }

    public MultiActionDialogConstructor exitButton(@Nullable ActionButton exitButton) {
        this.exitButton = exitButton;
        return this;
    }

    public MultiActionDialogConstructor columns(int columns) {
        this.columns = columns;
        return this;
    }

    @Override
    public Dialog construct(CommonDialogData data) {
        return new MultiActionDialog(
                data,
                actionButtons != null ? actionButtons : List.of(),
                exitButton,
                columns > 0 ? columns : 2
        );
    }
}
