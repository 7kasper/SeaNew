package nl.kasper7.seanew.biomebrush;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import nl.kasper7.seanew.biomebrush.util.BiomeUtils;
import nl.kasper7.seanew.generalutils.Brush;
import nl.kasper7.seanew.generalutils.Brushes;
import nl.kasper7.seanew.generalutils.ChunkUtils;
import nl.kasper7.seanew.generalutils.PlayerUtils;
import nl.kasper7.seanew.generalutils.SuperGeneralUtils;
import nl.kasper7.seanew.generalutils.TargetUtils;
import nl.kasper7.seanew.generalutils.TargetUtils.BlocksAndChunksAffected;

public class BiomeBrushCmd implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			Material key = PlayerUtils.materialInHand(player);
			if(args.length > 0) {
				if (key != Material.AIR) {
					Brushes.remove(player, key); //Remove previous brush if present.
					Biome selected = BiomeUtils.getBiome(args[0]);
					if (selected != null) {
						AtomicInteger selectedSize = new AtomicInteger(5);
						if (args.length > 1) {
							try {
								selectedSize.set(Integer.parseInt(args[1]));
							} catch (NumberFormatException e) {
								sender.sendMessage(ChatColor.RED + "Error: " + args[1] + " is not a number!");
								return false;
							}
						}
						BrushMode selectedMode = (args.length > 2  && args[2].toLowerCase().startsWith("sq"))
								? BrushMode.SQUARE : BrushMode.CIRCLE;
						Brushes.add(player, new Brush(key,
								(brush) -> { //Constructor
									brush.setMeta("size", selectedSize);
									brush.setMeta("biome", selected);
									brush.setMeta("mode", selectedMode);
									brush.setMeta("maxsize", new AtomicInteger(60));
									brush.setMeta("minsize", new AtomicInteger(1));
								},
								(brush, user) -> { //Right-click
									brush.setMeta("mode", 
										((BrushMode) brush.getMeta("mode")) == BrushMode.CIRCLE ? BrushMode.SQUARE : BrushMode.CIRCLE);
									user.sendActionBar(ChatColor.YELLOW + "Biomebrush mode set to: " 
											+ ChatColor.AQUA + ((BrushMode) brush.getMeta("mode")).name()
											+ ChatColor.YELLOW + ".");
									return true;
								},
								(brush, user) -> { //Left-click
									int size = ((AtomicInteger) brush.getMeta("size")).get();
									Biome biome = (Biome) brush.getMeta("biome");
									BrushMode mode = (BrushMode) brush.getMeta("mode");
									Location location = TargetUtils.getTarget(user);
									BlocksAndChunksAffected affected = mode == BrushMode.CIRCLE ?
											TargetUtils.getCircleAround(location, size) :
											TargetUtils.getSquareAround(location, size);
									affected.getBlocks().forEach(block -> block.setBiome(biome));
									affected.getChunks().forEach(chunk -> ChunkUtils.updateChunk(chunk));
									return true;
								},
								Brushes.DEFAULT_INCREMENT_SIZE_FUNCTION, //Drop
								Brushes.DEFAULT_DECREMENT_SIZE_FUNCTION //Swap
							));	
						player.sendActionBar(ChatColor.YELLOW + "Bound biomebrush for " + ChatColor.AQUA + selected.name() 
							+ ChatColor.YELLOW + " with size " + ChatColor.AQUA + selectedSize
							+ ChatColor.YELLOW + " to " + ChatColor.AQUA + key.name() 
							+ ChatColor.YELLOW + ".");
					} else {
						sender.sendMessage(ChatColor.RED + "Error: Biome " + args[0] + " not found!");
					}
				} else {
					sender.sendMessage(ChatColor.RED + "You cannot use AIR as a brush!");
				}
			} else {
				if (Brushes.hasBrushInHand(player)) {
					Brushes.remove(player);
					player.sendActionBar(ChatColor.YELLOW + "Cleared brushes from " + ChatColor.AQUA + key.name() + ChatColor.YELLOW + "!");
				} else {
					sender.sendMessage(ChatColor.AQUA + "Do you want to SeaNew biomes?");
					sender.sendMessage(ChatColor.YELLOW + "This makes the item in hand paint the selected biome.");
					sender.sendMessage(ChatColor.GOLD + "Usage: /bbrush [biome] (size) (square/circle)");
				}
			}
		} else {
			sender.sendMessage(ChatColor.RED + "Error: Only players can use brushes! Duh.");
		}
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length < 2) {
			return Arrays.stream(Biome.values())
				.map(biome -> biome.name().toLowerCase())
				.filter(name -> name.startsWith(args[0].toLowerCase().trim()))
				.collect(Collectors.toList());
		} else if (args.length == 3) {
			return Arrays.stream(BrushMode.values())
				.map(mode -> mode.name().toLowerCase())
				.filter(name -> name.startsWith(args[2].toLowerCase().trim()))
				.collect(Collectors.toList());
		}
		return SuperGeneralUtils.EMPTY_STRING_LIST;
	}

	private enum BrushMode {
		CIRCLE, SQUARE
	}

}
