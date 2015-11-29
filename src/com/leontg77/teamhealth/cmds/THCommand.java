package com.leontg77.teamhealth.cmds;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import com.leontg77.teamhealth.Main;
import com.leontg77.teamhealth.Utilities;
import com.leontg77.teamhealth.listeners.DeathListener;

/**
 * TeamHealth Command
 * 
 * @author LeonTG77
 */
public class THCommand implements CommandExecutor {	
	private BukkitRunnable run = null;
	public boolean enabled = false;

	public Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
	public Objective teamHealthName = board.getObjective("teamHealthName");
	public Objective teamHealth = board.getObjective("teamHealth");

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!sender.hasPermission("teamhealth.admin")) {
			sender.sendMessage(Main.PREFIX + "§cYou do not have the required permission.");
			return true;
		}
		
		if (args.length == 0) {
			sender.sendMessage(Main.PREFIX + "Usage: /teamhealth <enable|disable>");
			return true;
		}
		
		if (args[0].equalsIgnoreCase("enable")) {
			if (enabled) {
				sender.sendMessage(Main.PREFIX + "TeamHealth is already enabled.");
				return true;
			}

			Bukkit.getServer().getPluginManager().registerEvents(new DeathListener(), Main.plugin);
			Utilities.getInstance().broadcast(Main.PREFIX + "TeamHealth has been enabled.");
			enabled = true;
			
			teamHealth = board.registerNewObjective("teamHealth", "dummy");
			teamHealth.setDisplaySlot(DisplaySlot.PLAYER_LIST);
			
			teamHealthName = board.registerNewObjective("teamHealthName", "dummy");
			teamHealthName.setDisplaySlot(DisplaySlot.BELOW_NAME);
			teamHealthName.setDisplayName("§4♥");
			
			run = new BukkitRunnable() {
				public void run() {
					for (Player online : Bukkit.getServer().getOnlinePlayers()) {	
						Team team = Utilities.getInstance().getTeam(online);
						
						if (team == null) {
							int percent = Utilities.getInstance().makePercent(online.getHealth());
							
							if (teamHealth != null) {
								Score score = teamHealth.getScore(online.getName());
								score.setScore(percent);
							}
							
							if (teamHealthName != null) {
								Score score = teamHealthName.getScore(online.getName());
								score.setScore(percent);
							}
						}
						else {
							double health = 0;
							
							for (String entry : team.getEntries()) {
								Player target = Bukkit.getServer().getPlayer(entry);
								
								if (target != null) {
									health = health + target.getHealth();
								}
							}
							
							int percent = Utilities.getInstance().makePercent(health);
							
							if (teamHealth != null) {
								Score score = teamHealth.getScore(online.getName());
								score.setScore(percent);
							}
							
							if (teamHealthName != null) {
								Score score = teamHealthName.getScore(online.getName());
								score.setScore(percent);
							}
						}
					}
				}
			};
			
			run.runTaskTimer(Main.plugin, 1, 1);
			return true;
		}
		
		if (args[0].equalsIgnoreCase("disable")) {
			if (!enabled) {
				sender.sendMessage(Main.PREFIX + "TeamHealth is not enabled.");
				return true;
			}
			
			Utilities.getInstance().broadcast(Main.PREFIX + "TeamHealth has been disabled.");

			HandlerList.unregisterAll(new DeathListener());
			enabled = false;
			
			teamHealthName.unregister();
			teamHealth.unregister();
			
			run.cancel();
			run = null;
			return true;
		} 
		
		sender.sendMessage(Main.PREFIX + "Usage: /teamhealth <enable|disable>");
		return true;
	}
}