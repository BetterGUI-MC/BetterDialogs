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

import me.hsgamer.hscore.common.MapUtils;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class SpigotTextGetter implements TextGetter {
    @Override
    public Optional<Text> get(Map<String, Object> input, String... keys) {
        return Optional.ofNullable(MapUtils.getIfFound(input, keys))
                .map(Object::toString)
                .map(Text::of);
    }

    @Override
    public Map<String, Text> getMap(Map<String, Object> input, String key) {
        Object value = MapUtils.getIfFound(input, key);
        if (value instanceof Map<?, ?> map) {
            return map.entrySet().stream()
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
