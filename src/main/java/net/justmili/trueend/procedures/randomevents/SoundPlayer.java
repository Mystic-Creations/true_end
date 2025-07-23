package net.justmili.trueend.procedures.randomevents;

import net.justmili.trueend.TrueEnd;
import net.justmili.trueend.init.Blocks;
import net.justmili.trueend.network.Variables;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.sounds.SoundSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import java.util.Objects;
import static net.minecraft.world.level.block.Blocks.*;

import static net.justmili.trueend.init.Dimensions.NWAD;
import static net.justmili.trueend.procedures.DimSwapToBTD.BlockPosRandomX;
import static net.justmili.trueend.procedures.DimSwapToBTD.BlockPosRandomZ;

@Mod.EventBusSubscriber
public class SoundPlayer {
    public static final int randomRepeatCount = 3 + (int) (Math.random() * ((9 - 3) + 1));

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (!(event.player instanceof ServerPlayer player)) return;
        if (player.level().dimension() != NWAD) if (player.level().dimension() != Level.OVERWORLD) return;

        if (!Variables.randomEventsToggle) return;
        if (!(Math.random() < Variables.randomEventChance)) return;
        if (event.phase != TickEvent.Phase.END) return;

        double x = player.getX();
        double y = player.getY();
        double z = player.getZ();
        Level level = player.level();

        //Sound Players
        if (groundBlock(level, x, y, z) == Blocks.GRASS_BLOCK.get() || groundBlock(level, x, y, z) == GRASS_BLOCK) {
            playSounds(player, 8, "block.grass.step");
        }
        if (groundBlock(level, x, y, z) == SAND) {
            playSounds(player, 8, "block.sand.step");
        }
        if (groundBlock(level, x, y, z) == Blocks.DIRT.get() || groundBlock(level, x, y, z) == DIRT) {
            playSounds(player, 12, "block.gravel.break");
        }
        if (groundBlock(level, x, y, z) == Blocks.STONE.get() || groundBlock(level, x, y, z) == STONE) {
            playSounds(player, 10, "block.stone.break");
        }
        if (groundBlock(level, x, y, z) == DEEPSLATE) {
            playSounds(player, 16, "block.deepslate.break");
        }
    }

    public static Block groundBlock(Level level, double x, double y, double z) {
        return level.getBlockState(BlockPos.containing(x, y - 0.5, z)).getBlock();
    }

    private static void playSounds(ServerPlayer player, Integer delay, String soundId) {
        int soundX = BlockPosRandomX / 4;
        int soundY = 1 + (int) (Math.random() * ((8 - 1) + 1));
        int soundZ = BlockPosRandomZ / 4;
        LevelAccessor world = player.level();

        for (int index3 = 0; index3 < (SoundPlayer.randomRepeatCount - 1); index3++) {
            TrueEnd.wait(delay, () -> {
                if (world instanceof Level level) {
                    if (!level.isClientSide()) {
                        level.playSound(null, BlockPos.containing(soundX, soundY, soundZ), Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue(ResourceLocation.parse(soundId))), SoundSource.NEUTRAL, 1, 1);
                    }
                }
            });
        }
    }
}