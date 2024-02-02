package com.wamel.discordbot.event;

import com.wamel.discordbot.DiscordBot;
import com.wamel.discordbot.bot.JDAMain;
import com.wamel.discordbot.command.CMD$Verify;
import com.wamel.discordbot.config.ConfigManager$Config;
import com.wamel.discordbot.data.DataManager$Config;
import com.wamel.discordbot.util.ActionbarSender;
import net.dv8tion.jda.api.entities.Activity;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;

public class MinecraftEvent implements Listener {
    private static final DiscordBot plugin = DiscordBot.getInstance();
    public static ArrayList<Player> unLinkedPlayerList = new ArrayList<>();

    @EventHandler(priority = EventPriority.NORMAL)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        JDAMain.getJDA().getPresence().setActivity(Activity.playing(Bukkit.getOnlinePlayers().size() + "명이 서버를 플레이"));

        if(player.isOp() || player.hasPermission("verify.bypass"))
            return;
        if(DiscordBot.getIsVerified(player.getUniqueId().toString()) == false) {
            unLinkedPlayerList.add(player);
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.sendMessage(CMD$Verify.prefix + "디스코드 연동이 되어있지 않습니다! 디스코드 연동을 해주세요.");
                }
            }.runTaskLater(plugin, 40);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        unLinkedPlayerList.remove(player);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            JDAMain.getJDA().getPresence().setActivity(Activity.playing(Bukkit.getOnlinePlayers().size() + "명이 서버를 플레이"));
        }, 1L);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();

        if(player.isOp() || player.hasPermission("verify.bypass"))
            return;
        if(unLinkedPlayerList.contains(player)) {
            for(String allowedCmd : DataManager$Config.ALLOWED_COMMANDS) {
                String command = event.getMessage().replaceAll("/", "").split(" ")[0];
                if (command.equalsIgnoreCase(allowedCmd))
                    return;
            }

            player.sendMessage(CMD$Verify.prefix + "디스코드 연동이 되어있지 않습니다. §b/인증");
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if(player.isOp() || player.hasPermission("verify.bypass"))
            return;
        if(unLinkedPlayerList.contains(player)) {
            if(DataManager$Config.PREVENT_MOVING == true) {
                event.setCancelled(true);
                ActionbarSender.send(player, "§b▶ §f디스코드 연동이 되어있지 않습니다. §b/인증");
            }
        }
    }
}