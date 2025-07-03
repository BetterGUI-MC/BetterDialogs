package me.hsgamer.bettergui.betterdialogs.component.action;

import io.github.projectunified.unidialog.packetevents.action.PEDialogActionBuilder;
import me.hsgamer.bettergui.betterdialogs.builder.DialogComponentBuilder;
import me.hsgamer.bettergui.util.StringReplacerApplier;
import me.hsgamer.hscore.common.MapUtils;
import org.bukkit.entity.Player;

import java.util.Optional;

public class SuggestCommandActionComponent extends ActionComponent {
    private final String command;

    public SuggestCommandActionComponent(DialogComponentBuilder.Input input) {
        super(input);
        this.command = Optional.ofNullable(MapUtils.getIfFound(input.options(), "command", "suggest"))
                .map(Object::toString)
                .orElse("");
    }

    @Override
    protected void getAction(Player player, PEDialogActionBuilder builder) {
        builder.suggestCommand(StringReplacerApplier.replace(command, player.getUniqueId(), this));
    }
}
