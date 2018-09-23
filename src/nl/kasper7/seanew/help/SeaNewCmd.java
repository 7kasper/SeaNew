package nl.kasper7.seanew.help;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SeaNewCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		sender.sendMessage("=======[" + ChatColor.AQUA + "SeaNew" + ChatColor.RESET + "]=======");
		sender.sendMessage("/popbrush - Brush commands.");
		sender.sendMessage("========" + ChatColor.AQUA + "======" + ChatColor.RESET + "========");
		return false;
	}

}
