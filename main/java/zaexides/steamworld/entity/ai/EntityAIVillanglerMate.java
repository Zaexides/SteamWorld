package zaexides.steamworld.entity.ai;

import java.util.List;
import java.util.Random;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumParticleTypes;
import zaexides.steamworld.entity.villangler.EntityVillangler;

public class EntityAIVillanglerMate extends EntityAIBase
{
	private final EntityVillangler villangler;
	private final double moveSpeed;
	
	private EntityVillangler targetMate;
	
	private static final int MAX_NEARBY_VILLANGLERS_MULTIPLIER = 1/16;
	private int spawnBabyDelay = 0;
	
	public EntityAIVillanglerMate(EntityVillangler villangler, double speed) 
	{
		this.villangler = villangler;
		this.moveSpeed = speed;
		this.setMutexBits(3);
	}
	
	@Override
	public boolean shouldExecute() 
	{
		if(villangler.getGrowingAge() != 0)
			return false;
		else
		{
			List<EntityVillangler> nearbyVillanglers = villangler.world.getEntitiesWithinAABB(EntityVillangler.class, villangler.getEntityBoundingBox().grow(32.0));
			float chance = (1 - (nearbyVillanglers.size() * MAX_NEARBY_VILLANGLERS_MULTIPLIER)) * 0.0001f;
			if(villangler.world.rand.nextFloat() >= chance)
				return false;
			
			double nearestDistance = 8.0; //Assure the mate is within a smaller range than the checking range.
			for(EntityVillangler other : nearbyVillanglers)
			{
				if(other != villangler && other.getGrowingAge() == 0)
				{
					double distance = villangler.getDistanceSqToEntity(other);
					if(distance < nearestDistance)
					{
						nearestDistance = distance;
						targetMate = other;
					}
				}
			}
			return targetMate != null;
		}
	}
	
	@Override
	public boolean shouldContinueExecuting() 
	{
		return targetMate != null && targetMate.isEntityAlive() && spawnBabyDelay < 60;
	}
	
	@Override
	public void resetTask() 
	{
		this.targetMate = null;
		this.spawnBabyDelay = 0;
	}
	
	@Override
	public void updateTask() 
	{
		villangler.getLookHelper().setLookPositionWithEntity(targetMate, 10, villangler.getVerticalFaceSpeed());
		villangler.getNavigator().tryMoveToEntityLiving(targetMate, moveSpeed);
		spawnBabyDelay++;
		
		if(spawnBabyDelay >= 60 && villangler.getDistanceSqToEntity(targetMate) < 9)
		{
			spawnBaby();
		}
	}
	
	//Vanilla mate AI (near) copy
	private void spawnBaby()
    {
        EntityAgeable entityageable = this.villangler.createChild(this.targetMate);

        final net.minecraftforge.event.entity.living.BabyEntitySpawnEvent event = new net.minecraftforge.event.entity.living.BabyEntitySpawnEvent(villangler, targetMate, entityageable);
        final boolean cancelled = net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event);
        entityageable = event.getChild();
        if (cancelled) {
            //Reset the "inLove" state for the animals
            this.villangler.setGrowingAge(6000);
            this.targetMate.setGrowingAge(6000);
            return;
        }

        if (entityageable != null)
        {
            this.villangler.setGrowingAge(6000);
            this.targetMate.setGrowingAge(6000);
            entityageable.setGrowingAge(-24000);
            entityageable.setLocationAndAngles(this.villangler.posX, this.villangler.posY, this.villangler.posZ, 0.0F, 0.0F);
            villangler.world.spawnEntity(entityageable);
            Random random = this.villangler.getRNG();

            for (int i = 0; i < 7; ++i)
            {
                double d0 = random.nextGaussian() * 0.02D;
                double d1 = random.nextGaussian() * 0.02D;
                double d2 = random.nextGaussian() * 0.02D;
                double d3 = random.nextDouble() * (double)this.villangler.width * 2.0D - (double)this.villangler.width;
                double d4 = 0.5D + random.nextDouble() * (double)this.villangler.height;
                double d5 = random.nextDouble() * (double)this.villangler.width * 2.0D - (double)this.villangler.width;
                villangler.world.spawnParticle(EnumParticleTypes.HEART, this.villangler.posX + d3, this.villangler.posY + d4, this.villangler.posZ + d5, d0, d1, d2);
            }

            if (villangler.world.getGameRules().getBoolean("doMobLoot"))
            {
            	villangler.world.spawnEntity(new EntityXPOrb(villangler.world, this.villangler.posX, this.villangler.posY, this.villangler.posZ, random.nextInt(7) + 1));
            }
        }
    }
}
