package net.mysticcreations.true_end.client;

import net.mysticcreations.true_end.TrueEnd;
import net.mysticcreations.true_end.entity.renderer.UnknownEntityRenderer;
import net.mysticcreations.true_end.init.Blocks;
import net.mysticcreations.true_end.init.Items;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@Mod.EventBusSubscriber(modid = "true_end", bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        TrueEnd.EVENT_BUS.addListener(UnknownEntityRenderer::registerEntityRenderers);

        event.enqueueWork(() -> {
            ItemProperties.register(
                    Items.DREAMERS_COMPASS.get(), ResourceLocation.parse("angle"),
                    (stack, world, entity, seed) -> {
                        if (world == null || entity == null) {
                            return 0F;
                        }
                        CompoundTag tag = stack.getOrCreateTag();

                        if (!tag.getBoolean("TargetTracked")) {
                            return 0F;
                        }

                        BlockPos targetPos = new BlockPos(
                                tag.getInt("TargetX"),
                                tag.getInt("TargetY"),
                                tag.getInt("TargetZ")
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
                        float angleValue = (float) (relative / 360.0);

                        return angleValue;
                    }
            );
            ItemProperties.register(
                    Blocks.VOID.get().asItem(),
                    ResourceLocation.parse("true_end:type"),
                    (stack, world, entity, seed) -> {
                        CompoundTag bst = stack.getTagElement("BlockStateTag");
                        if (bst != null && "white".equals(bst.getString("type"))) {
                            return 1.0f;
                        }
                        return 0.0f;
                    }
            );
        });
    }
}
