package nl.kasper7.seanew.generalutils;

import java.util.Collection;
import java.util.UUID;

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

}
