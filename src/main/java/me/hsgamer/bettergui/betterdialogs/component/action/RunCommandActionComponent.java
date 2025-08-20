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
package me.hsgamer.bettergui.betterdialogs.component.action;

import io.github.projectunified.unidialog.core.action.DialogActionBuilder;
import me.hsgamer.bettergui.betterdialogs.builder.DialogComponentBuilder;
import me.hsgamer.bettergui.util.StringReplacerApplier;
import org.bukkit.entity.Player;

import java.util.Objects;

public class RunCommandActionComponent extends ActionComponent {
    private final String command;
    private final boolean isDynamic;

    public RunCommandActionComponent(DialogComponentBuilder.Input input) {
        super(input);
        if (input.options().containsKey("command")) {
            command = Objects.toString(input.options().get("command"));
            isDynamic = false;
        } else if (input.options().containsKey("template")) {
            command = Objects.toString(input.options().get("template"));
            isDynamic = true;
        } else {
            command = "";
            isDynamic = false;
        }
    }

    @Override
    protected void getAction(Player player, DialogActionBuilder<?, ?> builder) {
        String replacedCommand = StringReplacerApplier.replace(command, player.getUniqueId(), this);
        if (isDynamic) {
            builder.dynamicRunCommand(replacedCommand);
        } else {
            builder.runCommand(replacedCommand);
        }
    }
}
