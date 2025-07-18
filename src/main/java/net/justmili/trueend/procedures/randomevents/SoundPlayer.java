package net.justmili.trueend.procedures.randomevents;

import net.justmili.trueend.TrueEnd;
import net.justmili.trueend.init.Blocks;
import net.justmili.trueend.network.Variables;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.TickEvent;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraft.sounds.SoundSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import javax.annotation.Nullable;
import java.util.Objects;

import static net.justmili.trueend.init.Dimensions.NWAD;
import static net.justmili.trueend.procedures.DimSwapToBTD.BlockPosRandomX;
import static net.justmili.trueend.procedures.DimSwapToBTD.BlockPosRandomZ;

@Mod.EventBusSubscriber
public class SoundPlayer {
    public static final int randomRepeatCount = 3 + (int)(Math.random() * ((9 - 3) + 1));

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            execute(event, event.player.level(), event.player.getX(), event.player.getY(), event.player.getZ(), event.player);
        }
    }
    public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
        execute(null, world, x, y, z, entity);
    }
    private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z, Entity entity) {
        int soundX = BlockPosRandomX/4;
        int soundY = 1 + (int)(Math.random() * ((8 - 1) + 1));
        int soundZ = BlockPosRandomZ/4;
        double randEvtChance = Variables.randomEventChance;
        if (Variables.randomEventsToggle) {
            if (entity == null) return;
            entity.getCapability(Variables.PLAYER_VARS_CAP).ifPresent(data -> {
                if (data.hasBeenBeyond()) {
                    if ((entity.level().dimension()) == NWAD || (entity.level().dimension()) == Level.OVERWORLD) {
                        if (Math.random() < randEvtChance) {
                            if (Math.random() < randEvtChance) {
                                if ((world.getBlockState(BlockPos.containing(x, y - 0.5, z))).getBlock() == Blocks.GRASS_BLOCK.get() || (world.getBlockState(BlockPos.containing(x, y - 0.5, z))).getBlock() == net.minecraft.world.level.block.Blocks.GRASS_BLOCK) {
                                    for (int index0 = 0; index0 < randomRepeatCount; index0++) {
                                        TrueEnd.wait(6, () -> {
                                            if (world instanceof Level _level) {
                                                if (!_level.isClientSide()) {
                                                    _level.playSound(null, BlockPos.containing(x + 6, y - 6, z + 6), Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue(ResourceLocation.parse("block.grass.step"))), SoundSource.NEUTRAL, 1, 1);
                                                } else {
                                                    _level.playLocalSound(soundX, soundY, soundZ, Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue(ResourceLocation.parse("block.grass.step"))), SoundSource.NEUTRAL, 1, 1, false);
                                                }
                                            }
                                        });
                                    }
                                }
                            }
                            if (Math.random() < randEvtChance) {
                                if ((world.getBlockState(BlockPos.containing(x, y - 0.5, z))).getBlock() == Blocks.DIRT.get() || (world.getBlockState(BlockPos.containing(x, y - 0.5, z))).getBlock() == net.minecraft.world.level.block.Blocks.ROOTED_DIRT) {
                                    for (int index1 = 0; index1 < randomRepeatCount; index1++) {
                                        TrueEnd.wait(6, () -> {
                                            if (world instanceof Level _level) {
                                                if (!_level.isClientSide()) {
                                                    _level.playSound(null, BlockPos.containing(x + 6, y - 6, z + 6), Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue(ResourceLocation.parse("block.gravel.break"))), SoundSource.NEUTRAL, 1, 1);
                                                } else {
                                                    _level.playLocalSound(soundX, soundY, soundZ, Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue(ResourceLocation.parse("block.gravel.break"))), SoundSource.NEUTRAL, 1, 1, false);
                                                }
                                            }
                                        });
                                    }
                                }
                            }
                            if (Math.random() < randEvtChance) {
                                if ((world.getBlockState(BlockPos.containing(x, y - 0.5, z))).getBlock() == Blocks.STONE.get() || (world.getBlockState(BlockPos.containing(x, y - 0.5, z))).getBlock() == net.minecraft.world.level.block.Blocks.STONE) {
                                    for (int index2 = 0; index2 < randomRepeatCount; index2++) {
                                        TrueEnd.wait(8, () -> {
                                            if (world instanceof Level _level) {
                                                if (!_level.isClientSide()) {
                                                    _level.playSound(null, BlockPos.containing(x + 6, y - 6, z + 6), Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue(ResourceLocation.parse("block.stone.break"))), SoundSource.NEUTRAL, 1, 1);
                                                } else {
                                                    _level.playLocalSound(soundX, soundY, soundZ, Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue(ResourceLocation.parse("block.stone.break"))), SoundSource.NEUTRAL, 1, 1, false);
                                                }
                                            }
                                        });
                                    }
                                }
                            }
                            if (Math.random() < randEvtChance) {
                                if ((world.getBlockState(BlockPos.containing(x, y - 0.5, z))).getBlock() == net.minecraft.world.level.block.Blocks.DEEPSLATE) {
                                    for (int index3 = 0; index3 < (randomRepeatCount - 1); index3++) {
                                        TrueEnd.wait(8, () -> {
                                            if (world instanceof Level _level) {
                                                if (!_level.isClientSide()) {
                                                    _level.playSound(null, BlockPos.containing(x + 6, y - 6, z + 6), Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue(ResourceLocation.parse("block.grass.step"))), SoundSource.NEUTRAL, 1, 1);
                                                } else {
                                                    _level.playLocalSound(soundX, soundY, soundZ, Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue(ResourceLocation.parse("block.grass.step"))), SoundSource.NEUTRAL, 1, 1, false);
                                                }
                                            }
                                        });
                                    }
                                }
                            }
                        }
                    }
                }
            });
        }
    }
}