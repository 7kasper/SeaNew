package nl.kasper7.seanew.generalutils;

import org.bukkit.Material;
import org.bukkit.entity.Player;

public class PlayerUtils {

	public static Material materialInHand(Player player) {
		return player.getInventory().getItemInMainHand().getType();
	}

}
