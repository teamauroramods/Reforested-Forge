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

public class BirchTreeFeature extends Feature<BaseTreeFeatureConfig> {
    public BirchTreeFeature(Codec<BaseTreeFeatureConfig> config) {
        super(config);
    }

    @Override
    public boolean func_230362_a_(ISeedReader worldIn, StructureManager manager, ChunkGenerator generator, Random rand, BlockPos position, BaseTreeFeatureConfig config) {
        int heightOffset = rand.nextInt(3);
        int i = 6 + heightOffset;

        boolean flag = true;
        if (position.getY() >= 1 && position.getY() + i + 1 <= worldIn.getHeight()) {
            for (int j = 0; j < i; j++) {
                if (!isAirOrLeaves(worldIn, position.up(j))) {
                    flag = false;
                }
            }
            if (!flag) {
                return false;
            } else if (isValidGround(worldIn, position.down())) {
                setDirtAt(worldIn, position.down());

                for (int j = 0; j < i; j++) {
                    placeLogAt(worldIn, position.up(j), rand, config);
                }
                placeLeafAt(worldIn, position.up(i+1), rand, config);

                placeLeafAt(worldIn, position.add(0, i, 0), rand, config);
                placeLeafAt(worldIn, position.add(-1, i, 0), rand, config);
                placeLeafAt(worldIn, position.add(1, i, 0), rand, config);
                placeLeafAt(worldIn, position.add(0, i, -1), rand, config);
                placeLeafAt(worldIn, position.add(0, i, 1), rand, config);


                placeLeafAt(worldIn, position.add(0, i-1, 0), rand, config);
                placeLeafAt(worldIn, position.add(-1, i-1, 0), rand, config);
                placeLeafAt(worldIn, position.add(1, i-1, 0), rand, config);
                placeLeafAt(worldIn, position.add(0, i-1, -1), rand, config);
                placeLeafAt(worldIn, position.add(0, i-1, 1), rand, config);
                placeRandomLeafAt(worldIn, position.add(-1, i-1, -1), rand, config);
                placeRandomLeafAt(worldIn, position.add(-1, i-1, 1), rand, config);
                placeRandomLeafAt(worldIn, position.add(1, i-1, -1), rand, config);
                placeRandomLeafAt(worldIn, position.add(1, i-1, 1), rand, config);

                for (int x = -1; x <= 1; x++) {
                    for (int z = -1; z <= 1; z++) {
                        placeLeafAt(worldIn, position.add(x, i-2, z), rand, config);
                    }
                }

                placeLeafAt(worldIn, position.add(-1, i-3, 0), rand, config);
                placeLeafAt(worldIn, position.add(1, i-3, 0), rand, config);
                placeLeafAt(worldIn, position.add(0, i-3, -1), rand, config);
                placeLeafAt(worldIn, position.add(0, i-3, 1), rand, config);

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
        if (rand.nextBoolean()) {
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