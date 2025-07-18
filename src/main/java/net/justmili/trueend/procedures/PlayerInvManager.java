package net.justmili.trueend.procedures;

import net.justmili.trueend.TrueEnd;
import net.justmili.trueend.network.Variables;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;
import java.nio.file.Path;
import java.util.Random;

import static net.justmili.trueend.init.Dimensions.BTD;

public class PlayerInvManager {
    private static final double RETURN_CHANCE = 0.90;
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

        if (ModList.get().isLoaded("curios")) {
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
        //if (FabricLoader.getInstance().isModLoaded("trinkets")) {
        //    CompoundTag fullNbt = player.serializeNBT();
        //    if (fullNbt.contains("cardinal_components", Tag.TAG_COMPOUND)) {
        //        CompoundTag cardCaps = fullNbt.getCompound("cardinal_components");
        //        String trinketsKey = "trinkets:trinkets";
        //        if (cardCaps.contains(trinketsKey, Tag.TAG_COMPOUND)) {
        //            CompoundTag trinkTag = cardCaps.getCompound(trinketsKey).copy();
        //            root.put("TrinketsInventory", trinkTag);
        //        }
        //    }
        //}

        if (!saveDir.exists()) saveDir.mkdirs();
        File out = new File(saveDir, makeBackupFilename(player, "BTD"));
        try {
            NbtIo.writeCompressed(root, out);
        } catch (Exception e) {
            TrueEnd.LOGGER.error("Failed to save BTD for player {}", player.getName().getString(), e);
        }
    }

    // NWAD player inv management
    public static void saveInvNWAD(ServerPlayer player) {
        if (!Variables.clearDreamItems) return;
        CompoundTag root = new CompoundTag();
        ListTag mainList = new ListTag();
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

        if (ModList.get().isLoaded("curios")) {
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
        //if (FabricLoader.getInstance().isModLoaded("trinkets")) {
        //    CompoundTag fullNbt = player.serializeNBT();
        //    if (fullNbt.contains("cardinal_components", Tag.TAG_COMPOUND)) {
        //        CompoundTag cardCaps = fullNbt.getCompound("cardinal_components");
        //        String trinketsKey = "trinkets:trinkets";
        //        if (cardCaps.contains(trinketsKey, Tag.TAG_COMPOUND)) {
        //            CompoundTag trinkTag = cardCaps.getCompound(trinketsKey).copy();
        //            root.put("TrinketsInventory", trinkTag);
        //        }
        //    }
        //}

        File out = new File(saveDir, makeBackupFilename(player, "NWAD"));
        try {
            NbtIo.writeCompressed(root, out);
        } catch (Exception e) {
            TrueEnd.LOGGER.error("Failed to save NWAD for player {}", player.getName().getString(), e);
        }
    }

    public static void restoreInvWithChance(ServerPlayer player) {
        if (!Variables.clearDreamItems) return;
        File in = new File(saveDir, makeBackupFilename(player, "BTD"));
        if (!in.exists()) return;

        try {
            CompoundTag root = NbtIo.readCompressed(in);
            ListTag mainList = root.getList("Inventory", Tag.TAG_COMPOUND);
            for (Tag t : mainList) {
                CompoundTag entry = (CompoundTag) t;
                int slot = entry.getInt("Slot");
                ItemStack stack = ItemStack.of(entry.getCompound("Item"));
                if (RAND.nextDouble() < RETURN_CHANCE) {
                    player.getInventory().items.set(slot, stack);
                }
            }
            ListTag armorList = root.getList("Armor", Tag.TAG_COMPOUND);
            for (Tag t : armorList) {
                CompoundTag entry = (CompoundTag) t;
                int slot = entry.getInt("Slot");
                ItemStack stack = ItemStack.of(entry.getCompound("Item"));
                if (RAND.nextDouble() < RETURN_CHANCE) {
                    player.getInventory().armor.set(slot, stack);
                }
            }
            ListTag offList = root.getList("Offhand", Tag.TAG_COMPOUND);
            for (Tag t : offList) {
                CompoundTag entry = (CompoundTag) t;
                int slot = entry.getInt("Slot");
                ItemStack stack = ItemStack.of(entry.getCompound("Item"));
                if (RAND.nextDouble() < RETURN_CHANCE) {
                    player.getInventory().offhand.set(slot, stack);
                }
            }
            if (root.contains("CuriosInventory", Tag.TAG_COMPOUND)) {
                CompoundTag invTag = root.getCompound("CuriosInventory");
                CompoundTag forgeCaps = new CompoundTag();
                forgeCaps.put("curios:inventory", invTag);
                CompoundTag replay = new CompoundTag();
                replay.put("ForgeCaps", forgeCaps);
                if (RAND.nextDouble() < RETURN_CHANCE) {
                    player.load(replay);
                }
            }
            //if (FabricLoader.getInstance().isModLoaded("trinkets")
            //        && root.contains("TrinketsInventory", Tag.TAG_COMPOUND)) {
            //    CompoundTag trinkTag = root.getCompound("TrinketsInventory");
            //    CompoundTag cardCaps = new CompoundTag();
            //    cardCaps.put("trinkets:trinkets", trinkTag);
            //    CompoundTag replay = new CompoundTag();
            //    replay.put("cardinal_components", cardCaps);
            //    if (RAND.nextDouble() < RETURN_CHANCE) {
            //        player.load(replay);
            //    }
            //}
            in.delete();
        } catch (Exception e) {
            TrueEnd.LOGGER.error("Failed to restore BTD for player {}", player.getName().getString(), e);
        }
    }

    public static void restoreInv(ServerPlayer player) {
        if (!Variables.clearDreamItems) return;
        File in = new File(saveDir, makeBackupFilename(player, "NWAD"));
        if (!in.exists()) return;

        try {
            CompoundTag root = NbtIo.readCompressed(in);
            ListTag mainList = root.getList("Inventory", Tag.TAG_COMPOUND);
            for (Tag t : mainList) {
                CompoundTag entry = (CompoundTag) t;
                int slot = entry.getInt("Slot");
                ItemStack stack = ItemStack.of(entry.getCompound("Item"));
                player.getInventory().items.set(slot, stack);
            }
            ListTag armorList = root.getList("Armor", Tag.TAG_COMPOUND);
            for (Tag t : armorList) {
                CompoundTag entry = (CompoundTag) t;
                int slot = entry.getInt("Slot");
                ItemStack stack = ItemStack.of(entry.getCompound("Item"));
                player.getInventory().armor.set(slot, stack);
            }
            ListTag offList = root.getList("Offhand", Tag.TAG_COMPOUND);
            for (Tag t : offList) {
                CompoundTag entry = (CompoundTag) t;
                int slot = entry.getInt("Slot");
                ItemStack stack = ItemStack.of(entry.getCompound("Item"));
                player.getInventory().offhand.set(slot, stack);
            }
            if (root.contains("CuriosInventory", Tag.TAG_COMPOUND)) {
                CompoundTag invTag = root.getCompound("CuriosInventory");
                CompoundTag forgeCaps = new CompoundTag();
                forgeCaps.put("curios:inventory", invTag);
                CompoundTag replay = new CompoundTag();
                replay.put("ForgeCaps", forgeCaps);
                player.load(replay);
            }
            //if (FabricLoader.getInstance().isModLoaded("trinkets")
            //        && root.contains("TrinketsInventory", Tag.TAG_COMPOUND)) {
            //    CompoundTag trinkTag = root.getCompound("TrinketsInventory");
            //    CompoundTag cardCaps = new CompoundTag();
            //    cardCaps.put("trinkets:trinkets", trinkTag);
            //    CompoundTag replay = new CompoundTag();
            //    replay.put("cardinal_components", cardCaps);
            //    player.load(replay);
            //}
            in.delete();
        } catch (Exception e) {
            TrueEnd.LOGGER.error("Failed to restore NWAD for player {}", player.getName().getString(), e);
        }
    }

    public static void clearCuriosSlots(ServerPlayer player) {
        CompoundTag emptyCuriosInventory = new CompoundTag();
        emptyCuriosInventory.put("Curios", new CompoundTag());
        CompoundTag curiosTag = new CompoundTag();
        curiosTag.put("curios:inventory", emptyCuriosInventory);
        CompoundTag forgeCaps = new CompoundTag();
        forgeCaps.put("ForgeCaps", curiosTag);

        // And load it back into the player
        player.load(forgeCaps);
    }

    @SubscribeEvent
    public static void onDimensionChange(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (event.getFrom() != BTD) return;
        if (!Variables.clearDreamItems) return;

        player.getCapability(Variables.PLAYER_VARS_CAP).ifPresent(data -> {
            if (data.hasBeenBeyond()) {
                player.getInventory().clearContent();
                PlayerInvManager.restoreInvWithChance(player);
            }
        });
    }
}
