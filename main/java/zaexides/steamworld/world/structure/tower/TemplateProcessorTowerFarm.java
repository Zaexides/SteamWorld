package zaexides.steamworld.world.structure.tower;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.ITemplateProcessor;
import net.minecraft.world.gen.structure.template.Template.BlockInfo;
import zaexides.steamworld.entity.villangler.EntityVillangler;
import zaexides.steamworld.entity.villangler.EntityVillangler.VillanglerVariant;
import zaexides.steamworld.utility.interfaces.IResettable;

public class TemplateProcessorTowerFarm implements ITemplateProcessor, IInitializableProcessor, IResettable
{	
	private static final float VILLANGLER_CHANCE = 0.03f;
	private static final int MAX_VILLANGLERS = 3;
	private int currentVillanglerCount = 0;
	
	private IBlockState crop;
	
	public void Init(Random random)
	{
		int cropType = random.nextInt(12);
		if(cropType < 5)
			this.crop = Blocks.WHEAT.getDefaultState();
		else if (cropType < 8)
			this.crop = Blocks.CARROTS.getDefaultState();
		else if (cropType < 11)
			this.crop = Blocks.POTATOES.getDefaultState();
		else
			this.crop = Blocks.BEETROOTS.getDefaultState();
	}
	
	@Override
	public BlockInfo processBlock(World worldIn, BlockPos pos, BlockInfo blockInfoIn) 
	{
		if(blockInfoIn.blockState.getBlock() == Blocks.DIRT)
			blockInfoIn = new BlockInfo(pos, Blocks.FARMLAND.getDefaultState(), null);
		else if(blockInfoIn.blockState.getBlock() == Blocks.WHEAT)
			blockInfoIn = new BlockInfo(pos, crop, null);
		else if(blockInfoIn.blockState.getMaterial() == Material.AIR && !worldIn.canSeeSky(pos))
		{
			if(currentVillanglerCount < MAX_VILLANGLERS && worldIn.rand.nextFloat() < VILLANGLER_CHANCE)
			{
				EntityVillangler villangler = new EntityVillangler(worldIn, VillanglerVariant.DEFAULT);
				villangler.setPositionAndRotation(pos.getX(), pos.getY(), pos.getZ(), 0, 0);
				if(villangler.isNotColliding())
				{
					worldIn.spawnEntity(villangler);
					currentVillanglerCount++;
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
		currentVillanglerCount = 0;
	}
}
