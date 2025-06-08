package com.uacmc.anticheat;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class Logger {

    private final JavaPlugin plugin;
    private BufferedWriter writer;

    public Logger(JavaPlugin plugin) {
        this.plugin = plugin;
        try {
            plugin.getDataFolder().mkdirs();
            writer = new BufferedWriter(new FileWriter(plugin.getDataFolder() + "/anticheat.log", true));
        } catch (IOException e) {
            plugin.getLogger().warning("No se pudo abrir el archivo de log.");
        }
    }

    public void log(String message) {
        String log = "[" + LocalDateTime.now() + "] " + message;
        plugin.getLogger().info(log);
        try {
            if (writer != null) {
                writer.write(log);
                writer.newLine();
                writer.flush();
            }
        } catch (IOException e) {
            plugin.getLogger().warning("Error escribiendo en log: " + e.getMessage());
        }
    }
}
