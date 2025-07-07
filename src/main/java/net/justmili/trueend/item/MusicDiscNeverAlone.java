
package net.justmili.trueend.item;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class MusicDiscNeverAlone extends RecordItem {
    public MusicDiscNeverAlone() {
        super(8, () -> ForgeRegistries.SOUND_EVENTS.getValue(ResourceLocation.parse("true_end:music_disc.never_alone")), new Properties().stacksTo(1).rarity(Rarity.RARE), 5180);
    }

    @Override
    public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemstack, world, list, flag);
    }
}
