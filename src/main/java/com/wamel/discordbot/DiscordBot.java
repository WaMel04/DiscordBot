package com.wamel.discordbot;

import com.wamel.discordbot.bot.JDAMain;
import com.wamel.discordbot.command.CMD$Verify;
import com.wamel.discordbot.command.disbot.CMD$Disbot;
import com.wamel.discordbot.config.ConfigManager$Config;
import com.wamel.discordbot.data.DataManager$Config;
import com.wamel.discordbot.event.MinecraftEvent;
import com.wamel.discordbot.util.DiscordAPI;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.NewsChannel;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import javax.security.auth.login.LoginException;
import java.awt.*;

public class DiscordBot extends JavaPlugin implements Listener {

    private static DiscordBot instance;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();
        ConfigManager$Config.load();

        registerCommands();
        registerEvents();
        registerBot();

        if (!getServer().getPluginManager().isPluginEnabled(this))
            return;

        Bukkit.getConsoleSender().sendMessage("§a플러그인이 활성화되었습니다. by WaMel_ (종현#7737)");

        for(Player player : Bukkit.getOnlinePlayers()) {
            if(getIsVerified(player.getUniqueId().toString()) == false)
                if(player.isOp() || player.hasPermission("verify.bypass"))
                    continue;
                MinecraftEvent.unLinkedPlayerList.add(player);
        }
    }

    @Override
    public void onDisable() {
        if (JDAMain.getJDA() != null)
            JDAMain.getJDA().shutdownNow();
    }

    public static DiscordBot getInstance() {
        return instance;
    }

    private void registerCommands() {
        getCommand("인증").setExecutor(new CMD$Verify());
        getCommand("verify").setExecutor(new CMD$Verify());
        getCommand("disbot").setExecutor(new CMD$Disbot());
        getCommand("dbot").setExecutor(new CMD$Disbot());
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new MinecraftEvent(), this);
    }

    public static void registerBot() {
        if (!JDAMain.start()) {
            Bukkit.getConsoleSender().sendMessage("§c[DiscordBot] 봇 활성화에 실패했습니다! 플러그인을 비활성화합니다.");
            DiscordBot.getInstance().getServer().getPluginManager().disablePlugin(DiscordBot.getInstance());
        }
    }


    // API
    public static void cleanMessage(String id) {
        if(JDAMain.getJDA().getTextChannelById(id) == null) {
            NewsChannel channel = JDAMain.getJDA().getNewsChannelById(id);

            for(Message message : MessageHistory.getHistoryFromBeginning(channel).complete().getRetrievedHistory()) {
                message.delete().queue();
            }
        } else {
            TextChannel channel = JDAMain.getJDA().getTextChannelById(id);

            for(Message message : MessageHistory.getHistoryFromBeginning(channel).complete().getRetrievedHistory()) {
                message.delete().queue();
            }
        }
    }

    public static void notice(String name, String title, String message) {
        if(JDAMain.getJDA().getTextChannelById(name) == null) {
            NewsChannel channel = JDAMain.getJDA().getNewsChannelById(name);

            EmbedBuilder embed = new EmbedBuilder();
            embed.setColor(Color.CYAN);
            embed.setTitle(title);
            embed.setDescription(message);
            embed.setFooter(DataManager$Config.SERVER_NAME, DataManager$Config.DISCORD_PROFILE_ADDRESS);

            if(channel.canTalk()) {
                channel.sendMessageEmbeds(embed.build()).queue();
            }
        } else {
            TextChannel channel = JDAMain.getJDA().getTextChannelById(name);

            EmbedBuilder embed = new EmbedBuilder();
            embed.setColor(Color.CYAN);
            embed.setTitle(title);
            embed.setDescription(message);
            embed.setFooter(DataManager$Config.SERVER_NAME, DataManager$Config.DISCORD_PROFILE_ADDRESS);

            if(channel.canTalk()) {
                channel.sendMessageEmbeds(embed.build()).queue();
            }
        }
    }

    public static Boolean getIsVerified(String uuid) {
        if(ConfigManager$Config.getDiscordId(uuid) != null)
            return true;
        else
            return false;
    }

    public static Long getDiscordId(String uuid) {
        if(ConfigManager$Config.getDiscordId(uuid) == null)
            return null;
        else
            return (Long) ConfigManager$Config.getDiscordId(uuid);
    }

    public static String getDiscordTag(String uuid) {
        if(ConfigManager$Config.getDiscordId(uuid) == null)
            return null;

        User user = DiscordAPI.getDiscordUser((Long) ConfigManager$Config.getDiscordId(uuid));
        return user.getAsTag();
    }

    public static void unverify(String uuid) {
        User user = DiscordAPI.getDiscordUser((Long) ConfigManager$Config.getDiscordId(uuid));

        ConfigManager$Config.removeData(uuid);

        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(Color.RED);
        embed.setImage("https://mc-heads.net/avatar/" + uuid + "/100");
        embed.setTitle("인증이 해제되었습니다");
        embed.setDescription("관리자에 의해 연동이 해제되었습니다!");
        embed.setFooter("서버주소: " + DataManager$Config.SERVER_ADDRESS);
        DiscordAPI.sendPrivateMessageEmbed(user, embed);

        if(Bukkit.getPlayer(uuid) != null) {
            MinecraftEvent.unLinkedPlayerList.add(Bukkit.getPlayer(uuid));
        }

        Guild guild = JDAMain.getJDA().getGuildById(DataManager$Config.GUILD_ID);
        Member member = guild.getMemberById(user.getId());

        if(DataManager$Config.GIVE_VERIFY_ROLE == true) {
            Role role = guild.getRoleById(DataManager$Config.VERIFY_ROLE);

            if(role == null) {
                Bukkit.getConsoleSender().sendMessage("§cverify-role 역할이 서버에 존재하지 않습니다!");
                return;
            }

            guild.removeRoleFromMember(member, role).queue();
        }
        if(DataManager$Config.CHANGE_NICKNAME == true) {
            try {
                member.modifyNickname(member.getUser().getName()).queue();
            } catch (HierarchyException e) {
                Bukkit.getConsoleSender().sendMessage("§c인증을 완료한 플레이어가 봇과 동등, 혹은 그 이상의 권한을 가지고 있어 닉네임 변경에 실패했습니다!");
                Bukkit.getConsoleSender().sendMessage("§c이 오류는 웬만하면 디스코드 관리자에게만 나타날 것입니다. 아니라면 개발자인 '종현#7737'로 연락해주세요.");
            }
        }
    }
}
