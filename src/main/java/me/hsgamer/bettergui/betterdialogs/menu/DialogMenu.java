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
package me.hsgamer.bettergui.betterdialogs.menu;

import io.github.projectunified.unidialog.adventure.dialog.AdventureDialog;
import io.github.projectunified.unidialog.core.dialog.Dialog;
import io.github.projectunified.unidialog.core.payload.DialogPayload;
import me.hsgamer.bettergui.betterdialogs.DialogManagerProvider;
import me.hsgamer.bettergui.betterdialogs.builder.DialogComponentBuilder;
import me.hsgamer.bettergui.betterdialogs.component.DialogComponent;
import me.hsgamer.bettergui.betterdialogs.component.input.InputComponent;
import me.hsgamer.bettergui.betterdialogs.text.Text;
import me.hsgamer.bettergui.menu.BaseMenu;
import me.hsgamer.bettergui.util.ProcessApplierConstants;
import me.hsgamer.bettergui.util.SchedulerUtil;
import me.hsgamer.bettergui.util.StringReplacerApplier;
import me.hsgamer.hscore.collections.map.CaseInsensitiveStringMap;
import me.hsgamer.hscore.common.MapUtils;
import me.hsgamer.hscore.common.StringReplacer;
import me.hsgamer.hscore.config.Config;
import me.hsgamer.hscore.task.BatchRunnable;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public abstract class DialogMenu extends BaseMenu {
    private final Map<String, DialogComponent> componentMap = new LinkedHashMap<>();

    private final Text title;
    private final @Nullable Text externalTitle;
    private final boolean canCloseWithEscape;
    private final Dialog.AfterAction afterAction;

    DialogMenu(Config config) {
        super(config);

        title = DialogManagerProvider.textGetter().get(menuSettings, "title")
                .orElseGet(() -> Text.of("Dialog Menu"));
        externalTitle = DialogManagerProvider.textGetter().get(menuSettings, "external-title")
                .orElse(null);
        canCloseWithEscape = Optional.ofNullable(menuSettings.get("can-close-with-escape"))
                .map(Object::toString)
                .map(Boolean::parseBoolean)
                .orElse(true);
        afterAction = Optional.ofNullable(MapUtils.getIfFound(menuSettings, "after-action", "after"))
                .map(Object::toString)
                .map(s -> {
                    try {
                        return Dialog.AfterAction.valueOf(s.toUpperCase());
                    } catch (Exception e) {
                        return null;
                    }
                })
                .orElse(Dialog.AfterAction.CLOSE);

        for (Map.Entry<String, Object> configEntry : configSettings.entrySet()) {
            String key = configEntry.getKey();
            MapUtils.castOptionalStringObjectMap(configEntry.getValue())
                    .map(CaseInsensitiveStringMap::new)
                    .map(map -> new DialogComponentBuilder.Input(this, key, map))
                    .flatMap(DialogComponentBuilder.INSTANCE::build)
                    .ifPresent(customFormComponent -> componentMap.put(key, customFormComponent));
        }

        variableManager.register("dialog_", StringReplacer.of((original, uuid) -> {
            String[] split = original.split(":", 2);
            String component = split[0];
            String key = split.length > 1 ? split[1] : "";
            return Optional.ofNullable(componentMap.get(component))
                    .filter(InputComponent.class::isInstance)
                    .map(InputComponent.class::cast)
                    .map(provider -> provider.getValue(uuid, key))
                    .orElse(null);
        }));
    }

    protected abstract Dialog<?, ?, ?, ?> createDialogConstructor(Player player);

    public Dialog<?, ?, ?, ?> createDialog(Player player) {
        Dialog<?, ?, ?, ?> dialog = createDialogConstructor(player)
                .canCloseWithEscape(canCloseWithEscape)
                .afterAction(afterAction);

        String replacedTitle = StringReplacerApplier.replace(title.text(), player.getUniqueId(), this);
        if (title.isAdventure() && dialog instanceof AdventureDialog<?, ?, ?, ?> adventureDialog) {
            adventureDialog.title((Component) title.parser().apply(replacedTitle, player));
        } else {
            dialog.title(replacedTitle);
        }

        if (externalTitle != null) {
            String replacedExternalTitle = StringReplacerApplier.replace(externalTitle.text(), player.getUniqueId(), this);
            if (externalTitle.isAdventure() && dialog instanceof AdventureDialog<?, ?, ?, ?> adventureDialog) {
                adventureDialog.externalTitle((Component) externalTitle.parser().apply(replacedExternalTitle, player));
            } else {
                dialog.externalTitle(replacedExternalTitle);
            }
        }

        for (DialogComponent component : componentMap.values()) {
            component.apply(player, dialog);
        }

        return dialog;
    }

    private void applyInputs(DialogPayload payload) {
        for (DialogComponent component : componentMap.values()) {
            if (component instanceof InputComponent<?> inputComponent) {
                inputComponent.applyValue(payload);
            }
        }
    }

    public String registerAction(String actionName, Consumer<UUID> action) {
        String id = getName() + "_" + actionName;
        DialogManagerProvider.dialogManager().registerCustomAction(id, payload -> {
            applyInputs(payload);
            action.accept(payload.owner());
        });
        return id;
    }

    @Override
    protected boolean createChecked(Player player, String[] args, boolean bypass) {
        UUID uuid = player.getUniqueId();
        if (createDialog(player).opener().open(uuid)) {
            if (!openActionApplier.isEmpty()) {
                BatchRunnable batchRunnable = new BatchRunnable();
                batchRunnable.getTaskPool(ProcessApplierConstants.ACTION_STAGE).addLast(process -> openActionApplier.accept(uuid, process));
                SchedulerUtil.async().run(batchRunnable);
            }
            return true;
        }
        return false;
    }

    @Override
    public void update(Player player) {
        createDialog(player).opener().open(player.getUniqueId());
    }

    @Override
    public void close(Player player) {
        if (!DialogManagerProvider.dialogManager().clearDialog(player.getUniqueId())) {
            player.closeInventory();
        }
    }

    @Override
    public void closeAll() {
        // EMPTY
    }
}
