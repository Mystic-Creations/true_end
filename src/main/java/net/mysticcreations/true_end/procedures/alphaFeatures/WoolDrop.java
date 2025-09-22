package net.mysticcreations.true_end.procedures.alphaFeatures;

import net.mysticcreations.true_end.TrueEnd;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static net.mysticcreations.true_end.init.Dimensions.BTD;

@Mod.EventBusSubscriber
public class WoolDrop {
    @SubscribeEvent
    public static void onEntityAttacked(LivingHurtEvent event) {
        if (TrueEnd.inModList("nostalgic_tweaks")) return;
        Entity entity = event.getEntity();
        if (!(entity instanceof Sheep sheep && (entity.level().dimension()) == BTD)) return;
        Level world = sheep.level();
        if (world.isClientSide()) return;

        CompoundTag data = sheep.getPersistentData();
        String originalSpawnType = data.getString("forge:spawn_type");

        if (sheep.isSheared()) {
            data.putString("forge:spawn_type", originalSpawnType);
            return;
        }

        DyeColor color = sheep.getColor();
        Item woolItem = switch (color) {
            case WHITE -> Items.WHITE_WOOL;
            case ORANGE -> Items.ORANGE_WOOL;
            case MAGENTA -> Items.MAGENTA_WOOL;
            case LIGHT_BLUE -> Items.LIGHT_BLUE_WOOL;
            case YELLOW -> Items.YELLOW_WOOL;
            case LIME -> Items.LIME_WOOL;
            case PINK -> Items.PINK_WOOL;
            case GRAY -> Items.GRAY_WOOL;
            case LIGHT_GRAY -> Items.LIGHT_GRAY_WOOL;
            case CYAN -> Items.CYAN_WOOL;
            case PURPLE -> Items.PURPLE_WOOL;
            case BLUE -> Items.BLUE_WOOL;
            case BROWN -> Items.BROWN_WOOL;
            case GREEN -> Items.GREEN_WOOL;
            case RED -> Items.RED_WOOL;
            case BLACK -> Items.BLACK_WOOL;
        };

        int count = world.getRandom().nextInt(3) + 1;
        ItemStack stack = new ItemStack(woolItem, count);
        ItemEntity drop = new ItemEntity(
            world,
            sheep.getX(), sheep.getY() + 0.4, sheep.getZ(),
            stack
        );
        world.addFreshEntity(drop);
        sheep.setSheared(true);
        data.putString("forge:spawn_type", originalSpawnType);
    }
}
