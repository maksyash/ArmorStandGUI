package me.maksyash.asgui.listeners;

import me.maksyash.asgui.ArmorStandGUI;
import me.maksyash.asgui.utils.MenuUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.HashMap;

public class HandMenu implements Listener {
    ArmorStandGUI plugin;
    HashMap<Player, Boolean> handMenuActivity = new HashMap<>();
    HashMap<Player, Boolean> isOffHand = new HashMap<>();

    public HandMenu(ArmorStandGUI plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMenuClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        MenuUtils u = new MenuUtils(plugin, e, player);

        if (u.compareMenuTitles(plugin.getString("hand-menu"))) {
            if (MenuUtils.tempStand.get(player).isDead()) {
                player.closeInventory();
                player.sendMessage(plugin.getString("stand-broken-error"));
                return;
            }
            if (!handMenuActivity.containsKey(player))
                handMenuActivity.put(player, false);
            if (!isOffHand.containsKey(player))
                isOffHand.put(player, false);

            if (handMenuActivity.get(player)) {
                if (e.getCurrentItem() != null) {
                    if (u.compareWithMenuItem("done-item") || u.compareWithMenuItem("hand-left-item")
                            || u.compareWithMenuItem("hand-right-item")) {
                        e.setCancelled(true);
                        return;
                    }
                }
                if (isOffHand.get(player))
                    MenuUtils.tempStand.get(player).getEquipment().setItemInOffHand(e.getCurrentItem());
                else
                    MenuUtils.tempStand.get(player).getEquipment().setItemInMainHand(e.getCurrentItem());
                handMenuActivity.replace(player, false);
                u.sendMessage("hand-item-selected-msg");
                e.setCancelled(true);
                return;
            }

            if (e.getCurrentItem() == null) {
                return;
            }
            if(u.compareWithMenuItem("done-item")) {
                plugin.openMainMenu(player);
                u.sendMessage("hands-done-msg");
            } else if (u.compareWithMenuItem("hand-left-item")) {
                isOffHand.replace(player, true);
                handMenuActivity.replace(player, true);
                u.sendMessage("hand-item-select-msg");
            } else if (u.compareWithMenuItem("hand-right-item")) {
                isOffHand.replace(player, false);
                handMenuActivity.replace(player, true);
                u.sendMessage("hand-item-select-msg");
            }
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onHandMenuClose(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        if(handMenuActivity.containsKey(p))
            handMenuActivity.replace(p, false);
    }
}