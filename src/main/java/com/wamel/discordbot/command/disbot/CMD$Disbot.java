package com.wamel.discordbot.command.disbot;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;


public class CMD$Disbot implements CommandExecutor {

    public static final String prefix = "§9§l디스코드 §7| §f";

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        switch (cmd.getName().toLowerCase()) {
            case "discordbot":
            case "dbot":
                break;
            default:
                return false;
        }

        if(!(sender.isOp()))
            return false;
        if(args.length == 0) {
            sender.sendMessage("");
            sender.sendMessage(" §9§l<< §f§l디스코드 도움말 §9§l>>");
            sender.sendMessage("");
            sender.sendMessage(" §b/" + label + " reload §7- config를 리로드합니다.");
            sender.sendMessage(" §b/" + label + " notice [채널ID] [제목] [내용] §7- 해당 채널에 [내용]의 공지를 보냅니다.");
            sender.sendMessage(" §7띄어쓰기: _");
            sender.sendMessage(" §b/" + label + " clean [채널ID] §7- 해당 채널의 모든 메세지를 지웁니다.");
            sender.sendMessage(" §b/" + label + " ignore [닉네임] §7- 해당 유저가 인증 받지 않고도 서버 활동을 가능하게 합니다.");
            sender.sendMessage(" §b/" + label + " unignore [닉네임] §7- 해당 유저가 인증을 받아야만 서버 활동을 가능하게 합니다.");
            sender.sendMessage(" §b/" + label + " unverify [닉네임] §7- 해당 유저의 인증 상태를 해제합니다.");
            sender.sendMessage("");
            return false;
        }

        switch (args[0].toLowerCase()) {
            case "reload":
                CMD$Disbot$Reload.run(sender);
                break;
            case "notice":
                CMD$Disbot$Notice.run(sender, args, label);
                break;
            case "clean":
                CMD$Disbot$Clean.run(sender, args, label);
                break;
            case "ignore":
                CMD$Disbot$Ignore.run(sender, args, label);
                break;
            case "unignore":
                CMD$Disbot$Unignore.run(sender, args, label);
                break;
            case "unverify":
                CMD$Disbot$Unverify.run(sender, args, label);
                break;
        }
        return false;
    }

}
