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
package me.hsgamer.bettergui.betterdialogs.component.body;

import io.github.projectunified.unidialog.core.body.DialogBodyBuilder;
import io.github.projectunified.unidialog.core.dialog.Dialog;
import me.hsgamer.bettergui.betterdialogs.builder.DialogComponentBuilder;
import me.hsgamer.bettergui.betterdialogs.component.DialogComponent;
import org.bukkit.entity.Player;

public abstract class DialogBodyComponent extends DialogComponent {
    protected DialogBodyComponent(DialogComponentBuilder.Input input) {
        super(input);
    }

    protected abstract void apply(Player player, DialogBodyBuilder<?> builder);

    @Override
    public void apply(Player player, Dialog<?, ?, ?, ?> dialog) {
        dialog.body(builder -> apply(player, builder));
    }
}
