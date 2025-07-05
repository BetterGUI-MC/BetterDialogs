package me.hsgamer.bettergui.betterdialogs.component.action;

import io.github.projectunified.unidialog.core.action.DialogActionBuilder;
import me.hsgamer.bettergui.betterdialogs.builder.DialogComponentBuilder;
import me.hsgamer.bettergui.util.StringReplacerApplier;
import me.hsgamer.hscore.common.MapUtils;
import org.bukkit.entity.Player;

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
    protected void getAction(Player player, DialogActionBuilder<?, ?> builder) {
        builder.copyToClipboard(StringReplacerApplier.replace(value, player.getUniqueId(), this));
    }
}
