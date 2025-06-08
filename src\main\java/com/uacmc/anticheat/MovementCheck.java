package com.uacmc.anticheat;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MovementCheck implements Listener {

    private final SuspicionManager suspicionManager;
    private final Logger logger;

    public MovementCheck(AntiCheatPlugin plugin, SuspicionManager suspicionManager, Logger logger) {
        this.suspicionManager = suspicionManager;
        this.logger = logger;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR || player.isFlying()) {
            return;
        }

        Location from = event.getFrom();
        Location to = event.getTo();

        if (to == null) return;

        double distance = from.distance(to);

        boolean inWater = player.getLocation().getBlock().isLiquid();

        double maxDistance = inWater ? 1.5 : 1.0;

        if (distance > maxDistance) {
            suspicionManager.addSuspicion(player, 15, "Speedhack/flyhack - movimiento demasiado rápido: " + distance);
            player.teleport(from);
            player.sendMessage("§c[AntiCheat] Movimiento sospechoso bloqueado!");
            logger.log(player.getName() + " movió demasiado rápido: " + distance);
        }

        double verticalDiff = Math.abs(to.getY() - from.getY());
        if (verticalDiff > 1.5 && !player.isOnGround()) {
            suspicionManager.addSuspicion(player, 20, "Movimiento vertical imposible: " + verticalDiff);
            player.teleport(from);
            player.sendMessage("§c[AntiCheat] Movimiento vertical sospechoso bloqueado!");
            logger.log(player.getName() + " movimiento vertical sospechoso: " + verticalDiff);
        }
    }
}
