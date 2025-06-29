package me.hsgamer.bettergui.betterdialogs.component;

import me.hsgamer.bettergui.api.menu.MenuElement;
import me.hsgamer.bettergui.betterdialogs.constructor.DialogConstructor;
import me.hsgamer.bettergui.betterdialogs.constructor.DialogDataConstructor;
import me.hsgamer.bettergui.betterdialogs.menu.DialogMenu;
import org.bukkit.entity.Player;

import java.util.UUID;

public abstract class DialogComponent implements MenuElement {
    private final String name;
    private final DialogMenu menu;

    protected DialogComponent(String name, DialogMenu menu) {
        this.name = name;
        this.menu = menu;
    }

    public void apply(Player player, DialogDataConstructor dialogDataConstructor, DialogConstructor dialogConstructor) {
        // This method can be overridden to apply the dialog data and constructor
        // to the player in a specific way, if needed.
    }

    public String getValue(UUID uuid, String args) {
        return "";
    }

    public String getName() {
        return name;
    }

    @Override
    public DialogMenu getMenu() {
        return menu;
    }
}
