package nl.kasper7.seanew.popbrush.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.util.Vector;

@SuppressWarnings({"unchecked", "rawtypes"})
public class ChunkUtils {

	private static Class<?> craftChunkClazz;
	private static Class<?> nmsChunkClazz;
	private static Class<?> heightMapClazz;
	private static Class<Enum> heightMapTypeClazz;
	private static Method heightMapMethod;
	
	
	static {
		try {
			craftChunkClazz = Class.forName("org.bukkit.craftbukkit.v1_13_R2.CraftChunk");
			nmsChunkClazz = Class.forName("net.minecraft.server.v1_13_R2.Chunk");
			heightMapClazz = Class.forName("net.minecraft.server.v1_13_R2.HeightMap");
			heightMapTypeClazz = (Class<Enum>) Class.forName("net.minecraft.server.v1_13_R2.HeightMap$Type");
			heightMapMethod = heightMapClazz.getMethod("a", int.class, int.class);
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
	}
	public static Vector getLocalCoord(Location loc) {
		return new Vector(loc.getBlockX() - (loc.getChunk().getX() * 16), loc.getBlockY(), loc.getBlockZ() - (loc.getChunk().getZ() * 16));
	}

	public static int getHeight(Location loc, HeightMapType type) {
		try {
			Map<?, ?> heightMaps = (Map<?, ?>) nmsChunkClazz.getField("heightMap").get(craftChunkClazz.getMethod("getHandle").invoke(craftChunkClazz.cast(loc.getChunk())));
			Object hmType = Enum.valueOf(heightMapTypeClazz, type.name());
			Object heightMap = heightMaps.get(hmType);
			return (int) heightMapMethod.invoke(heightMap, loc.getBlockX() - (loc.getChunk().getX() * 16), loc.getBlockZ() - (loc.getChunk().getZ() * 16));
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException e) {
			e.printStackTrace();
		}
		return 256;
	}

	public static Location getHeighestLocation(Location loc, HeightMapType type) {
		return new Location(loc.getWorld(), loc.getBlockX(), ChunkUtils.getHeight(loc, HeightMapType.OCEAN_FLOOR), loc.getBlockZ());
	}

	public enum HeightMapType {
		LIGHT_BLOCKING, MOTION_BLOCKING, MOTION_BLOCKING_NO_LEAVES, OCEAN_FLOOR_WG, OCEAN_FLOOR, WORLD_SURFACE, WORLD_SURFACE_WG
	}

}
