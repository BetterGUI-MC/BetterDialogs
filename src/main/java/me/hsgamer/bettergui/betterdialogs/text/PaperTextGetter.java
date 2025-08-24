/*
   Copyright 2025-2025 Huynh Tien

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
package me.hsgamer.bettergui.betterdialogs.text;

import io.github.miniplaceholders.api.MiniPlaceholders;
import me.hsgamer.hscore.common.MapUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PaperTextGetter implements TextGetter {
    private static final boolean IS_MINI_PLACEHOLDERS_SUPPORTED;

    static {
        boolean isMiniPlaceholdersSupported = false;
        if (Bukkit.getPluginManager().getPlugin("MiniPlaceholders") != null) {
            try {
                Class<?> clazz = Class.forName("io.github.miniplaceholders.api.MiniPlaceholders");
                clazz.getDeclaredMethod("audienceGlobalPlaceholders");
                Class<?> pointerClass = Class.forName("net.kyori.adventure.pointer.Pointered");
                MiniMessage.class.getDeclaredMethod("deserialize", String.class, pointerClass, TagResolver.class);
                isMiniPlaceholdersSupported = true;
            } catch (Exception e) {
                // IGNORE
            }
        }
        IS_MINI_PLACEHOLDERS_SUPPORTED = isMiniPlaceholdersSupported;
    }

    public Component miniMessage(String input, Player player) {
        if (IS_MINI_PLACEHOLDERS_SUPPORTED) {
            TagResolver tagResolver = MiniPlaceholders.audienceGlobalPlaceholders();
            return MiniMessage.miniMessage().deserialize(input, player, tagResolver);
        }
        return MiniMessage.miniMessage().deserialize(input);
    }

    @Override
    public Optional<Text> get(Map<String, Object> input, String... keys) {
        String[] miniKeys = Arrays.stream(keys).flatMap(k -> Stream.of("mini-" + k, k + "$")).toArray(String[]::new);
        Optional<String> miniText = Optional.ofNullable(MapUtils.getIfFound(input, miniKeys)).map(Object::toString);
        if (miniText.isPresent()) {
            return Optional.of(new Text(true, miniText.get(), this::miniMessage));
        }

        String[] jsonKeys = Arrays.stream(keys).map(k -> "json-" + k).toArray(String[]::new);
        Optional<String> jsonText = Optional.ofNullable(MapUtils.getIfFound(input, jsonKeys)).map(Object::toString);
        if (jsonText.isPresent()) {
            return Optional.of(new Text(true, jsonText.get(), (s, p) -> GsonComponentSerializer.gson().deserialize(s)));
        }

        Optional<String> plainText = Optional.ofNullable(MapUtils.getIfFound(input, keys)).map(Object::toString);
        return plainText.map(s -> new Text(false, s, (str, p) -> str));
    }

    @Override
    public Map<String, Text> getMap(Map<String, Object> input, String key) {
        Object miniValue = MapUtils.getIfFound(input, "mini-" + key, key + "$");
        if (miniValue instanceof Map<?, ?> miniMap) {
            return miniMap.entrySet().stream()
                    .filter(e -> e.getKey() instanceof String)
                    .filter(e -> e.getValue() != null)
                    .map(e -> Map.entry((String) e.getKey(), new Text(true, e.getValue().toString(), this::miniMessage)))
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (a, b) -> a // In case of duplicate keys, keep the first one
                    ));
        }

        Object jsonValue = MapUtils.getIfFound(input, "json-" + key);
        if (jsonValue instanceof Map<?, ?> jsonMap) {
            return jsonMap.entrySet().stream()
                    .filter(e -> e.getKey() instanceof String)
                    .filter(e -> e.getValue() != null)
                    .map(e -> Map.entry((String) e.getKey(), new Text(true, e.getValue().toString(), (s, p) -> GsonComponentSerializer.gson().deserialize(s))))
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (a, b) -> a // In case of duplicate keys, keep the first one
                    ));
        }

        Object plainValue = MapUtils.getIfFound(input, key);
        if (plainValue instanceof Map<?, ?> plainMap) {
            return plainMap.entrySet().stream()
                    .filter(e -> e.getKey() instanceof String)
                    .filter(e -> e.getValue() != null)
                    .map(e -> Map.entry((String) e.getKey(), Text.of(e.getValue().toString())))
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            Map.Entry::getValue,
                            (a, b) -> a // In case of duplicate keys, keep the first one
                    ));
        }

        return Map.of();
    }
}
