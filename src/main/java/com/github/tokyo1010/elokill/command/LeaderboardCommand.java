package com.github.tokyo1010.elokill.command;

import com.github.tokyo1010.elokill.EloKill;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class LeaderboardCommand implements CommandExecutor {

    private final EloKill plugin;

    public LeaderboardCommand(EloKill plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Map<UUID, Integer> eloRatings = plugin.getEloRatings();
            if (eloRatings.isEmpty()) {
                player.sendMessage("No Elo ratings to display.");
                return true;
            }

            Map<UUID, Integer> sortedEloRatings = eloRatings.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

            player.sendMessage("§6Elo Leaderboard:");
            int rank = 1;
            for (Map.Entry<UUID, Integer> entry : sortedEloRatings.entrySet()) {
                Player p = Bukkit.getPlayer(entry.getKey());
                String playerName = p != null ? p.getName() : "Unknown";
                player.sendMessage(rank + ". " + playerName + ": " + entry.getValue());
                rank++;
            }
        }
        return true;
    }
}
