//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.wamel.discordbot.bot;

import com.wamel.discordbot.data.DataManager$Config;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.bukkit.Bukkit;

import javax.security.auth.login.LoginException;

public class JDAMain extends ListenerAdapter {

    private static JDA jda;

    private static String token = DataManager$Config.TOKEN;

    public static void start() throws LoginException {
        jda = JDABuilder.createDefault(token)
                .setStatus(OnlineStatus.ONLINE).setActivity(Activity.playing(Bukkit.getOnlinePlayers().size() + "명이 서버를 플레이"))
                .setEnableShutdownHook(true).addEventListeners(new JDAListener())
                .setChunkingFilter(ChunkingFilter.ALL)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .build();

        CommandListUpdateAction commands = jda.updateCommands();

        commands.addCommands(Commands.slash("안내메세지", "인증 안내메세지를 출력합니다.").setGuildOnly(true)
                .setDefaultPermissions(DefaultMemberPermissions.DISABLED));

        commands.queue();
    }

    public static JDA getJDA() {
        return jda;
    }

}
