package nl.kasper7.seanew.generalutils;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class Brushes {

	private static Multimap<UUID, Brush> brushes = HashMultimap.create();

	public static Collection<Brush> get(Player player) {
		return brushes.get(player.getUniqueId());
	}

	public static void add(Player player, Brush brush) {
		brushes.put(player.getUniqueId(), brush);
	}

	public static void removeAll(Player player) {
		brushes.removeAll(player.getUniqueId());
	}

	public static void remove(Player player) {
		remove(player, PlayerUtils.materialInHand(player));
	}

	public static void remove(Player player, Material key) {
		brushes.get(player.getUniqueId()).removeIf(brush -> brush.getKey() == key);
	}

	public static boolean hasBrushInHand(Player player) {
		return brushes.get(player.getUniqueId()).stream().anyMatch(brush -> brush.getKey() == player.getInventory().getItemInMainHand().getType());
	}

	// -= Utils =-
	public static final BiFunction<Brush, Player, Boolean> DEFAULT_INCREMENT_SIZE_FUNCTION = (brush, user) -> {
		AtomicInteger size = ((AtomicInteger) brush.getMeta("size"));
		AtomicInteger maxsize = ((AtomicInteger) brush.getMeta("maxsize"));
		user.sendActionBar(ChatColor.YELLOW + "Brush size size set to: " 
			+ ChatColor.AQUA + (size.get() < maxsize.get() ? size.incrementAndGet() : size.get())
			+ ChatColor.YELLOW + ".");
		return true;
	};

	public static final BiFunction<Brush, Player, Boolean> DEFAULT_DECREMENT_SIZE_FUNCTION = (brush, user) -> {
		AtomicInteger size = ((AtomicInteger) brush.getMeta("size"));
		AtomicInteger minsize = ((AtomicInteger) brush.getMeta("minsize"));
		user.sendActionBar(ChatColor.YELLOW + "Brush size size set to: " 
				+ ChatColor.AQUA + (size.get() > minsize.get() ? size.decrementAndGet() : size.get())
				+ ChatColor.YELLOW + ".");
		return true;
	};

}
