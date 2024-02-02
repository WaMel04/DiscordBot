package com.wamel.discordbot.command.disbot;

import com.wamel.discordbot.DiscordBot;
import com.wamel.discordbot.bot.JDAMain;
import com.wamel.discordbot.config.ConfigManager$Config;
import com.wamel.discordbot.data.DataManager$Config;
import com.wamel.discordbot.event.MinecraftEvent;
import com.wamel.discordbot.util.DiscordAPI;
import com.wamel.discordbot.util.ReadEPF;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.awt.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class CMD$Disbot$Ignore extends CMD$Disbot{

    public static void run(CommandSender sender, String[] args, String label) {
        if(args.length == 1) {
            sender.sendMessage(" §b/" + label + " ignore [닉네임] §7- 해당 유저가 인증 받지 않고도 서버 활동을 가능하게 합니다.");
            return;
        }

        CompletableFuture.runAsync(() -> {
            try {
                String uuid = ReadEPF.getUUID(args[1]).get(3, TimeUnit.SECONDS);

                if(uuid == null) {
                    sender.sendMessage("§c" + args[1] + "§6님은 서버에 접속한 적 없는 플레이어입니다.");
                    return;
                }
                if(DiscordBot.getIsVerified(uuid) == true) {
                    sender.sendMessage("§c" + args[1] + "§6님은 이미 인증이 되어있습니다.");
                    return;
                }

                ConfigManager$Config.saveData(uuid, -1l);

                sender.sendMessage("§c" + args[1] + "§6님은 이제 인증 받지 않고도 서버 활동이 가능합니다.");

                if(Bukkit.getPlayer(uuid) != null) {
                    Player target = Bukkit.getPlayer(uuid);
                    MinecraftEvent.unLinkedPlayerList.remove(target);
                    target.sendMessage("§6당신은 이제 인증 받지 않고도 서버 활동이 가능합니다.");
                }
            } catch (Exception e) {
                sender.sendMessage("§c" + args[1] + "님의 플레이어 데이터를 가져오는데 실패했습니다!");
                sender.sendMessage(e.getLocalizedMessage());
            }
        });

    }

}
