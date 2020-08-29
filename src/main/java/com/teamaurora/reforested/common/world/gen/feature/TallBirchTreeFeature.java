package com.teamaurora.reforested.common.world.gen.feature;

import com.mojang.serialization.Codec;
import com.teamaurora.reforested.common.world.gen.treedecorator.BeehiveTreeDecorator;
import net.minecraft.block.*;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.tags.BlockTags;
import net.minecraft.tileentity.BeehiveTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldWriter;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.IWorldGenerationBaseReader;
import net.minecraft.world.gen.IWorldGenerationReader;
import com.teamaurora.reforested.common.world.gen.feature.config.BirchFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.treedecorator.TreeDecorator;
import net.minecraftforge.common.IPlantable;

import java.util.Random;

public class TallBirchTreeFeature extends Feature<BirchFeatureConfig> {
    public TallBirchTreeFeature(Codec<BirchFeatureConfig> config) {
        super(config);
    }

    @Override
    public boolean func_230362_a_(ISeedReader worldIn, StructureManager manager, ChunkGenerator generator, Random rand, BlockPos position, BirchFeatureConfig config) {
        int heightOffset = rand.nextInt(4);
        int i = 12 + heightOffset;

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
                placeSmallRingAt(worldIn, position.up(i+1), rand, config);
                placeSmallDiscAt(worldIn, position.up(i), rand, config);

                placeLargeDiscAt(worldIn, position.up(i-1), rand, config);
                placeSolidDiscAt(worldIn, position.up(i-2), rand, config);
                placeLargeDiscAt(worldIn, position.up(i-3), rand, config);
                placeSolidDiscAt(worldIn, position.up(i-4), rand, config);
                placeLargeDiscAt(worldIn, position.up(i-5), rand, config);

                placeSmallRingAt(worldIn, position.up(i-6), rand, config);

                float chancef = config.beehiveChance == 0 ? 1.0F : 1.0F / config.beehiveChance;
                int chance = (int)Math.ceil(chancef);
                if (rand.nextInt(chance) == 0 && config.beehiveChance != 0) {
                    // place at y=i-7
                    Direction direction = BeehiveBlock.func_235331_a_(rand);
                    BlockState blockstate = Blocks.BEE_NEST.getDefaultState().with(BeehiveBlock.FACING, direction);
                    setLogState(worldIn, position.up(i-7).offset(direction), blockstate);
                    TileEntity tileentity = worldIn.getTileEntity(position.up(i-7).offset(direction));
                    if (tileentity instanceof BeehiveTileEntity) {
                        BeehiveTileEntity beehivetileentity = (BeehiveTileEntity)tileentity;
                        int j = 2 + rand.nextInt(2);

                        for(int k = 0; k < j; ++k) {
                            BeeEntity beeentity = new BeeEntity(EntityType.BEE, worldIn.getWorld());
                            beehivetileentity.tryEnterHive(beeentity, false, rand.nextInt(599));
                        }
                    }
                }

                int ring1Height = -1;
                int ring2Height = -1;

                if (heightOffset == 0) {
                    int ringState = rand.nextInt(2);
                    if (ringState == 1) {
                        ring1Height = 4;
                    }
                } else if (heightOffset == 1) {
                    int ringState = rand.nextInt(3);
                    if (ringState == 1) {
                        ring1Height = 4;
                    } else if (ringState == 2) {
                        ring1Height = 5;
                    }
                } else if (heightOffset == 2) {
                    int ringState = rand.nextInt(5);
                    if (ringState == 1) {
                        ring1Height = 4;
                    } else if (ringState == 2) {
                        ring1Height = 5;
                    } else if (ringState == 3) {
                        ring1Height = 6;
                    } else if (ringState == 4) {
                        ring1Height = 4;
                        ring2Height = 6;
                    }
                } else {
                    int ringState = rand.nextInt(8);
                    if (ringState == 1) {
                        ring1Height = 4;
                    } else if (ringState == 2) {
                        ring1Height = 5;
                    } else if (ringState == 3) {
                        ring1Height = 6;
                    } else if (ringState == 4) {
                        ring1Height = 7;
                    } else if (ringState == 5) {
                        ring1Height = 4;
                        ring2Height = 6;
                    } else if (ringState == 6) {
                        ring1Height = 5;
                        ring2Height = 7;
                    } else if (ringState == 7) {
                        ring1Height = 4;
                        ring2Height = 7;
                    }
                }

                if (ring1Height != -1) {
                    placeSmallDiscAt(worldIn, position.up(ring1Height), rand, config);
                }
                if (ring2Height != -1) {
                    placeSmallDiscAt(worldIn, position.up(ring2Height), rand, config);
                }

                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private void placeSmallRingAt(IWorldGenerationReader world, BlockPos pos, Random rand, BirchFeatureConfig config) {
        placeLeafAt(world, pos, rand, config);
        placeLeafAt(world, pos.north(), rand, config);
        placeLeafAt(world, pos.east(), rand, config);
        placeLeafAt(world, pos.south(), rand, config);
        placeLeafAt(world, pos.west(), rand, config);
    }

    private void placeSmallDiscAt(IWorldGenerationReader world, BlockPos pos, Random rand, BirchFeatureConfig config) {
        placeSmallRingAt(world, pos, rand, config);
        placeRandomLeafAt(world, pos.add(-1, 0, -1), rand, config);
        placeRandomLeafAt(world, pos.add(-1, 0, 1), rand, config);
        placeRandomLeafAt(world, pos.add(1, 0, -1), rand, config);
        placeRandomLeafAt(world, pos.add(1, 0, 1), rand, config);
    }

    private void placeLargeDiscAt(IWorldGenerationReader world, BlockPos pos, Random rand, BirchFeatureConfig config) {
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                placeLeafAt(world, pos.add(x, 0, z), rand, config);
            }
        }
        placeLeafAt(world, pos.add(-2, 0, 0), rand, config);
        placeLeafAt(world, pos.add(2, 0, 0), rand, config);
        placeLeafAt(world, pos.add(0, 0, -2), rand, config);
        placeLeafAt(world, pos.add(0, 0, 2), rand, config);

        placeRandomLeafAt(world, pos.add(-2, 0, -1), rand, config);
        placeRandomLeafAt(world, pos.add(-2, 0, 1), rand, config);
        placeRandomLeafAt(world, pos.add(2, 0, -1), rand, config);
        placeRandomLeafAt(world, pos.add(2, 0, 1), rand, config);
        placeRandomLeafAt(world, pos.add(-1, 0, -2), rand, config);
        placeRandomLeafAt(world, pos.add(1, 0, -2), rand, config);
        placeRandomLeafAt(world, pos.add(-1, 0, 2), rand, config);
        placeRandomLeafAt(world, pos.add(1, 0, 2), rand, config);
    }

    private void placeSolidDiscAt(IWorldGenerationReader world, BlockPos pos, Random rand, BirchFeatureConfig config) {
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                if (Math.abs(x) != 2 || Math.abs(z) != 2) {
                    placeLeafAt(world, pos.add(x, 0, z), rand, config);
                }
            }
        }
    }

    private void placeLogAt(IWorldWriter worldIn, BlockPos pos, Random rand, BirchFeatureConfig config) {
        this.setLogState(worldIn, pos, config.trunkProvider.getBlockState(rand, pos));
    }

    private void placeRandomLeafAt(IWorldGenerationReader world, BlockPos pos, Random rand, BirchFeatureConfig config) {
        if (rand.nextBoolean()) {
            placeLeafAt(world, pos, rand, config);
        }
    }

    private void placeLeafAt(IWorldGenerationReader world, BlockPos pos, Random rand, BirchFeatureConfig config)
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
