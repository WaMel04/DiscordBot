package com.wamel.discordbot.config;

import com.wamel.discordbot.DiscordBot;
import com.wamel.discordbot.bot.JDAMain;
import com.wamel.discordbot.data.DataManager$Config;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ConfigManager$Config {

    private static final DiscordBot plugin = DiscordBot.getInstance();

    public static Object readConfig(String key) {
        File file = new File(plugin.getDataFolder() + "//config.yml");
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
        return yaml.get(key);
    }

    public static void reload() {
        DataManager$Config.TOKEN = (String) readConfig("token");
        DataManager$Config.SERVER_NAME = (String) readConfig("server-name");
        DataManager$Config.SERVER_ADDRESS = (String) readConfig("server-address");
        DataManager$Config.GUILD_ID = (Long) readConfig("guild-id");
        DataManager$Config.DISCORD_ADDRESS = (String) readConfig("discord-address");
        DataManager$Config.DISCORD_PROFILE_ADDRESS = (String) readConfig("discord-profile-address");
        DataManager$Config.PREVENT_MOVING = (Boolean) ConfigManager$Config.readConfig("prevent-moving");
        DataManager$Config.CHANGE_NICKNAME = (Boolean) ConfigManager$Config.readConfig("change-nickname");
        DataManager$Config.GIVE_VERIFY_ROLE = (Boolean) ConfigManager$Config.readConfig("give-verify-role");
        DataManager$Config.VERIFY_ROLE = (Long) ConfigManager$Config.readConfig("verify-role");
        DataManager$Config.ALLOWED_COMMANDS = (List<String>) ConfigManager$Config.readConfig("allowed-commands");
        JDAMain.getJDA().shutdownNow();

        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    JDAMain.start();
                    plugin.getLogger().info("§a봇이 활성화되었습니다.");
                } catch (LoginException e) {
                    e.printStackTrace();
                    plugin.getLogger().warning("§c봇 활성화에 실패했습니다!");
                }
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
