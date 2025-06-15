package net.justmili.trueend.item;

import net.minecraft.world.item.CompassItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class DreamersCompass extends CompassItem {
    public DreamersCompass() {
        super(new Item.Properties().stacksTo(1).rarity(Rarity.RARE));
    }
    //Add actual functionality to it
    //Also change the texture
}