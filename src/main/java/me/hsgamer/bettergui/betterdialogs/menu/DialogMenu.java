package me.hsgamer.bettergui.betterdialogs.menu;

import me.hsgamer.bettergui.betterdialogs.constructor.DialogConstructor;
import me.hsgamer.bettergui.betterdialogs.constructor.DialogDataConstructor;
import me.hsgamer.bettergui.menu.BaseMenu;
import me.hsgamer.hscore.common.StringReplacer;
import me.hsgamer.hscore.config.Config;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class DialogMenu extends BaseMenu {
    private final Supplier<DialogConstructor> dialogConstructorSupplier;
    private final Map<String, DialogMenuComponent> componentMap = new LinkedHashMap<>();

    public DialogMenu(Config config, Supplier<DialogConstructor> dialogConstructorSupplier) {
        super(config);
        this.dialogConstructorSupplier = dialogConstructorSupplier;

        variableManager.register("form_", StringReplacer.of((original, uuid) -> {
            String[] split = original.split(":", 2);
            String component = split[0];
            String key = split.length > 1 ? split[1] : "";
            return Optional.ofNullable(componentMap.get(component))
                    .map(provider -> provider.getValue(uuid, key))
                    .orElse(null);
        }));
    }

    @Override
    protected boolean createChecked(Player player, String[] args, boolean bypass) {
        DialogDataConstructor dialogDataConstructor = DialogDataConstructor.create();
        DialogConstructor dialogConstructor = dialogConstructorSupplier.get();

        for (DialogMenuComponent component : componentMap.values()) {
            component.apply(player, dialogDataConstructor, dialogConstructor);
        }

        return true;
    }

    @Override
    public void update(Player player) {
        // EMPTY
    }

    @Override
    public void close(Player player) {
        // EMPTY
    }

    @Override
    public void closeAll() {
        // EMPTY
    }
}
