package me.hsgamer.bettergui.betterdialogs.component.action;

import com.github.retrooper.packetevents.protocol.chat.clickevent.OpenUrlClickEvent;
import com.github.retrooper.packetevents.protocol.dialog.action.Action;
import com.github.retrooper.packetevents.protocol.dialog.action.StaticAction;
import me.hsgamer.bettergui.betterdialogs.builder.DialogComponentBuilder;
import me.hsgamer.bettergui.util.StringReplacerApplier;
import me.hsgamer.hscore.common.MapUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class OpenUrlActionComponent extends ActionComponent {
    private final String url;

    public OpenUrlActionComponent(DialogComponentBuilder.Input input) {
        super(input);
        this.url = Optional.ofNullable(MapUtils.getIfFound(input.options(), "url", "link"))
                .map(Object::toString)
                .orElse(null);
    }

    @Override
    protected @Nullable Action getAction(Player player) {
        return url == null
                ? null
                : new StaticAction(new OpenUrlClickEvent(StringReplacerApplier.replace(url, player.getUniqueId(), this)));
    }
}
