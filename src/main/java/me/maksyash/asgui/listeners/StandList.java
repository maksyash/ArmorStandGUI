package me.maksyash.asgui.listeners;

import me.maksyash.asgui.ArmorStandGUI;
import me.maksyash.asgui.utils.MenuUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class StandList implements Listener {
    ArmorStandGUI plugin;

    public StandList(ArmorStandGUI plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMenuClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        MenuUtils u = new MenuUtils(plugin, e, player);

        if (u.compareMenuTitles(plugin.getString("list"))) {
            if (e.getCurrentItem() == null)
                return;
            if (u.compareWithMenuItem("custom-as-item")) {
                MenuUtils.selectedStandID.put(player, e.getSlot());
                plugin.openStandDeletion(player, MenuUtils.selectedStandID.get(player));
            }
            e.setCancelled(true);
        }
    }
}