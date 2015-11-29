package com.leontg77.teamhealth;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

/**
 * Utilities class.
 * <p>
 * Contains methods for making health to percent, getting online players and broadcasting.
 * 
 * @author LeonTG77
 */
public class Utilities {
	private Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
	private static Utilities instance = new Utilities();
	
	/**
	 * Gets the instance of the class.
	 * 
	 * @return The instance.
	 */
	public static Utilities getInstance() {
		return instance;
	}
	
	/**
	 * Broadcasts a message to everyone online.
	 * 
	 * @param message the message.
	 */
	public void broadcast(String message) {
		for (Player online : Bukkit.getServer().getOnlinePlayers()) {
			online.sendMessage(message);
		}
		
		Bukkit.getLogger().info(message.replaceAll("§l", "").replaceAll("§o", "").replaceAll("§r", "").replaceAll("§m", "").replaceAll("§n", ""));
	}

	/**
	 * Gets the team of the given player player.
	 * 
	 * @param player the player getting.
	 * @return The team, null if the player isn't on a team.
	 */
	public Team getTeam(OfflinePlayer player) {
		return board.getEntryTeam(player.getName());
	}
	
	/**
	 * Turn the health given into percent.
	 * 
	 * @param health the health of the player.
	 * @return the percent of the health.
	 */
	public int makePercent(double health) {
		double hearts = health / 2;
		double precent = hearts * 10;
		return (int) precent;
	}
}