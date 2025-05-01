package net.justmili.trueend.procedures;

import net.justmili.trueend.TrueEndMod;
import net.justmili.trueend.init.TrueEndModBlocks;
import net.justmili.trueend.network.TrueEndVariables;
import net.minecraft.world.level.block.Blocks;
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

import static net.justmili.trueend.procedures.DimKeyRegistry.NWAD;
import static net.minecraft.world.level.Level.OVERWORLD;

@Mod.EventBusSubscriber
public class SoundPlayer {
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
        if (entity == null) return;
        entity.getCapability(TrueEndVariables.PLAYER_VARS_CAP).ifPresent(data -> {
            if (data.hasBeenBeyond()) {
                int randEvtChance = (int) TrueEndVariables.MapVariables.get(world).getRandomEventChance();
                if ((entity.level().dimension()) == NWAD || (entity.level().dimension()) == OVERWORLD) {
                    if (Math.random() < randEvtChance) {
                        if (Math.random() < randEvtChance) {
                            if ((world.getBlockState(BlockPos.containing(x, y - 1, z))).getBlock() == TrueEndModBlocks.GRASS_BLOCK.get() || (world.getBlockState(BlockPos.containing(x, y - 1, z))).getBlock() == Blocks.GRASS_BLOCK) {
                                for (int index0 = 0; index0 < (int) (Math.random() * 10); index0++) {
                                    TrueEndMod.queueServerWork(15, () -> {
                                        if (world instanceof Level _level) {
                                            if (!_level.isClientSide()) {
                                                _level.playSound(null, BlockPos.containing(x + 6, y - 6, z + 6), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.grass.break")), SoundSource.NEUTRAL, 1, 1);
                                            } else {
                                                _level.playLocalSound((x + 6), (y - 6), (z + 6), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.grass.break")), SoundSource.NEUTRAL, 1, 1, false);
                                            }
                                        }
                                    });
                                }
                            }
                        }
                        if (Math.random() < randEvtChance) {
                            if ((world.getBlockState(BlockPos.containing(x, y - 1, z))).getBlock() == TrueEndModBlocks.DIRT.get() || (world.getBlockState(BlockPos.containing(x, y - 1, z))).getBlock() == Blocks.ROOTED_DIRT) {
                                for (int index1 = 0; index1 < (int) (Math.random() * 10); index1++) {
                                    TrueEndMod.queueServerWork(15, () -> {
                                        if (world instanceof Level _level) {
                                            if (!_level.isClientSide()) {
                                                _level.playSound(null, BlockPos.containing(x + 6, y - 6, z + 6), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.gravel.break")), SoundSource.NEUTRAL, 1, 1);
                                            } else {
                                                _level.playLocalSound((x + 6), (y - 6), (z + 6), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.gravel.break")), SoundSource.NEUTRAL, 1, 1, false);
                                            }
                                        }
                                    });
                                }
                            }
                        }
                        if (Math.random() < randEvtChance) {
                            if ((world.getBlockState(BlockPos.containing(x, y - 1, z))).getBlock() == TrueEndModBlocks.STONE.get() || (world.getBlockState(BlockPos.containing(x, y - 1, z))).getBlock() == Blocks.STONE) {
                                for (int index2 = 0; index2 < (int) (Math.random() * 10); index2++) {
                                    TrueEndMod.queueServerWork(15, () -> {
                                        if (world instanceof Level _level) {
                                            if (!_level.isClientSide()) {
                                                _level.playSound(null, BlockPos.containing(x + 6, y - 6, z + 6), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.stone.break")), SoundSource.NEUTRAL, 1, 1);
                                            } else {
                                                _level.playLocalSound((x + 6), (y - 6), (z + 6), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.stone.break")), SoundSource.NEUTRAL, 1, 1, false);
                                            }
                                        }
                                    });
                                }
                            }
                        }
                        if (Math.random() < randEvtChance) {
                            if ((world.getBlockState(BlockPos.containing(x, y - 1, z))).getBlock() == Blocks.DEEPSLATE) {
                                for (int index3 = 0; index3 < (int) (Math.random() * 10); index3++) {
                                    TrueEndMod.queueServerWork(15, () -> {
                                        if (world instanceof Level _level) {
                                            if (!_level.isClientSide()) {
                                                _level.playSound(null, BlockPos.containing(x + 6, y - 6, z + 6), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.grass.break")), SoundSource.NEUTRAL, 1, 1);
                                            } else {
                                                _level.playLocalSound((x + 6), (y - 6), (z + 6), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.grass.break")), SoundSource.NEUTRAL, 1, 1, false);
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