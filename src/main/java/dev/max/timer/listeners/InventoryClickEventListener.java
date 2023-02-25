package dev.max.timer.listeners;

import dev.max.timer.Main;
import dev.max.timer.language.LanguageHelper;
import dev.max.timer.timer.Timer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryClickEventListener implements Listener {

    @EventHandler
    public void onInventoryInteract(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if(event.getView().getTitle().equalsIgnoreCase(LanguageHelper.getMessage(Main.getInstance(), "timer.guiTitle"))) {
            event.setCancelled(true);

            if(event.getClick().isLeftClick()) {
                ItemStack clickedItem = event.getCurrentItem();

                // Resume Timer
                if(clickedItem.getItemMeta().getDisplayName().equalsIgnoreCase(LanguageHelper.getMessage(Main.getInstance(), "timer.guiResume"))) {
                    Timer timer = Main.getInstance().getTimer();


                    if(timer.isRunning()) {
                        player.sendMessage(timer.getPrefix() + LanguageHelper.getMessage(Main.getInstance(), "timer.running"));
                        player.closeInventory();
                        return;
                    }

                    player.sendTitle(LanguageHelper.getMessage(Main.getInstance(), "timer.resumeTitle"), "", 1*20, 2*20, 1*20);

                    timer.setRunning(true);
                    player.sendMessage(timer.getPrefix() + LanguageHelper.getMessage(Main.getInstance(), "timer.resume"));
                    Main.getInstance().getTimer().setRunning(true);

                    player.closeInventory();
                }

                // Pause Timer
                if(clickedItem.getItemMeta().getDisplayName().equalsIgnoreCase(LanguageHelper.getMessage(Main.getInstance(), "timer.guiPause"))) {
                    Timer timer = Main.getInstance().getTimer();
                    FileConfiguration timerConfig = Main.getInstance().getTimerConfig().toFileConfiguration();


                    if (!timer.isRunning()) {
                        player.sendMessage(timer.getPrefix() + LanguageHelper.getMessage(Main.getInstance(), "timer.nRunning"));
                        player.closeInventory();
                        return;
                    }

                    player.sendTitle(LanguageHelper.getMessage(Main.getInstance(), "timer.pauseTitle"), "", 1*20, 2*20, 1*20);

                    timerConfig.set("time", timer.getHours() + ":" + timer.getMinutes() + ":" + timer.getSeconds());
                    Main.getInstance().getTimerConfig().save();

                    timer.setRunning(false);
                    player.sendMessage(timer.getPrefix() + LanguageHelper.getMessage(Main.getInstance(), "timer.pause"));

                    player.closeInventory();
                }

                // Reset Timer
                if(clickedItem.getItemMeta().getDisplayName().equalsIgnoreCase(LanguageHelper.getMessage(Main.getInstance(), "timer.guiReset"))) {
                    Timer timer = Main.getInstance().getTimer();

                    timer.reset();
                    timer.setRunning(false);
                    player.sendMessage(timer.getPrefix() + LanguageHelper.getMessage(Main.getInstance(), "timer.reset"));

                    player.closeInventory();
                }

            }
        }
    }

}
