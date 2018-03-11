package zaexides.steamworld.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import zaexides.steamworld.blocks.item.ItemBlockLegacy;
import zaexides.steamworld.blocks.item.ItemBlockVariant;
import zaexides.steamworld.items.ItemInitializer;

public class BlockLegacy extends SteamWorldBlock
{
	public Block targetBlock;
	public int targetMeta;
	
	public BlockLegacy(String name, Material material, float hardness, Block targetBlock, int targetMeta) 
	{
		super(name, material, hardness);
		setTickRandomly(true);
		this.targetBlock = targetBlock;
		this.targetMeta = targetMeta;
	}
	
	@Override
	public void AddItemBlock(int maxStackSize) 
	{
		ItemInitializer.ITEMS.add(new ItemBlockLegacy(this).setRegistryName(this.getRegistryName()));
	}

	@Override
	public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random) 
	{
		replace(worldIn, pos);
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) 
	{
		replace(worldIn, pos);
	}
	
	private void replace(World world, BlockPos pos)
	{
		world.setBlockState(pos, targetBlock.getStateFromMeta(targetMeta));
		spawnReplacementParticles(world, pos);
	}
	
	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state,
			int fortune) 
	{
		drops.add(new ItemStack(targetBlock, 0, targetMeta));
		spawnReplacementParticles((World)world, pos);
	}
	
	private void spawnReplacementParticles(World world, BlockPos pos)
	{
		if(world.isRemote)
		{
			for(int a = 0; a < world.rand.nextInt(8); a++)
			{
				double x = Math.round(pos.getX() + world.rand.nextDouble());
				double y = Math.round(pos.getY() + world.rand.nextDouble());
				double z = Math.round(pos.getZ() + world.rand.nextDouble());
				world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, x, y, z, 0, 0.5, 0);
			}
		}
	}
}
