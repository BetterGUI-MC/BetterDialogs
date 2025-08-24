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

import io.github.retrooper.packetevents.adventure.serializer.gson.GsonComponentSerializer;
import me.hsgamer.hscore.common.MapUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class PacketEventsTextGetter implements TextGetter {
    @Override
    public Optional<Text> get(Map<String, Object> input, String... keys) {
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
        Object value = MapUtils.getIfFound(input, "json-" + key);
        if (value instanceof Map<?, ?> map) {
            return map.entrySet().stream()
                    .filter(e -> e.getKey() instanceof String)
                    .filter(e -> e.getValue() != null)
                    .map(e -> Map.entry((String) e.getKey(), new Text(false, e.getValue().toString(), (s, p) -> GsonComponentSerializer.gson().deserialize(s))))
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
