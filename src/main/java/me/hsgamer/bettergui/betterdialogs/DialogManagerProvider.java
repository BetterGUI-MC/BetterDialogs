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
package me.hsgamer.bettergui.betterdialogs;

import io.github.projectunified.minelib.scheduler.common.util.Platform;
import io.github.projectunified.unidialog.bungeecord.body.BungeeItemBody;
import io.github.projectunified.unidialog.core.DialogManager;
import io.github.projectunified.unidialog.core.body.ItemBody;
import io.github.projectunified.unidialog.packetevents.PocketEventsDialogManager;
import io.github.projectunified.unidialog.packetevents.body.PEItemBody;
import io.github.projectunified.unidialog.paper.PaperDialogManager;
import io.github.projectunified.unidialog.paper.body.PaperItemBody;
import io.github.projectunified.unidialog.spigot.SpigotDialogManager;
import io.github.projectunified.unidialog.viaversion.ViaVersionDialogManager;
import io.github.projectunified.unidialog.viaversion.body.ViaItemBody;
import io.github.projectunified.unidialog.viaversion.spigot.ViaItemUtil;
import io.github.retrooper.packetevents.util.SpigotReflectionUtil;
import me.hsgamer.bettergui.betterdialogs.text.PacketEventsTextGetter;
import me.hsgamer.bettergui.betterdialogs.text.PaperTextGetter;
import me.hsgamer.bettergui.betterdialogs.text.SpigotTextGetter;
import me.hsgamer.bettergui.betterdialogs.text.TextGetter;
import me.hsgamer.hscore.bukkit.utils.VersionUtils;
import me.hsgamer.hscore.common.Validate;
import me.hsgamer.hscore.logger.common.LogLevel;
import me.hsgamer.hscore.logger.common.Logger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Supplier;

public class DialogManagerProvider {
    private static DialogManager<?, ?, ?, ?, ?> dialogManager;
    private static TextGetter textGetter;
    private static BiConsumer<ItemStack, ItemBody<?, ?, ?>> itemConsumer;

    public static boolean init(String name, Plugin plugin, Logger logger) {
        DialogManagerType dialogManagerType = null;
        if (name.equals("auto")) {
            for (DialogManagerType type : DialogManagerType.values()) {
                if (type.isAvailable()) {
                    dialogManagerType = type;
                    break;
                }
            }
        } else {
            try {
                DialogManagerType type = DialogManagerType.valueOf(name.toUpperCase());
                if (type.isAvailable()) {
                    dialogManagerType = type;
                } else {
                    logger.log(LogLevel.WARN, "The specified dialog manager '" + name + "' is not available.");
                }
            } catch (IllegalArgumentException e) {
                logger.log(LogLevel.WARN, "Invalid dialog manager specified: " + name);
            }
        }
        if (dialogManagerType == null) {
            logger.log(LogLevel.ERROR, "No suitable dialog manager found. BetterDialogs will not be enabled.");
            return false;
        }

        logger.log(LogLevel.INFO, "Using " + dialogManagerType.name() + " for BetterDialogs");
        dialogManager = dialogManagerType.constructor.apply(plugin);
        textGetter = dialogManagerType.textGetterSupplier.get();
        itemConsumer = dialogManagerType.itemConsumer;
        return true;
    }

    public static DialogManager<?, ?, ?, ?, ?> dialogManager() {
        return dialogManager;
    }

    public static TextGetter textGetter() {
        return textGetter;
    }

    public static BiConsumer<ItemStack, ItemBody<?, ?, ?>> itemConsumer() {
        return itemConsumer;
    }

    private enum DialogManagerType {
        PAPER(
                () -> Platform.PAPER.isPlatform() && VersionUtils.isAtLeast(21, 7),
                plugin -> new PaperDialogManager(plugin, "betterdialogs"),
                PaperTextGetter::new,
                (itemStack, itemBody) -> ((PaperItemBody) itemBody).item(itemStack)
        ),
        PACKETEVENTS(
                () -> Bukkit.getPluginManager().getPlugin("packetevents") != null && VersionUtils.isAtLeast(21, 6),
                plugin -> new PocketEventsDialogManager("betterdialogs") {
                    @Override
                    protected @Nullable Player getPlayer(UUID uuid) {
                        return Bukkit.getPlayer(uuid);
                    }

                    @Override
                    protected UUID getPlayerId(Object player) {
                        Player p = (Player) player;
                        return p.getUniqueId();
                    }
                },
                PacketEventsTextGetter::new,
                (itemStack, itemBody) -> ((PEItemBody) itemBody).item(SpigotReflectionUtil.decodeBukkitItemStack(itemStack))
        ),
        SPIGOT(
                () -> Validate.isClassLoaded("net.md_5.bungee.api.dialog.Dialog"),
                plugin -> new SpigotDialogManager(plugin, "betterdialogs"),
                SpigotTextGetter::new,
                (itemStack, itemBody) -> ((BungeeItemBody) itemBody).item(itemStack)
        ),
        VIAVERSION(
                () -> Bukkit.getPluginManager().getPlugin("ViaVersion") != null,
                plugin -> new ViaVersionDialogManager("betterdialogs"),
                SpigotTextGetter::new,
                (itemStack, itemBody) -> ((ViaItemBody) itemBody).item(ViaItemUtil.fromItemStack(itemStack))
        );

        private final BooleanSupplier isAvailable;
        private final Function<Plugin, DialogManager<?, ?, ?, ?, ?>> constructor;
        private final Supplier<TextGetter> textGetterSupplier;
        private final BiConsumer<ItemStack, ItemBody<?, ?, ?>> itemConsumer;

        DialogManagerType(BooleanSupplier isAvailable, Function<Plugin, DialogManager<?, ?, ?, ?, ?>> constructor, Supplier<TextGetter> textGetterSupplier, BiConsumer<ItemStack, ItemBody<?, ?, ?>> itemConsumer) {
            this.isAvailable = isAvailable;
            this.constructor = constructor;
            this.textGetterSupplier = textGetterSupplier;
            this.itemConsumer = itemConsumer;
        }

        public boolean isAvailable() {
            return isAvailable.getAsBoolean();
        }
    }
}
