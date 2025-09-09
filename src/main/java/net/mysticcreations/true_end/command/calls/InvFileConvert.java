package net.mysticcreations.true_end.command.calls;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;
import java.nio.file.Path;

public class InvFileConvert {
    private static final File saveDir = FMLPaths.CONFIGDIR.get().resolve("true_end").toFile();
    private static String makeBackupFilename(ServerPlayer player, String suffix) {
        Path worldFolder = player.getServer().getWorldPath(LevelResource.LEVEL_DATA_FILE).getParent();
        String folderName = worldFolder.getFileName().toString();
        String cleanName = folderName.replaceAll("[^A-Za-z0-9]", "").toLowerCase();
        String uuid = player.getUUID().toString().replace("-", "");
        return uuid + "_" + cleanName + "_" + suffix + ".dat";
    }

    public static void execute(ServerPlayer player, String suffix) {
        try {
            if (!saveDir.exists()) saveDir.mkdirs();

            String filename = makeBackupFilename(player, suffix);
            File file = new File(saveDir, filename);
            if (!file.exists()) {
                player.sendSystemMessage(Component.literal("[True End] No backup file found for " + suffix));
                player.sendSystemMessage(Component.literal("[True End] File: " + filename));
                player.sendSystemMessage(Component.literal("[True End] File Path: " + saveDir));
                return;
            }

            CompoundTag root = NbtIo.readCompressed(file);

            if (root.contains("CuriosInventory", Tag.TAG_LIST) || !root.contains("CuriosInventory", Tag.TAG_COMPOUND)) {
                player.sendSystemMessage(Component.literal("[True End] Your Inventory Backup is already up-to-date"));
                return;
            }

            CompoundTag oldInv = root.getCompound("CuriosInventory");
            ListTag newCuriosList = new ListTag();

            if (oldInv.contains("Curios", Tag.TAG_LIST)) {
                ListTag handlerList = oldInv.getList("Curios", Tag.TAG_COMPOUND);
                for (Tag htag : handlerList) {
                    CompoundTag handlerCompound = (CompoundTag) htag;
                    String identifier = handlerCompound.getString("Identifier");
                    CompoundTag stacksHandler = handlerCompound.contains("StacksHandler", Tag.TAG_COMPOUND)
                        ? handlerCompound.getCompound("StacksHandler")
                        : null;
                    if (stacksHandler != null && stacksHandler.contains("Stacks", Tag.TAG_COMPOUND)) {
                        CompoundTag stacks = stacksHandler.getCompound("Stacks");
                        if (stacks.contains("Items", Tag.TAG_LIST)) {
                            ListTag items = stacks.getList("Items", Tag.TAG_COMPOUND);
                            for (Tag it : items) {
                                CompoundTag itemEntry = (CompoundTag) it;
                                int slot = itemEntry.contains("Slot") ? itemEntry.getInt("Slot") : 0;
                                CompoundTag itemCompound = itemEntry.copy();
                                itemCompound.remove("Slot");

                                CompoundTag newEntry = new CompoundTag();
                                newEntry.putString("Handler", identifier);
                                newEntry.putInt("Slot", slot);
                                newEntry.put("Item", itemCompound);
                                newCuriosList.add(newEntry);
                            }
                        }
                    }
                }
            } else {
                for (String key : oldInv.getAllKeys()) {
                    Tag potential = oldInv.get(key);
                    if (!(potential instanceof CompoundTag)) continue;
                    CompoundTag handlerCompound = (CompoundTag) potential;
                    CompoundTag stacksHandler = handlerCompound.contains("StacksHandler", Tag.TAG_COMPOUND)
                        ? handlerCompound.getCompound("StacksHandler")
                        : handlerCompound;
                    if (stacksHandler.contains("Stacks", Tag.TAG_COMPOUND)) {
                        CompoundTag stacks = stacksHandler.getCompound("Stacks");
                        if (stacks.contains("Items", Tag.TAG_LIST)) {
                            ListTag items = stacks.getList("Items", Tag.TAG_COMPOUND);
                            for (Tag it : items) {
                                CompoundTag itemEntry = (CompoundTag) it;
                                int slot = itemEntry.contains("Slot") ? itemEntry.getInt("Slot") : 0;
                                CompoundTag itemCompound = itemEntry.copy();
                                itemCompound.remove("Slot");
                                CompoundTag newEntry = new CompoundTag();
                                newEntry.putString("Handler", key);
                                newEntry.putInt("Slot", slot);
                                newEntry.put("Item", itemCompound);
                                newCuriosList.add(newEntry);
                            }
                        }
                    }
                }
            }

            if (newCuriosList.isEmpty()) {
                player.sendSystemMessage(Component.literal("[True End] No Curios items were found to convert"));
                return;
            }

            CompoundTag newRoot = root.copy();
            newRoot.remove("CuriosInventory");
            newRoot.put("CuriosInventory", newCuriosList);
            String oldName = file.getName();
            int dot = oldName.lastIndexOf('.');
            String base = dot >= 0 ? oldName.substring(0, dot) : oldName;
            File renamed = new File(file.getParentFile(), base + "_old.dat");
            if (!file.renameTo(renamed)) {
                renamed = new File(file.getParentFile(), base + "_old.dat");
            }

            NbtIo.writeCompressed(newRoot, file);
            renamed.delete();

            player.sendSystemMessage(Component.literal("[True End] Your Inventory Backup has been converted"));
        } catch (Exception e) {
            e.printStackTrace();
            player.sendSystemMessage(Component.literal("[True End] Failed to convert your Inventory Backup - check server logs"));
        }
    }
}
