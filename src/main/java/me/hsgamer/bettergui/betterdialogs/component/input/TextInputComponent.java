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
import me.hsgamer.hscore.common.Validate;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public class TextInputComponent extends InputComponent<String> {
    private final int width;
    private final @Nullable String label;
    private final String initial;
    private final int maxLength;
    private final Integer maxLines;
    private final Integer height;

    public TextInputComponent(DialogComponentBuilder.Input input) {
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
        initial = Optional.ofNullable(MapUtils.getIfFound(input.options(), "default", "initial"))
                .map(Object::toString)
                .orElse("");
        maxLength = Optional.ofNullable(input.options().get("max-length"))
                .map(Object::toString)
                .flatMap(Validate::getNumber)
                .map(Number::intValue)
                .filter(length -> length > 0)
                .orElse(32);
        maxLines = Optional.ofNullable(input.options().get("max-lines"))
                .map(Object::toString)
                .flatMap(Validate::getNumber)
                .map(Number::intValue)
                .filter(lines -> lines > 0)
                .orElse(null);
        height = Optional.ofNullable(input.options().get("height"))
                .map(Object::toString)
                .flatMap(Validate::getNumber)
                .map(Number::intValue)
                .filter(h -> h > 0)
                .orElse(null);
    }

    @Override
    protected void apply(Player player, DialogInputBuilder builder) {
        builder.textInput()
                .width(width)
                .label(StringReplacerApplier.replace(label, player.getUniqueId(), this))
                .initial(StringReplacerApplier.replace(initial, player.getUniqueId(), this))
                .maxLength(maxLength)
                .maxLines(maxLines)
                .height(height);
    }

    @Override
    protected String getValue(String value, UUID uuid, String args) {
        return value;
    }

    @Override
    protected String convertValue(UUID uuid, String rawValue) {
        return rawValue;
    }
}
