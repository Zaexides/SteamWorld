package zaexides.steamworld.world.structure.tower;

import java.util.Random;
import java.util.UUID;

import org.apache.logging.log4j.Level;

import com.mojang.authlib.GameProfile;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.ITemplateProcessor;
import net.minecraft.world.gen.structure.template.Template.BlockInfo;
import zaexides.steamworld.ModInfo;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.blocks.BlockObilisk;
import zaexides.steamworld.init.BlockInitializer;
import zaexides.steamworld.init.LootTableInitializer;
import zaexides.steamworld.te.TileEntityObilisk;

public class TemplateProcessorTowerFarm implements ITemplateProcessor, IInitializableProcessor
{	
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
				
		return blockInfoIn;
	}
}
