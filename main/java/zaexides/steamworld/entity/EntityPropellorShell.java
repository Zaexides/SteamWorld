package zaexides.steamworld.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.apache.logging.log4j.Level;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.PotionTypes;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.init.LootTableInitializer;

public class EntityPropellorShell extends EntityFlying implements IMob
{		
	private int cooldown = 0;
	private final int COOLDOWN_TIME = 120;
	
	public EntityPropellorShell(World worldIn) 
	{
		super(worldIn);
		setSize(.5f, 0.63f);
		moveHelper = new EntityPropellorShellMoveHelper(this);
	}
	
	@Override
	protected void initEntityAI() 
	{
		this.tasks.addTask(0, new EntityAIRandomFly(this));
	}
	
	@Override
	protected void applyEntityAttributes() 
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.1);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(5.0);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(15.0);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(10.0);
		this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(0.3);
	}
	
	@Override
	public boolean getCanSpawnHere()
	{
		return super.getCanSpawnHere() && this.world.getDifficulty() != EnumDifficulty.PEACEFUL;
	}
	
	@Override
	public void onUpdate()
    {
        super.onUpdate();

        if (!this.world.isRemote && this.world.getDifficulty() == EnumDifficulty.PEACEFUL)
        {
            this.setDead();
        }
        
        if(!world.isRemote)
        {
	        if(cooldown <= 0)
	        {
	        	ItemStack potionStack = new ItemStack(Items.LINGERING_POTION);
	        	
	        	if(rand.nextInt(10) == 0)
	        	{
	        		final List<PotionEffect> effects = Arrays.asList(new PotionEffect[]
		        			{
		        				new PotionEffect(MobEffects.UNLUCK, 1200, 0)
		        			});
		        	potionStack = PotionUtils.appendEffects(potionStack, effects);
	        	}
	        	else
	        	{
		        	final List<PotionEffect> effects = Arrays.asList(new PotionEffect[]
		        			{
		        				new PotionEffect(MobEffects.NAUSEA, 300, 0),
		        				new PotionEffect(MobEffects.INSTANT_DAMAGE, 1, 0)
		        			});
		        	potionStack = PotionUtils.appendEffects(potionStack, effects);
	        	}
	        	
	        	EntityPotion potion = new EntityPotion(world, this, potionStack);
	        	potion.setThrowableHeading(0, -1, 0, 1.0f, 0.1f);
	        	this.playSound(SoundEvents.ENTITY_EGG_THROW, 1.0f, 1.0f);
	        	this.world.spawnEntity(potion);
	        	cooldown = COOLDOWN_TIME;
	        }
	        else
	        {
	        	cooldown--;
	        }
        }
    }
	
	@Override
	public boolean isPotionApplicable(PotionEffect potioneffectIn) 
	{
		return false;
	}
	
	@Override
	protected float applyPotionDamageCalculations(DamageSource source, float damage) 
	{
		if(source.isMagicDamage())
			return 0.0f;
		return super.applyPotionDamageCalculations(source, damage);
	}
	
	@Override
	public float getEyeHeight() 
	{
		return 0.05f;
	}
	
	static class EntityPropellorShellMoveHelper extends EntityMoveHelper
	{
		public EntityPropellorShellMoveHelper(EntityPropellorShell propellorShell) 
		{
			super(propellorShell);
		}
		
		@Override
		public void onUpdateMoveHelper() 
		{
			if(this.action == Action.MOVE_TO)
			{
				double dx = this.posX - this.entity.posX;
				double dy = this.posY - this.entity.posY;
				double dz = this.posZ - this.entity.posZ;
				
				double d3 = (double)MathHelper.sqrt(dx * dx + dy * dy + dz * dz);
				double speed = this.speed * this.entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue();
				this.entity.motionX += dx / d3 * speed;
				this.entity.motionY += dy / d3 * speed;
				this.entity.motionZ += dz / d3 * speed;
				
				
				if((dx + dy + dz) < 1.2)
					this.action = Action.WAIT;
			}
		}
	}
	
	static class EntityAIRandomFly extends EntityAIBase
	{
		private EntityPropellorShell entity;
		private int cooldown = 0;
		
		private final int COOLDOWN_DURATION = 50;
		
		public EntityAIRandomFly(EntityPropellorShell entity) 
		{
			this.entity = entity;
			setMutexBits(1);
		}
		
		@Override
		public boolean shouldExecute() 
		{
			if(cooldown > 0)
			{
				cooldown--;
				return false;
			}
			return true;
		}
		
		@Override
		public boolean shouldContinueExecuting() 
		{
			return false;
		}
		
		@Override
		public void startExecuting() 
		{
			Random random = entity.getRNG();
			double next_x = entity.posX + (random.nextDouble() * 2 - 1) * 8;
			double next_y = entity.posY + (random.nextDouble() * 2 - 1) * 8;
			double next_z = entity.posZ + (random.nextDouble() * 2 - 1) * 8;
			
			BlockPos pos = new BlockPos(next_x, next_y, next_z);
			IBlockState state = entity.getEntityWorld().getBlockState(pos);
			
			if(state.getMaterial() == Material.AIR)
			{
				entity.getMoveHelper().setMoveTo(next_x, next_y, next_z, random.nextDouble() * .8 + .2);
				cooldown = COOLDOWN_DURATION;
			}
		}
	}
}
