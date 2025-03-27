tp @s ~ ~ ~ 180 0

fill ~-4 ~ ~-4 ~4 ~8 ~3 air
fill ~-2 ~-1 ~-2 ~2 ~-1 ~2 true_end:wooden_planks
fill ~-2 ~4 ~-2 ~2 ~4 ~2 true_end:wooden_planks

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

setblock ~ ~-1 ~-3 true_end:wooden_planks
setblock ~ ~ ~-3 minecraft:oak_door[half=lower]
setblock ~ ~1 ~-3 minecraft:oak_door[half=upper]

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

setblock ~1 ~ ~-2 minecraft:chest[facing=south]
loot insert ~1 ~ ~-2 loot true_end:chests/home


tp @s ~1 ~1 ~1 -40 40