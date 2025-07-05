package me.hsgamer.bettergui.betterdialogs.component.body;

import io.github.projectunified.unidialog.core.body.DialogBodyBuilder;
import io.github.projectunified.unidialog.core.body.TextBody;
import me.hsgamer.bettergui.betterdialogs.builder.DialogComponentBuilder;
import me.hsgamer.bettergui.util.StringReplacerApplier;
import me.hsgamer.hscore.common.MapUtils;
import me.hsgamer.hscore.common.Validate;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public class TextComponent extends DialogBodyComponent {
    private final String text;
    private final int width;

    public TextComponent(DialogComponentBuilder.Input input) {
        super(input);

        text = Optional.ofNullable(MapUtils.getIfFound(input.options(), "text", "message"))
                .map(Object::toString)
                .orElse("Text");
        width = Optional.ofNullable(input.options().get("width"))
                .map(Object::toString)
                .flatMap(Validate::getNumber)
                .map(Number::intValue)
                .filter(width -> width > 0)
                .orElse(150);
    }

    @Override
    protected void apply(Player player, DialogBodyBuilder<?> builder) {
        getBodyConsumer(player.getUniqueId()).accept(builder.text());
    }

    public Consumer<TextBody<?>> getBodyConsumer(UUID uuid) {
        return body -> body.text(StringReplacerApplier.replace(text, uuid, this)).width(width);
    }
}
