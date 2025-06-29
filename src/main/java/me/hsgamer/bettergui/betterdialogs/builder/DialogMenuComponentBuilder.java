package me.hsgamer.bettergui.betterdialogs.builder;

import me.hsgamer.bettergui.betterdialogs.menu.DialogMenu;
import me.hsgamer.bettergui.betterdialogs.menu.DialogMenuComponent;
import me.hsgamer.hscore.builder.FunctionalMassBuilder;
import me.hsgamer.hscore.collections.map.CaseInsensitiveStringMap;

import java.util.Map;
import java.util.Objects;

public class DialogMenuComponentBuilder extends FunctionalMassBuilder<DialogMenuComponentBuilder.Input, DialogMenuComponent> {
    public static final DialogMenuComponentBuilder INSTANCE = new DialogMenuComponentBuilder();

    private DialogMenuComponentBuilder() {

    }

    @Override
    protected String getType(Input input) {
        Map<String, Object> keys = new CaseInsensitiveStringMap<>(input.options);
        return Objects.toString(keys.get("type"), "");
    }

    public record Input(DialogMenu menu, String name, Map<String, Object> options) {
    }
}
