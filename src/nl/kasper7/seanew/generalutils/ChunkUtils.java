package nl.kasper7.seanew.generalutils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.util.Vector;

@SuppressWarnings({"unchecked", "rawtypes"})
public class ChunkUtils {

	private static BiFunction<HeightMapType, Location, Integer> heightFunc;

	static {
		//TODO Less hack?!?
		//Setup heightmap function.
		try {
			Class<?> craftChunkClazz = ReflectUtils.getCraftBukkitClazz("CraftChunk");
			Class<?> nmsChunkClazz = ReflectUtils.getNMSClazz("Chunk");
			Class<?> heightMapClazz = ReflectUtils.getNMSClazz("HeightMap");
			Class<Enum> heightMapTypeClazz = (Class<Enum>) ReflectUtils.getNMSClazz("HeightMap$Type");
			Method heightMapMethod = heightMapClazz.getMethod("a", int.class, int.class);
			heightFunc = (type, loc) -> {
				try {
					Map<?, ?> heightMaps = (Map<?, ?>) nmsChunkClazz.getField("heightMap").get(craftChunkClazz.getMethod("getHandle").invoke(craftChunkClazz.cast(loc.getChunk())));
					Object hmType = Enum.valueOf(heightMapTypeClazz, type.name());
					Object heightMap = heightMaps.get(hmType);
					return (int) heightMapMethod.invoke(heightMap, loc.getBlockX() - (loc.getChunk().getX() * 16), loc.getBlockZ() - (loc.getChunk().getZ() * 16));
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException e) {
					e.printStackTrace();
				}
				return 256;
			};
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException e) {
			//This is custom fallback option for non-vanilla bukkit implementations.
			heightFunc = (type, loc) -> {
				//TODO Ugh...
				List<Material> ignore = new ArrayList<>();
				ignore.add(Material.AIR);
				switch(type) {
					//TODO check if methods are correct. Maybe make precompiled ignore list for everyone?
					case WORLD_SURFACE: {
						ignore.addAll(Arrays.stream(Material.values())
								.filter(mat -> mat.name().contains("LOG"))
								.collect(Collectors.toList()));
						break;
					}
					case WORLD_SURFACE_WG: {
						//Fallthrough
					}
					case MOTION_BLOCKING_NO_LEAVES: {
						ignore.addAll(Arrays.stream(Material.values())
								.filter(mat -> mat.name().contains("LEAVES"))
								.collect(Collectors.toList()));
						//Fallthough
					}
					case MOTION_BLOCKING: {
						//Fallthrough
					}
					case LIGHT_BLOCKING: {
						break;
					}
					case OCEAN_FLOOR_WG: {
						ignore.add(Material.WATER);
						//Fallthrough
					}
					case OCEAN_FLOOR: {
						ignore.addAll(Arrays.stream(Material.values())
								.filter(mat -> mat.name().contains("CORAL") || mat.name().contains("GRASS") || mat.name().contains("PICKLE"))
								.collect(Collectors.toList()));
						break;
					}
					default: {
						break;
					}
				}
				for (int i = 255; i > 0; i--) {
					if (!ignore.contains(loc.getWorld().getBlockAt(loc.getBlockX(), i, loc.getBlockZ()).getBlockData().getMaterial())) {
						return i;
					}
				}
				return 256;
			};
		}
	}

	@SuppressWarnings("deprecation")
	public static void updateChunk(Chunk chunk) {
		chunk.getWorld().refreshChunk(chunk.getX(), chunk.getZ());
	}

	public static Vector getLocalCoord(Location loc) {
		return new Vector(loc.getBlockX() - (loc.getChunk().getX() * 16), loc.getBlockY(), loc.getBlockZ() - (loc.getChunk().getZ() * 16));
	}

	public static int getHeight(Location loc, HeightMapType type) {
		return heightFunc.apply(type, loc);
	}

	public static Location getHeighestLocation(Location loc, HeightMapType type) {
		return new Location(loc.getWorld(), loc.getBlockX(), ChunkUtils.getHeight(loc, HeightMapType.OCEAN_FLOOR), loc.getBlockZ());
	}

	public enum HeightMapType {
		LIGHT_BLOCKING, MOTION_BLOCKING, MOTION_BLOCKING_NO_LEAVES, OCEAN_FLOOR_WG, OCEAN_FLOOR, WORLD_SURFACE, WORLD_SURFACE_WG
	}

}
