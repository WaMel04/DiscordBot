package com.wamel.discordbot.command;

import com.wamel.discordbot.bot.JDAListener;
import com.wamel.discordbot.bot.JDAMain;
import com.wamel.discordbot.config.ConfigManager$Config;
import com.wamel.discordbot.data.DataManager$Config;
import com.wamel.discordbot.event.MinecraftEvent;
import com.wamel.discordbot.util.DiscordAPI;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.awt.*;


public class CMD$Verify implements CommandExecutor {

    public static final String prefix = "§9§l인증 §7| §f";

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player))
            return false;
        switch (cmd.getName()) {
            case "verify":
            case "인증":
                break;
            default:
                return false;
        }

        Player player = (Player) sender;

        if(args.length == 0) {
            player.sendMessage("");
            player.sendMessage(" §9§l<< §f§l인증 도움말 §9§l>>");
            player.sendMessage("");
            if(ConfigManager$Config.getDiscordId(player.getUniqueId().toString()) != null) {
                User user = DiscordAPI.getDiscordUser((Long) ConfigManager$Config.getDiscordId(player.getUniqueId().toString()));
                player.sendMessage(" §f당신은 이미 인증을 완료했습니다. §b연동된 디스코드 계정: " + user.getAsTag());
            } else {
                player.sendMessage(" §f디스코드 인증을 하셔야 서버 플레이가 가능합니다.");
                player.sendMessage(" §b디스코드 주소: §f" + DataManager$Config.DISCORD_ADDRESS);
            }
            player.sendMessage("");
            player.sendMessage(" §b/" + label + " [인증코드] §7- 디스코드와 마인크래프트 계졍을 연동합니다.");
            player.sendMessage("");
            return false;
        }

        String code = args[0];

        if(JDAListener.verifyCode.get(code) == null) {
            player.sendMessage(prefix + "존재하지 않는 인증 코드입니다.");
            return false;
        }

        User user = JDAListener.verifyCode.get(code);

        ConfigManager$Config.saveData(player.getUniqueId().toString(),
                user.getIdLong());
        JDAListener.verifyingList.remove(user);
        JDAListener.verifyCode.remove(code);

        player.sendMessage(prefix + "인증이 완료되었습니다. 계정을 연동합니다. §b" + player.getName() + " <-> " + user.getAsTag());

        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(Color.CYAN);
        embed.setImage("https://mc-heads.net/avatar/" + player.getUniqueId() + "/100");
        embed.setTitle("환영합니다");
        embed.setDescription("성공적으로 디스코드 연동을 완료했습니다!");
        embed.addField("연동을 변경하고 싶으면?", "디스코드 ID 변경, 마인크래프트 ID 변경으로 연동 변경을 원할 시 관리자에게 문의해주세요.", false);
        embed.setFooter("서버주소: " + DataManager$Config.SERVER_ADDRESS);
        DiscordAPI.sendPrivateMessageEmbed(user, embed);
        MinecraftEvent.unLinkedPlayerList.remove(player);

        Guild guild = JDAMain.getJDA().getGuildById(DataManager$Config.GUILD_ID);
        Member member = guild.getMemberById(user.getId());

        if(DataManager$Config.GIVE_VERIFY_ROLE == true) {
            Role role = guild.getRoleById(DataManager$Config.VERIFY_ROLE);

            if(role == null) {
                sender.sendMessage("§cverify-role 역할이 서버에 존재하지 않습니다! 관리자에게 문의해주세요.");
                return false;
            }
            guild.addRoleToMember(member, role).queue();
        }
        // 1.1 닉네임 변경
        if(DataManager$Config.CHANGE_NICKNAME == true) {
            try {
                member.modifyNickname(player.getName()).queue();
            } catch (HierarchyException e) {
                player.sendMessage("§c인증을 완료한 플레이어가 봇과 동등, 혹은 그 이상의 권한을 가지고 있어 닉네임 변경에 실패했습니다!");
                player.sendMessage("§c이 오류는 웬만하면 디스코드 관리자에게만 나타날 것입니다. 아니라면 개발자인 '종현#7737'로 연락해주세요.");
            }
        }
        return false;
    }
}
