package com.wamel.discordbot.util;

import com.wamel.discordbot.DiscordBot;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.UUID;

public class ReadEPF {
    private static final DiscordBot plugin = DiscordBot.getInstance();

    public static String getUUID(String name) {

        if(Bukkit.getPlayer(name) != null) {
            return Bukkit.getPlayer(name).getUniqueId().toString();
        }
        if(name.contains("-")) {
            return name;
        }
        File folder = new File(plugin.getDataFolder().getParentFile() + "//Essentials//userdata");
        for (File file : folder.listFiles()) {
            YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
            if(yaml.getString("last-account-name") == null) {
                if(yaml.getString("lastAccountName").equalsIgnoreCase(name)) {
                    return file.getName().replace(".yml", "");
                }
            } else {
                if(yaml.getString("last-account-name").equalsIgnoreCase(name)) {
                    return file.getName().replace(".yml", "");
                }
            }
        }
        return null;
    }

    public static String getName(String uuid) {
        if(Bukkit.getPlayer(UUID.fromString(uuid)) != null) {
            return Bukkit.getPlayer(UUID.fromString(uuid)).getName();
        }
        File folder = new File(plugin.getDataFolder().getParentFile() + "//Essentials//userdata");
        for (File file : folder.listFiles()) {
            if(file.getName().replace(".yml", "").equalsIgnoreCase(uuid)) {
                YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
                if(yaml.getString("last-account-name") == null)
                    return yaml.getString("lastAccountName");
                else
                    return yaml.getString("last-account-name");
            }
        }
        return null;
    }
}
