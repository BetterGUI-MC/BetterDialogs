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

import io.github.projectunified.unidialog.core.input.DialogInputBuilder;
import me.hsgamer.bettergui.betterdialogs.builder.DialogComponentBuilder;
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
    protected void apply(Player player, DialogInputBuilder builder) {
        builder.booleanInput()
                .label(StringReplacerApplier.replace(label, player.getUniqueId(), this))
                .initial(Boolean.parseBoolean(StringReplacerApplier.replace(initial, player.getUniqueId(), this)))
                .onTrue(StringReplacerApplier.replace(onTrue, player.getUniqueId(), this))
                .onFalse(StringReplacerApplier.replace(onFalse, player.getUniqueId(), this));
    }

    @Override
    protected String getValue(String value, UUID uuid, String args) {
        return value;
    }

    @Override
    protected String convertValue(UUID uuid, String rawValue) {
        try {
            float value = Float.parseFloat(rawValue);
            rawValue = value == 0 ? "false" : "true";
        } catch (NumberFormatException e) {
            // Ignore the exception, keep the raw value as is
        }

        if (rawValue.equalsIgnoreCase("true") || rawValue.equalsIgnoreCase("yes")) {
            return StringReplacerApplier.replace(onTrue, uuid, this);
        } else if (rawValue.equalsIgnoreCase("false") || rawValue.equalsIgnoreCase("no")) {
            return StringReplacerApplier.replace(onFalse, uuid, this);
        } else {
            return rawValue;
        }
    }
}
