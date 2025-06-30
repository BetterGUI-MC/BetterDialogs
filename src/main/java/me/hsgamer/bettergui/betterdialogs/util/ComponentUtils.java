package me.hsgamer.bettergui.betterdialogs.util;

import io.github.retrooper.packetevents.adventure.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.Component;

public interface ComponentUtils {
    static Component convertLegacy(String legacy) {
        return LegacyComponentSerializer.legacySection().deserialize(legacy);
    }
}
