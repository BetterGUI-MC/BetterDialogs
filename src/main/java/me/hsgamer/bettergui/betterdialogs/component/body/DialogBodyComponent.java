package me.hsgamer.bettergui.betterdialogs.component.body;

import com.github.retrooper.packetevents.protocol.dialog.body.DialogBody;
import me.hsgamer.bettergui.betterdialogs.builder.DialogComponentBuilder;
import me.hsgamer.bettergui.betterdialogs.component.DialogComponent;
import me.hsgamer.bettergui.betterdialogs.constructor.DialogConstructor;
import me.hsgamer.bettergui.betterdialogs.constructor.DialogDataConstructor;
import org.bukkit.entity.Player;

public abstract class DialogBodyComponent extends DialogComponent {
    protected DialogBodyComponent(DialogComponentBuilder.Input input) {
        super(input);
    }

    protected abstract DialogBody create(Player player);

    @Override
    public void apply(Player player, DialogDataConstructor dialogDataConstructor, DialogConstructor dialogConstructor) {
        DialogBody dialogBody = create(player);
        dialogDataConstructor.addBody(dialogBody);
    }
}
