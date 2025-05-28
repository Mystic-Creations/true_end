package net.justmili.trueend.procedures.events;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import static net.justmili.trueend.TrueEnd.MODID;
import static net.justmili.trueend.regs.DimKeyRegistry.BTD;

@Mod.EventBusSubscriber
public class AlphaFoodSystemBlockRC {
    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickBlock event) {
        if (event.getHand() != InteractionHand.MAIN_HAND) return;

        Player player = event.getEntity();
        ItemStack stack = event.getItemStack();
        int consumed = execute(event, player, stack);

        //Debug stuff
        String action = null;
        if (consumed == 0) {
            action = "Blocked Action";
        } else if (consumed == 1) {
            action = "Allowed Action";
        } else if (consumed == 2) {
            action = "Skipped Blocking Action";
        }
        System.out.println("[DEBUG] " + MODID + ": Consumed = " + consumed + ", " + action);
    }

    private static int execute(@Nullable PlayerInteractEvent.RightClickBlock event, @Nullable Player player, ItemStack stack) {
        if (player == null) return 0;
        if (player.level().dimension() != BTD) return 0;

        int consumed = 0;
        boolean other = false;

        if (stack.is(ItemTags.create(ResourceLocation.parse("true_end:btd_uneatables")))) {
            consumed = 0;
        } else {
            consumed = 2;
            other = true;
        }

        if (other) {
            System.out.println("[DEBUG] " + MODID + ": Not a food!");
        }

        if (consumed == 0) {
            assert event != null;
            if (event.isCancelable()) {
                event.setCanceled(true);
            }
        }
        return consumed;
    }

}