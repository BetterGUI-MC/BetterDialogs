package me.hsgamer.bettergui.betterdialogs.menu;

import me.hsgamer.bettergui.betterdialogs.BetterDialogs;
import me.hsgamer.bettergui.betterdialogs.constructor.ConfirmationDialogConstructor;
import me.hsgamer.hscore.config.Config;

public class ConfirmationDialogMenu extends DialogMenu<ConfirmationDialogConstructor> {
    public ConfirmationDialogMenu(BetterDialogs instance, Config config) {
        super(instance, config, ConfirmationDialogConstructor::create);
    }
}
