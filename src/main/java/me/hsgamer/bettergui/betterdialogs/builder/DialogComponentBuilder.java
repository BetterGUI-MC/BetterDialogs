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
package me.hsgamer.bettergui.betterdialogs.builder;

import me.hsgamer.bettergui.betterdialogs.component.DialogComponent;
import me.hsgamer.bettergui.betterdialogs.component.action.*;
import me.hsgamer.bettergui.betterdialogs.component.body.ItemComponent;
import me.hsgamer.bettergui.betterdialogs.component.body.TextComponent;
import me.hsgamer.bettergui.betterdialogs.component.input.BooleanInputComponent;
import me.hsgamer.bettergui.betterdialogs.component.input.NumberInputComponent;
import me.hsgamer.bettergui.betterdialogs.component.input.SingleOptionInputComponent;
import me.hsgamer.bettergui.betterdialogs.component.input.TextInputComponent;
import me.hsgamer.bettergui.betterdialogs.menu.DialogMenu;
import me.hsgamer.hscore.builder.FunctionalMassBuilder;
import me.hsgamer.hscore.collections.map.CaseInsensitiveStringMap;

import java.util.Map;
import java.util.Objects;

public final class DialogComponentBuilder extends FunctionalMassBuilder<DialogComponentBuilder.Input, DialogComponent> {
    public static final DialogComponentBuilder INSTANCE = new DialogComponentBuilder();

    private DialogComponentBuilder() {
        register(TextComponent::new, "plain-message", "message", "plain", "text");
        register(ItemComponent::new, "item");

        register(TextInputComponent::new, "text-input", "input");
        register(BooleanInputComponent::new, "boolean-input", "boolean", "toggle", "checkbox", "switch");
        register(NumberInputComponent::new, "number-input", "number", "range", "slider");
        register(SingleOptionInputComponent::new, "single-option-input", "select", "combobox", "radio");

        register(CustomActionComponent::new, "custom-action-button", "custom-action", "custom", "action", "button");
        register(RunCommandActionComponent::new, "run-command-action", "run-command", "command", "execute", "run");
        register(OpenUrlActionComponent::new, "open-url-action", "open-url", "open-link", "link", "url");
        register(SuggestCommandActionComponent::new, "suggest-command-action", "suggest-command", "suggest", "suggestion");
        register(CopyToClipboardActionCommand::new, "copy-to-clipboard-action", "copy-to-clipboard", "copy", "clipboard");
    }

    @Override
    protected String getType(Input input) {
        Map<String, Object> keys = new CaseInsensitiveStringMap<>(input.options);
        return Objects.toString(keys.get("type"), "text");
    }

    public record Input(DialogMenu menu, String name, Map<String, Object> options) {
    }
}
