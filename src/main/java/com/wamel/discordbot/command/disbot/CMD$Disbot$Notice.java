package com.wamel.discordbot.command.disbot;

import com.wamel.discordbot.bot.JDAMain;
import com.wamel.discordbot.data.DataManager$Config;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.NewsChannel;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.bukkit.command.CommandSender;

import java.awt.*;

public class CMD$Disbot$Notice extends CMD$Disbot{

    public static void run(CommandSender sender, String[] args, String label) {
        if(args.length < 4) {
            sender.sendMessage(" §b/" + label + " notice [채널ID] [제목] [내용] §7- 해당 채널에 [내용]의 공지를 보냅니다.");
            sender.sendMessage(" §7띄어쓰기: _");
            return;
        }

        String title = args[2].replaceAll("_", " ");
        String message = args[3].replaceAll("_", " ");
        if(JDAMain.getJDA().getTextChannelById(args[1]) == null) {
            NewsChannel channel = JDAMain.getJDA().getNewsChannelById(args[1]);

            EmbedBuilder embed = new EmbedBuilder();
            embed.setColor(Color.CYAN);
            embed.setTitle(title);
            embed.setDescription(message);
            embed.setFooter(DataManager$Config.SERVER_NAME, DataManager$Config.DISCORD_PROFILE_ADDRESS);

            if(channel.canTalk()) {
                channel.sendMessageEmbeds(embed.build()).queue();
                sender.sendMessage(prefix + "해당 채널에 공지를 전송했습니다.");
            }
        } else {
            TextChannel channel = JDAMain.getJDA().getTextChannelById(args[1]);

            EmbedBuilder embed = new EmbedBuilder();
            embed.setColor(Color.CYAN);
            embed.setTitle(title);
            embed.setDescription(message);
            embed.setFooter(DataManager$Config.SERVER_NAME, DataManager$Config.DISCORD_PROFILE_ADDRESS);

            if(channel.canTalk()) {
                channel.sendMessageEmbeds(embed.build()).queue();
                sender.sendMessage(prefix + "해당 채널에 공지를 전송했습니다.");
            }
        }
    }
}
