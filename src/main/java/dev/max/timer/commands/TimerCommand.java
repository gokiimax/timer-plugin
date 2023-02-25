package dev.max.timer.commands;

import com.google.gson.internal.LazilyParsedNumber;
import dev.max.timer.Main;
import dev.max.timer.gui.TimerGui;
import dev.max.timer.language.LanguageHelper;
import dev.max.timer.language.SupportedLanguages;
import dev.max.timer.timer.Timer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class TimerCommand implements CommandExecutor {

    private Main main = Main.getInstance();
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(args.length == 0) {
            sendUsage(sender);
            return true;
        }

        Player player = (Player) sender;

        switch (args[0].toLowerCase()) {
            case "gui": {
                TimerGui gui = new TimerGui(player);
                gui.openGui();
                break;
            }
            case "setlang": {

                if(args.length != 2) {
                    sendUsage(sender);
                    return true;
                }

                LanguageHelper.updateLanguage(main, args[1].toLowerCase());

                for(SupportedLanguages lang : LanguageHelper.getCurrentLanguage().values()) {
                    player.sendMessage(main.getTimer().getPrefix() + "Updated Language to§8: §9" + lang.name());
                }
                break;
            }
            case "resume": {
                Timer timer = Main.getInstance().getTimer();


                if(timer.isRunning()) {
                    sender.sendMessage(timer.getPrefix() + LanguageHelper.getMessage(main, "timer.running"));
                    break;
                }

                player.sendTitle(LanguageHelper.getMessage(main, "timer.resumeTitle"), "", 1*20, 2*20, 1*20);

                timer.setRunning(true);
                sender.sendMessage(timer.getPrefix() + LanguageHelper.getMessage(main, "timer.resume"));
                break;
            }
            case "pause": {
                Timer timer = Main.getInstance().getTimer();
                FileConfiguration timerConfig = Main.getInstance().getTimerConfig().toFileConfiguration();


                if (!timer.isRunning()) {
                    sender.sendMessage(timer.getPrefix() + LanguageHelper.getMessage(main, "timer.nRunning"));
                    break;
                }

                player.sendTitle(LanguageHelper.getMessage(main, "timer.pauseTitle"), "", 1*20, 2*20, 1*20);

                timerConfig.set("time", timer.getHours() + ":" + timer.getMinutes() + ":" + timer.getSeconds());
                Main.getInstance().getTimerConfig().save();

                timer.setRunning(false);
                sender.sendMessage(timer.getPrefix() + LanguageHelper.getMessage(main, "timer.pause"));
                break;
            }
            case "time": {
                Timer timer = Main.getInstance().getTimer();
                FileConfiguration timerConfig = Main.getInstance().getTimerConfig().toFileConfiguration();

                if (args.length != 2) {
                    sendUsage(sender);
                    return true;
                }

                try {
                    String[] time = new String[0];
                    if(args[1].matches("[0-9]{0,}:[0-9]{2}:[0-9]{2}")){
                       time = args[1].split(":");
                    } else {
                        sender.sendMessage(timer.getPrefix() + LanguageHelper.getMessage(main, "timer.NAN").replaceAll("%string%", args[1]));
                        break;
                    }

                    timer.setRunning(false);
                    timer.setSeconds(Integer.parseInt(time[2]));
                    timer.setMinutes(Integer.parseInt(time[1]));
                    timer.setHours(Integer.parseInt(time[0]));

                    timerConfig.set("time", timer.getHours() + ":" + timer.getMinutes() + ":" + timer.getSeconds());
                    Main.getInstance().getTimerConfig().save();

                    String timeText = time[0] + ":" + time[1] + ":" + time[2];
                    sender.sendMessage(timer.getPrefix() + LanguageHelper.getMessage(main, "timer.time").replaceAll("%time%", timeText));
                } catch (NumberFormatException e) {
                    sender.sendMessage(timer.getPrefix() + LanguageHelper.getMessage(main, "timer.NAN").replaceAll("%string%", e.getMessage()));
                }
                break;
            }
            case "reset": {
                Timer timer = Main.getInstance().getTimer();

                timer.reset();
                timer.setRunning(false);
                sender.sendMessage(timer.getPrefix() + LanguageHelper.getMessage(main, "timer.reset"));
                break;
            }
            default:
                sendUsage(sender);
                break;
        }

        return false;
    }

    private void sendUsage(CommandSender sender) {
        sender.sendMessage("§7═§8═§7═§8═§7═§8═§7═§8═§7═§8═§7═ §dTimer §8(§71§8/§71§8) §7═§8═§7═§8═§7═§8═§7═§8═§7═§8═§7═");
        sender.sendMessage("§7Use /timer to get this page");
        sender.sendMessage("\n");
        sender.sendMessage("§8» §d/timer gui: §7Open the timer gui");
        sender.sendMessage("§8» §d/timer resume: §7Resume the timer");
        sender.sendMessage("§8» §d/timer pause: §7Pause the timer");
        sender.sendMessage("§8» §d/timer reset: §7Reset the timer");
        sender.sendMessage("§8» §d/timer time <01:02:03>: §7Set the time of the timer");
        sender.sendMessage("§8» §d/timer setLang <en>: §7Set the language of the plugin");
        sender.sendMessage("\n");
        sender.sendMessage("§7═§8═§7═§8═§7═§8═§7═§8═§7═§8═§7═ §dTimer §8(§71§8/§71§8) §7═§8═§7═§8═§7═§8═§7═§8═§7═§8═§7═");
    }

}
