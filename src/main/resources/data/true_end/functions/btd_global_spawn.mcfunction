# "Gift"
setblock ~1 ~ ~-2 air
setblock ~1 ~ ~-2 chest[facing=south]{LootTable:"true_end:chests/home"} replace

# Terrain fix after terrain adapt (temp fix)
fill ~-4 ~-1 ~-4 ~4 ~-1 ~-4 true_end:grass_block replace true_end:dirt

# Makes the player look at the top half of the bed next to them as if they just woke up
tp @s ~1 ~0.1 ~1 -40 40
spawnpoint @s ~ ~ ~