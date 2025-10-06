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

import io.github.projectunified.unidialog.adventure.input.AdventureBooleanInput;
import io.github.projectunified.unidialog.core.input.BooleanInput;
import io.github.projectunified.unidialog.core.input.DialogInputBuilder;
import io.github.projectunified.unidialog.core.payload.DialogPayload;
import me.hsgamer.bettergui.betterdialogs.DialogManagerProvider;
import me.hsgamer.bettergui.betterdialogs.builder.DialogComponentBuilder;
import me.hsgamer.bettergui.betterdialogs.text.Text;
import me.hsgamer.bettergui.util.StringReplacerApplier;
import me.hsgamer.hscore.common.MapUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;

public class BooleanInputComponent extends InputComponent<Boolean> {
    private final Text label;
    private final String initial;
    private final String onTrue;
    private final String onFalse;

    public BooleanInputComponent(DialogComponentBuilder.Input input) {
        super(input);

        label = DialogManagerProvider.textGetter().get(input.options(), "label")
                .orElseGet(() -> Text.of("Boolean Input"));
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
        BooleanInput<?> input = builder.booleanInput()
                .initial(Boolean.parseBoolean(StringReplacerApplier.replace(initial, player.getUniqueId(), this)))
                .onTrue(StringReplacerApplier.replace(onTrue, player.getUniqueId(), this))
                .onFalse(StringReplacerApplier.replace(onFalse, player.getUniqueId(), this));

        String replacedLabel = StringReplacerApplier.replace(label.text(), player.getUniqueId(), this);
        if (label.isAdventure() && input instanceof AdventureBooleanInput<?> adventureInput) {
            adventureInput.label((Component) label.parser().apply(replacedLabel, player));
        } else {
            input.label(replacedLabel);
        }
    }

    @Override
    protected String getValue(Boolean value, UUID uuid, String args) {
        if (value == Boolean.TRUE) {
            return StringReplacerApplier.replace(onTrue, uuid, this);
        } else {
            return StringReplacerApplier.replace(onFalse, uuid, this);
        }
    }

    @Override
    protected Boolean getValue(String key, DialogPayload payload) {
        return payload.booleanValue(key);
    }
}
