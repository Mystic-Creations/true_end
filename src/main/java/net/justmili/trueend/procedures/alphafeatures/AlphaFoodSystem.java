package net.justmili.trueend.procedures.alphafeatures;

import java.util.Objects;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import static net.justmili.trueend.init.Dimensions.BTD;

@Mod.EventBusSubscriber
public class AlphaFoodSystem {
    @SubscribeEvent
    public static int onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        if (event.getHand() != InteractionHand.MAIN_HAND) return 0;

        Player player = event.getEntity();
        ItemStack stack = event.getItemStack();
        if (player == null) return 0;
        if (player.level().dimension() != BTD) return 0;
        float newHealth = player.getHealth();
        int consumed;

        if (stack.getItem() == Items.PORKCHOP) {
            newHealth += 1.5F;
            consumed = 1;
        } else if (stack.getItem() == Items.COOKED_PORKCHOP) {
            newHealth += 4.0F;
            consumed = 1;
        } else if (stack.getItem() == Items.BEEF) {
            newHealth += 3.0F;
            consumed = 1;
        } else if (stack.getItem() == Items.COOKED_BEEF) {
            newHealth += 8.0F;
            consumed = 1;
        } else if (stack.getItem() == Items.MUTTON) {
            newHealth += 2.0F;
            consumed = 1;
        } else if (stack.getItem() == Items.COOKED_MUTTON) {
            newHealth += 6.0F;
            consumed = 1;
        } else if (stack.getItem() == Items.CHICKEN) {
            newHealth += 2.0F;
            consumed = 1;
        } else if (stack.getItem() == Items.COOKED_CHICKEN) {
            newHealth += 6.0F;
            consumed = 1;
        } else if (stack.getItem() == Items.BREAD) {
            newHealth += 2.5F;
            consumed = 1;
        } else if (stack.getItem() == Items.APPLE) {
            newHealth += 2.0F;
            consumed = 1;
        } else if (stack.getItem() == Items.GOLDEN_APPLE) {
            newHealth += 10.0F;
            consumed = 1;
        } else if (stack.is(ItemTags.create(ResourceLocation.parse("true_end:btd_uneatables")))) {
            consumed = 0;
        } else {
            consumed = 2;
        }

        if (consumed == 1) {
            if (!healthCheck(event)) {
                stack.shrink(1);
                player.getInventory().setChanged();
                float maxHealth = player.getMaxHealth();
                player.setHealth(Math.min(newHealth, maxHealth));
                playEatSound(player.level(), player.getX(), player.getY(), player.getZ());

                event.setCanceled(true);
            } else {
                consumed = 0;
            }
        }

        if (consumed == 0) {
            if (event.isCancelable()) {
                event.setCanceled(true);
            }
        }
        return consumed;
    }
    @SubscribeEvent
    public static int onRightClickItem(PlayerInteractEvent.RightClickBlock event) {
        if (event.getHand() != InteractionHand.MAIN_HAND) return 0;

        Player player = event.getEntity();
        ItemStack stack = event.getItemStack();

        if (player == null) return 0;
        if (player.level().dimension() != BTD) return 0;

        int consumed;
        if (stack.is(ItemTags.create(ResourceLocation.parse("true_end:btd_uneatables")))) {
            consumed = 0;
        } else {
            consumed = 2;
        }

        if (consumed == 0) {
            if (event.isCancelable()) {
                event.setCanceled(true);
            }
        }
        return consumed;
    }

    private static void playEatSound(LevelAccessor world, double x, double y, double z) {
        double pitch = 0.8 + Math.random() * 0.4;

        if (world instanceof Level _level) {
            if (!_level.isClientSide()) {
                _level.playSound(null, BlockPos.containing(x, y, z),
                        Objects.requireNonNull(
                                ForgeRegistries.SOUND_EVENTS.getValue(ResourceLocation.parse("entity.generic.eat"))),
                        SoundSource.NEUTRAL, 1.0f, (float) pitch);
            } else {
                _level.playLocalSound(x, y, z,
                        Objects.requireNonNull(
                                ForgeRegistries.SOUND_EVENTS.getValue(ResourceLocation.parse("entity.generic.eat"))),
                        SoundSource.NEUTRAL, 1.0f, (float) pitch, false);
            }
        }
    }

    private static boolean healthCheck(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getEntity();
        if (player.getHealth() >= player.getMaxHealth()) {
            if (event.isCancelable()) {
                event.setCanceled(true);
            }
            return true;
        } else {
            return false;
        }
    }
}