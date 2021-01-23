package com.teamaurora.reforested.core.mixin;

import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.IngameGui;
import org.spongepowered.asm.mixin.Mixin;
//L91 856 ALOAD 0
@Mixin(IngameGui.class)
public class IngameGuiMixin extends AbstractGui {
    //This isn't going anywhere right now. Stand by.
}
