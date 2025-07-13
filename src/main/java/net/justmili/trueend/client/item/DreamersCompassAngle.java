package net.justmili.trueend.client.item;

import net.justmili.trueend.TrueEnd;
import net.justmili.trueend.init.Items;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = TrueEnd.MODID, value = Dist.CLIENT, bus   = Mod.EventBusSubscriber.Bus.MOD)
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
                        float playerYaw = entity.getYRot();
                        
                        double dx = targetPos.getX() - playerPos.getX();
                        double dz = targetPos.getZ() - playerPos.getZ();
                        
                        double targetAngle = Math.toDegrees(Math.atan2(dz, dx));
                        targetAngle = (targetAngle - 90.0) % 360.0;
                        
                        targetAngle = (targetAngle + 360.0) % 360.0;
                        playerYaw = (playerYaw + 360.0f) % 360.0f;
                        
                        double relative = (targetAngle - playerYaw + 360.0) % 360.0;

                        return (float)(relative / 360.0);
                    }
            );
        });
    }
}