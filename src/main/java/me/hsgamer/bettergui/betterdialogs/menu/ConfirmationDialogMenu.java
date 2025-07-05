package me.hsgamer.bettergui.betterdialogs.menu;

import io.github.projectunified.unidialog.core.dialog.Dialog;
import me.hsgamer.bettergui.betterdialogs.BetterDialogs;
import me.hsgamer.hscore.config.Config;
import org.bukkit.entity.Player;

public class ConfirmationDialogMenu extends DialogMenu {
    public ConfirmationDialogMenu(BetterDialogs instance, Config config) {
        super(instance, config);
    }

    @Override
    protected Dialog<?, ?, ?, ?> createDialogConstructor(Player player) {
        return instance.dialogManager().createConfirmationDialog();
    }
}
