package me.hsgamer.bettergui.betterdialogs.component.action;

import com.github.retrooper.packetevents.protocol.chat.clickevent.SuggestCommandClickEvent;
import com.github.retrooper.packetevents.protocol.dialog.action.Action;
import com.github.retrooper.packetevents.protocol.dialog.action.StaticAction;
import me.hsgamer.bettergui.betterdialogs.builder.DialogComponentBuilder;
import me.hsgamer.bettergui.util.StringReplacerApplier;
import me.hsgamer.hscore.common.MapUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

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
    protected @Nullable Action getAction(Player player) {
        return new StaticAction(new SuggestCommandClickEvent(StringReplacerApplier.replace(command, player.getUniqueId(), this)));
    }
}
