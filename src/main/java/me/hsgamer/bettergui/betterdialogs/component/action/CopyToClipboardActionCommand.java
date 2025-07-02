package me.hsgamer.bettergui.betterdialogs.component.action;

import com.github.retrooper.packetevents.protocol.chat.clickevent.CopyToClipboardClickEvent;
import com.github.retrooper.packetevents.protocol.dialog.action.Action;
import com.github.retrooper.packetevents.protocol.dialog.action.StaticAction;
import me.hsgamer.bettergui.betterdialogs.builder.DialogComponentBuilder;
import me.hsgamer.bettergui.util.StringReplacerApplier;
import me.hsgamer.hscore.common.MapUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class CopyToClipboardActionCommand extends ActionComponent {
    private final String value;

    public CopyToClipboardActionCommand(DialogComponentBuilder.Input input) {
        super(input);
        this.value = Optional.ofNullable(MapUtils.getIfFound(input.options(), "value", "text"))
                .map(Object::toString)
                .orElse("");
    }

    @Override
    protected @Nullable Action getAction(Player player) {
        return new StaticAction(new CopyToClipboardClickEvent(StringReplacerApplier.replace(value, player.getUniqueId(), this)));
    }
}
