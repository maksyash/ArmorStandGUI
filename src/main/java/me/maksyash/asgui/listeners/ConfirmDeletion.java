package me.maksyash.asgui.listeners;

import me.maksyash.asgui.ArmorStandGUI;
import me.maksyash.asgui.utils.MenuUtils;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ConfirmDeletion implements Listener {
    ArmorStandGUI plugin;

    public ConfirmDeletion(ArmorStandGUI plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMenuClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        MenuUtils u = new MenuUtils(plugin, e, player);

        if (u.compareMenuTitles(plugin.getString("confirm-deletion-menu"))) {
            if (MenuUtils.tempStand.get(player).isDead()) {
                player.closeInventory();
                player.sendMessage(plugin.getString("stand-broken-error"));
                return;
            }
            u.confirmMenuLogic(() -> {
                ArmorStand as = MenuUtils.tempStand.get(player);
                MenuUtils.tempStand.remove(player);
                as.remove();
                player.playSound(player.getLocation(), Sound.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, 0.3F, 1.0F);}, "delete-msg");
        }
    }
}