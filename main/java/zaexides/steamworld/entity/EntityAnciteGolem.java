package zaexides.steamworld.entity;

import java.util.List;

import org.apache.logging.log4j.Level;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.init.LootTableInitializer;

public class EntityAnciteGolem extends EntityGolem implements IMob
{		
	public boolean dormant = true;
	public float awakeningStep = 0.0f;
	
	private static final AxisAlignedBB PLAYER_CHECK_AREA = new AxisAlignedBB(-4, -4, -4, 4, 4, 4);
	
	public EntityAnciteGolem(World worldIn) 
	{
		super(worldIn);
		setSize(1.0f, 1.0f);
	}
	
	@Override
	public void onUpdate() 
	{
		super.onUpdate();
		
		if(ticksExisted % 20 == 0)
			updateState();
		
		if(dormant)
		{
			if(awakeningStep > 0.0f)
				awakeningStep -= 0.05f;
			if(awakeningStep < 0.0f)
				awakeningStep = 0.0f;
		}
		else
		{
			if(awakeningStep < 1.0f)
				awakeningStep += 0.05f;
			if(awakeningStep > 1.0f)
				awakeningStep = 1.0f;
		}
	}
	
	private void updateState()
	{
		EntityLivingBase revengeTarget = getRevengeTarget();
		if(revengeTarget != null && revengeTarget.isEntityAlive())
		{
			dormant = false;
			return;
		}
		
		List<EntityPlayer> players = world.getEntitiesWithinAABB(EntityPlayer.class, PLAYER_CHECK_AREA.offset(posX, posY, posZ));
		
		if(players.size() != 0)
		{
			for(EntityPlayer entityPlayer : players)
			{
				if(!entityPlayer.isCreative() && !entityPlayer.isSpectator())
				{
					dormant = false;
					return;
				}
			}
		}
		
		dormant = true;
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound compound) 
	{
		super.writeEntityToNBT(compound);
		compound.setBoolean("dormant", this.dormant);
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound compound) 
	{
		super.readEntityFromNBT(compound);
		this.dormant = compound.getBoolean("dormant");
		this.awakeningStep = dormant ? 0.0f : 1.0f;
	}
}
