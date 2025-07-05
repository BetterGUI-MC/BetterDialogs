package me.hsgamer.bettergui.betterdialogs.component;

import io.github.projectunified.unidialog.core.dialog.Dialog;
import me.hsgamer.bettergui.api.menu.MenuElement;
import me.hsgamer.bettergui.betterdialogs.builder.DialogComponentBuilder;
import me.hsgamer.bettergui.betterdialogs.menu.DialogMenu;
import org.bukkit.entity.Player;

public abstract class DialogComponent implements MenuElement {
    private final String name;
    private final DialogMenu menu;

    protected DialogComponent(DialogComponentBuilder.Input input) {
        this.name = input.name();
        this.menu = input.menu();
    }

    public abstract void apply(Player player, Dialog<?, ?, ?, ?> dialog);

    public String getName() {
        return name;
    }

    @Override
    public DialogMenu getMenu() {
        return menu;
    }
}
