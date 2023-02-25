package dev.max.timer.language;


import dev.max.timer.Main;
import dev.max.timer.utils.Config;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LanguageHelper implements Listener {

    private Main main;
    private static HashMap<String, SupportedLanguages> languagesHashMap = new HashMap<>();
    private static HashMap<SupportedLanguages, Map<String, Object>> languageContent = new HashMap<>();
    public LanguageHelper(Main main) {
        this.main = main;

        main.getServer().getPluginManager().registerEvents(this, main);
    }

    public void loadLanguageFiles() {
        for(SupportedLanguages languages : SupportedLanguages.values()) {
            String fileName = languages.getLang();
            File file = new File(main.getDataFolder() + "/languages/" + fileName + ".yml");

            main.saveResource("languages/" + fileName + ".yml", false);

            YamlConfiguration configFile = YamlConfiguration.loadConfiguration(file);
            languageContent.put(languages, configFile.getValues(true));
        }
    }

    public void loadLanguage() {

        Bukkit.getConsoleSender().sendMessage("Loading Language...");

        new BukkitRunnable() {
            @Override
            public void run() {
                SupportedLanguages language = SupportedLanguages.ENGLISH;

                for(SupportedLanguages lang : SupportedLanguages.values()) {
                    if(main.getConfig().getString("plugin.language").equalsIgnoreCase(lang.getLang())){
                        language = lang;
                        break;
                    }
                }

                languagesHashMap.put(main.getConfig().getString("plugin.language"), language);
            }
        }.runTaskLater(main, 20);
    }

    public static String getMessage(Main main, String messageKey)
    {
        return getMessage(languagesHashMap.get(main.getConfig().getString("plugin.language")), messageKey);
    }

    public static void updateLanguage(Main main, String language) {
        main.getConfig().set("plugin.language", language);
        main.saveConfig();
        languagesHashMap.clear();

        SupportedLanguages language1 = SupportedLanguages.ENGLISH;
        for (SupportedLanguages lang : SupportedLanguages.values()) {
            if(language.equalsIgnoreCase(lang.getLang())) {
                language1 = lang;
                break;
            }
        }
        languagesHashMap.put(main.getConfig().getString("plugin.language"), language1);
    }

    public static String getMessage(SupportedLanguages language, String messageKey)
    {
        return ChatColor.translateAlternateColorCodes('&', languageContent.get(language).get(messageKey).toString());
    }

    public static HashMap<String, SupportedLanguages> getCurrentLanguage() {
        return languagesHashMap;
    }
}
