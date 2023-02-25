package dev.max.timer;

import dev.max.timer.commands.TimerCommand;
import dev.max.timer.language.LanguageHelper;
import dev.max.timer.listeners.InventoryClickEventListener;
import dev.max.timer.timer.Timer;
import dev.max.timer.utils.Config;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main instance;
    private Config timerConfig;
    private Timer timer;
    private LanguageHelper languageHelper;

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        // Setup Configs
        SetupConfigs();

        languageHelper = new LanguageHelper(this);
        languageHelper.loadLanguageFiles();
        languageHelper.loadLanguage();

        FileConfiguration timerConfigCfg = timerConfig.toFileConfiguration();
        timer = new Timer(false, true, 0, 0, 0);
        timer = new Timer(false, timerConfigCfg.getBoolean("enabled"), timer.getTimeFromConfig(0), timer.getTimeFromConfig(1), timer.getTimeFromConfig(2));

        getCommand("timer").setExecutor(new TimerCommand());
    }

    private void SetupConfigs() {
        saveDefaultConfig();

        // Timer config
        timerConfig = new Config("timer.yml", getDataFolder());
        timerConfig.toFileConfiguration().addDefault("enabled", true);
        timerConfig.toFileConfiguration().addDefault("time", "0:0:0");
        timerConfig.toFileConfiguration().options().copyDefaults(true);
        timerConfig.save();

        getServer().getPluginManager().registerEvents(new InventoryClickEventListener(), this);
    }

    public static Main getInstance() {
        return instance;
    }

    public Config getTimerConfig() {
        return timerConfig;
    }

    public void setTimerConfig(Config timerConfig) {
        this.timerConfig = timerConfig;
    }

    public Timer getTimer() {
        return timer;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }
}
