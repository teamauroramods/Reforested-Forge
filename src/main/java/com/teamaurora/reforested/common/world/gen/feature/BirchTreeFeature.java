package com.teamaurora.reforested.common.world.gen.feature;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
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
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.math.vector.Vector3i;
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

import java.util.*;

public class BirchTreeFeature extends Feature<BirchFeatureConfig> {
    public BirchTreeFeature(Codec<BirchFeatureConfig> config) {
        super(config);
    }

    @Override
    public boolean func_230362_a_(ISeedReader worldIn, StructureManager manager, ChunkGenerator generator, Random rand, BlockPos position, BirchFeatureConfig config) {
        int heightOffset = rand.nextInt(3);
        int i = 6 + heightOffset;

        boolean flag = true;
        if (position.getY() >= 1 && position.getY() + i + 1 <= worldIn.getHeight()) {
            for (BlockPos pos : BlockPos.getAllInBoxMutable(position.add(-1, 0, -1), position.add(1, i, 1))) {
            //for (int j = 0; j < i; j++) {
                if (!isAirOrLeaves(worldIn, pos)) {
                    flag = false;
                }
            }
            if (!flag) {
                return false;
            } else if (isValidGround(worldIn, position.down())) {
                List<BlockPos> logPos = new ArrayList<>();
                List<BlockPos> leafPos = new ArrayList<>();

                setDirtAt(worldIn, position.down());

                for (int j = 0; j < i; j++) {
                    placeLogAt(worldIn, position.up(j), rand, config, logPos);
                }
                placeLeafAt(worldIn, position.up(i+1), rand, config, leafPos);

                placeLeafAt(worldIn, position.add(0, i, 0), rand, config, leafPos);
                placeLeafAt(worldIn, position.add(-1, i, 0), rand, config, leafPos);
                placeLeafAt(worldIn, position.add(1, i, 0), rand, config, leafPos);
                placeLeafAt(worldIn, position.add(0, i, -1), rand, config, leafPos);
                placeLeafAt(worldIn, position.add(0, i, 1), rand, config, leafPos);


                placeLeafAt(worldIn, position.add(0, i-1, 0), rand, config, leafPos);
                placeLeafAt(worldIn, position.add(-1, i-1, 0), rand, config, leafPos);
                placeLeafAt(worldIn, position.add(1, i-1, 0), rand, config, leafPos);
                placeLeafAt(worldIn, position.add(0, i-1, -1), rand, config, leafPos);
                placeLeafAt(worldIn, position.add(0, i-1, 1), rand, config, leafPos);
                placeRandomLeafAt(worldIn, position.add(-1, i-1, -1), rand, config, leafPos);
                placeRandomLeafAt(worldIn, position.add(-1, i-1, 1), rand, config, leafPos);
                placeRandomLeafAt(worldIn, position.add(1, i-1, -1), rand, config, leafPos);
                placeRandomLeafAt(worldIn, position.add(1, i-1, 1), rand, config, leafPos);

                for (int x = -1; x <= 1; x++) {
                    for (int z = -1; z <= 1; z++) {
                        placeLeafAt(worldIn, position.add(x, i-2, z), rand, config, leafPos);
                    }
                }

                placeLeafAt(worldIn, position.add(-1, i-3, 0), rand, config, leafPos);
                placeLeafAt(worldIn, position.add(1, i-3, 0), rand, config, leafPos);
                placeLeafAt(worldIn, position.add(0, i-3, -1), rand, config, leafPos);
                placeLeafAt(worldIn, position.add(0, i-3, 1), rand, config, leafPos);


                float chancef = config.beehiveChance == 0 ? 1.0F : 1.0F / config.beehiveChance;
                int chance = (int)Math.ceil(chancef);
                /*if (rand.nextInt(chance) == 0 && config.beehiveChance != 0) {
                    // place at y=i-4
                    Direction direction = BeehiveBlock.func_235331_a_(rand);
                    BlockState blockstate = Blocks.BEE_NEST.getDefaultState().with(BeehiveBlock.FACING, direction);
                    setLogState(worldIn, position.up(i-4).offset(direction), blockstate);
                    TileEntity tileentity = worldIn.getTileEntity(position.up(i-4).offset(direction));
                    if (tileentity instanceof BeehiveTileEntity) {
                        BeehiveTileEntity beehivetileentity = (BeehiveTileEntity)tileentity;
                        int j = 2 + rand.nextInt(2);

                        for(int k = 0; k < j; ++k) {
                            BeeEntity beeentity = new BeeEntity(EntityType.BEE, worldIn.getWorld());
                            beehivetileentity.tryEnterHive(beeentity, false, rand.nextInt(599));
                        }
                    }
                }*/

                Set<BlockPos> decSet = Sets.newHashSet();
                MutableBoundingBox mutableBoundingBox = MutableBoundingBox.getNewBoundingBox();
                if (!config.decorators.isEmpty()) {
                    logPos.sort(Comparator.comparingInt(Vector3i::getY));
                    leafPos.sort(Comparator.comparingInt(Vector3i::getY));
                    config.decorators.forEach((decorator)->{
                        decorator.func_225576_a_(worldIn, rand, logPos, leafPos, decSet, mutableBoundingBox);
                    });
                }

                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private void placeLogAt(IWorldWriter worldIn, BlockPos pos, Random rand, BirchFeatureConfig config, List<BlockPos> log) {
        this.setLogState(worldIn, pos, config.trunkProvider.getBlockState(rand, pos));
        log.add(pos);
    }

    private void placeRandomLeafAt(IWorldGenerationReader world, BlockPos pos, Random rand, BirchFeatureConfig config, List<BlockPos> leaf) {
        if (rand.nextBoolean()) {
            placeLeafAt(world, pos, rand, config, leaf);
        }
    }

    private void placeLeafAt(IWorldGenerationReader world, BlockPos pos, Random rand, BirchFeatureConfig config, List<BlockPos> leaf)
    {
        if (isAirOrLeaves(world, pos))
        {
            this.setLogState(world, pos, config.leavesProvider.getBlockState(rand, pos).with(LeavesBlock.DISTANCE, 1));
            leaf.add(pos);
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