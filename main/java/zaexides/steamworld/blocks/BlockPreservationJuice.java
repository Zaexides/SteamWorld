package zaexides.steamworld.blocks;

import java.lang.ref.Reference;

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
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
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
import zaexides.steamworld.items.ItemInitializer;
import zaexides.steamworld.utility.interfaces.IModeledObject;

public class BlockPreservationJuice extends BlockFluidClassic implements IModeledObject
{
	public BlockPreservationJuice() 
	{
		super(FluidPreservation.fluidPreservation, Material.WATER);
		setRegistryName("preservationLiquid");
		setUnlocalizedName(ModInfo.MODID + ".preservationLiquid");
		setCreativeTab(SteamWorld.CREATIVETAB);
		
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
		if(entityIn instanceof EntityLivingBase)
		{
			EntityLivingBase entityLiving = (EntityLivingBase)entityIn;
			
			if(entityLiving instanceof EntityMob)
			{
				PotionEffect witherEffect = new PotionEffect(MobEffects.WITHER, 100, 1);
				entityLiving.addPotionEffect(witherEffect);
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
					else
					{
						targetBlockState = BlockInitializer.BLOCK_DECORATIVE.getStateFromMeta(BlockDecorative.EnumType.PRESERVATION_COBBLE.getMeta());
					}
					world.setBlockState(pos, targetBlockState);
				}
			}
		}
	}
}
