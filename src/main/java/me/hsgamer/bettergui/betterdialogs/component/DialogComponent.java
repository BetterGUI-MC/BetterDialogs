package me.hsgamer.bettergui.betterdialogs.component;

import me.hsgamer.bettergui.api.menu.MenuElement;
import me.hsgamer.bettergui.betterdialogs.builder.DialogComponentBuilder;
import me.hsgamer.bettergui.betterdialogs.constructor.DialogConstructor;
import me.hsgamer.bettergui.betterdialogs.constructor.DialogDataConstructor;
import me.hsgamer.bettergui.betterdialogs.menu.DialogMenu;
import org.bukkit.entity.Player;

public abstract class DialogComponent implements MenuElement {
    private final String name;
    private final DialogMenu menu;

    protected DialogComponent(DialogComponentBuilder.Input input) {
        this.name = input.name();
        this.menu = input.menu();
    }

    public abstract void apply(Player player, DialogDataConstructor dialogDataConstructor, DialogConstructor dialogConstructor);

    public String getName() {
        return name;
    }

    @Override
    public DialogMenu getMenu() {
        return menu;
    }
}
