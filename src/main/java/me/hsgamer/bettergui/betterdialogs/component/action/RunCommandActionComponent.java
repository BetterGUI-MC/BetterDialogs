package me.hsgamer.bettergui.betterdialogs.component.action;

import com.github.retrooper.packetevents.protocol.chat.clickevent.RunCommandClickEvent;
import com.github.retrooper.packetevents.protocol.dialog.action.Action;
import com.github.retrooper.packetevents.protocol.dialog.action.DialogTemplate;
import com.github.retrooper.packetevents.protocol.dialog.action.DynamicRunCommandAction;
import com.github.retrooper.packetevents.protocol.dialog.action.StaticAction;
import me.hsgamer.bettergui.betterdialogs.builder.DialogComponentBuilder;
import me.hsgamer.bettergui.util.StringReplacerApplier;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class RunCommandActionComponent extends ActionComponent {
    private final String command;
    private final boolean isDynamic;

    public RunCommandActionComponent(DialogComponentBuilder.Input input) {
        super(input);
        if (input.options().containsKey("command")) {
            command = Objects.toString(input.options().get("command"));
            isDynamic = false;
        } else if (input.options().containsKey("template")) {
            command = Objects.toString(input.options().get("template"));
            isDynamic = true;
        } else {
            command = "";
            isDynamic = false;
        }
    }

    @Override
    protected @Nullable Action getAction(Player player) {
        String replacedCommand = StringReplacerApplier.replace(command, player.getUniqueId(), this);
        return isDynamic
                ? new DynamicRunCommandAction(new DialogTemplate(replacedCommand))
                : new StaticAction(new RunCommandClickEvent(replacedCommand));
    }
}
