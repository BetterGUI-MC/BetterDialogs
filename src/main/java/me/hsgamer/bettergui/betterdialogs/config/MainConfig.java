package me.hsgamer.bettergui.betterdialogs.config;

import me.hsgamer.hscore.config.annotation.Comment;
import me.hsgamer.hscore.config.annotation.ConfigPath;

public interface MainConfig {
    @ConfigPath("dialog-manager")
    @Comment({
            "The dialog manager to use",
            "Available options: paper, packetevents, spigot, auto",
    })
    default String dialogManager() {
        return "auto";
    }
}
