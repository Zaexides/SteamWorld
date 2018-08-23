package zaexides.steamworld.world.structure;

import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.ITemplateProcessor;
import net.minecraft.world.gen.structure.template.Template.BlockInfo;
import zaexides.steamworld.entity.villangler.EntityVillangler;
import zaexides.steamworld.entity.villangler.EntityVillangler.VillanglerVariant;
import zaexides.steamworld.init.LootTableInitializer;
import zaexides.steamworld.utility.interfaces.IResettable;

public class TemplateProcessorLab implements ITemplateProcessor, IResettable
{
	private static final float SCIENTIST_CHANCE = 0.05f;
	private static final int MAX_SCIENTISTS = 2;
	private int currentScientistCount = 0;
	
	@Override
	public BlockInfo processBlock(World worldIn, BlockPos pos, BlockInfo blockInfoIn) 
	{
		if(blockInfoIn.blockState.getBlock() == Blocks.CHEST)
		{
			TileEntity tileEntity = worldIn.getTileEntity(pos);
			if(tileEntity != null && tileEntity instanceof TileEntityLockableLoot)
			{
				((TileEntityLockableLoot)tileEntity).setLootTable(LootTableInitializer.WITHER_LAB, worldIn.rand.nextLong());
				return new BlockInfo(pos, blockInfoIn.blockState, tileEntity.serializeNBT());
			}
		}
		
		if(blockInfoIn.blockState.getMaterial() == Material.AIR && !worldIn.canSeeSky(pos))
		{
			if(currentScientistCount < MAX_SCIENTISTS && worldIn.rand.nextFloat() < SCIENTIST_CHANCE)
			{
				EntityVillangler villangler = new EntityVillangler(worldIn, VillanglerVariant.SCIENTIST);
				villangler.setPositionAndRotation(pos.getX(), pos.getY(), pos.getZ(), 0, 0);
				if(villangler.isNotColliding())
				{
					worldIn.spawnEntity(villangler);
					currentScientistCount++;
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
		currentScientistCount = 0;
	}
}
