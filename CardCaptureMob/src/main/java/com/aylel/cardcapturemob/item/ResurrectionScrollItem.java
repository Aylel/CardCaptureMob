package com.aylel.cardcapturemob.item;

import com.aylel.cardcapturemob.client.HealItemTooltipHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.network.chat.Component;
import java.util.List;

public class ResurrectionScrollItem extends Item {
    public enum Type { BASIC, ADVANCED, SUPREME; }
    private final Type type;
    public ResurrectionScrollItem(Properties properties, Type type) {
        super(properties);
        this.type = type;
    }
    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        switch (type) {
            case BASIC -> HealItemTooltipHelper.appendResurrectionBasicTooltip(stack, tooltip, flag);
            case ADVANCED -> HealItemTooltipHelper.appendResurrectionAdvancedTooltip(stack, tooltip, flag);
            case SUPREME -> HealItemTooltipHelper.appendResurrectionSupremeTooltip(stack, tooltip, flag);
        }
    }
}

