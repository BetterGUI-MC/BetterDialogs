package me.hsgamer.bettergui.betterdialogs;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.dialog.ConfirmationDialog;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerShowDialog;
import io.github.projectunified.minelib.plugin.command.CommandComponent;
import me.hsgamer.bettergui.api.addon.GetLogger;
import me.hsgamer.bettergui.api.addon.GetPlugin;
import me.hsgamer.bettergui.betterdialogs.listener.DialogCustomClickListener;
import me.hsgamer.hscore.expansion.common.Expansion;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class BetterDialogs implements Expansion, GetLogger, GetPlugin {
    @Override
    public void onEnable() {
        PacketEvents.getAPI().getEventManager().registerListener(new DialogCustomClickListener(this));
        getPlugin().get(CommandComponent.class).register(new Command("testdialog") {
            @Override
            public boolean execute(CommandSender commandSender, String s, String[] strings) {
                if (commandSender instanceof Player player) {
                    ConfirmationDialog dialog = TestDialog.createTestDialog();
                    WrapperPlayServerShowDialog packet = new WrapperPlayServerShowDialog(dialog);
                    PacketEvents.getAPI().getPlayerManager().sendPacket(player, packet);
                }
                return true;
            }
        });
    }
}
