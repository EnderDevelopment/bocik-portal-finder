package com.enderdevelopment.plugin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin implements Listener {
    private static final int MENU_SIZE = 27;
    private static final int START_SLOT = 13;
    private static final int SEARCH_RADIUS = 64;

    @Override
    public void onEnable() {
        if (getCommand("bocik") == null) {
            throw new IllegalStateException("The bocik command is missing from plugin.yml.");
        }
        getCommand("bocik").setExecutor(this);
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("Only players can use this command.", NamedTextColor.RED));
            return true;
        }
        openMenu(player);
        return true;
    }

    private void openMenu(Player player) {
        Inventory inventory = Bukkit.createInventory(new BocikMenu(), MENU_SIZE, Component.text("Bocik"));
        ItemStack start = new ItemStack(Material.LIME_WOOL);
        ItemMeta meta = start.getItemMeta();
        meta.displayName(Component.text("START", NamedTextColor.GREEN));
        start.setItemMeta(meta);
        inventory.setItem(START_SLOT, start);
        player.openInventory(inventory);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getView().getTopInventory().getHolder() instanceof BocikMenu)) {
            return;
        }
        event.setCancelled(true);
        if (!(event.getWhoClicked() instanceof Player player)
                || event.getRawSlot() != START_SLOT
                || event.getCurrentItem() == null
                || event.getCurrentItem().getType() != Material.LIME_WOOL) {
            return;
        }

        Location portal = findNearestPortal(player.getLocation());
        player.closeInventory();
        if (portal == null) {
            player.sendMessage(Component.text("No nether portal was found within 64 blocks.", NamedTextColor.RED));
            return;
        }
        Location destination = portal.clone().add(0.5, 1.0, 0.5);
        if (player.teleport(destination)) {
            player.sendMessage(Component.text("Teleported to the nearest nether portal.", NamedTextColor.GREEN));
        }
    }

    private Location findNearestPortal(Location origin) {
        World world = origin.getWorld();
        if (world == null) {
            return null;
        }
        int originX = origin.getBlockX();
        int originY = origin.getBlockY();
        int originZ = origin.getBlockZ();
        int minimumY = Math.max(world.getMinHeight(), originY - SEARCH_RADIUS);
        int maximumY = Math.min(world.getMaxHeight() - 1, originY + SEARCH_RADIUS);
        Location nearest = null;
        double nearestDistance = Double.MAX_VALUE;

        for (int x = originX - SEARCH_RADIUS; x <= originX + SEARCH_RADIUS; x++) {
            for (int z = originZ - SEARCH_RADIUS; z <= originZ + SEARCH_RADIUS; z++) {
                for (int y = minimumY; y <= maximumY; y++) {
                    if (world.getBlockAt(x, y, z).getType() != Material.NETHER_PORTAL) {
                        continue;
                    }
                    Location candidate = new Location(world, x, y, z);
                    double distance = candidate.distanceSquared(origin);
                    if (distance < nearestDistance) {
                        nearest = candidate;
                        nearestDistance = distance;
                    }
                }
            }
        }
        return nearest;
    }

    private static final class BocikMenu implements InventoryHolder {
        @Override
        public Inventory getInventory() {
            return Bukkit.createInventory(this, MENU_SIZE);
        }
    }
}
