package com.teamaurora.reforested.common.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldWriter;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.IWorldGenerationBaseReader;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.StructureManager;

import java.util.Random;

public class SmallMossyBoulderFeature extends Feature<NoFeatureConfig> {
    public SmallMossyBoulderFeature(Codec<NoFeatureConfig> config) {
        super(config);
    }

    @Override
    public boolean func_230362_a_(ISeedReader worldIn, StructureManager manager, ChunkGenerator generator, Random rand, BlockPos position, NoFeatureConfig config) {
        if (!isAirOrLeaves(worldIn, position)) return false;

        if (position.getY() >= 2 && position.getY() <= worldIn.getHeight()) {
            setState(worldIn, position, Blocks.MOSSY_COBBLESTONE.getDefaultState());
            if (isAirOrLeaves(worldIn, position.down())) {
                setState(worldIn, position.down(), Blocks.MOSSY_COBBLESTONE.getDefaultState());
            }

            if (rand.nextBoolean() && isAirOrLeaves(worldIn, position.add(1,0,0))) {
                setState(worldIn, position.add(1,0,0), Blocks.MOSSY_COBBLESTONE.getDefaultState());
                if (isAirOrLeaves(worldIn, position.add(1, -1, 0))) {
                    setState(worldIn, position.add(1,-1,0), Blocks.MOSSY_COBBLESTONE.getDefaultState());
                }
            }
            if (rand.nextBoolean() && isAirOrLeaves(worldIn, position.add(0,0,1))) {
                setState(worldIn, position.add(0,0,1), Blocks.MOSSY_COBBLESTONE.getDefaultState());
                if (isAirOrLeaves(worldIn, position.add(0, -1, 1))) {
                    setState(worldIn, position.add(0,-1,1), Blocks.MOSSY_COBBLESTONE.getDefaultState());
                }
            }
            if (rand.nextBoolean() && isAirOrLeaves(worldIn, position.add(1,0,1))) {
                setState(worldIn, position.add(1,0,1), Blocks.MOSSY_COBBLESTONE.getDefaultState());
                if (isAirOrLeaves(worldIn, position.add(1, -1, 1))) {
                    setState(worldIn, position.add(1,-1,1), Blocks.MOSSY_COBBLESTONE.getDefaultState());
                }
            }

            return true;
        } else {
            return false;
        }
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
            return state.isAir() || state.isIn(BlockTags.LEAVES) || state.getBlock() == Blocks.WATER || state.isIn(BlockTags.SMALL_FLOWERS) || state.isIn(BlockTags.TALL_FLOWERS) || state.getBlock() == Blocks.TALL_GRASS || state.getBlock() == Blocks.GRASS || state.getBlock() == Blocks.FERN || state.getBlock() == Blocks.LARGE_FERN;
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

    protected final void setState(IWorldWriter worldIn, BlockPos pos, BlockState state) {
        worldIn.setBlockState(pos, state, 18);
    }
}
