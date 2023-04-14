package com.wamel.discordbot.bot;

import com.wamel.discordbot.DiscordBot;
import com.wamel.discordbot.config.ConfigManager$Config;
import com.wamel.discordbot.data.DataManager$Config;
import com.wamel.discordbot.util.DiscordAPI;
import com.wamel.discordbot.util.ReadEPF;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.bukkit.scheduler.BukkitRunnable;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class JDAListener extends ListenerAdapter {

    private static final DiscordBot plugin = DiscordBot.getInstance();

    public static ArrayList<User> verifyingList = new ArrayList<>();
    // 인증을 진행 중인 디스코드 유저 리스트
    public static HashMap<String, User> verifyCode = new HashMap<>();
    // 인증 코드 Map (인증 코드 , 디스코드 유저 ID)

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if(event.getGuild() == null)
            return;

        switch(event.getName()) {
            case "안내메세지":
                EmbedBuilder embed = new EmbedBuilder();
                embed.setColor(Color.CYAN);
                embed.setTitle("디스코드 연동 시스템");
                embed.setDescription(DataManager$Config.SERVER_NAME + "에 접속해주셔서 감사합니다.");
                embed.addField("연동은 어떻게 하나요?", "아래의 확인 이모지를 누를시 DM으로 인증 번호가 전송됩니다." +
                        "\n이후 서버에 접속한 후, /인증 <인증번호>를 입력시 연동이 완료됩니다.", false);
                embed.addField("예시", "인증 번호가 `185392`일시 서버에서 `/인증 185392`를 입력하면 됩니다.", false);
                embed.addField("주의사항", "**연동은 한 번 할시 되돌릴 수 없습니다.**" +
                        "\n디스코드 ID 변경, 마인크래프트 ID 변경시 관리자에게 문의해주세요.", false);
                embed.setFooter("서버주소: " + DataManager$Config.SERVER_ADDRESS);

                event.replyEmbeds(embed.build())
                        .addActionRow( Button.success("get-verify-code", "\uD83C\uDFAB 인증 코드 받기"))
                        .queue();

                return;
            default:
                break;
        }
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if(!(event.getButton().getId().equalsIgnoreCase("get-verify-code")))
            return;

        event.deferEdit().queue();

        User user = event.getUser();

        if(ConfigManager$Config.getMinecraftUUID(user.getIdLong()) != null) {
            DiscordAPI.sendPrivateMessage(user, "당신은 이미 인증을 완료했습니다. 연동된 마인크래프트 계정: " + ReadEPF.getName((String) ConfigManager$Config.getMinecraftUUID(user.getIdLong())));
            return;
        }
        if(verifyingList.contains(user)) {
            DiscordAPI.sendPrivateMessage(user, "당신은 이미 인증 번호를 발급 받았습니다.");
            return;
        }

        // 겹치지 않을 때까지 인증 번호 생성
        String code = generateVerifyCode();

        if(verifyCode.get(code) != null) {
            while(verifyCode.get(code) == null) {
                code = generateVerifyCode();
            }
        }

        verifyingList.add(user);
        verifyCode.put(code, user);

        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(Color.CYAN);
        embed.setTitle(code);
        embed.setDescription("60초 후 인증 번호가 만료됩니다.");
        embed.addField("연동은 어떻게 하나요?",
                "서버에 접속한 후, `/인증 " + code + "` 를 입력시 연동이 완료됩니다.", false);
        embed.addField("주의사항", "**연동은 한 번 할시 되돌릴 수 없습니다.**" +
                "\n디스코드 ID 변경, 마인크래프트 ID 변경시 관리자에게 문의해주세요.", false);
        embed.setFooter("서버주소: " + DataManager$Config.SERVER_ADDRESS);
        DiscordAPI.sendPrivateMessageEmbed(user, embed);

        String finalCode = code;
        new BukkitRunnable() {
            @Override
            public void run() {
                if(verifyCode.get(finalCode) != null) {
                    verifyCode.remove(finalCode);
                    verifyingList.remove(user);
                    DiscordAPI.sendPrivateMessage(user, "인증 번호가 삭제되었습니다.");
                }
            }
        }.runTaskLater(plugin, 20*60);
    }

    private String generateVerifyCode() {
        return String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
    }

}
