package com.teamaurora.reforested.common.world.gen.treedecorator;

import com.mojang.serialization.Codec;
import net.minecraft.block.BeehiveBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.tileentity.BeehiveTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.treedecorator.TreeDecorator;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class BeehiveTreeDecorator extends TreeDecorator {
    // this code is abysmal but it works so fuck you
    public static final Codec<com.teamaurora.reforested.common.world.gen.treedecorator.BeehiveTreeDecorator> field_236863_a_ = Codec.FLOAT.fieldOf("probability").xmap(com.teamaurora.reforested.common.world.gen.treedecorator.BeehiveTreeDecorator::new, (p_236865_0_) -> {
        return p_236865_0_.probability;
    }).codec();
    /** Probability to generate a beehive */
    public final float probability;

    public BeehiveTreeDecorator(float probabilityIn) {
        this.probability = probabilityIn;
    }

    protected TreeDecoratorType<?> func_230380_a_() {
        return TreeDecoratorType.BEEHIVE;
    }

    public void func_225576_a_(IWorld world, Random rand, List<BlockPos> list1, List<BlockPos> list2, Set<BlockPos> set, MutableBoundingBox box) {
        if (!(rand.nextFloat() >= this.probability)) {
            Direction direction = BeehiveBlock.func_235331_a_(rand);
            int i = !list2.isEmpty() ? Math.max(list2.get(0).getY() - 1, list1.get(0).getY()) : Math.min(list1.get(0).getY() + 1 + rand.nextInt(3), list1.get(list1.size() - 1).getY());
            List<BlockPos> list = list1.stream().filter((p_236864_1_) -> {
                return p_236864_1_.getY() == i;
            }).collect(Collectors.toList());
            if (!list.isEmpty()) {
                BlockPos blockpos = list.get(rand.nextInt(list.size()));
                BlockPos blockpos1 = blockpos.offset(direction);
                if (Feature.func_236297_b_(world, blockpos1) && Feature.func_236297_b_(world, blockpos1.offset(Direction.SOUTH))) {
                    BlockState blockstate = Blocks.BEE_NEST.getDefaultState().with(BeehiveBlock.FACING, Direction.SOUTH);
                    this.func_227423_a_(world, blockpos1, blockstate, set, box);
                    TileEntity tileentity = world.getTileEntity(blockpos1);
                    if (tileentity instanceof BeehiveTileEntity) {
                        BeehiveTileEntity beehivetileentity = (BeehiveTileEntity)tileentity;
                        int j = 2 + rand.nextInt(2);

                        for(int k = 0; k < j; ++k) {
                            BeeEntity beeentity = new BeeEntity(EntityType.BEE, world.getWorld());
                            beehivetileentity.tryEnterHive(beeentity, false, rand.nextInt(599));
                        }
                    }

                }
            }
        }
    }
}
