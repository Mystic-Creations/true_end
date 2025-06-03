package net.justmili.trueend.entity;

import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;

public class UnknownEntity extends Monster {
    public UnknownEntity(EntityType<? extends Monster> type, Level world) {
        super(type, world);
    } // override this mili later

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                      .add(…);
    }

    @Override
    public void tick() {
        super.tick();
    }
}
