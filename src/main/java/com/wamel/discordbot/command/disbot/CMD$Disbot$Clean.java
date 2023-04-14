package com.wamel.discordbot.command.disbot;

import com.wamel.discordbot.bot.JDAMain;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.channel.concrete.NewsChannel;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.bukkit.command.CommandSender;

public class CMD$Disbot$Clean extends CMD$Disbot{

    public static void run(CommandSender sender, String[] args, String label) {
        if(args.length == 1) {
            sender.sendMessage(" §b/" + label + " clean [채널ID] §7- 해당 채널의 모든 메세지를 지웁니다.");
            return;
        }

        if(JDAMain.getJDA().getTextChannelById(args[1]) == null) {
            NewsChannel channel = JDAMain.getJDA().getNewsChannelById(args[1]);

            for(Message message : MessageHistory.getHistoryFromBeginning(channel).complete().getRetrievedHistory()) {
                message.delete().queue();
            }

            sender.sendMessage(prefix + "해당 채널의 메세지를 청소했습니다.");
        } else {
            TextChannel channel = JDAMain.getJDA().getTextChannelById(args[1]);

            for(Message message : MessageHistory.getHistoryFromBeginning(channel).complete().getRetrievedHistory()) {
                message.delete().queue();
            }

            sender.sendMessage(prefix + "해당 채널의 메세지를 청소했습니다.");
        }
    }
}
