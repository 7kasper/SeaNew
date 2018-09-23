package nl.kasper7.seanew.popbrush.util;

import java.util.Arrays;
import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;

public class TargetUtils {

	public static Location getTarget(LivingEntity entity) {
		return entity.getTargetBlock(new HashSet<Material>(Arrays.asList(Material.AIR, Material.WATER)), 200).getLocation();
	}

}
