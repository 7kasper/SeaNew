package nl.kasper7.seanew.popbrush.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.Bisected.Half;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.CoralWallFan;
import org.bukkit.block.data.type.SeaPickle;

import com.google.common.collect.Lists;

import nl.kasper7.seanew.generalutils.ChunkUtils;
import nl.kasper7.seanew.generalutils.ChunkUtils.HeightMapType;

public class GenUtils {

	public static int genKelp(Location location, Random random, int radius, int patchsize) {
        int n = 0; //Number of kelp plants generated
        for (int p = 0; p < patchsize; p++) {
            Location blockloc = oceanPatchLocation(location, radius, random);
            if (blockloc.getBlock().getBlockData().getMaterial() == Material.WATER) {
                BlockData kelp = Material.KELP.createBlockData();
                BlockData kelpPlant = Material.KELP_PLANT.createBlockData();
                int n3 = 1 + random.nextInt(10);
                for (int k = 0; k <= n3; ++k) {
                	Location blocklocup = blockloc.clone();
                	blocklocup.add(0, 1, 0);
                    if (blockloc.getBlock().getBlockData().getMaterial() == Material.WATER && blocklocup.getBlock().getBlockData().getMaterial() == Material.WATER) {
                        if (k == n3) {
                        	Ageable kelpy = (Ageable) kelp.clone();
                        	kelpy.setAge(random.nextInt(23));
                        	blockloc.getBlock().setBlockData(kelpy, false);
                            n++;
                        } else {
                        	blockloc.getBlock().setBlockData(kelpPlant, false);
                        }
                    } else if (k > 0) {
                    	Location blocklocdown = blockloc.clone();
                    	blocklocdown.subtract(0, 2, 0);
                        if (blocklocdown.getBlock().getBlockData().getMaterial() == Material.KELP) break;
                        blocklocdown.add(0, 1, 0);
                    	Ageable kelpy = (Ageable) kelp.clone();
                    	kelpy.setAge(random.nextInt(23));
                    	blocklocdown.getBlock().setBlockData(kelpy, false);
                        n++;
                        break;
                    }
                    blockloc.add(0, 1, 0);
                }
            }
        }
        return n;
	}

	public static int genSeaGrass(Location location, Random random, int radius, int patchsize, double tallchance) {
        int n = 0;
        for (int k = 0; k < patchsize; k++) {
            Location blockloc = oceanPatchLocation(location, radius, random);
            if (blockloc.getBlock().getBlockData().getMaterial() != Material.WATER) continue;
            boolean bl = random.nextDouble() < tallchance;
            BlockData blockLower = bl ? Material.TALL_SEAGRASS.createBlockData() : Material.SEAGRASS.createBlockData();
            if (bl) {
                BlockData blockUpper = Material.TALL_SEAGRASS.createBlockData(data -> ((Bisected) data).setHalf(Half.TOP));
                Location blocklocup = blockloc.clone();
                blocklocup.add(0, 1, 0);
                if (blocklocup.getBlock().getBlockData().getMaterial() == Material.WATER) {
                	blockloc.getBlock().setBlockData(blockLower, false);
                	blocklocup.getBlock().setBlockData(blockUpper, false);
                }
            } else {
            	blockloc.getBlock().setBlockData(blockLower, false);
            }
            n++;
        }
        return n;
	}

	public static int genSeaPicke(Location location, Random random, int radius, int patchsize) {
        int n = 0;
        for (int k = 0; k < patchsize; ++k) {
        	Location blockloc = oceanPatchLocation(location, radius, random);
        	BlockData pickle = Material.SEA_PICKLE.createBlockData(data -> {
        		((SeaPickle) data).setPickles(random.nextInt(4) + 1);
        		((SeaPickle) data).setWaterlogged(true);
        	});
            if (blockloc.getBlock().getType() != Material.WATER) continue;
        	blockloc.getBlock().setBlockData(pickle, false);
            n++;
        }
        return n;
	}

	public static int genRandomCoral(Location location, Random random, int radius, int patchsize) {
		int n = 0;
		for (int k = 0; k < patchsize; k++) {
			location = oceanPatchLocation(location, radius, random);
			BlockData blockData = randomCoralBlock(random).createBlockData();
			switch(random.nextInt(3)) {
				case 0: if (genCoralTree(location, random, blockData)) n++; break;
				case 1: if (genCoralClaw(location, random, blockData)) n++; break;
				case 2: if (genCoralMushroom(location, random, blockData)) n++; break;
			}
		}
		return n;
	}

	private static Location oceanPatchLocation(Location location, int radius, Random random) {
		return ChunkUtils.getHeighestLocation(new Location(location.getWorld(), 
				location.getBlockX() + random.nextInt(radius) - random.nextInt(radius), 0, 
				location.getZ() + random.nextInt(radius) - random.nextInt(radius)), HeightMapType.OCEAN_FLOOR);
	}

	private static List<Material> corals = Arrays.stream(Material.values()).filter(
			mat -> mat.name().contains("CORAL") && !mat.name().contains("WALL_FANG") && !mat.name().contains("DEAD")
		).collect(Collectors.toList());
	private static Material randomCoral(Random random) {
		return corals.get(random.nextInt(corals.size()));
	}
	private static List<Material> coralBlocks = Arrays.stream(Material.values()).filter(
			mat -> mat.name().contains("CORAL_BLOCK") && !mat.name().contains("DEAD")
		).collect(Collectors.toList());
	private static Material randomCoralBlock(Random random) {
		return coralBlocks.get(random.nextInt(coralBlocks.size()));
	}
	private static List<Material> coralWalls = Arrays.stream(Material.values()).filter(
			mat -> mat.name().contains("CORAL_WALL_FAN") && !mat.name().contains("DEAD")
		).collect(Collectors.toList());
	private static Material randomCoralWall(Random random) {
		return coralWalls.get(random.nextInt(coralWalls.size()));
	}
	private static List<BlockFace> directions = Arrays.asList(
			BlockFace.WEST, BlockFace.EAST, BlockFace.SOUTH, BlockFace.NORTH
		);
	private static BlockFace randomDirection(Random random) {
		return directions.get(random.nextInt(directions.size()));
	}
	
	//Abstract.
	private static boolean genBaseCoral(Location location, Random random, BlockData blockData) {
		Location blocklocup = location.clone();
		blocklocup.add(0, 1, 0);
        BlockData blockLocData = location.getBlock().getBlockData();
        if (blockLocData.getMaterial() != Material.WATER && !blockLocData.getMaterial().name().contains("CORAL") || blocklocup.getBlock().getBlockData().getMaterial() != Material.WATER) {
            return false;
        }
        location.getBlock().setBlockData(blockData, false);
        if (random.nextFloat() < 0.25f) {
        	blocklocup.getBlock().setBlockData(randomCoral(random).createBlockData(), false);
        } else if (random.nextFloat() < 0.05f) {
        	BlockData pickle = Material.SEA_PICKLE.createBlockData(data -> {
        		((SeaPickle) data).setPickles(random.nextInt(4) + 1);
        		((SeaPickle) data).setWaterlogged(true);
        	});
        	blocklocup.getBlock().setBlockData(pickle, false);
        }
        for (BlockFace face : directions) {
            Block adjecentBlock = location.getBlock().getRelative(face);
            if (random.nextFloat() >= 0.2f || adjecentBlock.getBlockData().getMaterial() != Material.WATER) continue;
            BlockData coralWall = randomCoralWall(random).createBlockData(data -> {
            	((CoralWallFan) data).setWaterlogged(true);
            	((CoralWallFan) data).setFacing(face);
            });
            adjecentBlock.setBlockData(coralWall, false);
        }
        return true;
	}

	private static boolean genCoralTree(Location location, Random random, BlockData blockData) {
		Location mutableLoc = location.clone();
        int baseHeight = random.nextInt(3) + 1;
        for (int k = 0; k < baseHeight; ++k) {
            if (!genBaseCoral(mutableLoc, random, blockData)) {
                return false;
            }
            mutableLoc.add(0, 1, 0);
        }
        int directionCount = random.nextInt(3) + 2;
        List<BlockFace> arrayList = directions;
        Collections.shuffle(arrayList, random);
        List<BlockFace> list = arrayList.subList(0, directionCount);
        for (BlockFace face : list) {
        	Location mutableTreeLoc = mutableLoc.clone();
        	mutableTreeLoc.add(face.getModX(), face.getModY(), face.getModZ());
            int branchHeight = random.nextInt(5) + 2;
            int branchBranch = 0;
            for (int k = 0; k < branchHeight && genBaseCoral(mutableTreeLoc, random, blockData); k++) {
            	mutableTreeLoc.add(0, 1, 0);
                if (k != 0 && (++branchBranch < 2 || random.nextFloat() >= 0.25f)) continue;
                mutableTreeLoc.add(face.getModX(), face.getModY(), face.getModZ());
                branchBranch = 0;
            }
        }
        return true;
	}

	private static boolean genCoralMushroom(Location location, Random random, BlockData blockData) {
        int n = random.nextInt(3) + 3;
        int n2 = random.nextInt(3) + 3;
        int n3 = random.nextInt(3) + 3;
        int n4 = random.nextInt(3) + 1;
        Location mutableLoc = location.clone();
        for (int k = 0; k <= n2; ++k) {
            for (int i2 = 0; i2 <= n; ++i2) {
                for (int i3 = 0; i3 <= n3; ++i3) {
                	mutableLoc.set(k + location.getX(), i2 + location.getY(), i3 + location.getZ());
                	mutableLoc = mutableLoc.getBlock().getRelative(BlockFace.DOWN, n4).getLocation();
                    if (
                    	(k != 0 && k != n2 || i2 != 0 && i2 != n) && 
                    	(i3 != 0 && i3 != n3 || i2 != 0 && i2 != n) && 
                    	(k != 0 && k != n2 || i3 != 0 && i3 != n3) && 
                    	(k == 0 || k == n2 || i2 == 0 || i2 == n || i3 == 0 || i3 == n3) && 
                    	random.nextFloat() >= 0.1f && 
                    	genBaseCoral(mutableLoc, random, blockData)
                    )
                    	continue;
                }
            }
        }
        return true;
	}

    @SuppressWarnings("incomplete-switch")
	private static BlockFace leftTurn(BlockFace that) {
        switch (that) {
            case NORTH: {
                return BlockFace.EAST;
            }
            case EAST: {
                return BlockFace.SOUTH;
            }
            case SOUTH: {
                return BlockFace.WEST;
            }
            case WEST: {
                return BlockFace.NORTH;
            }
        }
        throw new IllegalStateException("Unable to get Y-rotated facing of " + that);
    }

    @SuppressWarnings("incomplete-switch")
	private static BlockFace rightTurn(BlockFace that) {
        switch (that) {
            case NORTH: {
                return BlockFace.WEST;
            }
            case EAST: {
                return BlockFace.NORTH;
            }
            case SOUTH: {
                return BlockFace.EAST;
            }
            case WEST: {
                return BlockFace.SOUTH;
            }
        }
        throw new IllegalStateException("Unable to get CCW facing of " + that);
    }

	private static boolean genCoralClaw(Location location, Random random, BlockData blockData) {
        if (!genBaseCoral(location, random, blockData)) {
            return false;
        }
        BlockFace face = randomDirection(random);
        int n = random.nextInt(2) + 2;
        ArrayList<BlockFace> arrayList = Lists.newArrayList(face, leftTurn(face), rightTurn(face));
        Collections.shuffle(arrayList, random);
        List<BlockFace> list = arrayList.subList(0, n);
        block0 : for (BlockFace facing : list) {
            int n2;
            BlockFace tertFace;
            int n3;
            Location mutableLoc = location.clone();
            int n4 = random.nextInt(2) + 1;
            mutableLoc.add(facing.getModX(), facing.getModY(), facing.getModZ());
            if (facing == face) {
                tertFace = face;
                n3 = random.nextInt(3) + 2;
            } else {
            	mutableLoc.add(0, 1, 0);
                BlockFace[] arrenumDirection = new BlockFace[]{facing, BlockFace.UP};
                tertFace = arrenumDirection[random.nextInt(arrenumDirection.length)];
                n3 = random.nextInt(3) + 3;
            }
            for (n2 = 0; n2 < n4 && genBaseCoral(mutableLoc, random, blockData); ++n2) {
            	mutableLoc.add(tertFace.getModX(), tertFace.getModY(), tertFace.getModZ());
            }
            mutableLoc.add(tertFace.getOppositeFace().getModX(), tertFace.getOppositeFace().getModY(), tertFace.getOppositeFace().getModZ());
            mutableLoc.add(0, 1, 0);
            for (n2 = 0; n2 < n3; ++n2) {
            	mutableLoc.add(face.getModX(), face.getModY(), face.getModZ());
                if (!genBaseCoral(mutableLoc, random, blockData)) continue block0;
                if (random.nextFloat() >= 0.25f) continue;
                mutableLoc.add(0, 1, 0);
            }
        }
        return true;
	}

}
