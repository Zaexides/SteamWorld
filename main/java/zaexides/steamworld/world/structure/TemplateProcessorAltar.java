package zaexides.steamworld.world.structure;

import net.minecraft.block.Block;
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
import zaexides.steamworld.init.BlockInitializer;
import zaexides.steamworld.init.LootTableInitializer;
import zaexides.steamworld.utility.interfaces.IResettable;

public class TemplateProcessorAltar implements ITemplateProcessor
{
	@Override
	public BlockInfo processBlock(World worldIn, BlockPos pos, BlockInfo blockInfoIn) 
	{
		if(blockInfoIn.blockState.getBlock() == BlockInitializer.ALTAR)
		{
			EntityVillangler villangler = new EntityVillangler(worldIn, VillanglerVariant.CULTIST);
			villangler.setPositionAndRotation(pos.getX(), pos.getY() + 1, pos.getZ(), 0, 0);
			worldIn.spawnEntity(villangler);
		}
		
		return blockInfoIn;
	}
}
