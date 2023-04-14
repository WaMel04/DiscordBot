package com.wamel.discordbot.data;

import com.wamel.discordbot.config.ConfigManager$Config;

import java.util.List;

public class DataManager$Config {

    public static String TOKEN = (String) ConfigManager$Config.readConfig("token");
    public static String SERVER_NAME = (String) ConfigManager$Config.readConfig("server-name");
    public static String SERVER_ADDRESS = (String) ConfigManager$Config.readConfig("server-address");
    public static Long GUILD_ID = (Long) ConfigManager$Config.readConfig("guild-id");
    public static String DISCORD_ADDRESS = (String) ConfigManager$Config.readConfig("discord-address");
    public static String DISCORD_PROFILE_ADDRESS = (String) ConfigManager$Config.readConfig("discord-profile-address");
    public static Boolean PREVENT_MOVING = (Boolean) ConfigManager$Config.readConfig("prevent-moving");
    public static Boolean CHANGE_NICKNAME = (Boolean) ConfigManager$Config.readConfig("change-nickname");
    public static Boolean GIVE_VERIFY_ROLE = (Boolean) ConfigManager$Config.readConfig("give-verify-role");
    public static Long VERIFY_ROLE = (Long) ConfigManager$Config.readConfig("verify-role");
    public static List<String> ALLOWED_COMMANDS = (List<String>) ConfigManager$Config.readConfig("allowed-commands");
}
