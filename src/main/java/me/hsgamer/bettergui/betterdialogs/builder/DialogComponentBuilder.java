package me.hsgamer.bettergui.betterdialogs.builder;

import me.hsgamer.bettergui.betterdialogs.component.DialogComponent;
import me.hsgamer.bettergui.betterdialogs.component.body.ItemComponent;
import me.hsgamer.bettergui.betterdialogs.component.body.TextComponent;
import me.hsgamer.bettergui.betterdialogs.menu.DialogMenu;
import me.hsgamer.hscore.builder.FunctionalMassBuilder;
import me.hsgamer.hscore.collections.map.CaseInsensitiveStringMap;

import java.util.Map;
import java.util.Objects;

public final class DialogComponentBuilder extends FunctionalMassBuilder<DialogComponentBuilder.Input, DialogComponent> {
    public static final DialogComponentBuilder INSTANCE = new DialogComponentBuilder();

    private DialogComponentBuilder() {
        register(TextComponent::new, "plain-message", "message", "plain", "text");
        register(ItemComponent::new, "item");
    }

    @Override
    protected String getType(Input input) {
        Map<String, Object> keys = new CaseInsensitiveStringMap<>(input.options);
        return Objects.toString(keys.get("type"), "text");
    }

    public record Input(DialogMenu menu, String name, Map<String, Object> options) {
    }
}
