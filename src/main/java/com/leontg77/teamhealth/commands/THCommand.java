/*
 * Project: TeamHealth
 * Class: com.leontg77.teamhealth.commands.THCommand
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

package com.leontg77.teamhealth.commands;

import com.google.common.collect.Lists;
import com.leontg77.teamhealth.HealthTask;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.HandlerList;
import org.bukkit.scoreboard.Scoreboard;

import com.leontg77.teamhealth.Main;
import com.leontg77.teamhealth.listeners.DeathListener;
import org.bukkit.util.StringUtil;

import java.util.List;

/**
 * Team Health command class.
 *
 * @author LeonTG
 */
public class THCommand implements CommandExecutor, TabCompleter {
    private static final String PERMISSION = "teamhp.manage";

    private final DeathListener listener;
    private final Scoreboard board;

    private final Main plugin;

    public THCommand(Main plugin, DeathListener listener, Scoreboard board) {
        this.plugin = plugin;

        this.listener = listener;
        this.board = board;
    }

    private boolean enabled = false;
    private HealthTask task = null;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(Main.PREFIX + "Usage: /teamhealth <info/enable/disable>");
            return true;
        }

        if (args[0].equalsIgnoreCase("info")) {
            sender.sendMessage(Main.PREFIX + "Plugin creator: §aLeonTG");
            sender.sendMessage(Main.PREFIX + "Version: §av" + plugin.getDescription().getVersion());
            sender.sendMessage(Main.PREFIX + "Description:");
            sender.sendMessage("§8» §f" + plugin.getDescription().getDescription());
            return true;
        }

        if (args[0].equalsIgnoreCase("enable")) {
            if (!sender.hasPermission(PERMISSION)) {
                sender.sendMessage(ChatColor.RED + "You don't have permission.");
                return true;
            }

            if (enabled) {
                sender.sendMessage(Main.PREFIX + "Team Health is already enabled.");
                return true;
            }

            plugin.broadcast(Main.PREFIX + "Team Health has been enabled.");
            enabled = true;

            Bukkit.getPluginManager().registerEvents(listener, plugin);

            task = new HealthTask(plugin, board);
            task.runTaskTimer(plugin, 1L, 1L);
            return true;
        }

        if (args[0].equalsIgnoreCase("disable")) {
            if (!sender.hasPermission(PERMISSION)) {
                sender.sendMessage(ChatColor.RED + "You don't have permission.");
                return true;
            }

            if (!enabled) {
                sender.sendMessage(Main.PREFIX + "Team Health is not enabled.");
                return true;
            }

            plugin.broadcast(Main.PREFIX + "Team Health has been disabled.");
            enabled = false;

            HandlerList.unregisterAll(listener);

            if (task != null) {
                task.cancel();
            }

            task = null;
            return true;
        }

        sender.sendMessage(Main.PREFIX + "Usage: /teamhealth <info/enable/disable>");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> list = Lists.newArrayList();

        if (args.length == 1) {
            list.add("info");

            if (sender.hasPermission(PERMISSION)) {
                list.add("enable");
                list.add("disable");
            }
        }

        return StringUtil.copyPartialMatches(args[args.length - 1], list, Lists.newArrayList());
    }
}
