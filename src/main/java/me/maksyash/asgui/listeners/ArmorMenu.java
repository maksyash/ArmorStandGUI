package me.maksyash.asgui.listeners;

import me.maksyash.asgui.ArmorStandGUI;
import me.maksyash.asgui.utils.MenuUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;

public class ArmorMenu implements Listener {
    ArmorStandGUI plugin;

    public ArmorMenu(ArmorStandGUI plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMenuClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        MenuUtils u = new MenuUtils(plugin, e, player);

        if (u.compareMenuTitles(plugin.getString("armor-menu"))) {
            if (MenuUtils.tempStand.get(player).isDead()) {
                player.closeInventory();
                player.sendMessage(plugin.getString("stand-broken-error"));
                return;
            }
            if (e.getCurrentItem() == null)
                return;
            if (u.compareWithMenuItem("done-item")) {
                plugin.openMainMenu(player);
                return;
            }
            if (e.getCurrentItem().getItemMeta().getPersistentDataContainer()
                    .has(NamespacedKey.fromString("from_aspl_menu"), PersistentDataType.STRING)) {

                EntityEquipment asEquipment = MenuUtils.tempStand.get(player).getEquipment();
                Material armorItem = e.getCurrentItem().getType();
                EquipmentSlot slot = armorItem.getEquipmentSlot();
                String type = Arrays.stream(armorItem.name().split("_"))
                        .map((s) -> s.charAt(0) + s.substring(1).toLowerCase())
                        .reduce((prev, next) -> prev+' '+next).get();

                if (asEquipment.getItem(slot).getType() == armorItem) {
                    asEquipment.setItem(slot, null);
                    player.sendMessage(plugin.getString("removed-armor").replaceAll("%armor%", type));
                } else {
                    asEquipment.setItem(slot, new ItemStack(armorItem));
                    player.sendMessage(plugin.getString("added-armor").replaceAll("%armor%", type));
                }
            }
            e.setCancelled(true);
        }
    }
}