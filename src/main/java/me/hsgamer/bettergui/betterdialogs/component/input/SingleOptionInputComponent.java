/*
   Copyright 2025-2025 Huynh Tien

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
package me.hsgamer.bettergui.betterdialogs.component.input;

import io.github.projectunified.unidialog.adventure.input.AdventureSingleOptionInput;
import io.github.projectunified.unidialog.core.input.DialogInputBuilder;
import io.github.projectunified.unidialog.core.input.SingleOptionInput;
import me.hsgamer.bettergui.betterdialogs.DialogManagerProvider;
import me.hsgamer.bettergui.betterdialogs.builder.DialogComponentBuilder;
import me.hsgamer.bettergui.betterdialogs.text.Text;
import me.hsgamer.bettergui.util.StringReplacerApplier;
import me.hsgamer.hscore.common.MapUtils;
import me.hsgamer.hscore.common.Validate;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class SingleOptionInputComponent extends InputComponent<String> {
    private final int width;
    private final @Nullable Text label;
    private final Map<String, Text> options;
    private final @Nullable String defaultValue;

    public SingleOptionInputComponent(DialogComponentBuilder.Input input) {
        super(input);

        width = Optional.ofNullable(input.options().get("width"))
                .map(Object::toString)
                .flatMap(Validate::getNumber)
                .map(Number::intValue)
                .filter(width -> width > 0)
                .orElse(200);
        label = DialogManagerProvider.textGetter().get(input.options(), "label").orElse(null);
        options = DialogManagerProvider.textGetter().getMap(input.options(), "options");
        defaultValue = Optional.ofNullable(MapUtils.getIfFound(input.options(), "default", "initial"))
                .map(Object::toString)
                .orElse(null);
    }

    @Override
    protected void apply(Player player, DialogInputBuilder builder) {
        SingleOptionInput<?> input = builder.singleOptionInput().width(width);
        if (label != null) {
            String replacedLabel = StringReplacerApplier.replace(label.text(), player.getUniqueId(), this);
            if (label.isAdventure() && input instanceof AdventureSingleOptionInput<?> adventureInput) {
                adventureInput.label((Component) label.parser().apply(replacedLabel, player));
            } else {
                input.label(replacedLabel);
            }
        }
        String defaultValue = this.defaultValue != null
                ? StringReplacerApplier.replace(this.defaultValue, player.getUniqueId(), this)
                : null;
        for (Map.Entry<String, Text> entry : options.entrySet()) {
            String key = entry.getKey();
            String replacedValue = StringReplacerApplier.replace(entry.getValue().text(), player.getUniqueId(), this);
            if (entry.getValue().isAdventure() && input instanceof AdventureSingleOptionInput<?> adventureInput) {
                adventureInput.option(key, (Component) entry.getValue().parser().apply(replacedValue, player), Objects.equals(key, defaultValue));
            } else {
                input.option(key, replacedValue, Objects.equals(key, defaultValue));
            }
        }
    }

    @Override
    protected String getValue(String value, UUID uuid, String args) {
        if ("display".equalsIgnoreCase(args)) {
            Text displayValue = options.get(value);
            if (displayValue != null) {
                return StringReplacerApplier.replace(displayValue.text(), uuid, this);
            }
        }
        return value;
    }

    @Override
    protected String convertValue(UUID uuid, String rawValue) {
        return rawValue;
    }
}
