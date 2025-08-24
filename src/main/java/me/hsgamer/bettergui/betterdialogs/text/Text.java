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

import org.bukkit.entity.Player;

import java.util.function.BiFunction;

public record Text(boolean isAdventure, String text, BiFunction<String, Player, Object> parser) {
    public static Text of(String text) {
        return new Text(false, text, (s, p) -> s);
    }
}
