package me.hsgamer.bettergui.betterdialogs.component.action;

import io.github.projectunified.unidialog.core.action.DialogActionBuilder;
import me.hsgamer.bettergui.betterdialogs.builder.DialogComponentBuilder;
import me.hsgamer.bettergui.util.StringReplacerApplier;
import me.hsgamer.hscore.common.MapUtils;
import org.bukkit.entity.Player;

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
    protected void getAction(Player player, DialogActionBuilder<?, ?> builder) {
        builder.openUrl(StringReplacerApplier.replace(url, player.getUniqueId(), this));
    }
}
