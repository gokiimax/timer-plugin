package dev.max.timer.timer;

import dev.max.timer.Main;
import dev.max.timer.language.LanguageHelper;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Timer {
    private String prefix = ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("plugin.prefix") + "§7");
    private boolean running;
    private int seconds;
    private int minutes;
    private boolean enabled;
    private int hours;
    private Main main = Main.getInstance();

    public Timer(boolean running, boolean enabled, int seconds, int minutes, int hours) {
        this.running = running;
        this.enabled = enabled;
        this.seconds = seconds;
        this.minutes = minutes;
        this.hours = hours;

        run();
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        setHours(getTimeFromConfig(0));
        setMinutes(getTimeFromConfig(1));
        setSeconds(getTimeFromConfig(2));
        this.running = running;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public void sendActionBar() {
        for(Player player : Bukkit.getOnlinePlayers()) {
            if(!isRunning()) {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(LanguageHelper.getMessage(main, "timer.paused")));
                continue;
            }

            String timerText;
            if(getHours() > 0) {
                timerText = String.format("%02d", getHours()) + ":" + String.format("%02d", getMinutes()) + ":" + String.format("%02d", getSeconds());
            } else {
                timerText = String.format("%02d", getMinutes()) + ":" + String.format("%02d", getSeconds());
            }

            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.GOLD.toString() + ChatColor.BOLD + timerText));
        }
    }

    public void reset() {
        setHours(0);
        setMinutes(0);
        setSeconds(0);

        Main.getInstance().getTimerConfig().toFileConfiguration().set("time", "0:0:0");
        Main.getInstance().getTimerConfig().save();
    }

    public int getTimeFromConfig(int arg) {
        Main.getInstance().getTimerConfig().reload();
        FileConfiguration timerCfg = Main.getInstance().getTimerConfig().toFileConfiguration();
        return Integer.parseInt(timerCfg.getString("time").split(":")[arg]);
    }

    private void run() {
        if(this.enabled) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    sendActionBar();

                    if(!isRunning())
                        return;

                    setSeconds(getSeconds() + 1);

                    if(getSeconds() >= 60) {
                        setMinutes(getMinutes() + 1);
                        setSeconds(0);
                    }

                    if(getMinutes() >= 60) {
                        setMinutes(0);
                        setHours(getHours() + 1);
                    }
                }
            }.runTaskTimer(Main.getInstance(), 20, 20);
        }
    }
}
