package com.uacmc.anticheat;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class AntiCheatPlugin extends JavaPlugin {

    private SuspicionManager suspicionManager;
    private MovementCheck movementCheck;
    private CombatCheck combatCheck;
    private XrayCheck xrayCheck;
    private Logger logger;

    @Override
    public void onEnable() {
        logger = new Logger(this);
        suspicionManager = new SuspicionManager(this, logger);
        movementCheck = new MovementCheck(this, suspicionManager, logger);
        combatCheck = new CombatCheck(this, suspicionManager, logger);
        xrayCheck = new XrayCheck(this, suspicionManager, logger);

        Bukkit.getPluginManager().registerEvents(movementCheck, this);
        Bukkit.getPluginManager().registerEvents(combatCheck, this);
        Bukkit.getPluginManager().registerEvents(xrayCheck, this);

        getLogger().info("AntiCheat avanzado habilitado.");
    }
}
