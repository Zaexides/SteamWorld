package zaexides.steamworld.world.structure.tower;

import net.minecraft.block.BlockChest;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
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

public class TemplateProcessorTowerLibrary implements ITemplateProcessor, IResettable
{	
	private static final float LIBRARIAN_CHANCE = 0.03f;
	private static final int MAX_LIBRARIANS = 3;
	private int currentLibrarianCount = 0;
	
	@Override
	public BlockInfo processBlock(World worldIn, BlockPos pos, BlockInfo blockInfoIn) 
	{
		if(blockInfoIn.blockState.getBlock() == Blocks.BOOKSHELF)
		{
			if(worldIn.rand.nextFloat() < 0.005f)
			{
				EnumFacing facing = GetCorrectFace(worldIn, pos);
				IBlockState blockState = Blocks.CHEST.getDefaultState().withProperty(BlockChest.FACING, facing);
				worldIn.setBlockState(pos, blockState);
				TileEntity tileEntity = worldIn.getTileEntity(pos);
				if(tileEntity != null && tileEntity instanceof TileEntityLockableLoot)
				{
					((TileEntityLockableLoot)tileEntity).setLootTable(LootTableInitializer.TOWER_LIBRARY, worldIn.rand.nextLong());
					blockInfoIn = new BlockInfo(pos, blockState, tileEntity.serializeNBT());
				}
			}
		}
		
		if(blockInfoIn.blockState.getMaterial() == Material.AIR && !worldIn.canSeeSky(pos))
		{
			if(currentLibrarianCount < MAX_LIBRARIANS && worldIn.rand.nextFloat() < LIBRARIAN_CHANCE)
			{
				EntityVillangler villangler = new EntityVillangler(worldIn, VillanglerVariant.LIBRARIAN);
				villangler.setPositionAndRotation(pos.getX(), pos.getY(), pos.getZ(), 0, 0);
				if(villangler.isNotColliding())
				{
					worldIn.spawnEntity(villangler);
					currentLibrarianCount++;
				}
				else
					worldIn.removeEntity(villangler);
			}
		}
		
		return blockInfoIn;
	}
	
	private EnumFacing GetCorrectFace(World world, BlockPos pos)
	{
		for(int i = 2; i < 6; i++)
		{
			if(world.isAirBlock(pos.add(EnumFacing.VALUES[i].getDirectionVec())))
				return EnumFacing.VALUES[i];
		}
		return EnumFacing.EAST;
	}

	@Override
	public void Reset() 
	{
		currentLibrarianCount = 0;
	}
}
