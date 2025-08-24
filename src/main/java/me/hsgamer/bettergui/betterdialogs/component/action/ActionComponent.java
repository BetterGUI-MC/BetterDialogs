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

import io.github.projectunified.unidialog.adventure.action.AdventureDialogActionBuilder;
import io.github.projectunified.unidialog.core.action.DialogActionBuilder;
import io.github.projectunified.unidialog.core.dialog.*;
import me.hsgamer.bettergui.betterdialogs.DialogManagerProvider;
import me.hsgamer.bettergui.betterdialogs.builder.DialogComponentBuilder;
import me.hsgamer.bettergui.betterdialogs.component.DialogComponent;
import me.hsgamer.bettergui.betterdialogs.text.Text;
import me.hsgamer.bettergui.util.StringReplacerApplier;
import me.hsgamer.hscore.common.Validate;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Consumer;

public abstract class ActionComponent extends DialogComponent {
    private final Text label;
    private final @Nullable Text tooltip;
    private final int width;
    private final @Nullable String assign;

    protected ActionComponent(DialogComponentBuilder.Input input) {
        super(input);
        label = DialogManagerProvider.textGetter().get(input.options(), "label")
                .orElseGet(() -> Text.of("Action"));
        tooltip = DialogManagerProvider.textGetter().get(input.options(), "tooltip")
                .orElse(null);
        width = Optional.ofNullable(input.options().get("width"))
                .map(Object::toString)
                .flatMap(Validate::getNumber)
                .map(Number::intValue)
                .filter(width -> width > 0)
                .orElse(150);
        assign = Optional.ofNullable(input.options().get("assign"))
                .map(Object::toString)
                .orElse(null);
    }

    protected abstract void getAction(Player player, DialogActionBuilder<?, ?> builder);

    @Override
    public void apply(Player player, Dialog<?, ?, ?, ?> dialog) {
        Consumer<DialogActionBuilder<?, ?>> actionConsumer = builder -> {
            builder.width(width);

            String replacedLabel = StringReplacerApplier.replace(label.text(), player.getUniqueId(), this);
            if (label.isAdventure() && builder instanceof AdventureDialogActionBuilder<?, ?> adventureBuilder) {
                adventureBuilder.label((Component) label.parser().apply(replacedLabel, player));
            } else {
                builder.label(replacedLabel);
            }

            if (tooltip != null) {
                String replacedTooltip = StringReplacerApplier.replace(tooltip.text(), player.getUniqueId(), this);
                if (tooltip.isAdventure() && builder instanceof AdventureDialogActionBuilder<?, ?> adventureBuilder) {
                    adventureBuilder.tooltip((Component) tooltip.parser().apply(replacedTooltip, player));
                } else {
                    builder.tooltip(replacedTooltip);
                }
            }

            getAction(player, builder);
        };
        switch (dialog) {
            case ConfirmationDialog<?, ?, ?, ?, ?, ?> confirmationDialog -> {
                if (assign == null) {
                    if (!confirmationDialog.hasYesAction()) {
                        confirmationDialog.yesAction(actionConsumer::accept);
                    } else {
                        confirmationDialog.noAction(actionConsumer::accept);
                    }
                } else if (assign.equalsIgnoreCase("yes")) {
                    confirmationDialog.yesAction(actionConsumer::accept);
                } else if (assign.equalsIgnoreCase("no")) {
                    confirmationDialog.noAction(actionConsumer::accept);
                }
            }
            case MultiActionDialog<?, ?, ?, ?, ?, ?> multiActionDialog -> {
                if (assign == null || !assign.equalsIgnoreCase("exit")) {
                    multiActionDialog.action(actionConsumer::accept);
                } else {
                    multiActionDialog.exitAction(actionConsumer::accept);
                }
            }
            case NoticeDialog<?, ?, ?, ?, ?, ?> noticeDialog -> noticeDialog.action(actionConsumer::accept);
            case ServerLinksDialog<?, ?, ?, ?, ?, ?> serverLinksDialog ->
                    serverLinksDialog.exitAction(actionConsumer::accept);
            case null, default -> {
            }
        }
    }
}
