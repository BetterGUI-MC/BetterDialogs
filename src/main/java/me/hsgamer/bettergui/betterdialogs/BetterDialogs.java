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

import me.hsgamer.bettergui.api.addon.GetLogger;
import me.hsgamer.bettergui.api.addon.GetPlugin;
import me.hsgamer.bettergui.api.addon.Reloadable;
import me.hsgamer.bettergui.betterdialogs.config.MainConfig;
import me.hsgamer.bettergui.betterdialogs.menu.ConfirmationDialogMenu;
import me.hsgamer.bettergui.betterdialogs.menu.MultiActionDialogMenu;
import me.hsgamer.bettergui.betterdialogs.menu.NoticeDialogMenu;
import me.hsgamer.bettergui.betterdialogs.menu.ServerLinksDialogMenu;
import me.hsgamer.bettergui.betterdialogs.requirement.DialogSupportRequirement;
import me.hsgamer.bettergui.builder.MenuBuilder;
import me.hsgamer.bettergui.builder.RequirementBuilder;
import me.hsgamer.bettergui.util.SchedulerUtil;
import me.hsgamer.hscore.bukkit.config.BukkitConfig;
import me.hsgamer.hscore.config.proxy.ConfigGenerator;
import me.hsgamer.hscore.expansion.common.Expansion;
import me.hsgamer.hscore.expansion.extra.expansion.DataFolder;
import me.hsgamer.hscore.license.common.LicenseChecker;
import me.hsgamer.hscore.license.common.LicenseResult;
import me.hsgamer.hscore.license.polymart.PolymartLicenseChecker;
import me.hsgamer.hscore.license.spigotmc.SpigotLicenseChecker;
import me.hsgamer.hscore.logger.common.LogLevel;

import java.io.File;

public final class BetterDialogs implements Expansion, GetLogger, GetPlugin, Reloadable, DataFolder {
    private final MainConfig mainConfig = ConfigGenerator.newInstance(MainConfig.class, new BukkitConfig(new File(getDataFolder(), "config.yml")));

    @Override
    public boolean onLoad() {
        return DialogManagerProvider.init(
                mainConfig.dialogManager().toLowerCase(),
                getPlugin(),
                getLogger()
        );
    }

    @Override
    public void onEnable() {
        checkLicense();
        DialogManagerProvider.dialogManager().register();
        MenuBuilder.INSTANCE.register(ConfirmationDialogMenu::new, "confirmation-dialog", "confirm-dialog");
        MenuBuilder.INSTANCE.register(MultiActionDialogMenu::new, "multi-action-dialog", "action-dialog");
        MenuBuilder.INSTANCE.register(NoticeDialogMenu::new, "notice-dialog");
        MenuBuilder.INSTANCE.register(ServerLinksDialogMenu::new, "server-links-dialog", "links-dialog", "server-link-dialog", "link-dialog");
        if (DialogManagerProvider.DialogManagerType.VIAVERSION.isAvailable()) {
            RequirementBuilder.INSTANCE.register(DialogSupportRequirement::new, "dialog-support", "support-dialog");
        }
    }

    @Override
    public void onReload() {
        DialogManagerProvider.dialogManager().unregisterAllCustomActions();
    }

    @Override
    public void onDisable() {
        DialogManagerProvider.dialogManager().unregister();
    }

    private void checkLicense() {
        LicenseChecker licenseChecker = PolymartLicenseChecker.isAvailable()
                ? new PolymartLicenseChecker("8246", true, true)
                : new SpigotLicenseChecker("127759");
        SchedulerUtil.async().run(() -> {
            LicenseResult result = licenseChecker.checkLicense();
            switch (result.getStatus()) {
                case VALID:
                    getLogger().log(LogLevel.INFO, "Thank you for supporting BetterDialogs. Your support is greatly appreciated");
                    break;
                case INVALID:
                    getLogger().log(LogLevel.WARN, "Thank you for using BetterDialogs");
                    getLogger().log(LogLevel.WARN, "If you like this addon, please consider supporting it by purchasing from one of these platforms:");
                    getLogger().log(LogLevel.WARN, "- SpigotMC: https://www.spigotmc.org/resources/betterdialogs.127759/");
                    getLogger().log(LogLevel.WARN, "- Polymart: https://polymart.org/product/8246/betterdialogs");
                    break;
                case OFFLINE:
                    getLogger().log(LogLevel.WARN, "Cannot check your license for BetterDialogs. Please check your internet connection");
                    getLogger().log(LogLevel.WARN, "Note: You can still use this addon without a license, and there is no limit on the features");
                    getLogger().log(LogLevel.WARN, "However, if you like this addon, please consider supporting it by purchasing it from one of these platforms:");
                    getLogger().log(LogLevel.WARN, "- SpigotMC: https://www.spigotmc.org/resources/betterdialogs.127759/");
                    getLogger().log(LogLevel.WARN, "- Polymart: https://polymart.org/product/8246/betterdialogs");
                    break;
                case UNKNOWN:
                    getLogger().log(LogLevel.WARN, "Cannot check your license for BetterDialogs. Please try again later");
                    getLogger().log(LogLevel.WARN, "Note: You can still use this addon without a license, and there is no limit on the features");
                    break;
            }
        });
    }
}
