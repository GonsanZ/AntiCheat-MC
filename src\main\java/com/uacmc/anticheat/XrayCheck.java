package com.uacmc.anticheat;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class XrayCheck implements Listener {

    private final SuspicionManager suspicionManager;
    private final Logger logger;

    public XrayCheck(AntiCheatPlugin plugin, SuspicionManager suspicionManager, Logger logger) {
        this.suspicionManager = suspicionManager;
        this.logger = logger;
    }

    @EventHandler
    public void onBlockInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getClickedBlock() == null) return;

        Material blockType = event.getClickedBlock().getType();

        // Bloques valiosos para detectar posible xray
        if (blockType == Material.DIAMOND_ORE || blockType == Material.GOLD_ORE || blockType == Material.EMERALD_ORE || blockType == Material.REDSTONE_ORE) {
            Location loc = event.getClickedBlock().getLocation();

            // Muy simplificado: si el jugador no tiene línea directa (sin bloques entre medio) o está muy lejos, sospechoso
            if (!hasLineOfSight(player, loc)) {
                suspicionManager.addSuspicion(player, 25, "Posible XRay al interactuar con " + blockType.name());
                player.sendMessage("§c[AntiCheat] Interacción sospechosa con bloque valioso detectada.");
                logger.log(player.getName() + " posible XRay en " + loc);
            }
        }
    }

    private boolean hasLineOfSight(Player player, Location blockLoc) {
        Location eye = player.getEyeLocation();

        Vector direction = blockLoc.toVector().subtract(eye.toVector()).normalize();

        Location checkLoc = eye.clone();
        double maxDistance = eye.distance(blockLoc);

        for (double d = 0; d < maxDistance; d += 0.5) {
            checkLoc.add(direction.multiply(0.5));
            if (checkLoc.getBlock().getType().isSolid() && !checkLoc.getBlock().getLocation().equals(blockLoc)) {
                return false;
            }
        }
        return true;
    }
}
