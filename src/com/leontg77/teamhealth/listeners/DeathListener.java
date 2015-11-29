package com.leontg77.teamhealth.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scoreboard.Team;

import com.leontg77.teamhealth.Utilities;

/**
 * Death listener class.
 * 
 * @author LeonTG77
 */
public class DeathListener implements Listener {

	@EventHandler
    public void onDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		Team team = Utilities.getInstance().getTeam(player);

        if (team != null) {
            team.removeEntry(player.getName());
        }
    }
}