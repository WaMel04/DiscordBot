package com.wamel.discordbot.command.disbot;

import com.wamel.discordbot.DiscordBot;
import com.wamel.discordbot.bot.JDAMain;
import com.wamel.discordbot.config.ConfigManager$Config;
import com.wamel.discordbot.data.DataManager$Config;
import com.wamel.discordbot.event.MinecraftEvent;
import com.wamel.discordbot.util.DiscordAPI;
import com.wamel.discordbot.util.ReadEPF;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.NewsChannel;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.awt.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class CMD$Disbot$Unverify extends CMD$Disbot{

    public static void run(CommandSender sender, String[] args, String label) {
        if(args.length == 1) {
            sender.sendMessage(" §b/" + label + " unverify [닉네임] §7- 해당 유저의 인증 상태를 해제합니다.");
            return;
        }

        CompletableFuture.runAsync(() -> {
            try {
                String uuid = ReadEPF.getUUID(args[1]).get(3, TimeUnit.SECONDS);

                if(uuid == null) {
                    sender.sendMessage("§c" + args[1] + "§6님은 서버에 접속한 적 없는 플레이어입니다.");
                    return;
                }
                if(DiscordBot.getIsVerified(uuid) == false) {
                    sender.sendMessage("§c" + args[1] + "§6님은 인증을 한 기록이 없습니다.");
                    return;
                }

                User user = DiscordAPI.getDiscordUser((Long) ConfigManager$Config.getDiscordId(uuid));

                ConfigManager$Config.removeData(uuid);

                EmbedBuilder embed = new EmbedBuilder();
                embed.setColor(Color.RED);
                embed.setImage("https://mc-heads.net/avatar/" + uuid + "/100");
                embed.setTitle("인증이 해제되었습니다");
                embed.setDescription("관리자에 의해 연동이 해제되었습니다!");
                embed.setFooter("서버주소: " + DataManager$Config.SERVER_ADDRESS);
                DiscordAPI.sendPrivateMessageEmbed(user, embed);

                sender.sendMessage("§c" + args[1] + "§6님의 인증을 해제했습니다.");

                if(Bukkit.getPlayer(uuid) != null) {
                    MinecraftEvent.unLinkedPlayerList.add(Bukkit.getPlayer(uuid));
                }

                if (DataManager$Config.ENABLE_MEMBER_INTENT) {
                    Guild guild = JDAMain.getJDA().getGuildById(DataManager$Config.GUILD_ID);
                    Member member = guild.getMemberById(user.getId());

                    if(DataManager$Config.GIVE_VERIFY_ROLE) {
                        Role role = guild.getRoleById(DataManager$Config.VERIFY_ROLE);

                        guild.removeRoleFromMember(member, role).queue();
                    }
                    if(DataManager$Config.CHANGE_NICKNAME) {
                        try {
                            member.modifyNickname(null).queue();
                        } catch (HierarchyException e) {
                            sender.sendMessage("§c인증을 완료한 플레이어가 봇과 동등, 혹은 그 이상의 권한을 가지고 있어 닉네임 변경에 실패했습니다!");
                            sender.sendMessage("§c이 오류는 웬만하면 디스코드 관리자에게만 나타날 것입니다. 아니라면 개발자인 '종현#7737'로 연락해주세요.");
                        }
                    }
                }
            } catch (Exception e) {
                sender.sendMessage("§c" + args[1] + "님의 플레이어 데이터를 가져오는데 실패했습니다!");
                sender.sendMessage(e.getLocalizedMessage());
            }
        });
    }
}
