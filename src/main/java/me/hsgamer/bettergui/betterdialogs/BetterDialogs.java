package me.hsgamer.bettergui.betterdialogs;

import io.github.projectunified.minelib.scheduler.common.util.Platform;
import io.github.projectunified.unidialog.core.DialogManager;
import io.github.projectunified.unidialog.packetevents.PocketEventsDialogManager;
import io.github.projectunified.unidialog.paper.PaperDialogManager;
import io.github.projectunified.unidialog.spigot.SpigotDialogManager;
import me.hsgamer.bettergui.api.addon.GetLogger;
import me.hsgamer.bettergui.api.addon.GetPlugin;
import me.hsgamer.bettergui.api.addon.Reloadable;
import me.hsgamer.bettergui.betterdialogs.config.MainConfig;
import me.hsgamer.bettergui.betterdialogs.menu.ConfirmationDialogMenu;
import me.hsgamer.bettergui.betterdialogs.menu.MultiActionDialogMenu;
import me.hsgamer.bettergui.betterdialogs.menu.NoticeDialogMenu;
import me.hsgamer.bettergui.betterdialogs.menu.ServerLinksDialogMenu;
import me.hsgamer.bettergui.builder.MenuBuilder;
import me.hsgamer.hscore.bukkit.config.BukkitConfig;
import me.hsgamer.hscore.bukkit.utils.VersionUtils;
import me.hsgamer.hscore.common.Validate;
import me.hsgamer.hscore.config.proxy.ConfigGenerator;
import me.hsgamer.hscore.expansion.common.Expansion;
import me.hsgamer.hscore.expansion.extra.expansion.DataFolder;
import me.hsgamer.hscore.logger.common.LogLevel;
import me.hsgamer.hscore.logger.common.Logger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.UUID;
import java.util.function.BooleanSupplier;
import java.util.function.Function;

public final class BetterDialogs implements Expansion, GetLogger, GetPlugin, Reloadable, DataFolder {
    private final MainConfig mainConfig = ConfigGenerator.newInstance(MainConfig.class, new BukkitConfig(new File(getDataFolder(), "config.yml")));
    private DialogManager<?, ?, ?, ?, ?> dialogManager;

    @Override
    public boolean onLoad() {
        String dialogManagerName = mainConfig.dialogManager().toLowerCase();
        Logger logger = getLogger();
        if (dialogManagerName.equals("auto")) {
            for (DialogManagerType type : DialogManagerType.values()) {
                if (type.isAvailable()) {
                    dialogManager = type.create(getPlugin());
                    logger.log(LogLevel.INFO, "Using " + type.name() + " for BetterDialogs");
                    return true;
                }
            }
            logger.log(LogLevel.WARN, "No available dialog manager found.");
        } else {
            try {
                DialogManagerType type = DialogManagerType.valueOf(dialogManagerName.toUpperCase());
                if (type.isAvailable()) {
                    dialogManager = type.create(getPlugin());
                    logger.log(LogLevel.INFO, "Using " + type.name() + " for BetterDialogs");
                    return true;
                } else {
                    logger.log(LogLevel.WARN, "The specified dialog manager '" + dialogManagerName + "' is not available.");
                }
            } catch (IllegalArgumentException e) {
                logger.log(LogLevel.WARN, "Invalid dialog manager specified: " + dialogManagerName);
            }
        }
        return false;
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

    private enum DialogManagerType {
        PAPER(
                () -> Platform.PAPER.isPlatform() && VersionUtils.isAtLeast(21, 7),
                plugin -> new PaperDialogManager(plugin, "betterdialogs")
        ),
        PACKETEVENTS(
                () -> Bukkit.getPluginManager().getPlugin("packetevents") != null,
                plugin -> new PocketEventsDialogManager("betterdialogs") {
                    @Override
                    protected @Nullable Player getPlayer(UUID uuid) {
                        return Bukkit.getPlayer(uuid);
                    }

                    @Override
                    protected UUID getPlayerId(Object player) {
                        Player p = (Player) player;
                        return p.getUniqueId();
                    }
                }
        ),
        SPIGOT(
                () -> Validate.isClassLoaded("net.md_5.bungee.api.dialog.Dialog"),
                plugin -> new SpigotDialogManager(plugin, "betterdialogs")
        );

        private final BooleanSupplier isAvailable;
        private final Function<Plugin, DialogManager<?, ?, ?, ?, ?>> constructor;

        DialogManagerType(BooleanSupplier isAvailable, Function<Plugin, DialogManager<?, ?, ?, ?, ?>> constructor) {
            this.isAvailable = isAvailable;
            this.constructor = constructor;
        }

        public boolean isAvailable() {
            return isAvailable.getAsBoolean();
        }

        public DialogManager<?, ?, ?, ?, ?> create(Plugin plugin) {
            return constructor.apply(plugin);
        }
    }
}
