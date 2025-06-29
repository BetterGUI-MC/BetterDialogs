package me.hsgamer.bettergui.betterdialogs.builder;

import me.hsgamer.bettergui.betterdialogs.component.action.ActionComponent;
import me.hsgamer.bettergui.betterdialogs.component.action.CustomActionComponent;
import me.hsgamer.bettergui.betterdialogs.component.button.ButtonComponent;
import me.hsgamer.hscore.builder.FunctionalMassBuilder;
import me.hsgamer.hscore.collections.map.CaseInsensitiveStringMap;

import java.util.Map;
import java.util.Objects;

public class ActionComponentBuilder extends FunctionalMassBuilder<ActionComponentBuilder.Input, ActionComponent> {
    public static final ActionComponentBuilder INSTANCE = new ActionComponentBuilder();

    private ActionComponentBuilder() {
        register(CustomActionComponent::new, "custom");
    }

    @Override
    protected String getType(Input input) {
        Map<String, Object> keys = new CaseInsensitiveStringMap<>(input.options);
        return Objects.toString(keys.get("action-type"), "custom");
    }

    public record Input(ButtonComponent buttonComponent, Map<String, Object> options) {
    }
}
