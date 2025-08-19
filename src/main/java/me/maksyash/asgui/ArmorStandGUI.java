package me.maksyash.asgui;

import me.maksyash.asgui.commands.ASCommand;
import me.maksyash.asgui.config.ASListConfig;
import me.maksyash.asgui.listeners.*;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

import static me.maksyash.asgui.utils.MenuUtils.*;

public final class ArmorStandGUI extends JavaPlugin implements Listener {

    public static ArmorStandGUI plugin;

    public String getString(String str) {
        return ChatColor.translateAlternateColorCodes('&', getConfig().getString(str));
    }
    private void regHandler(Listener listener) {
        getServer().getPluginManager().registerEvents(listener, this);
    }

    @Override
    public void onEnable() {
        System.out.println("ArmorStandGUI plugin is enabled!!!");
        plugin = this;
        regHandler(new MainMenu(this));
        regHandler(new ArmorMenu(this));
        regHandler(new HandMenu(this));
        regHandler(new ConfirmCreation(this));
        regHandler(new ConfirmDeletion(this));
        regHandler(new StandList(this));
        regHandler(new StandDeletion(this));
        getCommand("as").setExecutor(new ASCommand(this));
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        ASListConfig.setup();
        ASListConfig.save();
    }

    public void openList(Player p) {
        Inventory list = Bukkit.createInventory(p, 54, getString("list"));
        List<String> UUIDs = ASListConfig.get().getStringList(p.getName());
        for (int i = 0; i < UUIDs.size(); i++) {
            ArmorStand as = (ArmorStand) Bukkit.getEntity(UUID.fromString(UUIDs.get(i)));
            if (as == null) {
                UUIDs.remove(i--);
                ASListConfig.get().set(p.getName(), UUIDs);
                ASListConfig.save();
                continue;
            }
            createASItem(as, list, i);
        }
        p.openInventory(list);
    }
    public void openStandDeletion(Player p, int id) {
        Inventory stand_deletion = Bukkit.createInventory(p, 9, getString("stand-deletion"));
        ArmorStand as = (ArmorStand) Bukkit.getEntity(UUID.fromString(ASListConfig.get().getStringList(p.getName()).get(id)));
        if (as == null) {
            openList(p);
            return;
        }
        createASItem(as, stand_deletion, 4);
        createItem(Material.RED_WOOL, "delete-stand-item", stand_deletion, 2);
        createItem(Material.GREEN_WOOL, "go-back-item", stand_deletion, 6);
        p.openInventory(stand_deletion);
    }

    public void openMainMenu(Player p) {
        Inventory main_menu = Bukkit.createInventory(p, 9, getString("main-menu"));

        createItem(Material.GREEN_WOOL, "create-item", main_menu, 7);
        createItem(Material.RED_WOOL, "delete-item", main_menu, 8);
        createItem(Material.ARMOR_STAND, "arms-item", main_menu, 0);
        createItem(Material.BEACON, "glow-item", main_menu, 1);
        createItem(Material.LEATHER_CHESTPLATE, "armor-item", main_menu, 2);
        createItem(Material.DIAMOND_SWORD, "hand-item", main_menu, 3);
        createItem(Material.SMOOTH_STONE_SLAB, "base-item", main_menu, 4);

        List<String> unbreakable_lore = Arrays.asList(getString("unbreakable-lore1"), getString("unbreakable-lore2"));

        ItemStack invulnerable = createItem(Material.BEDROCK, "invulnerable-item", main_menu, 5);
        ItemMeta invulnerable_meta = invulnerable.getItemMeta();
        ArrayList<String> invulnerable_lore = new ArrayList<>(unbreakable_lore);
        invulnerable_lore.addAll(0, Arrays.asList(getString("invulnerable-lore"), ""));
        invulnerable_meta.setLore(invulnerable_lore);
        invulnerable.setItemMeta(invulnerable_meta);
        main_menu.setItem(5, invulnerable);

        ItemStack visible = createItem(Material.GLASS_PANE, "visible-item", main_menu, 6);
        ItemMeta visible_meta = visible.getItemMeta();
        visible_meta.setLore(unbreakable_lore);
        visible.setItemMeta(visible_meta);
        main_menu.setItem(6, visible);

        p.openInventory(main_menu);
    }

    public void openArmorMenu(Player p) {
        Inventory armor_menu = Bukkit.createInventory(p, 45, getString("armor-menu"));

        createArmorItem(Material.LEATHER_HELMET, armor_menu, 0);
        createArmorItem(Material.IRON_HELMET, armor_menu, 1);
        createArmorItem(Material.CHAINMAIL_HELMET, armor_menu, 2);
        createArmorItem(Material.GOLDEN_HELMET, armor_menu, 3);
        createArmorItem(Material.DIAMOND_HELMET, armor_menu, 4);
        createArmorItem(Material.NETHERITE_HELMET, armor_menu, 5);

        createArmorItem(Material.PLAYER_HEAD, armor_menu, 9);
        createArmorItem(Material.CREEPER_HEAD, armor_menu, 10);
        createArmorItem(Material.ZOMBIE_HEAD, armor_menu, 11);
        createArmorItem(Material.SKELETON_SKULL, armor_menu, 12);
        createArmorItem(Material.WITHER_SKELETON_SKULL, armor_menu, 13);
        createArmorItem(Material.DRAGON_HEAD, armor_menu, 14);
        createArmorItem(Material.CARVED_PUMPKIN, armor_menu, 15);

        createArmorItem(Material.LEATHER_CHESTPLATE, armor_menu, 18);
        createArmorItem(Material.IRON_CHESTPLATE, armor_menu, 19);
        createArmorItem(Material.CHAINMAIL_CHESTPLATE, armor_menu, 20);
        createArmorItem(Material.GOLDEN_CHESTPLATE, armor_menu, 21);
        createArmorItem(Material.DIAMOND_CHESTPLATE, armor_menu, 22);
        createArmorItem(Material.NETHERITE_CHESTPLATE, armor_menu, 23);
        createArmorItem(Material.ELYTRA, armor_menu, 24);

        createArmorItem(Material.LEATHER_LEGGINGS, armor_menu, 27);
        createArmorItem(Material.IRON_LEGGINGS, armor_menu, 28);
        createArmorItem(Material.CHAINMAIL_LEGGINGS, armor_menu, 29);
        createArmorItem(Material.GOLDEN_LEGGINGS, armor_menu, 30);
        createArmorItem(Material.DIAMOND_LEGGINGS, armor_menu, 31);
        createArmorItem(Material.NETHERITE_LEGGINGS, armor_menu, 32);

        createArmorItem(Material.LEATHER_BOOTS, armor_menu, 36);
        createArmorItem(Material.IRON_BOOTS, armor_menu, 37);
        createArmorItem(Material.CHAINMAIL_BOOTS, armor_menu, 38);
        createArmorItem(Material.GOLDEN_BOOTS, armor_menu, 39);
        createArmorItem(Material.DIAMOND_BOOTS, armor_menu, 40);
        createArmorItem(Material.NETHERITE_BOOTS, armor_menu, 41);

        createItem(Material.GREEN_WOOL, "done-item", armor_menu, 26);
        p.openInventory(armor_menu);
    }

    public void openHandMenu(Player p) {
        Inventory hand_menu = Bukkit.createInventory(p, 9, getString("hand-menu"));
        createItem(Material.GREEN_WOOL, "done-item", hand_menu, 4);
        createItem(Material.SHIELD, "hand-left-item", hand_menu, 2);
        createItem(Material.DIAMOND_SWORD, "hand-right-item", hand_menu, 6);
        p.openInventory(hand_menu);
    }
    public void openConfirmMenu(Player p, String menu_name) {
        Inventory menu = Bukkit.createInventory(p, 27, menu_name);
        createItem(Material.GREEN_STAINED_GLASS_PANE, "confirm-yes-item", menu, 0,1,2,3,9,10,11,12,18,19,20,21);
        createItem(Material.RED_STAINED_GLASS_PANE, "confirm-no-item", menu, 5,6,7,8,14,15,16,17,23,24,25,26);
        p.openInventory(menu);
    }
}