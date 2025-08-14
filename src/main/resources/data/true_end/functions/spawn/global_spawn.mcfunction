# "Gift"
setblock ~1 ~ ~-2 air
setblock ~1 ~ ~-2 chest[facing=south]{LootTable:"true_end:chests/home"} replace

# Makes the player look at the top half of the bed next to them as if they just woke up
tp @s ~ ~ ~ -40 40
spawnpoint @s ~ ~ ~