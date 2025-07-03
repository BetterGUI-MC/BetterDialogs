package me.hsgamer.bettergui.betterdialogs.component.body;

import io.github.projectunified.unidialog.packetevents.body.PEDialogBodyBuilder;
import io.github.projectunified.unidialog.packetevents.dialog.PEDialog;
import me.hsgamer.bettergui.betterdialogs.builder.DialogComponentBuilder;
import me.hsgamer.bettergui.betterdialogs.component.DialogComponent;
import org.bukkit.entity.Player;

public abstract class DialogBodyComponent extends DialogComponent {
    protected DialogBodyComponent(DialogComponentBuilder.Input input) {
        super(input);
    }

    protected abstract void apply(Player player, PEDialogBodyBuilder builder);

    @Override
    public void apply(Player player, PEDialog<?> dialog) {
        dialog.body(builder -> apply(player, builder));
    }
}
