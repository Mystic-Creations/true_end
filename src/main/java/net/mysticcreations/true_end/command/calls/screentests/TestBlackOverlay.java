package net.mysticcreations.true_end.command.calls.screentests;

import io.netty.buffer.Unpooled;
import net.mysticcreations.true_end.client.gui.inventory.BlackOverlay;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.network.NetworkHooks;

public class TestBlackOverlay {
    private static LevelAccessor world;
    public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
        TestBlackOverlay.world = world;
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
                    return new BlackOverlay(id, inventory, new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(pos));
                }
            }, pos);
        }
    }
}

