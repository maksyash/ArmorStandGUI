package me.maksyash.asgui.listeners;

import me.maksyash.asgui.ArmorStandGUI;
import me.maksyash.asgui.config.ASListConfig;
import me.maksyash.asgui.utils.MenuUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.List;
import java.util.UUID;

public class StandDeletion implements Listener {
    ArmorStandGUI plugin;

    public StandDeletion(ArmorStandGUI plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMenuClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        MenuUtils u = new MenuUtils(plugin, e, player);

        if (u.compareMenuTitles(plugin.getString("stand-deletion"))) {
            if (e.getCurrentItem() == null)
                return;
            if (u.compareWithMenuItem("delete-stand-item")) {
                List<String> UUIDs = ASListConfig.get().getStringList(player.getName());
                ArmorStand as = (ArmorStand) Bukkit.getEntity(UUID.fromString(UUIDs.get(MenuUtils.selectedStandID.get(player))));

                UUIDs.remove((int) MenuUtils.selectedStandID.get(player));
                ASListConfig.get().set(player.getName(), UUIDs);
                ASListConfig.save();
                if (as != null)
                    as.remove();
                u.sendMessage("delete-msg");
                plugin.openList(player);
            } else if (u.compareWithMenuItem("go-back-item"))
                plugin.openList(player);

            e.setCancelled(true);
        }
    }
}