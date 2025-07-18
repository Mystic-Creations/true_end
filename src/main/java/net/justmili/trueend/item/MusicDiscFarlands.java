
package net.justmili.trueend.item;

import net.justmili.trueend.init.Sounds;
import net.minecraftforge.registries.ForgeRegistries;

import net.minecraft.world.level.Level;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.network.chat.Component;

import java.util.List;

public class MusicDiscFarlands extends RecordItem {
    public MusicDiscFarlands() {
        super(8, () -> ForgeRegistries.SOUND_EVENTS.getValue(Sounds.MUSIC_FARLANDS.getId()), new Item.Properties().stacksTo(1).rarity(Rarity.RARE), 2540);
    }

    @Override
    public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemstack, world, list, flag);
    }
}
