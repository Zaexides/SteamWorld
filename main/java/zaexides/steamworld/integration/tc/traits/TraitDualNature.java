package zaexides.steamworld.integration.tc.traits;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import slimeknights.tconstruct.library.traits.AbstractTrait;

public class TraitDualNature extends AbstractTrait
{
	public TraitDualNature() 
	{
		super("dual_nature", 0x078596);
	}
	
	@Override
	public void miningSpeed(ItemStack tool, BreakSpeed event) 
	{
		EntityPlayer player = event.getEntityPlayer();
		double yPos = player.posY;
		float coeff = (255-(float)yPos) / 255.0f;
		
		if(yPos > 255 || coeff < 0.025f)
			coeff = 0.025f;
		else if(yPos < 0)
			coeff = 1.0f;
		
		coeff *= 2;
		
		event.setNewSpeed((event.getOriginalSpeed() + event.getNewSpeed()) * coeff);
	}
	
	@Override
	public float damage(ItemStack tool, EntityLivingBase player, EntityLivingBase target, float damage, float newDamage,
			boolean isCritical) 
	{
		double yPos = player.posY;
		float coeff = ((float)yPos) / 255.0f;
		
		if(yPos > 255)
			coeff = 1.0f;
		else if(yPos < 0 || coeff < 0.025f)
			coeff = 0.025f;
		
		coeff *= 2;
		
		return newDamage * coeff;
	}
}
