package me.maksyash.asgui.listeners;

import me.maksyash.asgui.ArmorStandGUI;
import me.maksyash.asgui.utils.MenuUtils;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.EquipmentSlot;

public class MainMenu implements Listener {
    ArmorStandGUI plugin;

    public MainMenu(ArmorStandGUI plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMenuClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        MenuUtils u = new MenuUtils(plugin, e, player);

        if (u.compareMenuTitles(plugin.getString("main-menu"))) {
            if (e.getCurrentItem() == null)
                return;
            ArmorStand as;
            if(!MenuUtils.tempStand.containsKey(player) || MenuUtils.tempStand.get(player).isDead()) {
                as = (ArmorStand) player.getWorld().spawnEntity(player.getLocation(), EntityType.ARMOR_STAND);
                as.addScoreboardTag("fromASpl");
                for (EquipmentSlot slot : EquipmentSlot.values()) {
                    as.addEquipmentLock(slot, ArmorStand.LockType.REMOVING_OR_CHANGING);
                    as.addEquipmentLock(slot, ArmorStand.LockType.ADDING);
                }
                MenuUtils.tempStand.put(player, as);
            } else
                as = MenuUtils.tempStand.get(player);

            if (u.compareWithMenuItem("arms-item")) {
                u.mainMenuLogic(as::hasArms, as::setArms, "set-arms-msg", "remove-arms-msg");
            }
            else if (u.compareWithMenuItem("glow-item")) {
                u.mainMenuLogic(as::isGlowing, as::setGlowing, "set-glow-msg", "remove-glow-msg");
            }
            else if (u.compareWithMenuItem("armor-item")) {
                plugin.openArmorMenu(player);
            }
            else if (u.compareWithMenuItem("hand-item")) {
                plugin.openHandMenu(player);
            }
            else if (u.compareWithMenuItem("base-item")) {
                u.mainMenuLogic(as::hasBasePlate, as::setBasePlate, "set-base-msg", "remove-base-msg");
            }
            else if (u.compareWithMenuItem("invulnerable-item")) {
                u.mainMenuLogic(as::isInvulnerable, as::setInvulnerable, "set-invulnerable-msg", "remove-invulnerable-msg");
            }
            else if (u.compareWithMenuItem("visible-item")) {
                u.mainMenuLogic(as::isVisible, as::setVisible, "set-visible-msg", "remove-visible-msg");
            }
            else if (u.compareWithMenuItem("create-item")) {
                plugin.openConfirmMenu(player, plugin.getString("confirm-creation-menu"));
            }
            else if (u.compareWithMenuItem("delete-item")) {
                plugin.openConfirmMenu(player, plugin.getString("confirm-deletion-menu"));
            }

            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onStandBroken(EntityDeathEvent e) {
        if (e.getEntity() instanceof ArmorStand) {
            ArmorStand stand = (ArmorStand) e.getEntity();
            if (stand.getScoreboardTags().contains("fromASpl"))
                e.getDrops().clear();
        }
    }
}