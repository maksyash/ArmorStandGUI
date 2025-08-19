package me.maksyash.asgui.commands;

import me.maksyash.asgui.ArmorStandGUI;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class ASCommand implements CommandExecutor, TabExecutor {

    ArmorStandGUI plugin;

    public ASCommand(ArmorStandGUI plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        ResourceBundle rb = ResourceBundle.getBundle("asgui", new Locale("", ""));

        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission("asgui.command")) {
                if (args.length > 0) {
                    plugin.openList(p);
                } else {
                    plugin.openMainMenu(p);
                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1.0F, 1.0F);
                }
            } else {
                p.sendMessage(plugin.getString("no-permission"));
            }
        } else if (sender instanceof BlockCommandSender) {
            BlockCommandSender cb = (BlockCommandSender) sender;
            cb.sendMessage(ChatColor.translateAlternateColorCodes('&', rb.getString("availability")));
        } else
            System.out.println(ChatColor.translateAlternateColorCodes('&', rb.getString("availability")));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1){
            List<String> arguments = new ArrayList<>();
            arguments.add("list");
            return arguments;
        }
        return null;
    }
}