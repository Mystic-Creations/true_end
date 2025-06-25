package net.justmili.trueend.procedures.events;

import net.justmili.trueend.TrueEnd;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;
import java.util.Random;

public class PlayerInvManager {
    private static final double RETURN_CHANCE = 0.90;
    private static final Random RAND = new Random();

    /** Call this before inventory clearing */
    public static void savePlayerInventory(ServerPlayer player) {
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

        // 3. Write to disk
        File configDir = FMLPaths.CONFIGDIR.get().toFile();
        File out = new File(configDir, player.getUUID() + "_TEholder.dat");
        try {
            NbtIo.writeCompressed(root, out);
        } catch (Exception e) {
            TrueEnd.LOGGER.error("Failed to save TEholder for player {}", player.getName().getString(), e);
        }
    }

    /** Call this when player leaves the dream dimension */
    public static void restorePlayerInventory(ServerPlayer player) {
        File configDir = FMLPaths.CONFIGDIR.get().toFile();
        File in = new File(configDir, player.getUUID() + "_TEholder.dat");
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
            in.delete();
        } catch (Exception e) {
            TrueEnd.LOGGER.error("Failed to restore TEholder for player {}", player.getName().getString(), e);
        }
    }
}
