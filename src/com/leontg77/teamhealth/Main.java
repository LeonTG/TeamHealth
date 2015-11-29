package com.leontg77.teamhealth;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import com.leontg77.teamhealth.cmds.THCommand;

/**
 * Main class of the TeamHealth plugin.
 * 
 * @author LeonTG77
 */
public class Main extends JavaPlugin {
	public static final String PREFIX = "§c§lTeamHealth §8» §7";
	
	public static Main plugin;
	private THCommand cmd;
	
	@Override
	public void onDisable() {
		PluginDescriptionFile file = getDescription();
		getLogger().info(file.getName() + " is now disabled.");
		plugin = null;
		
		if (cmd.enabled) {
			cmd.teamHealthName.unregister();
			cmd.teamHealth.unregister();
		}
	}
	
	@Override
	public void onEnable() {
		PluginDescriptionFile file = getDescription();
		getLogger().info(file.getName() + " v" + file.getVersion() + " is now enabled.");
		plugin = this;
		
		cmd = new THCommand();
		getCommand("teamhealth").setExecutor(cmd);
	}
}