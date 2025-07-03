package me.hsgamer.bettergui.betterdialogs;

import io.github.projectunified.unidialog.packetevents.PocketEventsDialogManager;
import me.hsgamer.bettergui.api.addon.GetLogger;
import me.hsgamer.bettergui.api.addon.GetPlugin;
import me.hsgamer.bettergui.api.addon.Reloadable;
import me.hsgamer.bettergui.betterdialogs.menu.ConfirmationDialogMenu;
import me.hsgamer.bettergui.betterdialogs.menu.MultiActionDialogMenu;
import me.hsgamer.bettergui.betterdialogs.menu.NoticeDialogMenu;
import me.hsgamer.bettergui.betterdialogs.menu.ServerLinksDialogMenu;
import me.hsgamer.bettergui.builder.MenuBuilder;
import me.hsgamer.hscore.expansion.common.Expansion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public final class BetterDialogs implements Expansion, GetLogger, GetPlugin, Reloadable {
    private final PocketEventsDialogManager dialogManager = new PocketEventsDialogManager("betterdialogs") {
        @Override
        protected @Nullable Player getPlayer(UUID uuid) {
            return Bukkit.getPlayer(uuid);
        }

        @Override
        protected UUID getPlayerId(Object player) {
            Player p = (Player) player;
            return p.getUniqueId();
        }
    };

    @Override
    public void onEnable() {
        dialogManager.register();
        MenuBuilder.INSTANCE.register(config -> new ConfirmationDialogMenu(this, config), "confirmation-dialog", "confirm-dialog");
        MenuBuilder.INSTANCE.register(config -> new MultiActionDialogMenu(this, config), "multi-action-dialog", "action-dialog");
        MenuBuilder.INSTANCE.register(config -> new NoticeDialogMenu(this, config), "notice-dialog");
        MenuBuilder.INSTANCE.register(config -> new ServerLinksDialogMenu(this, config), "server-links-dialog", "links-dialog", "server-link-dialog", "link-dialog");
    }

    @Override
    public void onReload() {
        dialogManager.unregisterAllCustomActions();
    }

    @Override
    public void onDisable() {
        dialogManager.unregister();
    }

    public PocketEventsDialogManager dialogManager() {
        return dialogManager;
    }
}
