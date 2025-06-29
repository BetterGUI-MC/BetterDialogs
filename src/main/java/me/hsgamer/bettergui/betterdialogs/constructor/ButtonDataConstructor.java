package me.hsgamer.bettergui.betterdialogs.constructor;

import com.github.retrooper.packetevents.protocol.dialog.button.CommonButtonData;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

public class ButtonDataConstructor {
    private Component label;
    private @Nullable Component tooltip;
    private int width;

    private ButtonDataConstructor() {
        // Private constructor to prevent instantiation
    }

    public static ButtonDataConstructor create() {
        return new ButtonDataConstructor();
    }

    public ButtonDataConstructor label(Component label) {
        this.label = label;
        return this;
    }

    public ButtonDataConstructor tooltip(@Nullable Component tooltip) {
        this.tooltip = tooltip;
        return this;
    }

    public ButtonDataConstructor width(int width) {
        this.width = width;
        return this;
    }

    public CommonButtonData construct() {
        return new CommonButtonData(
                label != null ? label : Component.text("Button"),
                tooltip,
                width > 0 ? width : 150
        );
    }
}
