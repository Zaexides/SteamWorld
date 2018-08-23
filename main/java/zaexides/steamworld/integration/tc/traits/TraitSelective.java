package zaexides.steamworld.integration.tc.traits;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import slimeknights.tconstruct.library.traits.AbstractTraitLeveled;
import zaexides.steamworld.blocks.BlockPreservationJuice;

public class TraitSelective extends AbstractTraitLeveled
{
	public TraitSelective(int level) 
	{
		super("selective", TextFormatting.GREEN.getColorIndex(), 2, level);
	}

	@Override
	public float damage(ItemStack tool, EntityLivingBase player, EntityLivingBase target, float damage, float newDamage,
			boolean isCritical) 
	{
		if(target instanceof EntityMob)
			return newDamage;
		else
			return newDamage * -0.5f + (0.1f * levels);
	}
	
	@Override
	public void onHit(ItemStack tool, EntityLivingBase player, EntityLivingBase target, float damage,
			boolean isCritical) 
	{
		if(levels > 1)
			BlockPreservationJuice.ApplyEffect(target);
	}
}
