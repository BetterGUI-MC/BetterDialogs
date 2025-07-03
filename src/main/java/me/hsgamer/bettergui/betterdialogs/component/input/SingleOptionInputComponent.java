package me.hsgamer.bettergui.betterdialogs.component.input;

import io.github.projectunified.unidialog.packetevents.input.PEDialogInputBuilder;
import io.github.projectunified.unidialog.packetevents.input.PESingleOptionInput;
import me.hsgamer.bettergui.betterdialogs.builder.DialogComponentBuilder;
import me.hsgamer.bettergui.util.StringReplacerApplier;
import me.hsgamer.hscore.common.MapUtils;
import me.hsgamer.hscore.common.Validate;
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
    protected void apply(Player player, PEDialogInputBuilder builder) {
        PESingleOptionInput input = builder.singleOptionInput()
                .label(label == null ? null : StringReplacerApplier.replace(label, player.getUniqueId(), this))
                .width(width);
        String defaultValue = this.defaultValue != null
                ? StringReplacerApplier.replace(this.defaultValue, player.getUniqueId(), this)
                : null;
        for (Map.Entry<String, String> entry : options.entrySet()) {
            String key = entry.getKey();
            String value = StringReplacerApplier.replace(entry.getValue(), player.getUniqueId(), this);
            input.option(key, value, Objects.equals(key, defaultValue));
        }
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
    protected String convertValue(UUID uuid, String rawValue) {
        return rawValue;
    }
}
