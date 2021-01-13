package com.teamaurora.reforested.common.world.gen.feature;

import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
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

public class BirchFeature extends Feature<BaseTreeFeatureConfig> {
    public BirchFeature(Codec<BaseTreeFeatureConfig> config) {
        super(config);
    }

    @Override
    public boolean generate(ISeedReader worldIn, ChunkGenerator generator, Random rand, BlockPos position, BaseTreeFeatureConfig config) {
        int height = rand.nextInt(3) + 4;
        if (position.getY() <= 0 || position.getY() + height > worldIn.getHeight() - 2) {
            return false;
        }
        if (!isValidGround(worldIn, position.down())) {
            return false;
        }
        List<BlockPos> logs = new ArrayList<>();
        List<BlockPos> leaves = new ArrayList<>();

        for (int i = 0; i < height; i++) {
            logs.add(position.up(i));
            if (i >= height - 3) {
                addDiscThicc(position.up(i), leaves, rand);
            }
        }
        addDiscThinn(position.up(height), leaves, rand);
        leaves.add(position.up(height+1));

        List<BlockPos> leavesClean = cleanLeavesArray(leaves, logs);

        boolean flag = true;
        for (BlockPos log : logs) {
            if (!isAirOrLeaves(worldIn, log)) {
                flag = false;
            }
        }
        if (!flag) return false;

        setDirtAt(worldIn, position.down());

        for (BlockPos log : logs) {
            setLogState(worldIn, log, config.trunkProvider.getBlockState(rand, log));
        }
        for (BlockPos leaf : leavesClean) {
            if (isAirOrLeaves(worldIn, leaf)) setLogState(worldIn, leaf, config.leavesProvider.getBlockState(rand, leaf));
        }


        Set<BlockPos> decSet = Sets.newHashSet();
        MutableBoundingBox mutableBoundingBox = MutableBoundingBox.getNewBoundingBox();

        if (!config.decorators.isEmpty()) {
            logs.sort(Comparator.comparingInt(Vector3i::getY));
            leavesClean.sort(Comparator.comparingInt(Vector3i::getY));
            config.decorators.forEach((decorator) -> decorator.func_225576_a_(worldIn, rand, logs, leavesClean, decSet, mutableBoundingBox));
        }

        return true;
    }

    private void addDiscThicc(BlockPos pos, List<BlockPos> leaf, Random rand) {
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                if (x == 0 || z == 0 || rand.nextBoolean()) {
                    leaf.add(pos.add(x, 0, z));
                }
            }
        }
    }

    private void addDiscThinn(BlockPos pos, List<BlockPos> leaf, Random rand) {
        leaf.add(pos);
        for (int i = 0; i < 4; i++) {
            if (rand.nextBoolean()) {
                leaf.add(pos.offset(Direction.byHorizontalIndex(i)));
            }
        }
    }

    private List<BlockPos> cleanLeavesArray(List<BlockPos> leaves, List<BlockPos> logs) {
        List<BlockPos> newLeaves = new ArrayList<>();
        for (BlockPos leaf : leaves) {
            if (!logs.contains(leaf)) {
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