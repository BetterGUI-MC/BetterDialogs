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
package me.hsgamer.bettergui.betterdialogs.component.body;

import io.github.projectunified.unidialog.core.body.DialogBodyBuilder;
import io.github.projectunified.unidialog.core.body.TextBody;
import me.hsgamer.bettergui.betterdialogs.builder.DialogComponentBuilder;
import me.hsgamer.bettergui.util.StringReplacerApplier;
import me.hsgamer.hscore.common.MapUtils;
import me.hsgamer.hscore.common.Validate;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public class TextComponent extends DialogBodyComponent {
    private final String text;
    private final int width;

    public TextComponent(DialogComponentBuilder.Input input) {
        super(input);

        text = Optional.ofNullable(MapUtils.getIfFound(input.options(), "text", "message"))
                .map(Object::toString)
                .orElse("Text");
        width = Optional.ofNullable(input.options().get("width"))
                .map(Object::toString)
                .flatMap(Validate::getNumber)
                .map(Number::intValue)
                .filter(width -> width > 0)
                .orElse(150);
    }

    @Override
    protected void apply(Player player, DialogBodyBuilder<?> builder) {
        getBodyConsumer(player.getUniqueId()).accept(builder.text());
    }

    public Consumer<TextBody<?>> getBodyConsumer(UUID uuid) {
        return body -> body.text(StringReplacerApplier.replace(text, uuid, this)).width(width);
    }
}
