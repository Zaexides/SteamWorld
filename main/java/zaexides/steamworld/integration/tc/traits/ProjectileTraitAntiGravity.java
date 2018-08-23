package zaexides.steamworld.integration.tc.traits;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.entity.EntityProjectileBase;
import slimeknights.tconstruct.library.traits.AbstractProjectileTrait;

public class ProjectileTraitAntiGravity extends AbstractProjectileTrait
{
	private final double PROJECTILE_GRAVITY = 0.05000000074505806D;
	
	public ProjectileTraitAntiGravity() 
	{
		super("antigrav", 0x65F7C9);
	}
	
	@Override
	public void onMovement(EntityProjectileBase projectile, World world, double slowdown) 
	{
		if(!projectile.hasNoGravity())
			projectile.motionY += PROJECTILE_GRAVITY * 2;
	}
	
	@Override
	public void onProjectileUpdate(EntityProjectileBase projectile, World world, ItemStack toolStack) 
	{
		if(projectile.posY > 512)
			projectile.setDead();
	}
}
