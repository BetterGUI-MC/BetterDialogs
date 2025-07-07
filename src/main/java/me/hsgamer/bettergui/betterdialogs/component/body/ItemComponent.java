package me.hsgamer.bettergui.betterdialogs.component.body;

import io.github.projectunified.unidialog.core.body.DialogBodyBuilder;
import io.github.projectunified.unidialog.core.body.ItemBody;
import io.github.projectunified.unidialog.packetevents.body.PEItemBody;
import io.github.projectunified.unidialog.paper.body.PaperItemBody;
import io.github.projectunified.unidialog.spigot.body.SpigotItemBody;
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
                .description(description != null ? description.getBodyConsumer(player.getUniqueId())::accept : null)
                .height(height);
        ItemStack itemStack = itemBuilder.build(player.getUniqueId());
        if (itemBody instanceof PEItemBody peItemBody) {
            peItemBody.item(SpigotReflectionUtil.decodeBukkitItemStack(itemStack));
        } else if (itemBody instanceof PaperItemBody paperItemBody) {
            paperItemBody.item(itemStack);
        } else if (itemBody instanceof SpigotItemBody spigotItemBody) {
            spigotItemBody.item(itemStack);
        }
    }
}
