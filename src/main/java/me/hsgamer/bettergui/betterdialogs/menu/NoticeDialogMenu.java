package me.hsgamer.bettergui.betterdialogs.menu;

import io.github.projectunified.unidialog.core.dialog.Dialog;
import me.hsgamer.bettergui.betterdialogs.BetterDialogs;
import me.hsgamer.hscore.config.Config;
import org.bukkit.entity.Player;

public class NoticeDialogMenu extends DialogMenu {
    public NoticeDialogMenu(BetterDialogs instance, Config config) {
        super(instance, config);
    }

    @Override
    protected Dialog<?, ?, ?, ?> createDialogConstructor(Player player) {
        return instance.dialogManager().createNoticeDialog();
    }
}
