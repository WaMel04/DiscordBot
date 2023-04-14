package com.wamel.discordbot.util;

import com.wamel.discordbot.bot.JDAMain;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;

public class DiscordAPI {

    public static User getDiscordUser(Long discordId) {
        return JDAMain.getJDA().retrieveUserById(discordId).complete();
    }

    public static void sendPrivateMessage(User user, String message) {
        user.openPrivateChannel().queue(channel -> {
            channel.sendMessage(message).queue();
        });
    }

    public static void sendPrivateMessageEmbed(User user, EmbedBuilder embed) {
        user.openPrivateChannel().queue(channel -> {
            channel.sendMessageEmbeds(embed.build()).queue();
        });
    }

}
