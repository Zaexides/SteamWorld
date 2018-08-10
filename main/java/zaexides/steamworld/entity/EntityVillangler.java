package zaexides.steamworld.entity;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.INpc;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.world.World;

public class EntityVillangler extends EntityAgeable implements INpc
{
	public EntityVillangler(World worldIn) 
	{
		super(worldIn);
		setSize(0.8f, 2.0f);
	}
	
	@Override
	public float getEyeHeight()
	{
		return 1.5f;
	}
	
	@Override
	public EntityAgeable createChild(EntityAgeable ageable) 
	{
		return new EntityVillangler(world);
	}
	
	@Override
	protected void initEntityAI() 
	{
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityAIWanderAvoidWater(this, 0.6));
		this.tasks.addTask(2, new EntityAIWatchClosest(this, EntityLiving.class, 4.0f));
	}
}
