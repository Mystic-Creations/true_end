package net.justmili.trueend.procedures.devcmd.screentests;

import io.netty.buffer.Unpooled;
import net.justmili.trueend.init.Sounds;
import net.justmili.trueend.world.inventory.FunnyMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.network.NetworkHooks;

import java.util.Objects;

public class Funny {
    public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
        if (entity == null) return;
        if (!(entity instanceof ServerPlayer serverPlayer)) return;

        BlockPos pos = BlockPos.containing(x, y, z);
        NetworkHooks.openScreen(serverPlayer, new MenuProvider() {
            @Override
            public Component getDisplayName() {
                return Component.literal("Funny");
            }

            @Override
            public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
                Funny.playVineBoom();
                return new FunnyMenu(
                        id,
                        inventory,
                        new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(pos)
                );
            }
        }, pos);

        new Thread(() -> {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException ignored) {
            }
            if (serverPlayer.level().getServer() != null) {
                Objects.requireNonNull(serverPlayer.level().getServer()).execute(() -> {
                    if (serverPlayer.containerMenu instanceof FunnyMenu) {
                        serverPlayer.closeContainer();
                    }
                });
            }
        }).start();
    }

    public static void playVineBoom() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level != null && mc.player != null) {
            mc.level.playLocalSound(
                    mc.player.getX(),
                    mc.player.getY(),
                    mc.player.getZ(),
                    Sounds.VINE_BOOM.get(),
                    SoundSource.MASTER,
                    1.0f,
                    1.0f,
                    false
            );
        }
    }
}
