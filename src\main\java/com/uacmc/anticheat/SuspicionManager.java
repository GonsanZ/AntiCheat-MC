package com.uacmc.anticheat;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class SuspicionManager {

    private final AntiCheatPlugin plugin;
    private final Logger logger;
    private final HashMap<UUID, Integer> suspicionPoints = new HashMap<>();

    private final int BAN_THRESHOLD = 100;
    private final int WARN_THRESHOLD = 50;

    public SuspicionManager(AntiCheatPlugin plugin, Logger logger) {
        this.plugin = plugin;
        this.logger = logger;
    }

    public void addSuspicion(Player player, int points, String reason) {
        UUID uuid = player.getUniqueId();
        int current = suspicionPoints.getOrDefault(uuid, 0);
        current += points;
        suspicionPoints.put(uuid, current);

        logger.log(player.getName() + " ganando " + points + " puntos por: " + reason + " (Total: " + current + ")");

        if (current >= BAN_THRESHOLD) {
            banPlayer(player, reason);
        } else if (current >= WARN_THRESHOLD) {
            warnPlayer(player, reason);
        }
    }

    private void warnPlayer(Player player, String reason) {
        player.sendMessage("§c[AntiCheat] Advertencia: actividad sospechosa detectada (" + reason + "). Si persiste, serás baneado.");
        logger.log("Advertencia enviada a " + player.getName() + " por " + reason);
    }

    private void banPlayer(Player player, String reason) {
        Bukkit.getScheduler().runTask(plugin, () -> {
            player.kickPlayer("Has sido baneado por trampa: " + reason);
            Bukkit.getBanList(org.bukkit.BanList.Type.NAME).addBan(player.getName(), "Trampa detectada: " + reason, null, "AntiCheat");
            Bukkit.getLogger().info("Jugador " + player.getName() + " baneado por trampa: " + reason);
        });
    }
}
