package me.hsgamer.bettergui.betterdialogs.menu;

import me.hsgamer.bettergui.betterdialogs.BetterDialogs;
import me.hsgamer.bettergui.betterdialogs.constructor.MultiActionDialogConstructor;
import me.hsgamer.hscore.config.Config;

public class MultiActionDialogMenu extends DialogMenu<MultiActionDialogConstructor> {
    public MultiActionDialogMenu(BetterDialogs instance, Config config) {
        super(instance, config, MultiActionDialogConstructor::create);
    }
}
