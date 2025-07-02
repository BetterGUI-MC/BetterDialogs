package me.hsgamer.bettergui.betterdialogs.menu;

import me.hsgamer.bettergui.betterdialogs.BetterDialogs;
import me.hsgamer.bettergui.betterdialogs.constructor.ServerLinksDialogConstructor;
import me.hsgamer.hscore.common.MapUtils;
import me.hsgamer.hscore.common.Validate;
import me.hsgamer.hscore.config.Config;
import org.bukkit.entity.Player;

import java.util.Optional;

public class ServerLinksDialogMenu extends DialogMenu<ServerLinksDialogConstructor> {
    private final int columns;
    private final int buttonWidth;

    public ServerLinksDialogMenu(BetterDialogs instance, Config config) {
        super(instance, config, ServerLinksDialogConstructor::create);
        this.columns = Optional.ofNullable(menuSettings.get("columns"))
                .map(Object::toString)
                .flatMap(Validate::getNumber)
                .map(Number::intValue)
                .filter(columns -> columns > 0)
                .orElse(2);
        this.buttonWidth = Optional.ofNullable(MapUtils.getIfFound(menuSettings, "button-width", "width"))
                .map(Object::toString)
                .flatMap(Validate::getNumber)
                .map(Number::intValue)
                .filter(width -> width > 0)
                .orElse(150);
    }

    @Override
    protected void modifyDialog(Player player, ServerLinksDialogConstructor dialogConstructor) {
        dialogConstructor.columns(columns).buttonWidth(buttonWidth);
    }
}
