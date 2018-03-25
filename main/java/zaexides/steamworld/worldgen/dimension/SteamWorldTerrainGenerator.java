package zaexides.steamworld.worldgen.dimension;

import java.util.Random;

import org.apache.logging.log4j.Level;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.NoiseGeneratorOctaves;
import net.minecraft.world.gen.NoiseGeneratorPerlin;
import net.minecraftforge.event.terraingen.InitNoiseGensEvent.ContextEnd;
import net.minecraftforge.event.terraingen.InitNoiseGensEvent.ContextOverworld;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.blocks.BlockDecorative;
import zaexides.steamworld.init.BlockInitializer;
import zaexides.steamworld.worldgen.biomes.BiomeSteamWorld;

public class SteamWorldTerrainGenerator 
{
	private final World world;
	private final Random rand;
	
	public Biome[] biomes;
	
	private NoiseGeneratorOctaves mainPerlin;
	private NoiseGeneratorOctaves minLimitPerlin;
    private NoiseGeneratorOctaves maxLimitPerlin;
    private NoiseGeneratorOctaves depthNoise;
    private NoiseGeneratorPerlin surfaceNoise;
    
    private double[] depthBuffer = new double[256];
    
    private final double[] heightMap;
    private final float[] biomeWeights;
    
    private final IBlockState STONE = BlockInitializer.BLOCK_DECORATIVE.getStateFromMeta(BlockDecorative.EnumType.SKY_STONE.getMeta());
	
	public SteamWorldTerrainGenerator(World world, Random random) 
	{
		this.world = world;
		this.rand = random;
		minLimitPerlin = new NoiseGeneratorOctaves(rand, 16);
		maxLimitPerlin = new NoiseGeneratorOctaves(rand, 16);
        surfaceNoise = new NoiseGeneratorPerlin(rand, 4);
        depthNoise = new NoiseGeneratorOctaves(rand, 16);
		mainPerlin = new NoiseGeneratorOctaves(rand, 8);
		heightMap = new double[825];
		
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
	
	private void generateHeightMap(int x4ChunkX, int x4ChunkZ)
	{
		double[] depthRegion = null, mainNoiseRegion = null, minLimitRegion = null, maxLimitRegion = null;
		depthRegion = this.depthNoise.generateNoiseOctaves(depthRegion, x4ChunkX, x4ChunkZ, 5, 5, 200.0D, 200.0D, 0.5D);
        mainNoiseRegion = this.mainPerlin.generateNoiseOctaves(mainNoiseRegion, x4ChunkX, 0, x4ChunkZ, 5, 33, 5, 8.55515D, 4.277575D, 8.55515D);
        minLimitRegion = this.minLimitPerlin.generateNoiseOctaves(minLimitRegion, x4ChunkX, 0, x4ChunkZ, 5, 33, 5, 684.412D, 684.412D, 684.412D);
        maxLimitRegion = this.maxLimitPerlin.generateNoiseOctaves(maxLimitRegion, x4ChunkX, 0, x4ChunkZ, 5, 33, 5, 684.412D, 684.412D, 684.412D);
        int l = 0;
        int i1 = 0;

        for (int j1 = 0; j1 < 5; ++j1) 
        {
            for (int k1 = 0; k1 < 5; ++k1) 
            {
                float f = 0.0F;
                float f1 = 0.0F;
                float f2 = 0.0F;
                byte b0 = 2;

                for (int l1 = -b0; l1 <= b0; ++l1) 
                {
                    for (int i2 = -b0; i2 <= b0; ++i2) 
                    {
                        Biome biome = this.biomes[j1 + 2 + (k1 + 2) * 10];
                        float baseHeight = biome.getBaseHeight();
                        float variation = biome.getHeightVariation();

                        float f5 = biomeWeights[l1 + 2 + (i2 + 2) * 5] / (baseHeight + 2.0F);
                        f += variation * f5;
                        f1 += baseHeight * f5;
                        f2 += f5;
                    }
                }

                f /= f2;
                f1 /= f2;
                f = f * 0.9F + 0.1F;
                f1 = (f1 * 4.0F - 1.0F) / 8.0F;
                double d12 = depthRegion[i1] / 8000.0D;

                if (d12 < 0.0D) 
                {
                    d12 = -d12 * 0.3D;
                }

                d12 = d12 * 3.0D - 2.0D;

                if (d12 < 0.0D) 
                {
                    d12 /= 2.0D;

                    if (d12 < -1.0D)
                        d12 = -1.0D;

                    d12 /= 1.4D;
                    d12 /= 2.0D;
                } 
                else 
                {
                    if (d12 > 1.0D)
                        d12 = 1.0D;

                    d12 /= 8.0D;
                }

                ++i1;
                double d13 = f1;
                double d14 = f;
                d13 += d12 * 0.2D;
                d13 = d13 * 8.5D / 8.0D;
                double d5 = 8.5D + d13 * 4.0D;

                for (int j2 = 0; j2 < 33; ++j2) 
                {
                    double d6 = (j2 - d5) * 12.0D * 128.0D / 256.0D / d14;

                    if (d6 < 0.0D) 
                    {
                        d6 *= 4.0D;
                    }

                    double d7 = minLimitRegion[l] / 512.0D;
                    double d8 = maxLimitRegion[l] / 512.0D;
                    double d9 = (mainNoiseRegion[l] / 10.0D + 1.0D) / 2.0D;
                    double d10 = MathHelper.clampedLerp(d7, d8, d9) - d6;

                    if (j2 > 29) 
                    {
                        double d11 = ((j2 - 29) / 3.0F);
                        d10 = d10 * (1.0D - d11) + -10.0D * d11;
                    }
                    
                	this.heightMap[l] = d10;
                    ++l;
                }
            }
        }
	}
	
	public void generate(int chunkX, int chunkZ, ChunkPrimer primer)
	{
		generateHeightMap(chunkX * 4, chunkZ * 4);
		
        for (int x4 = 0; x4 < 4; ++x4) 
        {
            int l = x4 * 5;
            int i1 = (x4 + 1) * 5;

            for (int z4 = 0; z4 < 4; ++z4) 
            {
                int k1 = (l + z4) * 33;
                int l1 = (l + z4 + 1) * 33;
                int i2 = (i1 + z4) * 33;
                int j2 = (i1 + z4 + 1) * 33;

                for (int height32 = 0; height32 < 32; ++height32) 
                {
                    double d0 = 0.125D;
                    double d1 = heightMap[k1 + height32];
                    double d2 = heightMap[l1 + height32];
                    double d3 = heightMap[i2 + height32];
                    double d4 = heightMap[j2 + height32];
                    double d5 = (heightMap[k1 + height32 + 1] - d1) * d0;
                    double d6 = (heightMap[l1 + height32 + 1] - d2) * d0;
                    double d7 = (heightMap[i2 + height32 + 1] - d3) * d0;
                    double d8 = (heightMap[j2 + height32 + 1] - d4) * d0;

                    for (int h = 0; h < 8; ++h) 
                    {
                        double d9 = 0.25D;
                        double d10 = d1;
                        double d11 = d2;
                        double d12 = (d3 - d1) * d9;
                        double d13 = (d4 - d2) * d9;
                        int height = (height32 * 8) + h;
                        
                        for (int x = 0; x < 4; ++x) 
                        {
                            double d14 = 0.25D;
                            double d16 = (d11 - d10) * d14;
                            double d15 = d10 - d16;

                            for (int z = 0; z < 4; ++z) 
                            {
                            	double d17 = d15 += d16;
                            	int bx = x4 * 4 + x;
                        		int bz = z4 * 4 + z;
                        		
                                if (d17 > 0.0D)
                                    primer.setBlockState(bx, height, bz, STONE);
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
                	((BiomeSteamWorld)biome).generateBiomeTerrainSteamWorld(world, rand, primer, x * 16 + i, z * 16 + j, this.depthBuffer[i + j * 16]);
            }
        }
    }
}
