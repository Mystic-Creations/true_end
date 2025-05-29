package net.justmili.trueend.procedures.events;

import java.util.Objects;

import javax.annotation.Nullable;

import static net.justmili.trueend.TrueEnd.MODID;
import static net.justmili.trueend.regs.DimKeyRegistry.BTD;

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

@Mod.EventBusSubscriber
public class AlphaFoodSystem {
    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
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

    private static int execute(@Nullable PlayerInteractEvent.RightClickItem event, @Nullable Player player, ItemStack stack) {
        if (player == null) return 0;
        if (player.level().dimension() != BTD) return 0;

        float newHealth = player.getHealth();
        int consumed = 0;
        boolean other = false;

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
            other = true;
        }

        if (other) {
            System.out.println("[DEBUG] " + MODID + ": Not a food!");
        }

        if (consumed == 1) {
            assert event != null;
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
            assert event != null;
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
                        Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue(ResourceLocation.parse("entity.generic.eat"))),
                        SoundSource.NEUTRAL, 1.0f, (float) pitch);
            } else {
                _level.playLocalSound(x, y, z,
                        Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue(ResourceLocation.parse("entity.generic.eat"))),
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
            System.out.println("[DEBUG] true_end: Player at full health.");
            return true;
        } else {
            System.out.println("[DEBUG] " + MODID + ": Player not at full health.");
            return false;
        }
    }

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickBlock event) {
        if (event.getHand() != InteractionHand.MAIN_HAND) return;

        Player player = event.getEntity();
        ItemStack stack = event.getItemStack();
        int consumed = execute2(event, player, stack);

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

    private static int execute2(@Nullable PlayerInteractEvent.RightClickBlock event, @Nullable Player player, ItemStack stack) {
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