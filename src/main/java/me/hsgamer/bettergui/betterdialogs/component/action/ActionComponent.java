package me.hsgamer.bettergui.betterdialogs.component.action;

import com.github.retrooper.packetevents.protocol.dialog.action.Action;
import me.hsgamer.bettergui.api.menu.Menu;
import me.hsgamer.bettergui.api.menu.MenuElement;
import me.hsgamer.bettergui.betterdialogs.builder.ActionComponentBuilder;
import org.bukkit.entity.Player;

public abstract class ActionComponent implements MenuElement {
    private final Menu menu;

    public ActionComponent(ActionComponentBuilder.Input input) {
        this.menu = input.buttonComponent().getMenu();
    }

    public abstract Action getAction(Player player);

    @Override
    public Menu getMenu() {
        return menu;
    }
}
