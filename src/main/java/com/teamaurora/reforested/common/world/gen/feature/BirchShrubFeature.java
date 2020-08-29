package com.teamaurora.reforested.common.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeavesBlock;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldWriter;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.IWorldGenerationBaseReader;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraftforge.common.IPlantable;

import java.util.Random;

public class BirchShrubFeature extends Feature<BaseTreeFeatureConfig> {
    public BirchShrubFeature(Codec<BaseTreeFeatureConfig> config) {
        super(config);
    }

    @Override
    public boolean func_230362_a_(ISeedReader worldIn, StructureManager manager, ChunkGenerator generator, Random rand, BlockPos position, BaseTreeFeatureConfig config) {
        if (position.getY() >= 1 && position.getY() + 1 <= worldIn.getHeight()) {
            if (!isAirOrLeaves(worldIn, position)) {
                return false;
            } else if (isValidGround(worldIn, position.down())) {
                placeLogAt(worldIn, position, rand, config);

                placeLeafAt(worldIn, position.north(), rand, config);
                placeLeafAt(worldIn, position.east(), rand, config);
                placeLeafAt(worldIn, position.south(), rand, config);
                placeLeafAt(worldIn, position.west(), rand, config);
                placeLeafAt(worldIn, position.up(), rand, config);

                placeRandomLeafAt(worldIn, position.add(-1, 0, -1), rand, config);
                placeRandomLeafAt(worldIn, position.add(-1, 0, 1), rand, config);
                placeRandomLeafAt(worldIn, position.add(1, 0, -1), rand, config);
                placeRandomLeafAt(worldIn, position.add(1, 0, 1), rand, config);

                placeRandomLeafAt(worldIn, position.add(-1, 1, 0), rand, config);
                placeRandomLeafAt(worldIn, position.add(1, 1, 0), rand, config);
                placeRandomLeafAt(worldIn, position.add(0, 1, -1), rand, config);
                placeRandomLeafAt(worldIn, position.add(0, 1, 1), rand, config);

                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private void placeLogAt(IWorldWriter worldIn, BlockPos pos, Random rand, BaseTreeFeatureConfig config) {
        this.setLogState(worldIn, pos, config.trunkProvider.getBlockState(rand, pos));
    }

    private void placeRandomLeafAt(IWorldGenerationReader world, BlockPos pos, Random rand, BaseTreeFeatureConfig config) {
        if (rand.nextInt(3) == 0) {
            placeLeafAt(world, pos, rand, config);
        }
    }

    private void placeLeafAt(IWorldGenerationReader world, BlockPos pos, Random rand, BaseTreeFeatureConfig config)
    {
        if (isAirOrLeaves(world, pos))
        {
            this.setLogState(world, pos, config.leavesProvider.getBlockState(rand, pos).with(LeavesBlock.DISTANCE, 1));
        }
    }

    protected final void setLogState(IWorldWriter worldIn, BlockPos pos, BlockState state) {
        worldIn.setBlockState(pos, state, 18);
    }

    public static boolean isAir(IWorldGenerationBaseReader worldIn, BlockPos pos)
    {
        if (!(worldIn instanceof net.minecraft.world.IBlockReader))
        {
            return worldIn.hasBlockState(pos, BlockState::isAir);
        }
        else
        {
            return worldIn.hasBlockState(pos, state -> state.isAir((net.minecraft.world.IBlockReader) worldIn, pos));
        }
    }

    public static boolean isAirOrLeaves(IWorldGenerationBaseReader worldIn, BlockPos pos)
    {
        if (worldIn instanceof net.minecraft.world.IWorldReader)
        {
            return worldIn.hasBlockState(pos, state -> state.canBeReplacedByLeaves((net.minecraft.world.IWorldReader) worldIn, pos));
        }
        return worldIn.hasBlockState(pos, (state) -> {
            return state.isAir() || state.isIn(BlockTags.LEAVES);
        });
    }

    public static void setDirtAt(IWorld worldIn, BlockPos pos)
    {
        Block block = worldIn.getBlockState(pos).getBlock();
        if (block == Blocks.GRASS_BLOCK || block == Blocks.FARMLAND)
        {
            worldIn.setBlockState(pos, Blocks.DIRT.getDefaultState(), 18);
        }
    }

    public static boolean isValidGround(IWorld world, BlockPos pos)
    {
        return world.getBlockState(pos).canSustainPlant(world, pos, Direction.UP, (IPlantable) Blocks.BIRCH_SAPLING);
    }
}
