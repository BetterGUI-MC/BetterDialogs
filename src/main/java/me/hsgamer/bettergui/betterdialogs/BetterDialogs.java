package me.hsgamer.bettergui.betterdialogs;

import io.github.projectunified.minelib.scheduler.common.util.Platform;
import io.github.projectunified.unidialog.core.DialogManager;
import io.github.projectunified.unidialog.packetevents.PocketEventsDialogManager;
import io.github.projectunified.unidialog.paper.PaperDialogManager;
import io.github.projectunified.unidialog.spigot.SpigotDialogManager;
import me.hsgamer.bettergui.api.addon.GetLogger;
import me.hsgamer.bettergui.api.addon.GetPlugin;
import me.hsgamer.bettergui.api.addon.Reloadable;
import me.hsgamer.bettergui.betterdialogs.menu.ConfirmationDialogMenu;
import me.hsgamer.bettergui.betterdialogs.menu.MultiActionDialogMenu;
import me.hsgamer.bettergui.betterdialogs.menu.NoticeDialogMenu;
import me.hsgamer.bettergui.betterdialogs.menu.ServerLinksDialogMenu;
import me.hsgamer.bettergui.builder.MenuBuilder;
import me.hsgamer.hscore.bukkit.utils.VersionUtils;
import me.hsgamer.hscore.common.Validate;
import me.hsgamer.hscore.expansion.common.Expansion;
import me.hsgamer.hscore.logger.common.LogLevel;
import me.hsgamer.hscore.logger.common.Logger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public final class BetterDialogs implements Expansion, GetLogger, GetPlugin, Reloadable {
    private DialogManager<?, ?, ?, ?, ?> dialogManager;

    @Override
    public boolean onLoad() {
        if (Platform.PAPER.isPlatform() && VersionUtils.isAtLeast(21, 7)) {
            dialogManager = new PaperDialogManager(getPlugin(), "betterdialogs");
        } else if (Bukkit.getPluginManager().getPlugin("packetevents") != null) {
            dialogManager = new PocketEventsDialogManager("betterdialogs") {
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
        } else if (Validate.isClassLoaded("net.md_5.bungee.api.dialog.Dialog")) {
            dialogManager = new SpigotDialogManager(getPlugin(), "betterdialogs");
        } else {
            Logger logger = getLogger();
            logger.log(LogLevel.WARN, "BetterDialogs is not supported on this platform.");
            logger.log(LogLevel.WARN, "The only supported platforms are:");
            logger.log(LogLevel.WARN, "- PocketEvents (with Packetevents plugin)");
            logger.log(LogLevel.WARN, "- Paper 1.21.7+");
            logger.log(LogLevel.WARN, "- Spigot 1.21.6+ (with BungeeCord Dialog API)");
            logger.log(LogLevel.WARN, "Please use the correct platform to use BetterDialogs.");
            return false;
        }
        return true;
    }

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

    public DialogManager<?, ?, ?, ?, ?> dialogManager() {
        return dialogManager;
    }
}
