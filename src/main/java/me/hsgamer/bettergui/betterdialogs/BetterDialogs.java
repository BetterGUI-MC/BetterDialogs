package me.hsgamer.bettergui.betterdialogs;

import com.github.retrooper.packetevents.PacketEvents;
import me.hsgamer.bettergui.api.addon.GetLogger;
import me.hsgamer.bettergui.api.addon.GetPlugin;
import me.hsgamer.bettergui.api.addon.Reloadable;
import me.hsgamer.bettergui.betterdialogs.constructor.ConfirmationDialogConstructor;
import me.hsgamer.bettergui.betterdialogs.constructor.MultiActionDialogConstructor;
import me.hsgamer.bettergui.betterdialogs.constructor.NoticeDialogConstructor;
import me.hsgamer.bettergui.betterdialogs.constructor.ServerLinksDialogConstructor;
import me.hsgamer.bettergui.betterdialogs.listener.DialogCustomClickListener;
import me.hsgamer.bettergui.betterdialogs.menu.DialogMenu;
import me.hsgamer.bettergui.builder.MenuBuilder;
import me.hsgamer.hscore.expansion.common.Expansion;

public final class BetterDialogs implements Expansion, GetLogger, GetPlugin, Reloadable {
    private final DialogCustomClickListener dialogCustomClickListener = new DialogCustomClickListener(this);

    @Override
    public void onEnable() {
        PacketEvents.getAPI().getEventManager().registerListener(dialogCustomClickListener);
        MenuBuilder.INSTANCE.register(config -> new DialogMenu(this, config, ConfirmationDialogConstructor::create), "confirmation-dialog", "confirm-dialog");
        MenuBuilder.INSTANCE.register(config -> new DialogMenu(this, config, MultiActionDialogConstructor::create), "multi-action-dialog", "action-dialog");
        MenuBuilder.INSTANCE.register(config -> new DialogMenu(this, config, NoticeDialogConstructor::create), "notice-dialog");
        MenuBuilder.INSTANCE.register(config -> new DialogMenu(this, config, ServerLinksDialogConstructor::create), "server-links-dialog", "links-dialog", "server-link-dialog", "link-dialog");
    }

    @Override
    public void onReload() {
        dialogCustomClickListener.clearActions();
    }

    public DialogCustomClickListener dialogCustomClickListener() {
        return dialogCustomClickListener;
    }
}
