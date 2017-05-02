/*
 * Project: TeamHealth
 * Class: com.leontg77.teamhealth.HealthTask
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

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

/**
 * Health task runnable class.
 *
 * @author LeonTG
 */
public class HealthTask extends BukkitRunnable {
    private final Main plugin;

    private final Objective belowName;
    private final Objective tabList;

    public HealthTask(Main plugin, Scoreboard board) {
        this.plugin = plugin;

        Objective belowName = board.getObjective("THBelowName");
        Objective tabList = board.getObjective("THTabList");

        if (belowName == null) {
            this.belowName = board.registerNewObjective("THBelowName", "dummy");
        } else {
            this.belowName = belowName;
        }

        if (tabList == null) {
            this.tabList = board.registerNewObjective("THTabList", "dummy");
        } else {
            this.tabList = tabList;
        }

        this.belowName.setDisplaySlot(DisplaySlot.BELOW_NAME);
        this.belowName.setDisplayName("§4❤§f");

        this.tabList.setDisplaySlot(DisplaySlot.PLAYER_LIST);
    }

    @Override
    public void run() {
        for (Player online : Bukkit.getOnlinePlayers()) {
            int percent = plugin.makePercent(plugin.getTeamHealth(online));

            if (belowName != null) {
                Score score = belowName.getScore(online.getName());
                score.setScore(percent);
            }

            if (tabList != null) {
                Score score = tabList.getScore(online.getName());
                score.setScore(percent);
            }
        }
    }
}