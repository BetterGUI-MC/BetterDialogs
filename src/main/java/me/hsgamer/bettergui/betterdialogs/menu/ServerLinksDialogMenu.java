package me.hsgamer.bettergui.betterdialogs.menu;

import me.hsgamer.bettergui.betterdialogs.BetterDialogs;
import me.hsgamer.bettergui.betterdialogs.constructor.ServerLinksDialogConstructor;
import me.hsgamer.hscore.config.Config;

public class ServerLinksDialogMenu extends DialogMenu<ServerLinksDialogConstructor> {
    public ServerLinksDialogMenu(BetterDialogs instance, Config config) {
        super(instance, config, ServerLinksDialogConstructor::create);
    }
}
