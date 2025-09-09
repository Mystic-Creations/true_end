package net.mysticcreations.true_end.command.calls.screentests;

import net.mysticcreations.true_end.init.Sounds;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.network.NetworkHooks;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.MenuProvider;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.BlockPos;

import net.mysticcreations.true_end.client.gui.inventory.Funny;

import io.netty.buffer.Unpooled;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class TestFunny {
    private static LevelAccessor world;

    public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
        TestFunny.world = world;
        if (entity == null)
            return;
        if (entity instanceof ServerPlayer serverPlayer) {
            BlockPos pos = BlockPos.containing(x, y, z);
            NetworkHooks.openScreen(serverPlayer, new MenuProvider() {
                @Override
                public Component getDisplayName() {
                    return Component.literal("Funny");
                }

                @Override
                public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
                    Minecraft mc = Minecraft.getInstance();
                    assert mc.level != null;
                    mc.level.playLocalSound(x, y, z, Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue(Sounds.VINE_BOOM.getId())), SoundSource.MASTER, 1, 1, false);
                    return new Funny(id, inventory, new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(pos));
                }
            }, pos);
        }
        if (entity instanceof ServerPlayer serverPlayer) {
            new Thread(() -> {
                try {
                    Thread.sleep(1100L);
                } catch (InterruptedException ignored) {
                }
                if (serverPlayer.level().getServer() != null) {
                    Objects.requireNonNull(serverPlayer.level().getServer()).execute(() -> {
                        if (serverPlayer.containerMenu instanceof Funny) {
                            serverPlayer.closeContainer();
                        }
                    });
                }
            }).start();
        }
    }
}

