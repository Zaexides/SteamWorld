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
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIFindEntityNearestPlayer;
import net.minecraft.entity.ai.EntityMoveHelper;
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
import zaexides.steamworld.entity.ai.FloatingMoveHelper;
import zaexides.steamworld.init.LootTableInitializer;

public class EntityPropellorShell extends EntityFlying implements IMob
{		
	private int cooldown = 0;
	private final int COOLDOWN_TIME = 120;
	
	public EntityPropellorShell(World worldIn) 
	{
		super(worldIn);
		setSize(.5f, 0.63f);
		moveHelper = new FloatingMoveHelper(this);
	}
	
	@Override
	protected void initEntityAI() 
	{
		this.tasks.addTask(0, new EntityAIFly(this));
		this.targetTasks.addTask(1, new EntityAIFindEntityNearestPlayer(this));
	}
	
	@Override
	protected void applyEntityAttributes() 
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(3.0);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(5.0);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(15.0);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).setBaseValue(10.0);
		this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(0.3);
	}
	
	@Override
	public boolean getCanSpawnHere()
	{
		BlockPos pos = new BlockPos(this);
		return world.getBlockState(pos).getMaterial() == Material.AIR && this.world.getDifficulty() != EnumDifficulty.PEACEFUL;
	}
	
	private void SpawnPotion(float xDir, float yDir, float zDir, float velocity)
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
    	potion.setThrowableHeading(xDir, yDir, zDir, velocity, 0.1f);
    	this.playSound(SoundEvents.ENTITY_EGG_THROW, 1.0f, 1.0f);
    	this.world.spawnEntity(potion);
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
        	EntityLivingBase target = this.getAttackTarget();
        	
	        if(cooldown <= 0 && target != null && target.isEntityAlive())
	        {
	        	SpawnPotion(0, -1, 0, 1.0f);
	        	cooldown = COOLDOWN_TIME;
	        }
	        else if(cooldown > 0)
	        {
	        	cooldown--;
	        }
        }
    }
	
	@Override
	public void onDeath(DamageSource cause) 
	{
		if(!this.isDead && !world.isRemote)
		{
			SpawnPotion(-1, 1, -1, 0.1f);
			SpawnPotion(-1, 1, 1, 0.1f);
			SpawnPotion(1, 1, 1, 0.1f);
			SpawnPotion(1, 1, -1, 0.1f);
		}
		super.onDeath(cause);
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
	
	static class EntityAIFly extends EntityAIBase
	{
		private EntityPropellorShell entity;
		private int cooldown = 0;
		
		private final int COOLDOWN_DURATION = 50;
		
		public EntityAIFly(EntityPropellorShell entity) 
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
			double next_x, next_y, next_z;
			Random random = entity.getRNG();
			
			EntityLivingBase target = entity.getAttackTarget();
			if(target == null || !target.isEntityAlive())
			{
				next_x = entity.posX + (random.nextDouble() * 2 - 1) * 8;
				next_y = entity.posY + (random.nextDouble() * 2 - 1) * 8;
				next_z = entity.posZ + (random.nextDouble() * 2 - 1) * 8;
			}
			else
			{
				next_x = target.posX;
				next_y = target.posY + 3.0;
				next_z = target.posZ;
			}
			
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
