package com.uacmc.anticheat;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.UUID;

public class CombatCheck implements Listener {

    private final SuspicionManager suspicionManager;
    private final Logger logger;
    private HashMap<UUID, Long> lastClickTimes = new HashMap<>();
    private HashMap<UUID, Location> lastHitLocation = new HashMap<>();
    private HashMap<UUID, Float> lastYaw = new HashMap<>();

    public CombatCheck(AntiCheatPlugin plugin, SuspicionManager suspicionManager, Logger logger) {
        this.suspicionManager = suspicionManager;
        this.logger = logger;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;
        if (!(event.getEntity() instanceof Player)) return;

        Player attacker = (Player) event.getDamager();

        long now = System.currentTimeMillis();

        UUID attackerId = attacker.getUniqueId();
        Long lastClick = lastClickTimes.getOrDefault(attackerId, 0L);

        if (now - lastClick < 100) { // 10 clicks per second -> posible autoclicker
            suspicionManager.addSuspicion(attacker, 10, "Posible autoclicker");
            logger.log(attacker.getName() + " ataques muy rápidos.");
        }

        lastClickTimes.put(attackerId, now);

        // Check aimbot por movimientos muy precisos
        Location from = lastHitLocation.getOrDefault(attackerId, attacker.getLocation());
        Location nowLoc = attacker.getLocation();

        float lastYawVal = lastYaw.getOrDefault(attackerId, nowLoc.getYaw());
        float yawDiff = Math.abs(nowLoc.getYaw() - lastYawVal);

        if (yawDiff > 150) { // giro demasiado rápido, posible aimbot
            suspicionManager.addSuspicion(attacker, 15, "Posible aimbot (giro rápido)");
            logger.log(attacker.getName() + " giro rápido sospechoso: " + yawDiff);
        }

        lastHitLocation.put(attackerId, nowLoc);
        lastYaw.put(attackerId, nowLoc.getYaw());
    }
}
