package nl.kasper7.seanew.popbrush;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import nl.kasper7.seanew.generalutils.Brush;
import nl.kasper7.seanew.generalutils.Brushes;
import nl.kasper7.seanew.generalutils.PlayerUtils;
import nl.kasper7.seanew.generalutils.SuperGeneralUtils;
import nl.kasper7.seanew.generalutils.TargetUtils;
import nl.kasper7.seanew.popbrush.util.GenUtils;

public class PopBrushCmd implements CommandExecutor, TabCompleter {

	private Random random = new Random();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			Material key = PlayerUtils.materialInHand(player);
			if (args.length > 0) {
				if (key != Material.AIR) {
					SupportedPopulator selectedPopulator = getPopulator(args[0]);
					if (selectedPopulator != null) {
						AtomicInteger selectedSize = fromArg(sender, args, 1, selectedPopulator == SupportedPopulator.CORAL ? 1 : 8, 40, 1);
						AtomicInteger selectedAmount = fromArg(sender, args, 2, selectedPopulator == SupportedPopulator.CORAL ? 1 : 8, 100, 1);
						AtomicInteger extraValue = fromArg(sender, args, 3, 50, 100, 0);
						Consumer<Brush> commonConstructor = (brush) -> {
							brush.setMeta("size", selectedSize);
							brush.setMeta("amount", selectedAmount);
							brush.setMeta("extra", extraValue);
							brush.setMeta("maxsize", 40);
							brush.setMeta("minsize", 1);
						};
						BiFunction<Brush, Player, Boolean> rightClick = (brush, user) -> { return true;	};
						BiFunction<Brush, Player, Boolean> leftClick = (brush, user) -> { return true; };
						switch (selectedPopulator) {
							case KELP: leftClick = (brush, user) -> 
								GenUtils.genKelp(TargetUtils.getTarget(user), random, brush.getIntMeta("size"), brush.getIntMeta("amount")) > 0; 
								break;
							case GRASS: leftClick = (brush, user) -> 
								GenUtils.genSeaGrass(TargetUtils.getTarget(user), random, brush.getIntMeta("size"), brush.getIntMeta("amount"), brush.getIntMeta("extra") / 100d) > 0;
								rightClick = (brush, user) -> {
											int extra = brush.getIntMeta("extra");
											if (extra < 1) {
												extra = 25;
											} else if (extra <= 25) {
												extra = 50;
											} else if (extra <= 50) {
												extra = 75;
											} else if (extra <= 75) {
												extra = 100;
											} else {
												extra = 0;
											}
											brush.setIntMeta("extra", extra);
											user.sendActionBar(ChatColor.YELLOW + "Tall grass change set to: " + ChatColor.AQUA + extra	+ "%" + ChatColor.YELLOW + ".");
											return true;
										};
								break;
							case PICKLE: leftClick = (brush, user) -> 
								GenUtils.genSeaPicke(TargetUtils.getTarget(user), random, brush.getIntMeta("size"), brush.getIntMeta("amount")) > 0;
								break;
							case CORAL: leftClick = (brush, user) ->
								GenUtils.genRandomCoral(TargetUtils.getTarget(user), random, brush.getIntMeta("size"), brush.getIntMeta("amount")) > 0;
								break;
						}
						Brushes.add(player, new Brush(key, commonConstructor, rightClick, leftClick, Brushes.DEFAULT_INCREMENT_SIZE_FUNCTION, Brushes.DEFAULT_DECREMENT_SIZE_FUNCTION));
						player.sendActionBar(ChatColor.YELLOW + "Bound populationbrush for " + ChatColor.AQUA + selectedPopulator.name() 
						+ ChatColor.YELLOW + " with size " + ChatColor.AQUA + selectedSize
						+ ChatColor.YELLOW + " and amount " + ChatColor.AQUA + selectedAmount
						+ ChatColor.YELLOW + " to " + ChatColor.AQUA + key.name() 
						+ ChatColor.YELLOW + ".");
					} else {
						sender.sendMessage(ChatColor.RED + "Error: Unknown populator: " + args[0] + "!");
					}
				} else {
					sender.sendMessage(ChatColor.RED + "You cannot use AIR as a brush!");
				}
			} else {
				if (Brushes.hasBrushInHand(player)) {
					Brushes.remove(player);
					player.sendActionBar(ChatColor.YELLOW + "Cleared brushes from " + ChatColor.AQUA + key.name() + ChatColor.YELLOW + "!");
				} else {
					sender.sendMessage(ChatColor.AQUA + "Do you want to SeaNew world population?");
					sender.sendMessage(ChatColor.YELLOW + "This makes the item in hand paint with selected populator.");
					sender.sendMessage(ChatColor.GOLD + "Usage: /pbrush [populator] (radius) (patchsize) (specific values...)");
				}
			}
		} else {
			sender.sendMessage(ChatColor.RED + "Error: Only players can use brushes! Duh.");
		}
		return false;
	}

	private AtomicInteger fromArg(CommandSender sender, String[] args, int pos, int defaultValue, int max, int min) {
		AtomicInteger ret = new AtomicInteger(defaultValue);
		if (args.length > pos) { try {
				ret.set(Integer.parseInt(args[pos]));
				if (ret.get() < min || ret.get() > max) {
					ret.set(defaultValue);
					sender.sendMessage(ChatColor.RED + "Error: " + args[1] + " is out of bounds! Option can be between " + min + " and " + max + " Defaulting to: " + defaultValue);
				}
			} catch (NumberFormatException e) {
				sender.sendMessage(ChatColor.RED + "Error: " + args[1] + " is not a number! Defaulting to: " + defaultValue);
				ret.set(-999);
			}
		}
		return ret;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length < 2) {
			return Arrays.stream(SupportedPopulator.values())
					.map(suppop -> suppop.name().toLowerCase())
					.filter(name -> name.startsWith(args[0].toLowerCase().trim()))
					.collect(Collectors.toList());
		}
		return SuperGeneralUtils.EMPTY_STRING_LIST;
	}

	private SupportedPopulator getPopulator(String name) {
		try {
			return SupportedPopulator.valueOf(name.toLowerCase().trim());
		} catch (IllegalArgumentException e) {
			return null;
		}
	}
	private enum SupportedPopulator {
		KELP, GRASS, PICKLE, CORAL
	}

}
