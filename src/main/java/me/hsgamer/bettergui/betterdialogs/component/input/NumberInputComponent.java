package me.hsgamer.bettergui.betterdialogs.component.input;

import io.github.projectunified.unidialog.packetevents.input.PEDialogInputBuilder;
import me.hsgamer.bettergui.betterdialogs.builder.DialogComponentBuilder;
import me.hsgamer.bettergui.util.StringReplacerApplier;
import me.hsgamer.hscore.common.MapUtils;
import me.hsgamer.hscore.common.Validate;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.util.Optional;
import java.util.UUID;

public class NumberInputComponent extends InputComponent<Number> {
    private final int width;
    private final String label;
    private final String labelFormat;
    private final String start;
    private final String end;
    private final @Nullable String initial;
    private final @Nullable String step;

    public NumberInputComponent(DialogComponentBuilder.Input input) {
        super(input);
        width = Optional.ofNullable(input.options().get("width"))
                .map(Object::toString)
                .flatMap(Validate::getNumber)
                .map(Number::intValue)
                .filter(width -> width > 0)
                .orElse(200);
        label = Optional.ofNullable(input.options().get("label"))
                .map(Object::toString)
                .orElse("");
        labelFormat = Optional.ofNullable(input.options().get("label-format"))
                .map(Object::toString)
                .orElse("options.generic_value");
        start = Optional.ofNullable(input.options().get("start"))
                .map(Object::toString)
                .orElse("0");
        end = Optional.ofNullable(input.options().get("end"))
                .map(Object::toString)
                .orElse("100");
        initial = Optional.ofNullable(MapUtils.getIfFound(input.options(), "default", "initial"))
                .map(Object::toString)
                .orElse(null);
        step = Optional.ofNullable(input.options().get("step"))
                .map(Object::toString)
                .orElse(null);
    }

    @Override
    protected void apply(Player player, PEDialogInputBuilder builder) {
        builder.numberRangeInput()
                .width(width)
                .label(StringReplacerApplier.replace(label, player.getUniqueId(), this))
                .labelFormat(labelFormat)
                .start(Validate.getNumber(StringReplacerApplier.replace(this.start, player.getUniqueId(), this))
                        .map(Number::floatValue)
                        .orElse(0F))
                .end(Validate.getNumber(StringReplacerApplier.replace(this.end, player.getUniqueId(), this))
                        .map(Number::floatValue)
                        .orElse(100F))
                .initial(Optional.ofNullable(initial)
                        .map(s -> StringReplacerApplier.replace(s, player.getUniqueId(), this))
                        .flatMap(Validate::getNumber)
                        .map(Number::floatValue)
                        .orElse(null))
                .step(Optional.ofNullable(step)
                        .map(s -> StringReplacerApplier.replace(s, player.getUniqueId(), this))
                        .flatMap(Validate::getNumber)
                        .map(Number::floatValue)
                        .orElse(null));
    }

    @Override
    protected String getValue(Number value, UUID uuid, String args) {
        if (args.isEmpty()) {
            return String.valueOf(value);
        } else {
            try {
                DecimalFormat decimalFormat = new DecimalFormat(args);
                return decimalFormat.format(value);
            } catch (Exception e) {
                return "INVALID_FORMAT";
            }
        }
    }

    @Override
    protected Number convertValue(UUID uuid, String rawValue) {
        return Validate.getNumber(rawValue).orElse(null);
    }
}
