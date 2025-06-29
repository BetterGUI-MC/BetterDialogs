package me.hsgamer.bettergui.betterdialogs.constructor;

import com.github.retrooper.packetevents.protocol.dialog.CommonDialogData;
import com.github.retrooper.packetevents.protocol.dialog.DialogAction;
import com.github.retrooper.packetevents.protocol.dialog.body.DialogBody;
import com.github.retrooper.packetevents.protocol.dialog.input.Input;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DialogDataConstructor {
    private Component title;
    private @Nullable Component externalTitle;
    private boolean canCloseWithEscape;
    private boolean pause;
    private DialogAction afterAction;
    private List<DialogBody> body;
    private List<Input> inputs;

    private DialogDataConstructor() {
        // Private constructor to prevent instantiation
    }

    public static DialogDataConstructor create() {
        return new DialogDataConstructor();
    }

    public DialogDataConstructor title(Component title) {
        this.title = title;
        return this;
    }

    public DialogDataConstructor externalTitle(@Nullable Component externalTitle) {
        this.externalTitle = externalTitle;
        return this;
    }

    public DialogDataConstructor canCloseWithEscape(boolean canCloseWithEscape) {
        this.canCloseWithEscape = canCloseWithEscape;
        return this;
    }

    public DialogDataConstructor pause(boolean pause) {
        this.pause = pause;
        return this;
    }

    public DialogDataConstructor afterAction(DialogAction afterAction) {
        this.afterAction = afterAction;
        return this;
    }

    public DialogDataConstructor body(List<DialogBody> body) {
        this.body = body;
        return this;
    }

    public DialogDataConstructor inputs(List<Input> inputs) {
        this.inputs = inputs;
        return this;
    }

    public DialogDataConstructor addBody(DialogBody dialogBody) {
        if (this.body == null) {
            this.body = new ArrayList<>();
        }
        this.body.add(dialogBody);
        return this;
    }

    public DialogDataConstructor addInput(Input input) {
        if (this.inputs == null) {
            this.inputs = new ArrayList<>();
        }
        this.inputs.add(input);
        return this;
    }

    public CommonDialogData construct() {
        return new CommonDialogData(
                title == null ? Component.text("Dialog") : title,
                externalTitle,
                canCloseWithEscape,
                pause,
                afterAction == null ? DialogAction.CLOSE : afterAction,
                body == null ? Collections.emptyList() : body,
                inputs == null ? Collections.emptyList() : inputs
        );
    }
}
