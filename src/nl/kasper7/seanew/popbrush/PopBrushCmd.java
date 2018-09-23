package nl.kasper7.seanew.popbrush;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import nl.kasper7.seanew.popbrush.util.GenUtils;
import nl.kasper7.seanew.popbrush.util.TargetUtils;

public class PopBrushCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if(args.length > 0) {
				Location location = TargetUtils.getTarget(player);
				Random random = new Random();
				sender.sendMessage("LOOKING AT: " + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ());
				switch(args[0].toLowerCase().trim()) {
					case "kelp": {
						sender.sendMessage("Genereted kelp: " + GenUtils.genKelp(location, random));
						break;
					}
					case "grass": {
						sender.sendMessage("Genereted grass: " + GenUtils.genSeaGrass(location, random, 8, 0.5));
						break;
					}
					case "pickle": {
						sender.sendMessage("Genereted pickle: " + GenUtils.genSeaPicke(location, random, 8));
						break;
					}
					case "coral": {
						sender.sendMessage("Genereted coral: " + GenUtils.genRandomCoral(location, random));
						break;
					}
				}
			}
		} else {
			sender.sendMessage(ChatColor.RED + " Only players can use brushes! Duh.");
		}
		return false;
	}

}
