package com.github.tokyo1010.elokill;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class EloKill extends JavaPlugin {

    private Map<UUID, Integer> eloRatings;
    private File eloFile;

    @Override
    public void onEnable() {
        // Plugin startup logic
        eloFile = new File(getDataFolder(), "elo.dat");
        loadEloRatings();
        getServer().getPluginManager().registerEvents(new PlayerKillListener(this), this);
        getCommand("leaderboard").setExecutor(new com.github.tokyo1010.elokill.command.LeaderboardCommand(this));
        getCommand("elo").setExecutor(new com.github.tokyo1010.elokill.command.EloCommand(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        saveEloRatings();
    }

    public int getElo(UUID playerId) {
        return eloRatings.getOrDefault(playerId, 1000);
    }

    public void setElo(UUID playerId, int elo) {
        eloRatings.put(playerId, elo);
    }

    public Map<UUID, Integer> getEloRatings() {
        return eloRatings;
    }

    @SuppressWarnings("unchecked")
    private void loadEloRatings() {
        if (eloFile.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(eloFile))) {
                eloRatings = (Map<UUID, Integer>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                eloRatings = new HashMap<>();
            }
        } else {
            eloRatings = new HashMap<>();
        }
    }

    private void saveEloRatings() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(eloFile))) {
            oos.writeObject(eloRatings);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
