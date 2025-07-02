package me.hsgamer.bettergui.betterdialogs.menu;

import me.hsgamer.bettergui.betterdialogs.BetterDialogs;
import me.hsgamer.bettergui.betterdialogs.constructor.DialogConstructor;
import me.hsgamer.bettergui.betterdialogs.constructor.NoticeDialogConstructor;
import me.hsgamer.hscore.config.Config;
import org.bukkit.entity.Player;

public class NoticeDialogMenu extends DialogMenu {
    public NoticeDialogMenu(BetterDialogs instance, Config config) {
        super(instance, config);
    }

    @Override
    protected DialogConstructor createDialogConstructor(Player player) {
        return NoticeDialogConstructor.create();
    }
}
