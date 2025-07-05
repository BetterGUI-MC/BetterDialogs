package me.hsgamer.bettergui.betterdialogs.component.input;

import io.github.projectunified.unidialog.core.dialog.Dialog;
import io.github.projectunified.unidialog.core.input.DialogInputBuilder;
import me.hsgamer.bettergui.betterdialogs.builder.DialogComponentBuilder;
import me.hsgamer.bettergui.betterdialogs.component.DialogComponent;
import org.bukkit.entity.Player;

import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public abstract class InputComponent<T> extends DialogComponent {
    private final String key;
    private final Map<UUID, T> values = new ConcurrentHashMap<>();

    protected InputComponent(DialogComponentBuilder.Input input) {
        super(input);
        key = normalizeName(getName());
    }

    private static String normalizeName(String name) {
        return name.replaceAll("[^a-zA-Z0-9_]", "_").toLowerCase(Locale.ROOT);
    }

    protected abstract void apply(Player player, DialogInputBuilder builder);

    protected abstract String getValue(T value, UUID uuid, String args);

    protected abstract T convertValue(UUID uuid, String rawValue);

    public String getValue(UUID uuid, String args) {
        T value = values.get(uuid);
        if (value == null) {
            return "";
        }
        return getValue(value, uuid, args);
    }

    public void applyValue(UUID uuid, Map<String, String> map) {
        String rawValue = map.get(key);
        if (rawValue != null) {
            T value = convertValue(uuid, rawValue);
            if (value != null) {
                values.put(uuid, value);
                return;
            }
        }
        values.remove(uuid);
    }

    @Override
    public void apply(Player player, Dialog<?, ?, ?, ?> dialog) {
        dialog.input(key, builder -> apply(player, builder));
    }
}
