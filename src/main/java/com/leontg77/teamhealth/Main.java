/*
 * Project: TeamHealth
 * Class: com.leontg77.teamhealth.Main
 *
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Leon Vaktskjold <leontg77@gmail.com>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.leontg77.teamhealth;

import com.leontg77.teamhealth.commands.THCommand;
import com.leontg77.teamhealth.listeners.DeathListener;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

/**
 * Main class of the plugin.
 *
 * @author LeonTG
 */
public class Main extends JavaPlugin {
    public static final String PREFIX = "§cTeam Health §8» §7";

    private Scoreboard board;

    @Override
    public void onEnable() {
        this.board = Bukkit.getScoreboardManager().getMainScoreboard();

        DeathListener listener = new DeathListener(this);
        THCommand command = new THCommand(this, listener, board);

        getCommand("teamhealth").setExecutor(command);
    }

    /**
     * Send the given message to all online players.
     *
     * @param message The message to send.
     */
    public void broadcast(String message) {
        for (Player online : Bukkit.getOnlinePlayers()) {
            online.sendMessage(message);
        }

        Bukkit.getLogger().info(message);
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
    int makePercent(double health) {
        double hearts = health / 2;
        double percent = hearts * 10;

        return (int) percent;
    }

    /**
     * Get the team health of this player, or his health if he's not a team.
     *
     * @param player The player to get for.
     * @return The team health.
     */
    double getTeamHealth(Player player) {
        Team team = getTeam(player);

        if (team == null) {
            return player.getHealth();
        }

        double health = 0;

        for (String entry : team.getEntries()) {
            Player online = Bukkit.getPlayerExact(entry);

            if (online == null) {
                continue;
            }

            health += online.getHealth();
        }

        return health;
    }
}