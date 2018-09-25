package nl.kasper7.seanew.generalutils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;

public class TargetUtils {

	public static Location getTarget(LivingEntity entity) {
		return entity.getTargetBlock(new HashSet<Material>(Arrays.asList(Material.AIR, Material.WATER)), 200).getLocation();
	}

	public static BlocksAndChunksAffected getCircleAround(Location location, int radius) {
		Set<Block> blocks = new HashSet<>();
		Set<Chunk> chunks = new HashSet<>();
	    int cx = location.getBlockX();
	    int cy = location.getBlockY();
	    int cz = location.getBlockZ();
	    World world = location.getWorld();
	    for (int x = cx - radius; x <= cx + radius; x++) {
	        for (int z = cz - radius; z <= cz + radius; z++) {
	            if ((cx - x) * (cx - x) + (cz - z) * (cz - z) <= (radius * radius)) {
	            	Block b = world.getBlockAt(x, cy, z);
	                blocks.add(b);
	                chunks.add(b.getChunk());
	            }
	        }
	    }
		return new BlocksAndChunksAffected(blocks, chunks);
	}

	public static BlocksAndChunksAffected getSquareAround(Location location, int size) {
		Set<Block> blocks = new HashSet<>();
		Set<Chunk> chunks = new HashSet<>();
	    int cx = location.getBlockX();
	    int cy = location.getBlockY();
	    int cz = location.getBlockZ();
	    World world = location.getWorld();
	    for (int x = cx - size; x <= cx + size; x++) {
	        for (int z = cz - size; z <= cz + size; z++) {
            	Block b = world.getBlockAt(x, cy, z);
                blocks.add(b);
                chunks.add(b.getChunk());
	        }
	    }
		return new BlocksAndChunksAffected(blocks, chunks);
	}

	public static class BlocksAndChunksAffected {

		private Set<Block> blocks;
		private Set<Chunk> chunks;

		public BlocksAndChunksAffected(Set<Block> blocks, Set<Chunk> chunks) {
			super();
			this.blocks = blocks;
			this.chunks = chunks;
		}

		public Set<Block> getBlocks() {
			return blocks;
		}

		public void setBlocks(Set<Block> blocks) {
			this.blocks = blocks;
		}

		public Set<Chunk> getChunks() {
			return chunks;
		}

		public void setChunks(Set<Chunk> chunks) {
			this.chunks = chunks;
		}

	}

}
