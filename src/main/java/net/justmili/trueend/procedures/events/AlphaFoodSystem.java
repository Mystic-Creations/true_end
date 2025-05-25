package net.justmili.trueend.procedures.events;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

import java.util.Objects;

import static net.justmili.trueend.regs.DimKeyRegistry.BTD;

@Mod.EventBusSubscriber
public class AlphaFoodSystem {
    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        if (event.getHand() != InteractionHand.MAIN_HAND) return;

        Player player = event.getEntity();
        ItemStack stack = event.getItemStack();

        // Cancel if already full health
        if (player.getHealth() >= player.getMaxHealth()) {
            if (event.isCancelable()) {
                event.setCanceled(true);
            }
            return;
        }

        // Execute and cancel vanilla handling if consumed
        if (execute(event, player, stack)) {
            if (event.isCancelable()) {
                event.setCanceled(true);
            }
        }
    }

    private static boolean execute(@Nullable PlayerInteractEvent.RightClickItem event, @Nullable Player player, ItemStack stack) {
        if (player == null) return false;
        if (player.level().dimension() != BTD) return false;

        float newHealth = player.getHealth();
        boolean consumed = false;

        // Check food and apply health
        if (stack.getItem() == Items.PORKCHOP) {
            newHealth += 1.5F; consumed = true;
        } else if (stack.getItem() == Items.COOKED_PORKCHOP) {
            newHealth += 4.0F; consumed = true;
        } else if (stack.getItem() == Items.BEEF) {
            newHealth += 3.0F; consumed = true;
        } else if (stack.getItem() == Items.COOKED_BEEF) {
            newHealth += 8.0F; consumed = true;
        } else if (stack.getItem() == Items.MUTTON) {
            newHealth += 2.0F; consumed = true;
        } else if (stack.getItem() == Items.COOKED_MUTTON) {
            newHealth += 6.0F; consumed = true;
        } else if (stack.getItem() == Items.CHICKEN) {
            newHealth += 2.0F; consumed = true;
        } else if (stack.getItem() == Items.COOKED_CHICKEN) {
            newHealth += 6.0F; consumed = true;
        } else if (stack.getItem() == Items.BREAD) {
            newHealth += 2.5F; consumed = true;
        }

        if (consumed) {
            stack.shrink(1);
            player.getInventory().setChanged();
            float maxHealth = player.getMaxHealth();
            player.setHealth(Math.min(newHealth, maxHealth));
            playEatSound(player.level(), player.getX(), player.getY(), player.getZ());
        }

        return consumed;
    }

    private static void playEatSound(LevelAccessor world, double x, double y, double z) {
        double pitch = 0.8 + Math.random() * 0.4;

        if (world instanceof Level _level) {
            if (!_level.isClientSide()) {
                _level.playSound(null, BlockPos.containing(x, y, z),
                        Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue(ResourceLocation.parse("entity.generic.eat"))),
                        SoundSource.NEUTRAL, 1.0f, (float) pitch);
            } else {
                _level.playLocalSound(x, y, z,
                        Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue(ResourceLocation.parse("entity.generic.eat"))),
                        SoundSource.NEUTRAL, 1.0f, (float) pitch, false);
            }
        }
    }
}

