package me.hsgamer.bettergui.betterdialogs.component.action;

import com.github.retrooper.packetevents.protocol.dialog.action.Action;
import com.github.retrooper.packetevents.protocol.dialog.action.DialogTemplate;
import com.github.retrooper.packetevents.protocol.dialog.action.DynamicRunCommandAction;
import me.hsgamer.bettergui.betterdialogs.builder.DialogComponentBuilder;
import me.hsgamer.hscore.common.MapUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class RunCommandActionComponent extends ActionComponent {
    private final String template;

    public RunCommandActionComponent(DialogComponentBuilder.Input input) {
        super(input);
        template = Optional.ofNullable(MapUtils.getIfFound(input.options(), "command", "template"))
                .map(Object::toString)
                .orElse("");
    }

    @Override
    protected @Nullable Action getAction(Player player) {
        return new DynamicRunCommandAction(new DialogTemplate(template));
    }
}
