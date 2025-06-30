package me.hsgamer.bettergui.betterdialogs.component.input;

import com.github.retrooper.packetevents.protocol.dialog.input.BooleanInputControl;
import com.github.retrooper.packetevents.protocol.dialog.input.InputControl;
import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTByte;
import com.github.retrooper.packetevents.protocol.nbt.NBTString;
import me.hsgamer.bettergui.betterdialogs.builder.DialogComponentBuilder;
import me.hsgamer.bettergui.betterdialogs.util.ComponentUtils;
import me.hsgamer.bettergui.util.StringReplacerApplier;
import me.hsgamer.hscore.common.MapUtils;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;

public class BooleanInputComponent extends InputComponent<String> {
    private final String label;
    private final String initial;
    private final String onTrue;
    private final String onFalse;

    public BooleanInputComponent(DialogComponentBuilder.Input input) {
        super(input);

        label = Optional.ofNullable(input.options().get("label"))
                .map(Object::toString)
                .orElse("Boolean Input");
        initial = Optional.ofNullable(MapUtils.getIfFound(input.options(), "default", "initial"))
                .map(Object::toString)
                .orElse("");
        onTrue = Optional.ofNullable(input.options().get("on-true"))
                .map(Object::toString)
                .orElse("true");
        onFalse = Optional.ofNullable(input.options().get("on-false"))
                .map(Object::toString)
                .orElse("false");
    }

    @Override
    protected InputControl createControl(Player player) {
        return new BooleanInputControl(
                ComponentUtils.convertLegacy(StringReplacerApplier.replace(label, player.getUniqueId(), this)),
                Boolean.parseBoolean(StringReplacerApplier.replace(initial, player.getUniqueId(), this)),
                StringReplacerApplier.replace(onTrue, player.getUniqueId(), this),
                StringReplacerApplier.replace(onFalse, player.getUniqueId(), this)
        );
    }

    @Override
    protected String getValue(String value, UUID uuid, String args) {
        return value;
    }

    @Override
    protected String getValue(UUID uuid, NBT nbt) {
        return switch (nbt) {
            case NBTString nbtString -> nbtString.getValue();
            case NBTByte nbtByte -> StringReplacerApplier.replace(nbtByte.getAsBool() ? onTrue : onFalse, uuid, this);
            default -> null;
        };
    }
}
