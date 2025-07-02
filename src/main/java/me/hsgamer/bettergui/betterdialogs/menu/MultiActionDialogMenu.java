package me.hsgamer.bettergui.betterdialogs.menu;

import me.hsgamer.bettergui.betterdialogs.BetterDialogs;
import me.hsgamer.bettergui.betterdialogs.constructor.DialogConstructor;
import me.hsgamer.bettergui.betterdialogs.constructor.MultiActionDialogConstructor;
import me.hsgamer.hscore.common.Validate;
import me.hsgamer.hscore.config.Config;
import org.bukkit.entity.Player;

import java.util.Optional;

public class MultiActionDialogMenu extends DialogMenu {
    private final int columns;

    public MultiActionDialogMenu(BetterDialogs instance, Config config) {
        super(instance, config);
        this.columns = Optional.ofNullable(menuSettings.get("columns"))
                .map(Object::toString)
                .flatMap(Validate::getNumber)
                .map(Number::intValue)
                .filter(columns -> columns > 0)
                .orElse(2);
    }

    @Override
    protected DialogConstructor createDialogConstructor(Player player) {
        return MultiActionDialogConstructor.create().columns(columns);
    }
}
