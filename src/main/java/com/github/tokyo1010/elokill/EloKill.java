package com.github.tokyo1010.elokill;

import org.bukkit.plugin.java.JavaPlugin;

public final class EloKill extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new PlayerKillListener(this), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
