package zaexides.steamworld.worldgen.biomes;

import java.util.Random;

import org.apache.logging.log4j.Level;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zaexides.steamworld.SteamWorld;

public abstract class BiomeSteamWorld extends Biome
{
	public BiomeSteamWorld(BiomeProperties properties) 
	{
		super(properties);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getGrassColorAtPos(BlockPos pos) 
	{
		double temperature = (double)MathHelper.clamp(getFloatTemperature(pos), 0.0f, 1.0f);
        double humidity = (double)MathHelper.clamp(getRainfall(), 0.0f, 1.0f);
        humidity *= temperature;
        
        int r = getFloatTemperature(pos) >= 10.0 ? 200 : 0;
        int g = (int)((1.0 - humidity) * 255.0);
        int b = (int)((1.0 - temperature) * 255.0);
        
        if(r > 0)
        {
        	b -= r;
        	g -= r;
        }
        if(b < 0)
        	b = 0;
        if(g < 64)
        	g = 64;
        
        int col = (r << 16) | (g << 8) | b;
        
        return col;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getFoliageColorAtPos(BlockPos pos) 
	{
		return getGrassColorAtPos(pos);
	}
	
	public void generateBiomeTerrainSteamWorld(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int x, int z, double noiseVal)
	{
		int topLayerHeight = -1;
		int primerX = x & 15;
		int primerZ = z & 15;
		
		for(int y = 255; y >= 0; y--)
		{
			IBlockState blockState = chunkPrimerIn.getBlockState(primerX, y, primerZ);
			
			if(blockState.getMaterial() == Material.AIR)
				topLayerHeight = -1;
			else if(blockState.getBlock() == Blocks.STONE)
			{
				if(topLayerHeight == -1)
				{
					topLayerHeight = y;
				}
				
				if(y == topLayerHeight)
					chunkPrimerIn.setBlockState(primerX, y, primerZ, topBlock);
				else if(y >= topLayerHeight - 3)
					chunkPrimerIn.setBlockState(primerX, y, primerZ, fillerBlock);
			}
		}
	}
}
