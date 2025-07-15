package com.github.tokyo1010.elokill.command;

import com.github.tokyo1010.elokill.EloKill;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EloCommand implements CommandExecutor {

    private final EloKill plugin;

    public EloCommand(EloKill plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {
                int elo = plugin.getElo(player.getUniqueId());
                player.sendMessage("Your Elo is: " + elo);
            } else {
                Player target = Bukkit.getPlayer(args[0]);
                if (target != null) {
                    int elo = plugin.getElo(target.getUniqueId());
                    player.sendMessage(target.getName() + "'s Elo is: " + elo);
                } else {
                    player.sendMessage("Player not found.");
                }
            }
        }
        return true;
    }
}
