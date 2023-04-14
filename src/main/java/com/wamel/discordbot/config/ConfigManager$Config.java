package com.wamel.discordbot.config;

import com.wamel.discordbot.DiscordBot;
import com.wamel.discordbot.bot.JDAMain;
import com.wamel.discordbot.data.DataManager$Config;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ConfigManager$Config {

    private static final DiscordBot plugin = DiscordBot.getInstance();

    public static Object readConfig(String key) {
        File file = new File(plugin.getDataFolder(), "config.yml");
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
        return yaml.get(key);
    }


    public static void load() {
        File file = new File(plugin.getDataFolder(), "config.yml");
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);

        DataManager$Config.TOKEN = yaml.getString("token");
        DataManager$Config.SERVER_NAME = yaml.getString("server-name");
        DataManager$Config.SERVER_ADDRESS = yaml.getString("server-address");
        DataManager$Config.GUILD_ID = yaml.getLong("guild-id");
        DataManager$Config.DISCORD_ADDRESS = yaml.getString("discord-address");
        DataManager$Config.DISCORD_PROFILE_ADDRESS = yaml.getString("discord-profile-address");
        DataManager$Config.PREVENT_MOVING = yaml.getBoolean("prevent-moving");
        DataManager$Config.ENABLE_MEMBER_INTENT = yaml.getBoolean("enable-member-intent");
        DataManager$Config.CHANGE_NICKNAME = yaml.getBoolean("change-nickname");
        DataManager$Config.GIVE_VERIFY_ROLE = yaml.getBoolean("give-verify-role");
        DataManager$Config.VERIFY_ROLE = yaml.getLong("verify-role");
        DataManager$Config.ALLOWED_COMMANDS = yaml.getStringList("allowed-commands");
    }

    public static void reload() {
        load();
        JDAMain.getJDA().shutdownNow();

        new BukkitRunnable() {
            @Override
            public void run() {
                DiscordBot.registerBot();
            }
        }.runTaskLater(plugin, 20);
    }

    public static Object getDiscordId(String uuid) {
        File file = new File(plugin.getDataFolder() + "//data.yml");
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
        Map<String, Object> map = yaml.getValues(false);
        return (Long) map.get(uuid);
    }

    public static Object getMinecraftUUID(Long discordID) {
        File file = new File(plugin.getDataFolder() + "//data.yml");
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
        Map<String, Object> map = yaml.getValues(false);
        for(String uuid : map.keySet()) { // uuid = 디스코드 ID 형식
            if(map.get(uuid).equals(discordID))
                return uuid;
        }
        return null;
    }

    public static void saveData(String uuid, Long discordID) {
        File file = new File(plugin.getDataFolder() + "//data.yml");
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
        yaml.set(uuid, discordID);
        try {
            yaml.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void removeData(String uuid) {
        File file = new File(plugin.getDataFolder() + "//data.yml");
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
        yaml.set(uuid, null);
        try {
            yaml.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
