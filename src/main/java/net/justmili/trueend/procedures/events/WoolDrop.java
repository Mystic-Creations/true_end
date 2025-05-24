package net.justmili.trueend.procedures.events;

import net.minecraft.world.item.Item;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.item.ItemEntity;

import javax.annotation.Nullable;

import static net.justmili.trueend.regs.DimKeyRegistry.BTD;

@Mod.EventBusSubscriber
public class WoolDrop {
    @SubscribeEvent
    public static void onEntityAttacked(LivingHurtEvent event) {
        if (event == null || event.getEntity() == null) return;
        LevelAccessor worldAcc = event.getEntity().level();
        Entity entity = event.getEntity();
        execute(event, worldAcc, entity);
    }

    private static void execute(@Nullable LivingHurtEvent event, LevelAccessor worldAcc, Entity entity) {
        if (!(entity instanceof Sheep sheep && (entity.level().dimension()) == BTD)) return;
        Level world = sheep.level();
        if (world.isClientSide()) return;

        if (sheep.isSheared()) return;
        DyeColor color = sheep.getColor();
        Item woolItem = switch (color) {
            case WHITE      -> Items.WHITE_WOOL;
            case ORANGE     -> Items.ORANGE_WOOL;
            case MAGENTA    -> Items.MAGENTA_WOOL;
            case LIGHT_BLUE -> Items.LIGHT_BLUE_WOOL;
            case YELLOW     -> Items.YELLOW_WOOL;
            case LIME       -> Items.LIME_WOOL;
            case PINK       -> Items.PINK_WOOL;
            case GRAY       -> Items.GRAY_WOOL;
            case LIGHT_GRAY -> Items.LIGHT_GRAY_WOOL;
            case CYAN       -> Items.CYAN_WOOL;
            case PURPLE     -> Items.PURPLE_WOOL;
            case BLUE       -> Items.BLUE_WOOL;
            case BROWN      -> Items.BROWN_WOOL;
            case GREEN      -> Items.GREEN_WOOL;
            case RED        -> Items.RED_WOOL;
            case BLACK      -> Items.BLACK_WOOL;
        };

        int count = world.getRandom().nextInt(3) + 1;

        ItemStack stack = new ItemStack(woolItem, count);
        ItemEntity drop = new ItemEntity(
                world,
                sheep.getX(), sheep.getY() + 0.5, sheep.getZ(),
                stack
        );
        world.addFreshEntity(drop);
        sheep.setSheared(true);
    }
}
