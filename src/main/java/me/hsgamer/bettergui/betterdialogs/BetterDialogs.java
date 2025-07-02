package me.hsgamer.bettergui.betterdialogs;

import com.github.retrooper.packetevents.PacketEvents;
import me.hsgamer.bettergui.api.addon.GetLogger;
import me.hsgamer.bettergui.api.addon.GetPlugin;
import me.hsgamer.bettergui.api.addon.Reloadable;
import me.hsgamer.bettergui.betterdialogs.listener.DialogCustomClickListener;
import me.hsgamer.bettergui.betterdialogs.menu.ConfirmationDialogMenu;
import me.hsgamer.bettergui.betterdialogs.menu.MultiActionDialogMenu;
import me.hsgamer.bettergui.betterdialogs.menu.NoticeDialogMenu;
import me.hsgamer.bettergui.betterdialogs.menu.ServerLinksDialogMenu;
import me.hsgamer.bettergui.builder.MenuBuilder;
import me.hsgamer.hscore.expansion.common.Expansion;

public final class BetterDialogs implements Expansion, GetLogger, GetPlugin, Reloadable {
    private final DialogCustomClickListener dialogCustomClickListener = new DialogCustomClickListener(this);

    @Override
    public void onEnable() {
        PacketEvents.getAPI().getEventManager().registerListener(dialogCustomClickListener);
        MenuBuilder.INSTANCE.register(config -> new ConfirmationDialogMenu(this, config), "confirmation-dialog", "confirm-dialog");
        MenuBuilder.INSTANCE.register(config -> new MultiActionDialogMenu(this, config), "multi-action-dialog", "action-dialog");
        MenuBuilder.INSTANCE.register(config -> new NoticeDialogMenu(this, config), "notice-dialog");
        MenuBuilder.INSTANCE.register(config -> new ServerLinksDialogMenu(this, config), "server-links-dialog", "links-dialog", "server-link-dialog", "link-dialog");
    }

    @Override
    public void onReload() {
        dialogCustomClickListener.clearActions();
    }

    public DialogCustomClickListener dialogCustomClickListener() {
        return dialogCustomClickListener;
    }
}
