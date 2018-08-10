package zaexides.steamworld.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityAnemoneStinger extends EntityTippedArrow
{
	private static final double VELOCITY_MULTIPLIER = 0.1d;
	
	public EntityAnemoneStinger(World worldIn) 
	{
		super(worldIn);
	}
	
	public EntityAnemoneStinger(World worldIn, EntityLivingBase shooter) 
	{
		super(worldIn, shooter);
	}
	
	@Override
	protected void arrowHit(EntityLivingBase living) 
	{
		super.arrowHit(living);
		
		if(shootingEntity != null && shootingEntity.isEntityAlive())
		{
			double dx = shootingEntity.posX - living.posX;
			double dy = (shootingEntity.posY - living.posY) + 2;
			double dz = shootingEntity.posZ - living.posZ;
			
			if(dy < 2)
				dy = 2;
			
			living.motionX += dx * VELOCITY_MULTIPLIER;
			living.motionY += dy * VELOCITY_MULTIPLIER;
			living.motionZ += dz * VELOCITY_MULTIPLIER;
		}
	}
}
