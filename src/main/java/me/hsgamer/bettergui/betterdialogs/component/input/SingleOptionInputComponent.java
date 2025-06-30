package me.hsgamer.bettergui.betterdialogs.component.input;

import com.github.retrooper.packetevents.protocol.dialog.input.InputControl;
import com.github.retrooper.packetevents.protocol.dialog.input.SingleOptionInputControl;
import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTString;
import me.hsgamer.bettergui.betterdialogs.builder.DialogComponentBuilder;
import me.hsgamer.bettergui.betterdialogs.util.ComponentUtils;
import me.hsgamer.bettergui.util.StringReplacerApplier;
import me.hsgamer.hscore.common.MapUtils;
import me.hsgamer.hscore.common.Validate;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class SingleOptionInputComponent extends InputComponent<String> {
    private final int width;
    private final @Nullable String label;
    private final Map<String, String> options;
    private final @Nullable String defaultValue;

    public SingleOptionInputComponent(DialogComponentBuilder.Input input) {
        super(input);

        width = Optional.ofNullable(input.options().get("width"))
                .map(Object::toString)
                .flatMap(Validate::getNumber)
                .map(Number::intValue)
                .filter(width -> width > 0)
                .orElse(200);
        label = Optional.ofNullable(input.options().get("label"))
                .map(Object::toString)
                .orElse(null);
        options = Optional.ofNullable(input.options().get("options"))
                .flatMap(MapUtils::castOptionalStringObjectMap)
                .orElse(Collections.emptyMap())
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> Optional.ofNullable(entry.getValue()).map(Object::toString).orElse(entry.getKey()),
                        (a, b) -> a, // In case of duplicate keys, keep the first one
                        HashMap::new
                ));
        defaultValue = Optional.ofNullable(MapUtils.getIfFound(input.options(), "default", "initial"))
                .map(Object::toString)
                .orElse(null);
    }

    @Override
    protected InputControl createControl(Player player) {
        Component label;
        boolean labelVisible;
        if (this.label != null) {
            label = ComponentUtils.convertLegacy(StringReplacerApplier.replace(this.label, player.getUniqueId(), this));
            labelVisible = true;
        } else {
            label = Component.empty();
            labelVisible = false;
        }

        String defaultValue = this.defaultValue != null
                ? StringReplacerApplier.replace(this.defaultValue, player.getUniqueId(), this)
                : null;

        List<SingleOptionInputControl.Entry> entries = options.entrySet().stream()
                .map(entry -> {
                    String key = entry.getKey();
                    String value = StringReplacerApplier.replace(entry.getValue(), player.getUniqueId(), this);
                    return new SingleOptionInputControl.Entry(
                            key,
                            ComponentUtils.convertLegacy(value),
                            Objects.equals(key, defaultValue)
                    );
                })
                .toList();

        return new SingleOptionInputControl(width, entries, label, labelVisible);
    }

    @Override
    protected String getValue(String value, UUID uuid, String args) {
        if ("display".equalsIgnoreCase(args)) {
            String displayValue = options.getOrDefault(value, value);
            return StringReplacerApplier.replace(displayValue, uuid, this);
        }
        return value;
    }

    @Override
    protected String getValue(UUID uuid, NBT nbt) {
        return nbt instanceof NBTString nbtString ? nbtString.getValue() : null;
    }
}
