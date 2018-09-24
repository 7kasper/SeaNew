package nl.kasper7.seanew.biomebrush.util;

import org.bukkit.block.Biome;

public class BiomeUtils {

	public static Biome getBiome(String name) {
		try {
			return Biome.valueOf(name.toUpperCase());
		} catch (IllegalArgumentException e) {
			return null;
		}
	}

	public static Biome getNextBiome(Biome biome) {
		return Biome.values()[(biome.ordinal() + 1) < Biome.values().length ? biome.ordinal() + 1 : 0];
	}

}
