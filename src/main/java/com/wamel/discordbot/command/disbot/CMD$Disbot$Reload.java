package com.wamel.discordbot.command.disbot;

import com.wamel.discordbot.config.ConfigManager$Config;
import com.wamel.discordbot.data.DataManager$Config;
import org.bukkit.command.CommandSender;

public class CMD$Disbot$Reload extends CMD$Disbot{

    public static void run(CommandSender sender) {
        ConfigManager$Config.reload();
        sender.sendMessage("");
        sender.sendMessage(prefix + "config를 리로드했습니다.");
        sender.sendMessage("");
        sender.sendMessage(" §a§l* 변경된 데이터");
        sender.sendMessage("");
        sender.sendMessage(" §7token: " + DataManager$Config.TOKEN);
        sender.sendMessage(" §7server-name: " + DataManager$Config.SERVER_NAME);
        sender.sendMessage(" §7server-address: " + DataManager$Config.SERVER_ADDRESS);
        sender.sendMessage(" §7guild-id: " + DataManager$Config.GUILD_ID);
        sender.sendMessage(" §7discord-address: " + DataManager$Config.DISCORD_ADDRESS);
        sender.sendMessage(" §7discord-profile-address: " + DataManager$Config.DISCORD_PROFILE_ADDRESS);
        sender.sendMessage(" §7prevent-moving: " + DataManager$Config.PREVENT_MOVING);
        sender.sendMessage(" §7change-nickname: " + DataManager$Config.CHANGE_NICKNAME);
        sender.sendMessage(" §7give-verify-role: " + DataManager$Config.GIVE_VERIFY_ROLE);
        sender.sendMessage(" §7verify-role: " + DataManager$Config.VERIFY_ROLE);
        sender.sendMessage(" §7allowed-commands: " + DataManager$Config.ALLOWED_COMMANDS);
        sender.sendMessage("");
    }
}
