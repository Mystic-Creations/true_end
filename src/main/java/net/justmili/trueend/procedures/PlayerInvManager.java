package net.justmili.trueend.procedures;

import net.justmili.trueend.TrueEnd;
import net.justmili.trueend.network.Variables;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLPaths;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

import java.io.File;
import java.nio.file.Path;
import java.util.Map;
import java.util.Random;

import static net.justmili.trueend.TrueEnd.LOGGER;
import static net.justmili.trueend.init.Dimensions.BTD;

@Mod.EventBusSubscriber
public class PlayerInvManager {
    private static final Random RAND = new Random();
    private static final File saveDir = FMLPaths.CONFIGDIR.get().resolve("true_end").toFile();

    private static String makeBackupFilename(ServerPlayer player, String suffix) {
        Path worldFolder = player.getServer().getWorldPath(LevelResource.LEVEL_DATA_FILE).getParent();
        String folderName = worldFolder.getFileName().toString();
        String cleanName = folderName.replaceAll("[^A-Za-z0-9]", "").toLowerCase();
        String uuid = player.getUUID().toString().replace("-", "");
        return uuid + "_" + cleanName + "_" + suffix + ".dat";
    }

    // BTD player inv management
    public static void saveInvBTD(ServerPlayer player) {
        if (!Variables.clearDreamItems) return;
        CompoundTag root = new CompoundTag();
        ListTag mainList = new ListTag();
        saveInventory(player, root, mainList);

        if (!saveDir.exists()) saveDir.mkdirs();
        File out = new File(saveDir, makeBackupFilename(player, "BTD"));
        try {
            NbtIo.writeCompressed(root, out);
        } catch (Exception e) {
            LOGGER.error("Failed to save BTD for player {}", player.getName().getString(), e);
        }
    }
    public static void restoreInvWithChance(ServerPlayer player) {
        if (!Variables.clearDreamItems) return;
        File in = new File(saveDir, makeBackupFilename(player, "BTD"));
        if (!in.exists()) return;

        restoreInventory(player, in, 0.90);
    }

    // NWAD player inv management
    public static void saveInvNWAD(ServerPlayer player) {
        if (!Variables.clearDreamItems) return;
        CompoundTag root = new CompoundTag();
        ListTag mainList = new ListTag();
        saveInventory(player, root, mainList);

        File out = new File(saveDir, makeBackupFilename(player, "NWAD"));
        try {
            NbtIo.writeCompressed(root, out);
        } catch (Exception e) {
            LOGGER.error("Failed to save NWAD for player {}", player.getName().getString(), e);
        }
    }
    public static void restoreInv(ServerPlayer player) {
        if (!Variables.clearDreamItems) return;
        File in = new File(saveDir, makeBackupFilename(player, "NWAD"));
        if (!in.exists()) return;

        restoreInventory(player, in, 1.0);
    }

    public static void saveInventory(ServerPlayer player, CompoundTag root, ListTag mainList) {
        for (int i = 0; i < player.getInventory().items.size(); i++) {
            ItemStack stack = player.getInventory().items.get(i);
            if (!stack.isEmpty()) {
                CompoundTag entry = new CompoundTag();
                entry.putInt("Slot", i);
                entry.put("Item", stack.save(new CompoundTag()));
                mainList.add(entry);
            }
        }
        root.put("Inventory", mainList);

        ListTag armorList = new ListTag();
        for (int i = 0; i < player.getInventory().armor.size(); i++) {
            ItemStack stack = player.getInventory().armor.get(i);
            if (!stack.isEmpty()) {
                CompoundTag entry = new CompoundTag();
                entry.putInt("Slot", i);
                entry.put("Item", stack.save(new CompoundTag()));
                armorList.add(entry);
            }
        }
        root.put("Armor", armorList);

        ListTag offList = new ListTag();
        for (int i = 0; i < player.getInventory().offhand.size(); i++) {
            ItemStack stack = player.getInventory().offhand.get(i);
            if (!stack.isEmpty()) {
                CompoundTag entry = new CompoundTag();
                entry.putInt("Slot", i);
                entry.put("Item", stack.save(new CompoundTag()));
                offList.add(entry);
            }
        }
        root.put("Offhand", offList);

        if (TrueEnd.inModList("curios")) {
            CompoundTag fullNbt = player.serializeNBT();
            if (fullNbt.contains("ForgeCaps", Tag.TAG_COMPOUND)) {
                CompoundTag forgeCaps = fullNbt.getCompound("ForgeCaps");
                String curiosKey = "curios:inventory";
                if (forgeCaps.contains(curiosKey, Tag.TAG_COMPOUND)) {
                    CompoundTag invTag = forgeCaps.getCompound(curiosKey).copy();
                    root.put("CuriosInventory", invTag);
                }
            }
        }
    }
    public static void restoreInventory(ServerPlayer player, File in, Double chance) {
        try {
            CompoundTag root = NbtIo.readCompressed(in);
            ListTag mainList = root.getList("Inventory", Tag.TAG_COMPOUND);
            for (Tag t : mainList) {
                CompoundTag entry = (CompoundTag) t;
                int slot = entry.getInt("Slot");
                ItemStack stack = ItemStack.of(entry.getCompound("Item"));
                if (RAND.nextDouble() < chance) {
                    player.getInventory().items.set(slot, stack);
                }
            }
            ListTag armorList = root.getList("Armor", Tag.TAG_COMPOUND);
            for (Tag t : armorList) {
                CompoundTag entry = (CompoundTag) t;
                int slot = entry.getInt("Slot");
                ItemStack stack = ItemStack.of(entry.getCompound("Item"));
                if (RAND.nextDouble() < chance) {
                    player.getInventory().armor.set(slot, stack);
                }
            }
            ListTag offList = root.getList("Offhand", Tag.TAG_COMPOUND);
            for (Tag t : offList) {
                CompoundTag entry = (CompoundTag) t;
                int slot = entry.getInt("Slot");
                ItemStack stack = ItemStack.of(entry.getCompound("Item"));
                if (RAND.nextDouble() < chance) {
                    player.getInventory().offhand.set(slot, stack);
                }
            }
            if (root.contains("CuriosInventory", Tag.TAG_COMPOUND)) {
                CompoundTag invTag = root.getCompound("CuriosInventory");
                CompoundTag forgeCaps = new CompoundTag();
                forgeCaps.put("curios:inventory", invTag);
                CompoundTag replay = new CompoundTag();
                replay.put("ForgeCaps", forgeCaps);
                if (RAND.nextDouble() < chance) {
                    player.load(replay);
                }
            }
            in.delete();
        } catch (Exception e) {
            LOGGER.error("Failed to restore BTD for player {}", player.getName().getString(), e);
        }
    }
    public static void clearCuriosSlots(ServerPlayer player) {
        if (!TrueEnd.inModList("curios")) return;
        CuriosApi.getCuriosInventory(player).ifPresent(curiosInv -> {
            Map<String, ICurioStacksHandler> curios = curiosInv.getCurios();
            curios.forEach((id, stackHandler) -> {
                if (stackHandler == null) return;
                var stacks = stackHandler.getStacks();
                int slots = stacks.getSlots();
                for (int i = 0; i < slots; i++) {
                    stacks.setStackInSlot(i, ItemStack.EMPTY);
                }
            });
        });
    }

    @SubscribeEvent
    public static void onDimensionChange(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (!(event.getFrom().equals(BTD) && event.getTo().equals(Level.OVERWORLD))) return;
        if (!Variables.clearDreamItems) return;

        player.getCapability(Variables.PLAYER_VARS_CAP).ifPresent(data -> {
            if (data.hasBeenBeyond()) {
                player.getInventory().clearContent();
                clearCuriosSlots(player);
                PlayerInvManager.restoreInvWithChance(player);
            }
        });
    }
}
