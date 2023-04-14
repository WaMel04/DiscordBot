package com.wamel.discordbot.util;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.entity.Player;

public class ActionbarSender {

    public static void send(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new ComponentBuilder(message).create());
    }

}
