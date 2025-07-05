package me.hsgamer.bettergui.betterdialogs.component.body;

import io.github.projectunified.unidialog.core.body.DialogBodyBuilder;
import io.github.projectunified.unidialog.core.dialog.Dialog;
import me.hsgamer.bettergui.betterdialogs.builder.DialogComponentBuilder;
import me.hsgamer.bettergui.betterdialogs.component.DialogComponent;
import org.bukkit.entity.Player;

public abstract class DialogBodyComponent extends DialogComponent {
    protected DialogBodyComponent(DialogComponentBuilder.Input input) {
        super(input);
    }

    protected abstract void apply(Player player, DialogBodyBuilder<?> builder);

    @Override
    public void apply(Player player, Dialog<?, ?, ?, ?> dialog) {
        dialog.body(builder -> apply(player, builder));
    }
}
