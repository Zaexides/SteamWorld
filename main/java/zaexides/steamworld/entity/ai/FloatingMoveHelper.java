package zaexides.steamworld.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.util.math.MathHelper;

public class FloatingMoveHelper extends EntityMoveHelper
{
	protected final double maxDistance;
	
	public FloatingMoveHelper(EntityLiving entitylivingIn) 
	{
		this(entitylivingIn, 0.1);
	}
	
	public FloatingMoveHelper(EntityLiving entityLivingIn, double maxDistance) 
	{
		super(entityLivingIn);
		this.maxDistance = maxDistance;
	}

	@Override
	public void onUpdateMoveHelper() 
	{
		if(this.action == Action.MOVE_TO)
		{
			double dx = this.posX - this.entity.posX;
			double dy = this.posY - this.entity.posY;
			double dz = this.posZ - this.entity.posZ;
			
			double squaredDistance = dx * dx + dy * dy + dz * dz;
			
			float speed = (float)(this.speed * this.entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue());
			
			float rot = (float)(MathHelper.atan2(dz, dx) * (180D / Math.PI)) - 90.0F;
            this.entity.rotationYaw = this.limitAngle(this.entity.rotationYaw, rot, 90.0F);
			
			double clampedVerticalMovement = dy;
			
			if(clampedVerticalMovement > speed)
				clampedVerticalMovement = speed;
			else if(clampedVerticalMovement < -speed)
				clampedVerticalMovement = -speed;
			
			this.entity.setMoveVertical((float)clampedVerticalMovement);
			
			if(squaredDistance < maxDistance)
			{
				this.entity.setMoveForward(0.0f);
				this.entity.setMoveVertical(0.0f);
				this.action = Action.WAIT;
			}
		}
	}
}
