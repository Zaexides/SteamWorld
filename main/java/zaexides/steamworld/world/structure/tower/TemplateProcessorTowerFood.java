package zaexides.steamworld.world.structure.tower;

import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.ITemplateProcessor;
import net.minecraft.world.gen.structure.template.Template.BlockInfo;
import zaexides.steamworld.entity.villangler.EntityVillangler;
import zaexides.steamworld.entity.villangler.EntityVillangler.VillanglerVariant;
import zaexides.steamworld.init.LootTableInitializer;
import zaexides.steamworld.utility.interfaces.IResettable;

public class TemplateProcessorTowerFood implements ITemplateProcessor, IResettable
{	
	private static final float ECONOMIST_CHANCE = 0.08f;
	private static final int MAX_ECONOMIST = 2;
	private int currentEconomistCount = 0;
	
	@Override
	public BlockInfo processBlock(World worldIn, BlockPos pos, BlockInfo blockInfoIn) 
	{
		if(blockInfoIn.blockState.getBlock() == Blocks.HAY_BLOCK)
		{
			int blockType = worldIn.rand.nextInt(17);
			IBlockState blockState;
			
			NBTTagCompound compound = null;
			
			if(blockType < 8)
				blockState = Blocks.HAY_BLOCK.getDefaultState().withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.values()[worldIn.rand.nextInt(EnumFacing.Axis.values().length)]);
			else if(blockType < 12)
				blockState = Blocks.MELON_BLOCK.getDefaultState();
			else if (blockType < 16)
				blockState = Blocks.PUMPKIN.getDefaultState().withProperty(BlockHorizontal.FACING, EnumFacing.values()[worldIn.rand.nextInt(4) + 2]);
			else
			{
				blockState = Blocks.CHEST.getDefaultState().withProperty(BlockChest.FACING, EnumFacing.values()[worldIn.rand.nextInt(4) + 2]);
				worldIn.setBlockState(pos, blockState);
				TileEntity tileEntity = worldIn.getTileEntity(pos);
				if(tileEntity != null && tileEntity instanceof TileEntityLockableLoot)
				{
					((TileEntityLockableLoot)tileEntity).setLootTable(LootTableInitializer.TOWER_FOOD, worldIn.rand.nextLong());
					compound = tileEntity.serializeNBT();
				}
			}
			
			blockInfoIn = new BlockInfo(pos, blockState, compound);
		}
		
		if(blockInfoIn.blockState.getMaterial() == Material.AIR && !worldIn.canSeeSky(pos))
		{
			if(currentEconomistCount < MAX_ECONOMIST && worldIn.rand.nextFloat() < ECONOMIST_CHANCE)
			{
				EntityVillangler villangler = new EntityVillangler(worldIn, VillanglerVariant.ECONOMIST);
				villangler.setPositionAndRotation(pos.getX(), pos.getY(), pos.getZ(), 0, 0);
				if(villangler.isNotColliding())
				{
					worldIn.spawnEntity(villangler);
					currentEconomistCount++;
				}
				else
					worldIn.removeEntity(villangler);
			}
		}
		
		return blockInfoIn;
	}

	@Override
	public void Reset() 
	{
		currentEconomistCount = 0;
	}
}
