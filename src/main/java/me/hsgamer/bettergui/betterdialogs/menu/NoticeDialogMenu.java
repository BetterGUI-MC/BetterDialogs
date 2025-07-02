package me.hsgamer.bettergui.betterdialogs.menu;

import me.hsgamer.bettergui.betterdialogs.BetterDialogs;
import me.hsgamer.bettergui.betterdialogs.constructor.NoticeDialogConstructor;
import me.hsgamer.hscore.config.Config;

public class NoticeDialogMenu extends DialogMenu<NoticeDialogConstructor> {
    public NoticeDialogMenu(BetterDialogs instance, Config config) {
        super(instance, config, NoticeDialogConstructor::create);
    }
}
