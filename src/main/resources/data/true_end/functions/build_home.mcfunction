# Make player face north (face "home" door)
tp @s ~ ~ ~ 180 0

# Terrain modifications / terrain clear
fill ~3 ~ ~3 ~-3 ~5 ~-4 air
fill ~-1 ~ ~-5 ~1 ~5 ~-5 air

# Terrain modification / anti-water measures
fill ~-5 ~-1 ~-5 ~5 ~-1 ~5 true_end:cobblestone replace #true_end:build_home_replacables_fluid

fill ~5 ~-1 ~-5 ~5 ~-16 ~-5 true_end:wood replace #true_end:build_home_replacables_fluid
fill ~-5 ~-1 ~-5 ~-5 ~-16 ~-5 true_end:wood replace #true_end:build_home_replacables_fluid
fill ~-5 ~-1 ~5 ~-5 ~-16 ~5 true_end:wood replace #true_end:build_home_replacables_fluid
fill ~5 ~-1 ~5 ~5 ~-16 ~5 true_end:wood replace #true_end:build_home_replacables_fluid

# Floor and ceiling
fill ~-2 ~-1 ~-2 ~2 ~-1 ~2 true_end:wooden_planks
fill ~-2 ~4 ~-2 ~2 ~4 ~2 true_end:wooden_planks
setblock ~ ~-1 ~-3 true_end:wooden_planks

# Supports and walls
fill ~-3 ~ ~-3 ~-3 ~3 ~-3 true_end:wood
setblock ~-3 ~4 ~-3 true_end:wood_6_sided[axis=z]
fill ~3 ~ ~-3 ~3 ~3 ~-3 true_end:wood
setblock ~3 ~4 ~-3 true_end:wood_6_sided[axis=x]
fill ~-3 ~ ~3 ~-3 ~3 ~3 true_end:wood
setblock ~-3 ~4 ~3 true_end:wood_6_sided[axis=x]
fill ~3 ~ ~3 ~3 ~3 ~3 true_end:wood
setblock ~3 ~4 ~3 true_end:wood_6_sided[axis=z]

fill ~-2 ~4 ~-3 ~2 ~4 ~-3 true_end:wood[axis=x]
fill ~-2 ~4 ~3 ~2 ~4 ~3 true_end:wood[axis=x]
fill ~-3 ~4 ~-2 ~-3 ~4 ~2 true_end:wood[axis=z]
fill ~3 ~4 ~-2 ~3 ~4 ~2 true_end:wood[axis=z]

fill ~-2 ~ ~-3 ~2 ~ ~-3 true_end:cobblestone
fill ~-2 ~ ~3 ~2 ~ ~3 true_end:cobblestone
fill ~-3 ~ ~-2 ~-3 ~ ~2 true_end:cobblestone
fill ~3 ~ ~-2 ~3 ~ ~2 true_end:cobblestone

fill ~-2 ~1 ~-3 ~2 ~3 ~-3 true_end:wooden_planks
fill ~-2 ~1 ~3 ~2 ~3 ~3 true_end:wooden_planks
fill ~-3 ~1 ~-2 ~-3 ~3 ~2 true_end:wooden_planks
fill ~3 ~1 ~-2 ~3 ~3 ~2 true_end:wooden_planks

# Windows
fill ~3 ~1 ~-1 ~3 ~2 ~1 true_end:glass

# Details
setblock ~ ~ ~-3 true_end:door[half=lower,facing=south]
setblock ~ ~1 ~-3 true_end:door[half=upper,facing=south]
setblock ~2 ~ ~1 red_bed[part=foot,facing=south]
setblock ~2 ~ ~2 red_bed[part=head,facing=south]
setblock ~ ~2 ~-2 wall_torch[facing=south]
setblock ~ ~2 ~-4 wall_torch[facing=north]
setblock ~-1 ~ ~2 minecraft:chest[type=right]
setblock ~-2 ~ ~2 minecraft:chest[type=left]
setblock ~-1 ~1 ~2 minecraft:chest[type=right]
setblock ~-2 ~1 ~2 minecraft:chest[type=left]
fill ~-2 ~ ~-2 ~-1 ~1 ~-2 minecraft:furnace[facing=south]
setblock ~2 ~ ~-2 minecraft:crafting_table

# "Gift"
setblock ~1 ~ ~-2 chest[facing=south]{LootTable:"true_end:chests/home"} replace

# Terrain fix after terrain adapt (temp fix)
fill ~-4 ~-1 ~-4 ~4 ~-1 ~-4 true_end:grass_block replace true_end:dirt

# Makes the player look at the top half of the bed next to them as if they just woke up
tp @s ~1 ~0.1 ~1 -40 40
spawnpoint @s ~ ~ ~
kill @e[type=item,distance=..32]