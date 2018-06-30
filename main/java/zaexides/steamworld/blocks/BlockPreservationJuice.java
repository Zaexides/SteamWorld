package zaexides.steamworld.blocks;

import java.lang.ref.Reference;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.BlockFluidFinite;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zaexides.steamworld.ModInfo;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.fluids.FluidPreservation;
import zaexides.steamworld.fluids.FluidSteam;
import zaexides.steamworld.init.BlockInitializer;
import zaexides.steamworld.init.ItemInitializer;
import zaexides.steamworld.utility.interfaces.IModeledObject;

public class BlockPreservationJuice extends BlockFluidClassic implements IModeledObject
{
	public BlockPreservationJuice() 
	{
		super(FluidPreservation.fluidPreservation, Material.WATER);
		setRegistryName("preservationLiquid");
		setUnlocalizedName(ModInfo.MODID + ".preservationLiquid");
		setCreativeTab(SteamWorld.CREATIVETAB_BLOCKS);
		
		BlockInitializer.BLOCKS.add(this);
	}

	@Override
	public void RegisterModels() 
	{
		SteamWorld.proxy.RegisterCustomMeshFluid(this, "preservationLiquid");
	}
	
	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) 
	{
		ApplyEffect(entityIn);
	}
	
	public static void ApplyEffect(Entity entityIn)
	{
		if(entityIn instanceof EntityLivingBase)
		{
			EntityLivingBase entityLiving = (EntityLivingBase)entityIn;
			
			if(entityLiving instanceof EntityMob)
			{
				PotionEffect witherEffect = new PotionEffect(MobEffects.WITHER, 100, 1);
				PotionEffect nauseaEffect = new PotionEffect(MobEffects.NAUSEA, 100, 0);
				PotionEffect weakEffect = new PotionEffect(MobEffects.WEAKNESS, 1200, 2);
				
				entityLiving.addPotionEffect(nauseaEffect);
				entityLiving.addPotionEffect(witherEffect);
				entityLiving.addPotionEffect(weakEffect);
			}
			else
			{
				PotionEffect regenerationEffect = new PotionEffect(MobEffects.REGENERATION, 100, 1);
				PotionEffect healthBoostEffect = new PotionEffect(MobEffects.HEALTH_BOOST, 1200, 2);
				PotionEffect waterBreathEffect = new PotionEffect(MobEffects.WATER_BREATHING, 100, 0);
				
				entityLiving.addPotionEffect(regenerationEffect);
				entityLiving.addPotionEffect(waterBreathEffect);
				
				PotionEffect appliedHealthBoost = entityLiving.getActivePotionEffect(MobEffects.HEALTH_BOOST);
				if(appliedHealthBoost == null || appliedHealthBoost.getAmplifier() < 2)
					entityLiving.addPotionEffect(healthBoostEffect);
			}
		}
	}
	
	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state) 
	{
		super.onBlockAdded(world, pos, state);
		tryMix(world, pos);
	}
	
	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block neighborBlock,
			BlockPos neighbourPos) 
	{
		super.neighborChanged(state, world, pos, neighborBlock, neighbourPos);
		tryMix(world, pos);
	}
	
	public void tryMix(World world, BlockPos pos)
	{
		for(EnumFacing facing : EnumFacing.values())
		{
			if(facing != EnumFacing.DOWN)
			{
				IBlockState blockState = world.getBlockState(pos.offset(facing));
				if(blockState.getMaterial().isLiquid() && blockState.getBlock() != this)
				{
					IBlockState targetBlockState = Blocks.AIR.getDefaultState();
					if(blockState.getBlock() == Blocks.LAVA || blockState.getBlock() == Blocks.FLOWING_LAVA)
					{
						world.createExplosion(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 1, true);
					}
					else if(blockState.getBlock() instanceof BlockWitheringJuice)
					{
						targetBlockState = Blocks.COBBLESTONE.getDefaultState();
						world.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5f, 1);
					}
					else
					{
						targetBlockState = BlockInitializer.BLOCK_DECORATIVE.getStateFromMeta(BlockDecorative.EnumType.PRESERVATION_COBBLE.getMeta());
						world.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5f, 1);
					}
					world.setBlockState(pos, targetBlockState);
				}
			}
		}
	}
	
	@Override
	public Vec3d getFogColor(World world, BlockPos pos, IBlockState state, Entity entity, Vec3d originalColor,
			float partialTicks) 
	{
		return new Vec3d(0.05f, 0.2f, 0.04f);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) 
	{
		if(rand.nextDouble() <= 0.005)
			worldIn.playSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.BLOCK_NOTE_CHIME, SoundCategory.BLOCKS, rand.nextFloat() * 0.5f, rand.nextFloat() * 1.5f - 0.5f, false);
		
		if(rand.nextDouble() > 0.1)
			return;
		
		double xPos = pos.getX() + rand.nextDouble();
		double yPos = pos.getY() + rand.nextDouble();
		double zPos = pos.getZ() + rand.nextDouble();
		
		worldIn.spawnParticle(EnumParticleTypes.END_ROD, xPos, yPos, zPos, 0, rand.nextDouble() * 0.1, 0);
	}
	
	@Override
	public float[] getBeaconColorMultiplier(IBlockState state, World world, BlockPos pos, BlockPos beaconPos) 
	{
		final float[] colMain = new float[] { 0.8f, 1.0f, 0.557f};
		final float[] colAlt1 = new float[] { 0.988f, 0.718f, 0.698f};
		final float[] colAlt2 = new float[] { 0.89f, 0.718f, 0.98f};
		
		final float chance = 0.0234f;
		float randVal = world.rand.nextFloat();
		
		if(randVal <= chance)
			return colAlt1;
		else if((randVal - chance) <= chance)
			return colAlt2;
		else
			return colMain;
	}
}
