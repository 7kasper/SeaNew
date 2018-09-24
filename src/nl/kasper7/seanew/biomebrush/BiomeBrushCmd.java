package nl.kasper7.seanew.biomebrush;

import java.util.ArrayList;
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
						Brushes.add(player, new Brush(key,
								(brush) -> {
									brush.setMeta("size", selectedSize);
									brush.setMeta("biome", selected);
								},
								(brush, user) -> {
									brush.setMeta("biome", BiomeUtils.getNextBiome((Biome) brush.getMeta("biome")));
									user.sendActionBar(ChatColor.YELLOW + "Biomebrush biome set to: " 
											+ ChatColor.AQUA + ((Biome) brush.getMeta("biome")).name()
											+ ChatColor.YELLOW + ".");
									return true;
								},
								(brush, user) -> {
									int radius = ((AtomicInteger) brush.getMeta("size")).get();
									Biome biome = (Biome) brush.getMeta("biome");
									Location location = TargetUtils.getTarget(user);
									BlocksAndChunksAffected affected = TargetUtils.getCircleAround(location, radius);
									affected.getBlocks().forEach(block -> block.setBiome(biome));
									affected.getChunks().forEach(chunk -> ChunkUtils.updateChunk(chunk));
									return true;
								},
								(brush, user) -> {
									user.sendActionBar(ChatColor.YELLOW + "Biomebrush size size set to: " 
										+ ChatColor.AQUA + ((AtomicInteger) brush.getMeta("size")).incrementAndGet()
										+ ChatColor.YELLOW + ".");
									return true;
								},
								(brush, user) -> {
									user.sendActionBar(ChatColor.YELLOW + "Biomebrush size size set to: " 
											+ ChatColor.AQUA + ((AtomicInteger) brush.getMeta("size")).decrementAndGet()
											+ ChatColor.YELLOW + ".");
									return true;
								}
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
					sender.sendMessage(ChatColor.YELLOW + "This makes the item in hand a biome brush for the selected biome.");
				}
			}
		} else {
			sender.sendMessage(ChatColor.RED + "Error: Only players can use brushes! Duh.");
		}
		return false;
	}

	static final List<String> empty = new ArrayList<>(); 
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length < 2) {
			return Arrays.stream(Biome.values())
				.map(biome -> biome.name().toLowerCase())
				.filter(name -> name.startsWith(args[0].toLowerCase().trim()))
				.collect(Collectors.toList());
		}
		return empty;
	}

}
