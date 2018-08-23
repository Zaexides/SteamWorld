package zaexides.steamworld.integration.tc;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import zaexides.steamworld.utility.interfaces.IModeledObject;

public class BlockFluidTerrite extends BlockTinkersIntegrationFluid implements IModeledObject
{
	public BlockFluidTerrite(Fluid fluid) 
	{
		super(fluid);
		setTickRandomly(true);
	}
	
	@Override
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) 
	{
		if(rand.nextInt(4) == 0)
		{
			IBlockState topBlockState = worldIn.getBlockState(pos.up());
			if(topBlockState.getMaterial() == Material.AIR || !topBlockState.isOpaqueCube())
			{
				double particleX = pos.getX() + rand.nextDouble();
				double particleZ = pos.getZ() + rand.nextDouble();
				double particleY = pos.getY() + stateIn.getBoundingBox(worldIn, pos).maxY;
				
				worldIn.spawnParticle(EnumParticleTypes.FLAME, particleX, particleY, particleZ, 0, 0, 0);
			}
		}
	}
	
	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) 
	{
		super.updateTick(world, pos, state, rand);
		
		if(rand.nextInt(3) == 0)
		{
			int tries = rand.nextInt(4) + 1;
			for(int i = 0; i < tries; i++)
			{
				int xPos = pos.getX() + rand.nextInt(5) - 2;
				int yPos = pos.getY() + rand.nextInt(5) - 2;
				int zPos = pos.getZ() + rand.nextInt(5) - 2;
				
				BlockPos otherPos = new BlockPos(xPos, yPos, zPos);
				
				IBlockState currentState = world.getBlockState(otherPos);
				
				if(currentState.getBlock() == Blocks.AIR && world.getBlockState(otherPos.down()).getMaterial() != Material.AIR)
				{
					world.setBlockState(otherPos, Blocks.FIRE.getDefaultState());
				}
			}
		}
	}
}
