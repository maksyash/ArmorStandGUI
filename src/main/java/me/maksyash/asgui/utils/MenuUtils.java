package me.maksyash.asgui.utils;

import me.maksyash.asgui.ArmorStandGUI;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class MenuUtils {

    public static HashMap<Player, Integer> selectedStandID = new HashMap<>();
    public static HashMap<Player, ArmorStand> tempStand = new HashMap<>();
    ArmorStandGUI plugin;
    InventoryClickEvent e;
    Player player;

    public MenuUtils(ArmorStandGUI plugin, InventoryClickEvent e, Player player) {
        this.plugin = plugin;
        this.e = e;
        this.player = player;
    }

    public boolean compareWithMenuItem(String name) {
        return e.getCurrentItem().getItemMeta().getDisplayName().equals(plugin.getString(name));
    }
    public boolean compareMenuTitles(String title) {
        return player.getOpenInventory().getTitle().equals(title);
    }
    public void sendMessage(String msg) {
        player.sendMessage(plugin.getString(msg));
    }

    public void mainMenuLogic(Supplier<Boolean> supplier, Consumer<Boolean> consumer, String set_msg, String remove_msg) {
        if (supplier.get()) {
            consumer.accept(false);
            player.sendMessage(plugin.getString(remove_msg));
        } else {
            consumer.accept(true);
            player.sendMessage(plugin.getString(set_msg));
        }
    }

    public void confirmMenuLogic(Runnable runnable, String confirm_msg) {
        if (e.getCurrentItem() == null) {
            return;
        }
        String displayName = e.getCurrentItem().getItemMeta().getDisplayName();
        if(displayName.equals(plugin.getString("confirm-yes-item"))) {
            runnable.run();
            player.sendMessage(plugin.getString(confirm_msg));
            player.closeInventory();
        } else if (displayName.equals(plugin.getString("confirm-no-item"))) {
            plugin.openMainMenu(player);
        }
        e.setCancelled(true);
    }

    public static void createASItem(@NotNull ArmorStand as, Inventory inv, int index) {
        ItemStack as_item = new ItemStack(Material.ARMOR_STAND);
        ItemMeta as_meta = as_item.getItemMeta();
        as_meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', ArmorStandGUI.plugin.getConfig().getString("custom-as-item")));
        ArrayList<String> as_lore = new ArrayList<>();
        Location as_loc = as.getLocation();
        as_lore.add("X: "+as_loc.getX());
        as_lore.add("Y: "+as_loc.getY());
        as_lore.add("Z: "+as_loc.getZ());
        EquipmentSlot[] values = EquipmentSlot.values();
        for (int i = values.length-1; i >= 0; i--) {
            String name = as.getEquipment().getItem(values[i]).getType().name();
            if (!name.equals("AIR"))
                as_lore.add(values[i].toString().toLowerCase()+": "+name);
        }
        as_meta.setLore(as_lore);
        as_item.setItemMeta(as_meta);
        inv.setItem(index, as_item);
    }

    public static ItemStack createItem(Material material, String displayName, Inventory menu, int... index) {
        ItemStack item = new ItemStack(material);
        ItemMeta item_meta = item.getItemMeta();
        item_meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', ArmorStandGUI.plugin.getConfig().getString(displayName)));
        item.setItemMeta(item_meta);
        for (int j : index)
            menu.setItem(j, item);
        return item;
    }

    public static void createArmorItem(Material material, Inventory menu, int index) {
        ItemStack item = new ItemStack(material);
        ItemMeta item_meta = item.getItemMeta();
        item_meta.getPersistentDataContainer().set(NamespacedKey.fromString("from_aspl_menu"), PersistentDataType.STRING, "");
        item.setItemMeta(item_meta);
        menu.setItem(index, item);
    }
}