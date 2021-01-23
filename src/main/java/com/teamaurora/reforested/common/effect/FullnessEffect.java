package com.teamaurora.reforested.common.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

public class FullnessEffect extends Effect {
    protected FullnessEffect(EffectType typeIn, int liquidColorIn) {
        super(typeIn, liquidColorIn);
    }
    public FullnessEffect() {
        this(EffectType.BENEFICIAL, 0xEFB742);
    }

    @Override
    public void performEffect(LivingEntity entityLivingBaseIn, int amplifier) {
        if (entityLivingBaseIn instanceof PlayerEntity) {
            PlayerEntity playerIn = (PlayerEntity) entityLivingBaseIn;
            float exhaustion_removed = -(0.005F * (float)(amplifier + 1));
            playerIn.getFoodStats().addExhaustion(exhaustion_removed);
            playerIn.getFoodStats().addStats(1, 0F);
        }
    }
}
