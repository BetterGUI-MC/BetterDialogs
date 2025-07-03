package me.hsgamer.bettergui.betterdialogs.component.action;

import io.github.projectunified.unidialog.packetevents.action.PEDialogActionBuilder;
import me.hsgamer.bettergui.betterdialogs.builder.DialogComponentBuilder;
import me.hsgamer.bettergui.util.StringReplacerApplier;
import org.bukkit.entity.Player;

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
    protected void getAction(Player player, PEDialogActionBuilder builder) {
        String replacedCommand = StringReplacerApplier.replace(command, player.getUniqueId(), this);
        if (isDynamic) {
            builder.dynamicRunCommand(replacedCommand);
        } else {
            builder.runCommand(replacedCommand);
        }
    }
}
