package com.teamaurora.reforested.common.world.gen.feature;

import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.*;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.IWorldGenerationBaseReader;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraftforge.common.IPlantable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;

public class FancyBirchFeature extends Feature<BaseTreeFeatureConfig> {
    public FancyBirchFeature(Codec<BaseTreeFeatureConfig> config) {
        super(config);
    }

    private class AxisBlockPos {
        public BlockPos pos;
        public Direction.Axis axis;

        public AxisBlockPos(BlockPos p, Direction.Axis a) {
            pos = p;
            axis = a;
        }
    }

    @Override
    public boolean generate(ISeedReader worldIn, ChunkGenerator generator, Random rand, BlockPos position, BaseTreeFeatureConfig config) {
        int height = rand.nextInt(6) + 7;
        if (position.getY() <= 0 || position.getY() + height > worldIn.getHeight() - 4) {
            return false;
        }
        if (!isValidGround(worldIn, position.down())) {
            return false;
        }
        List<AxisBlockPos> logs = new ArrayList<>();
        List<BlockPos> leaves = new ArrayList<>();

        for (int i = 0; i < height; i++) {
            logs.add(new AxisBlockPos(position.up(i), Direction.Axis.Y));
        }
        for (int i = 0; i < 4; i++) {
            if (rand.nextInt(16) < height && height >= 8) {
                addBranch(position.up(rand.nextInt(height-7) + 5), leaves, logs, rand, Direction.byHorizontalIndex(i));
            }
        }
        if (height == 9) {
            addBranch(position.up(rand.nextInt(height-7) + 5), leaves, logs, rand, Direction.byHorizontalIndex(rand.nextInt(4)));
        } else if (height >= 10) {
            int i1 = rand.nextInt(4);
            int i2 = (i1 + rand.nextInt(3) + 1) % 4;
            addBranch(position.up(rand.nextInt(height-7) + 5), leaves, logs, rand, Direction.byHorizontalIndex(i1));
            addBranch(position.up(rand.nextInt(height-7) + 5), leaves, logs, rand, Direction.byHorizontalIndex(i2));
        }
        addCanopy(position.up(height-1), leaves, rand);

        List<BlockPos> leavesClean = cleanLeavesArray(leaves, logs);

        boolean flag = true;
        for (AxisBlockPos log : logs) {
            if (!isAirOrLeaves(worldIn, log.pos)) {
                flag = false;
            }
        }
        if (!flag) return false;

        setDirtAt(worldIn, position.down());

        for (AxisBlockPos log : logs) {
            setLogState(worldIn, log.pos, config.trunkProvider.getBlockState(rand, log.pos).with(RotatedPillarBlock.AXIS, log.axis));
        }
        for (BlockPos leaf : leavesClean) {
            if (isAirOrLeaves(worldIn, leaf)) setLogState(worldIn, leaf, config.leavesProvider.getBlockState(rand, leaf));
        }


        Set<BlockPos> decSet = Sets.newHashSet();
        MutableBoundingBox mutableBoundingBox = MutableBoundingBox.getNewBoundingBox();

        List<BlockPos> logsPos = new ArrayList<>();
        for (AxisBlockPos log : logs) {
            logsPos.add(log.pos);
        }

        if (!config.decorators.isEmpty()) {
            logsPos.sort(Comparator.comparingInt(Vector3i::getY));
            leavesClean.sort(Comparator.comparingInt(Vector3i::getY));
            config.decorators.forEach((decorator) -> decorator.func_225576_a_(worldIn, rand, logsPos, leavesClean, decSet, mutableBoundingBox));
        }

        return true;
    }

    private void addBranch(BlockPos pos, List<BlockPos> leaves, List<AxisBlockPos> logs, Random rand, Direction dir) {
        int bendAmount = rand.nextInt(3) - 1;
        BlockPos pos1;
        if (rand.nextInt(3) == 0) {
            // double diagonal
            pos1 = pos.offset(dir).offset(dir.rotateY(), bendAmount);
        } else {
            // single diagonal
            pos1 = pos.offset(dir);
        }
        logs.add(new AxisBlockPos(pos1, dir.getAxis()));
        BlockPos pos2 = pos1.offset(dir).offset(dir.rotateY(), bendAmount).up();
        logs.add(new AxisBlockPos(pos2, dir.getAxis()));
        addCanopy(pos2, leaves, rand);
    }

    private void addCanopy(BlockPos pos, List<BlockPos> leaves, Random rand) {
        addDisc1Corners(pos.down(), leaves, rand);
        addDisc2Corners(pos, leaves, rand);
        addDisc2Corners(pos.up(), leaves, rand);
        addDisc2(pos.up(2), leaves);
        addDisc1Corners(pos.up(3), leaves, rand);
        addDisc1(pos.up(4), leaves);
    }

    private void addDisc1(BlockPos pos, List<BlockPos> leaves) {
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                if (x == 0 || z == 0) {
                    leaves.add(pos.add(x, 0, z));
                }
            }
        }
    }

    private void addDisc1Corners(BlockPos pos, List<BlockPos> leaves, Random rand) {
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                if (x == 0 || z == 0 || rand.nextBoolean()) {
                    leaves.add(pos.add(x, 0, z));
                }
            }
        }
    }

    private void addDisc2(BlockPos pos, List<BlockPos> leaves) {
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                if (Math.abs(x) != 2 || Math.abs(z) != 2) {
                    leaves.add(pos.add(x, 0, z));
                }
            }
        }
    }

    private void addDisc2Corners(BlockPos pos, List<BlockPos> leaves, Random rand) {
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                if (Math.abs(x) != 2 || Math.abs(z) != 2 || rand.nextBoolean()) {
                    leaves.add(pos.add(x, 0, z));
                }
            }
        }
    }

    private List<BlockPos> cleanLeavesArray(List<BlockPos> leaves, List<AxisBlockPos> logs) {
        List<BlockPos> logsPos = new ArrayList<>();
        for (AxisBlockPos log : logs) {
            logsPos.add(log.pos);
        }
        List<BlockPos> newLeaves = new ArrayList<>();
        for (BlockPos leaf : leaves) {
            if (!logsPos.contains(leaf)) {
                newLeaves.add(leaf);
            }
        }
        return newLeaves;
    }

    protected final void setLogState(IWorldWriter worldIn, BlockPos pos, BlockState state) {
        worldIn.setBlockState(pos, state, 18);
    }

    public static boolean isAir(IWorldGenerationBaseReader worldIn, BlockPos pos) {
        if (!(worldIn instanceof IBlockReader)) {
            return worldIn.hasBlockState(pos, BlockState::isAir);
        }
        else {
            return worldIn.hasBlockState(pos, state -> state.isAir((IBlockReader) worldIn, pos));
        }
    }

    public static boolean isAirOrLeaves(IWorldGenerationBaseReader worldIn, BlockPos pos) {
        if (worldIn instanceof IWorldReader) {
            return worldIn.hasBlockState(pos, state -> state.canBeReplacedByLeaves((IWorldReader) worldIn, pos));
        }
        return worldIn.hasBlockState(pos, (state) -> state.isAir() || state.isIn(BlockTags.LEAVES));
    }

    public static void setDirtAt(IWorld worldIn, BlockPos pos) {
        Block block = worldIn.getBlockState(pos).getBlock();
        if (block == Blocks.GRASS_BLOCK || block == Blocks.FARMLAND) {
            worldIn.setBlockState(pos, Blocks.DIRT.getDefaultState(), 16);
        }
    }

    public static boolean isValidGround(IWorld world, BlockPos pos) {
        return world.getBlockState(pos).canSustainPlant(world, pos, Direction.UP, (IPlantable) Blocks.BIRCH_SAPLING);
    }
}
