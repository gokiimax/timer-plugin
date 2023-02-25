package dev.max.timer.gui;

import dev.max.timer.Main;
import dev.max.timer.language.LanguageHelper;
import dev.max.timer.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class TimerGui {

    private Player player;
    public TimerGui(Player player) {
        this.player = player;
    }

    public void openGui() {
        player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1, 3);

        HashMap<Integer, ItemStack> integerItemStackHashMap = new HashMap<>();

        for (int i = 0; i < 3*9; i++) {
            integerItemStackHashMap.put(i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setName("§8§kbbb").build());
        }

        integerItemStackHashMap.put(11, new ItemBuilder(Material.GREEN_DYE).setName(LanguageHelper.getMessage(Main.getInstance(), "timer.guiResume")).build());
        integerItemStackHashMap.put(13, new ItemBuilder(Material.RED_DYE).setName(LanguageHelper.getMessage(Main.getInstance(), "timer.guiPause")).build());
        integerItemStackHashMap.put(15, new ItemBuilder(Material.BARRIER).setName(LanguageHelper.getMessage(Main.getInstance(), "timer.guiReset")).build());

        Inventory i = Bukkit.createInventory(null, 3*9, LanguageHelper.getMessage(Main.getInstance(), "timer.guiTitle"));
        for(Map.Entry<Integer, ItemStack> integerItemStackEntry : integerItemStackHashMap.entrySet()) {
            i.setItem(integerItemStackEntry.getKey(), integerItemStackEntry.getValue());
        }

        this.player.openInventory(i);
    }

}
