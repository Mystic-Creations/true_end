package net.justmili.trueend.client.item;

import net.justmili.trueend.TrueEnd;
import net.justmili.trueend.init.Items;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(
        modid = TrueEnd.MODID,
        value = Dist.CLIENT,
        bus   = Mod.EventBusSubscriber.Bus.MOD
)
public class DreamersCompassAngle {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemProperties.register(
                    Items.DREAMERS_COMPASS.get(), ResourceLocation.parse("angle"),
                    (stack, world, entity, seed) -> {
                        if (world == null || entity == null) {
                            return 0F;
                        }
                        CompoundTag tag = stack.getOrCreateTag();
                        BlockPos targetPos = new BlockPos(
                                tag.getInt("LodestonePosX"),
                                tag.getInt("LodestonePosY"),
                                tag.getInt("LodestonePosZ")
                                );
                        BlockPos playerPos = entity.blockPosition();
                        float playerYaw = entity.getYRot(); // in degrees

// Delta vector from player to target
                        double dx = targetPos.getX() - playerPos.getX();
                        double dz = targetPos.getZ() - playerPos.getZ();

// Angle to target in degrees (0 = east, 90 = south)
                        double targetAngle = Math.toDegrees(Math.atan2(dz, dx));
                        targetAngle = (targetAngle - 90.0) % 360.0; // Fix 90° counter-clockwise offset

// Normalize both angles
                        targetAngle = (targetAngle + 360.0) % 360.0;
                        playerYaw = (playerYaw + 360.0f) % 360.0f;

// Relative angle between where player is looking and where target is
                        double relative = (targetAngle - playerYaw + 360.0) % 360.0;

// Now convert to 0.0 - 1.0 float for the predicate
                        float angleValue = (float)(relative / 360.0);

                        return angleValue;


                        //try using .getLodestonePosition()
                    }
            );
        });
    }
}