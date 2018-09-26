package nl.kasper7.seanew.help;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SeaNewCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		sender.sendMessage(ChatColor.AQUA + "Do you want to SeaNew features?");
		sender.sendMessage(ChatColor.YELLOW + "This plugin provides some features to spice up your old oceans a bit.");
		sender.sendMessage(ChatColor.GOLD + "Usage: /bbrush [biome] (size) (square/circle)");
		sender.sendMessage(ChatColor.GOLD + "Usage: /pbrush [populator] (radius) (patchsize) (extra)");
		return false;
	}

}
