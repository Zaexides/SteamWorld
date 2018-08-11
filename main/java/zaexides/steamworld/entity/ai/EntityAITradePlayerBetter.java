package zaexides.steamworld.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;

public class EntityAITradePlayerBetter<T extends EntityLiving & IMerchant> extends EntityAIBase
{
	private final T merchant;
	
	public EntityAITradePlayerBetter(T merchant) 
	{
		this.merchant = merchant;
		this.setMutexBits(5);
	}
	
	@Override
	public boolean shouldExecute() 
	{
		if (!this.merchant.isEntityAlive())
        {
            return false;
        }
        else if (this.merchant.isInWater())
        {
            return false;
        }
        else if (!this.merchant.onGround)
        {
            return false;
        }
        else if (this.merchant.velocityChanged)
        {
            return false;
        }
        else
        {
            EntityPlayer entityplayer = this.merchant.getCustomer();

            if (entityplayer == null)
            {
                return false;
            }
            else if (this.merchant.getDistanceSqToEntity(entityplayer) > 16.0D)
            {
                return false;
            }
            else
            {
                return entityplayer.openContainer != null;
            }
        }
	}
	
	@Override
	public void startExecuting() 
	{
		this.merchant.getNavigator().clearPathEntity();
	}
	
	@Override
	public void resetTask() 
	{
		this.merchant.setCustomer(null);
	}
}	
