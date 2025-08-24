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

import io.github.projectunified.unidialog.adventure.input.AdventureNumberRangeInput;
import io.github.projectunified.unidialog.core.input.DialogInputBuilder;
import io.github.projectunified.unidialog.core.input.NumberRangeInput;
import me.hsgamer.bettergui.betterdialogs.DialogManagerProvider;
import me.hsgamer.bettergui.betterdialogs.builder.DialogComponentBuilder;
import me.hsgamer.bettergui.betterdialogs.text.Text;
import me.hsgamer.bettergui.util.StringReplacerApplier;
import me.hsgamer.hscore.common.MapUtils;
import me.hsgamer.hscore.common.Validate;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.util.Optional;
import java.util.UUID;

public class NumberInputComponent extends InputComponent<Number> {
    private final int width;
    private final Text label;
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
        label = DialogManagerProvider.textGetter().get(input.options(), "label")
                .orElseGet(() -> Text.of(""));
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
    protected void apply(Player player, DialogInputBuilder builder) {
        NumberRangeInput<?> input = builder.numberRangeInput()
                .width(width)
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

        String replacedLabel = StringReplacerApplier.replace(label.text(), player.getUniqueId(), this);
        if (label.isAdventure() && input instanceof AdventureNumberRangeInput<?> adventureInput) {
            adventureInput.label((Component) label.parser().apply(replacedLabel, player));
        } else {
            input.label(replacedLabel);
        }
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
