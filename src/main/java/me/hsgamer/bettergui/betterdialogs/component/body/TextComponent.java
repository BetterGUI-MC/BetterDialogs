package me.hsgamer.bettergui.betterdialogs.component.body;

import com.github.retrooper.packetevents.protocol.dialog.body.PlainMessage;
import com.github.retrooper.packetevents.protocol.dialog.body.PlainMessageDialogBody;
import io.github.retrooper.packetevents.adventure.serializer.legacy.LegacyComponentSerializer;
import me.hsgamer.bettergui.betterdialogs.builder.DialogComponentBuilder;
import me.hsgamer.bettergui.util.StringReplacerApplier;
import me.hsgamer.hscore.common.MapUtils;
import me.hsgamer.hscore.common.Validate;
import org.bukkit.entity.Player;

import java.util.Optional;

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

    public PlainMessage createMessage(Player player) {
        return new PlainMessage(
                LegacyComponentSerializer.legacySection().deserialize(StringReplacerApplier.replace(text, player.getUniqueId(), this)),
                width
        );
    }

    @Override
    public PlainMessageDialogBody create(Player player) {
        return new PlainMessageDialogBody(createMessage(player));
    }
}
