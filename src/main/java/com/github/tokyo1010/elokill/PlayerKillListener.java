package com.github.tokyo1010.elokill;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerKillListener implements Listener {

    private final EloKill plugin;
    private final Map<UUID, Integer> eloRatings = new HashMap<>();
    private static final int DEFAULT_ELO = 1000;
    private static final int K_FACTOR = 32;

    public PlayerKillListener(EloKill plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (event.getEntity().getKiller() != null) {
            Player killer = event.getEntity().getKiller();
            Player victim = event.getEntity();

            // Drop player head
            ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) skull.getItemMeta();
            meta.setOwningPlayer(victim);
            int killerElo = getElo(killer.getUniqueId());
            meta.setDisplayName(getRarity(killerElo) + " " + victim.getName() + "'s Head");
            skull.setItemMeta(meta);
            victim.getWorld().dropItemNaturally(victim.getLocation(), skull);

            // Update Elo
            updateElo(killer, victim);
        }
    }

    private int getElo(UUID playerId) {
        return eloRatings.getOrDefault(playerId, DEFAULT_ELO);
    }

    private void updateElo(Player killer, Player victim) {
        int killerElo = getElo(killer.getUniqueId());
        int victimElo = getElo(victim.getUniqueId());

        double killerExpected = 1 / (1 + Math.pow(10, (double) (victimElo - killerElo) / 400));
        double victimExpected = 1 / (1 + Math.pow(10, (double) (killerElo - victimElo) / 400));

        int newKillerElo = (int) (killerElo + K_FACTOR * (1 - killerExpected));
        int newVictimElo = (int) (victimElo + K_FACTOR * (0 - victimExpected));

        eloRatings.put(killer.getUniqueId(), newKillerElo);
        eloRatings.put(victim.getUniqueId(), newVictimElo);

        killer.sendMessage("You killed " + victim.getName() + "! Your Elo is now " + newKillerElo);
        victim.sendMessage("You were killed by " + killer.getName() + "! Your Elo is now " + newVictimElo);
    }

    private String getRarity(int elo) {
        if (elo < 1000) {
            return "[N]";
        } else if (elo < 1100) {
            return "[R]";
        } else if (elo < 1300) {
            return "[SR]";
        } else {
            return "[SSR]";
        }
    }
}
