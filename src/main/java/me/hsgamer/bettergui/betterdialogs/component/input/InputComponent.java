package me.hsgamer.bettergui.betterdialogs.component.input;

import com.github.retrooper.packetevents.protocol.dialog.input.Input;
import com.github.retrooper.packetevents.protocol.dialog.input.InputControl;
import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import me.hsgamer.bettergui.betterdialogs.builder.DialogComponentBuilder;
import me.hsgamer.bettergui.betterdialogs.component.DialogComponent;
import me.hsgamer.bettergui.betterdialogs.constructor.DialogConstructor;
import me.hsgamer.bettergui.betterdialogs.constructor.DialogDataConstructor;
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

    protected abstract InputControl createControl(Player player);

    protected abstract String getValue(T value, String args);

    protected abstract T getValue(NBT nbt);

    public String getValue(UUID uuid, String args) {
        T value = values.get(uuid);
        if (value == null) {
            return "";
        }
        return getValue(value, args);
    }

    public void applyValue(UUID uuid, NBTCompound nbtCompound) {
        NBT nbt = nbtCompound.getTagOrNull(key);
        if (nbt != null) {
            T value = getValue(nbt);
            values.put(uuid, value);
        } else {
            values.remove(uuid);
        }
    }

    @Override
    public void apply(Player player, DialogDataConstructor dialogDataConstructor, DialogConstructor dialogConstructor) {
        Input input = new Input(key, createControl(player));
        dialogDataConstructor.addInput(input);
    }
}
