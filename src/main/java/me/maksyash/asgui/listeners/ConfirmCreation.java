package me.maksyash.asgui.listeners;

import me.maksyash.asgui.ArmorStandGUI;
import me.maksyash.asgui.config.ASListConfig;
import me.maksyash.asgui.utils.MenuUtils;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;

public class ConfirmCreation implements Listener {
    ArmorStandGUI plugin;

    public ConfirmCreation(ArmorStandGUI plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMenuClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        MenuUtils u = new MenuUtils(plugin, e, player);

        if (u.compareMenuTitles(plugin.getString("confirm-creation-menu"))) {
            if (MenuUtils.tempStand.get(player).isDead()) {
                player.closeInventory();
                player.sendMessage(plugin.getString("stand-broken-error"));
                return;
            }
            u.confirmMenuLogic(() -> {
                ArmorStand as = MenuUtils.tempStand.get(player);
                List<String> UUIDs = ASListConfig.get().getStringList(player.getName());
                UUIDs.add(as.getUniqueId().toString());
                ASListConfig.get().set(player.getName(), UUIDs);
                ASListConfig.save();
                MenuUtils.tempStand.remove(player);
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, 0.9F, 1.0F);}, "create-msg");
        }
    }
}