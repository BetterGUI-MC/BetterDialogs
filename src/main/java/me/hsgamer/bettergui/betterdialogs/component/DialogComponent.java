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
package me.hsgamer.bettergui.betterdialogs.component;

import io.github.projectunified.unidialog.core.dialog.Dialog;
import me.hsgamer.bettergui.api.menu.MenuElement;
import me.hsgamer.bettergui.betterdialogs.builder.DialogComponentBuilder;
import me.hsgamer.bettergui.betterdialogs.menu.DialogMenu;
import org.bukkit.entity.Player;

public abstract class DialogComponent implements MenuElement {
    private final String name;
    private final DialogMenu menu;

    protected DialogComponent(DialogComponentBuilder.Input input) {
        this.name = input.name();
        this.menu = input.menu();
    }

    public abstract void apply(Player player, Dialog<?, ?, ?, ?> dialog);

    public String getName() {
        return name;
    }

    @Override
    public DialogMenu getMenu() {
        return menu;
    }
}
