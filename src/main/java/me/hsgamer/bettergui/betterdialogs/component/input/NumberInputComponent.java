package me.hsgamer.bettergui.betterdialogs.component.input;

import com.github.retrooper.packetevents.protocol.dialog.input.InputControl;
import com.github.retrooper.packetevents.protocol.dialog.input.NumberRangeInputControl;
import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTNumber;
import com.github.retrooper.packetevents.protocol.nbt.NBTString;
import me.hsgamer.bettergui.betterdialogs.builder.DialogComponentBuilder;
import me.hsgamer.bettergui.betterdialogs.util.ComponentUtils;
import me.hsgamer.bettergui.util.StringReplacerApplier;
import me.hsgamer.hscore.common.Validate;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.Optional;

public class NumberInputComponent extends InputComponent<Number> {
    private final int width;
    private final String label;
    private final String labelFormat;
    private final String start;
    private final String end;
    private final String initial;
    private final String step;

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
        initial = Optional.ofNullable(input.options().get("initial"))
                .map(Object::toString)
                .orElse("0");
        step = Optional.ofNullable(input.options().get("step"))
                .map(Object::toString)
                .orElse("1");
    }

    @Override
    protected InputControl createControl(Player player) {
        return new NumberRangeInputControl(
                width,
                ComponentUtils.convertLegacy(StringReplacerApplier.replace(label, player.getUniqueId(), this)),
                labelFormat,
                new NumberRangeInputControl.RangeInfo(
                        Validate.getNumber(StringReplacerApplier.replace(this.start, player.getUniqueId(), this))
                                .map(Number::floatValue)
                                .orElse(0F),
                        Validate.getNumber(StringReplacerApplier.replace(this.end, player.getUniqueId(), this))
                                .map(Number::floatValue)
                                .orElse(100F),
                        Validate.getNumber(StringReplacerApplier.replace(this.initial, player.getUniqueId(), this))
                                .map(Number::floatValue)
                                .orElse(0F),
                        Validate.getNumber(StringReplacerApplier.replace(this.step, player.getUniqueId(), this))
                                .map(Number::floatValue)
                                .orElse(1F)
                )
        );
    }

    @Override
    protected String getValue(Number value, String args) {
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
    protected Number getValue(NBT nbt) {
        return switch (nbt) {
            case NBTNumber nbtNumber -> nbtNumber.getAsNumber();
            case NBTString nbtString -> Validate.getNumber(nbtString.getValue()).orElse(null);
            case null, default -> null;
        };
    }
}
