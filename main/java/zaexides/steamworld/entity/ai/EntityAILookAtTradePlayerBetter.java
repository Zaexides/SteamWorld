package zaexides.steamworld.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;

public class EntityAILookAtTradePlayerBetter<T extends EntityLiving & IMerchant> extends EntityAIWatchClosest
{
	public IMerchant merchant;
	
	public EntityAILookAtTradePlayerBetter(T merchant)
    {
        super(merchant, EntityPlayer.class, 8.0F);
        this.merchant = merchant;
    }
	
	@Override
	public boolean shouldExecute() 
	{
		if(this.merchant.getCustomer() != null)
		{
			this.closestEntity = this.merchant.getCustomer();
			return true;
		}
		else
			return false;
	}
}
