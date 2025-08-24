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

import io.github.projectunified.unidialog.bungeecord.body.BungeeItemBody;
import io.github.projectunified.unidialog.core.body.DialogBodyBuilder;
import io.github.projectunified.unidialog.core.body.ItemBody;
import io.github.projectunified.unidialog.packetevents.body.PEItemBody;
import io.github.projectunified.unidialog.paper.body.PaperItemBody;
import io.github.retrooper.packetevents.util.SpigotReflectionUtil;
import me.hsgamer.bettergui.betterdialogs.builder.DialogComponentBuilder;
import me.hsgamer.bettergui.builder.ItemModifierBuilder;
import me.hsgamer.bettergui.util.StringReplacerApplier;
import me.hsgamer.hscore.bukkit.item.BukkitItemBuilder;
import me.hsgamer.hscore.common.MapUtils;
import me.hsgamer.hscore.common.Validate;
import me.hsgamer.hscore.minecraft.item.ItemBuilder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ItemComponent extends DialogBodyComponent {
    private final ItemBuilder<ItemStack> itemBuilder;
    private final @Nullable TextComponent description;
    private final boolean showDecorations;
    private final boolean showTooltip;
    private final int width;
    private final int height;

    public ItemComponent(DialogComponentBuilder.Input input) {
        super(input);
        itemBuilder = StringReplacerApplier.apply(new BukkitItemBuilder(), this);
        ItemModifierBuilder.INSTANCE.build(input.options()).forEach(itemBuilder::addItemModifier);

        description = Optional.ofNullable(input.options().get("description"))
                .flatMap(MapUtils::castOptionalStringObjectMap)
                .map(map -> new DialogComponentBuilder.Input(getMenu(), getName() + "_description", map))
                .map(TextComponent::new)
                .orElse(null);
        showDecorations = Optional.ofNullable(input.options().get("show-decorations"))
                .map(Object::toString)
                .map(Boolean::parseBoolean)
                .orElse(true);
        showTooltip = Optional.ofNullable(input.options().get("show-tooltip"))
                .map(Object::toString)
                .map(Boolean::parseBoolean)
                .orElse(true);
        width = Optional.ofNullable(input.options().get("width"))
                .map(Object::toString)
                .flatMap(Validate::getNumber)
                .map(Number::intValue)
                .filter(width -> width > 0)
                .orElse(16);
        height = Optional.ofNullable(input.options().get("height"))
                .map(Object::toString)
                .flatMap(Validate::getNumber)
                .map(Number::intValue)
                .filter(width -> width > 0)
                .orElse(16);
    }

    @Override
    public void apply(Player player, DialogBodyBuilder<?> builder) {
        ItemBody<?, ?, ?> itemBody = builder.item()
                .showDecorations(showDecorations)
                .showTooltip(showTooltip)
                .width(width)
                .description(description != null ? description.getBodyConsumer(player)::accept : null)
                .height(height);
        ItemStack itemStack = itemBuilder.build(player.getUniqueId());
        if (itemBody instanceof PEItemBody peItemBody) {
            peItemBody.item(SpigotReflectionUtil.decodeBukkitItemStack(itemStack));
        } else if (itemBody instanceof PaperItemBody paperItemBody) {
            paperItemBody.item(itemStack);
        } else if (itemBody instanceof BungeeItemBody spigotItemBody) {
            spigotItemBody.item(itemStack);
        }
    }
}
