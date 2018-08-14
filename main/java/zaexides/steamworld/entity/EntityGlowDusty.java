package zaexides.steamworld.entity;

import java.awt.Color;

import org.apache.logging.log4j.Level;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWaterFlying;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.init.LootTableInitializer;

public class EntityGlowDusty extends EntityFlyingAnimal
{
	private static final DataParameter<Integer> COLOR = EntityDataManager.<Integer>createKey(EntityGlowDusty.class, DataSerializers.VARINT);
	
	public EntityGlowDusty(World worldIn) 
	{
		super(worldIn);
		setSize(0.25f, 0.25f);
	}
	
	public EntityGlowDusty(World worldIn, int color) 
	{
		this(worldIn);
		setColor(color);
	}
	
	@Override
	protected void initEntityAI() 
	{
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityAIMate(this, 0.7));
		this.tasks.addTask(2, new EntityAIPanic(this, 1.0));
		this.tasks.addTask(4, new EntityAIWanderAvoidWaterFlying(this, 0.7));
		this.tasks.addTask(5, new EntityAIFollowParent(this, 0.8));
		this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(7, new EntityAILookIdle(this));
	}
	
	@Override
	protected void applyEntityAttributes() 
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(1.0);
		this.getEntityAttribute(SharedMonsterAttributes.FLYING_SPEED).setBaseValue(1.3);
	}
	
	@Override
	protected void entityInit() 
	{
		super.entityInit();
		this.dataManager.register(COLOR, 0x00ffffff);
	}
	
	@Override
	public float getEyeHeight() 
	{
		return this.height * 0.5f;
	}
	
	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) 
	{
		float s = 1.0f;
		float b = 1.0f;
		if(rand.nextFloat() < 0.02f)
			s = 0.5f;
		if(rand.nextFloat() < 0.02f)
			b = 0.5f;
		
		setColor(Color.HSBtoRGB(rand.nextFloat(), s, b));
		return super.onInitialSpawn(difficulty, livingdata);
	}
	
	@Override
	public boolean isBreedingItem(ItemStack stack) 
	{
		return stack.getItem() == Items.REDSTONE;
	}
	
	public int getColor() 
	{
		return dataManager.get(COLOR);
	}
	
	public void setColor(int value)
	{
		dataManager.set(COLOR, value);
	}

	@Override
	public EntityAgeable createChild(EntityAgeable ageable) 
	{
		int newColor = getColor();
		
		if(ageable instanceof EntityGlowDusty)
		{
			EntityGlowDusty other = (EntityGlowDusty)ageable;
			newColor += other.getColor();
			newColor *= 0.5;
		}
		return new EntityGlowDusty(world, newColor);
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound compound) 
	{
		super.writeEntityToNBT(compound);
		compound.setInteger("Color", getColor());
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound compound) 
	{
		super.readEntityFromNBT(compound);
		if(compound.hasKey("Color"))
			setColor(compound.getInteger("Color"));
	}
	
	@Override
	protected ResourceLocation getLootTable() 
	{
		return LootTableInitializer.DROPS_GLOWDUSTY;
	}
}
