package zaexides.steamworld.world.dimension;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.NoiseGeneratorOctaves;
import net.minecraft.world.gen.NoiseGeneratorPerlin;
import zaexides.steamworld.blocks.BlockDecorative;
import zaexides.steamworld.init.BlockInitializer;
import zaexides.steamworld.world.biomes.BiomeSteamWorld;

public class TerrainGeneratorSkyOfOld
{
	private static final int CHUNK_GEN_SIZE = 2;
	private static final int CHUNK_XZ_SCALE = 3;
	private static final int CHUNK_Y_SCALE = 33;
	
	private static final float ONE_THIRD = 1/3;
	
	private final World world;
	private final Random random;
	private final IBlockState STONE = BlockInitializer.BLOCK_DECORATIVE.getStateFromMeta(BlockDecorative.EnumType.SKY_STONE.getMeta());
	private final IBlockState AIR = Blocks.AIR.getDefaultState();
	
	private double[] buffer;
	private NoiseGeneratorOctaves mainNoise;
	private double[] mainPerlin;
	private NoiseGeneratorOctaves secondaryNoise;
	private double[] secondaryPerlin;
	private NoiseGeneratorOctaves tertiaryNoise;
	private double[] tertiaryPerlin;

	public Biome[] biomes;
	private final float[] biomeWeights;
	
	private NoiseGeneratorPerlin surfaceNoise;
	private double[] depthBuffer = new double[256];
	
	public TerrainGeneratorSkyOfOld(World world, Random random) 
	{
		this.world = world;
		this.random = random;
		
		this.mainNoise = new NoiseGeneratorOctaves(random, 16);
		this.secondaryNoise = new NoiseGeneratorOctaves(random, 8);
		this.tertiaryNoise = new NoiseGeneratorOctaves(random, 16);
		
		this.surfaceNoise = new NoiseGeneratorPerlin(random, 4);
		
		this.biomeWeights = new float[25];
        for (int j = -2; j <= 2; ++j) 
        {
            for (int k = -2; k <= 2; ++k) 
            {
                float f = 10.0F / MathHelper.sqrt((float)(j * j + k * k) + 0.2F);
                this.biomeWeights[j + 2 + (k + 2) * 5] = f;
            }
        }
	}
	
	public void generate(int chunk_x, int chunk_z, ChunkPrimer primer)
	{
		this.buffer = GetHeights(this.buffer, chunk_x, chunk_z);
		GenerateChunk(chunk_x, chunk_z, primer);
	}
	
	private double[] GetHeights(double[] buffer, int x, int z)
	{
		if(buffer == null)
			buffer = new double[CHUNK_XZ_SCALE * CHUNK_Y_SCALE * CHUNK_XZ_SCALE];
		
		mainPerlin = mainNoise.generateNoiseOctaves(mainPerlin, x * CHUNK_GEN_SIZE, 0, z * CHUNK_GEN_SIZE, CHUNK_XZ_SCALE, CHUNK_Y_SCALE, CHUNK_XZ_SCALE, 800.0, 800.0, 800.0);
		secondaryPerlin = secondaryNoise.generateNoiseOctaves(secondaryPerlin, x * CHUNK_GEN_SIZE, 0, z * CHUNK_GEN_SIZE, CHUNK_XZ_SCALE, CHUNK_Y_SCALE, CHUNK_XZ_SCALE, 120.6, 65.2, 120.6);
		tertiaryPerlin = tertiaryNoise.generateNoiseOctaves(tertiaryPerlin, x * CHUNK_GEN_SIZE, 0, z * CHUNK_GEN_SIZE, CHUNK_XZ_SCALE, CHUNK_Y_SCALE, CHUNK_XZ_SCALE, 242.0, 80.0, 242.0);
		int i = x / 2;
        int j = z / 2;
        
        for(int lx = 0, buffPos = 0; lx < CHUNK_XZ_SCALE; lx++)
        {
        	for(int lz = 0; lz < CHUNK_XZ_SCALE; lz++)
        	{
        		float f = GetBiomeHeight(lx, lz);
        		
        		for(int ly = 0; ly < CHUNK_Y_SCALE; ly++, buffPos++)
        		{
        			double d2 = this.mainPerlin[buffPos] / 512.0D;
                    double d3 = this.secondaryPerlin[buffPos] / 512.0D;
                    double d5 = (this.tertiaryPerlin[buffPos] / 10.0D + 1.0D) / 2.0D;
                    double d4;

                    if (d5 < 0.0D)
                    {
                        d4 = d2;
                    }
                    else if (d5 > 1.0D)
                    {
                        d4 = d3;
                    }
                    else
                    {
                        d4 = d2 + (d3 - d2) * d5;
                    }

                    d4 = d4 - 8.0D;
                    d4 = d4 + (double)f;
                    int k1 = 2;

                    if (ly > CHUNK_Y_SCALE / 2 - k1)
                    {
                        double d6 = (double)((float)(ly - (CHUNK_Y_SCALE / 2 - k1)) / 64.0F);
                        d6 = MathHelper.clamp(d6, 0.0D, 1.0D);
                        d4 = d4 * (1.0D - d6) + -3000.0D * d6;
                    }

                    k1 = 8;

                    if (ly < k1)
                    {
                        double d7 = (double)((float)(k1 - ly) / ((float)k1 - 1.0F));
                        d4 = d4 * (1.0D - d7) + -30.0D * d7;
                    }

                    buffer[buffPos] = d4;
        		}
        	}
        }
		
		return buffer;
	}
	
	private float GetBiomeHeight(int x, int z)
	{
		float h = 0.0f;
		float f = 0.0f;
		for(int wx = 0; wx < 2; wx++)
		{
			for(int wz = 0; wz < 2; wz++)
			{
				Biome biome = biomes[x + 2 + (z + 2) * 10];
				float biomeWeight = biomeWeights[wx + 2 + (wz + 2) * 5];
				
				f += biomeWeight;
				h += biome.getBaseHeight() * biomeWeight;
				h += random.nextFloat() * biome.getHeightVariation() * biomeWeight;
			}
		}
		
		h /= f;
		return h;
	}
	
	private void GenerateChunk(int chunk_x, int chunk_z, ChunkPrimer primer)
	{
		for(int cx = 0; cx < CHUNK_GEN_SIZE; cx++)
		{
			for(int cz = 0; cz < CHUNK_GEN_SIZE; cz++)
			{
				for(int cy = 0; cy < 32; cy++)
				{
					double d0 = 0.25D;
                    double d1 = this.buffer[((cx + 0) * CHUNK_XZ_SCALE + cz + 0) * CHUNK_Y_SCALE + cy + 0];
                    double d2 = this.buffer[((cx + 0) * CHUNK_XZ_SCALE + cz + 1) * CHUNK_Y_SCALE + cy + 0];
                    double d3 = this.buffer[((cx + 1) * CHUNK_XZ_SCALE + cz + 0) * CHUNK_Y_SCALE + cy + 0];
                    double d4 = this.buffer[((cx + 1) * CHUNK_XZ_SCALE + cz + 1) * CHUNK_Y_SCALE + cy + 0];
                    double d5 = (this.buffer[((cx + 0) * CHUNK_XZ_SCALE + cz + 0) * CHUNK_Y_SCALE + cy + 1] - d1) * 0.25D;
                    double d6 = (this.buffer[((cx + 0) * CHUNK_XZ_SCALE + cz + 1) * CHUNK_Y_SCALE + cy + 1] - d2) * 0.25D;
                    double d7 = (this.buffer[((cx + 1) * CHUNK_XZ_SCALE + cz + 0) * CHUNK_Y_SCALE + cy + 1] - d3) * 0.25D;
                    double d8 = (this.buffer[((cx + 1) * CHUNK_XZ_SCALE + cz + 1) * CHUNK_Y_SCALE + cy + 1] - d4) * 0.25D;
                    
                    for (int l1 = 0; l1 < 4; ++l1)
                    {
                        double d9 = 0.125D;
                        double d10 = d1;
                        double d11 = d2;
                        double d12 = (d3 - d1) * 0.125D;
                        double d13 = (d4 - d2) * 0.125D;

                        for (int i2 = 0; i2 < 8; ++i2)
                        {
                            double d14 = 0.125D;
                            double d15 = d10;
                            double d16 = (d11 - d10) * 0.125D;

                            for (int j2 = 0; j2 < 8; ++j2)
                            {
                                IBlockState iblockstate = AIR;

                                if (d15 > 0.0D)
                                {
                                    iblockstate = STONE;
                                }

                                int k2 = i2 + cx * 8;
                                int l2 = l1 + cy * 4;
                                int i3 = j2 + cz * 8;
                                primer.setBlockState(k2, l2, i3, iblockstate);
                                d15 += d16;
                            }

                            d10 += d12;
                            d11 += d13;
                        }

                        d1 += d5;
                        d2 += d6;
                        d3 += d7;
                        d4 += d8;
                    }
				}
			}
		}
	}
	
	public void replaceBiomeBlocks(int x, int z, ChunkPrimer primer, IChunkGenerator generator, Biome[] biomes) 
	{
        if (!net.minecraftforge.event.ForgeEventFactory.onReplaceBiomeBlocks(generator, x, z, primer, this.world)) return;
        this.depthBuffer = this.surfaceNoise.getRegion(this.depthBuffer, (double)(x * 16), (double)(z * 16), 16, 16, 0.0625D, 0.0625D, 1.0D);

        for (int i = 0; i < 16; ++i) 
        {
            for (int j = 0; j < 16; ++j) 
            {
                Biome biome = biomes[i + j * 16];
                if(biome instanceof BiomeSteamWorld)
                	((BiomeSteamWorld)biome).generateBiomeTerrainSteamWorld(world, random, primer, x * 16 + i, z * 16 + j, this.depthBuffer[i + j * 16]);
            }
        }
    }
}
