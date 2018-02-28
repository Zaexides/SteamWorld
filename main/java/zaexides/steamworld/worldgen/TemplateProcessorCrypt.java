package zaexides.steamworld.worldgen;

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
import zaexides.steamworld.LootTableInitializer;
import zaexides.steamworld.ModInfo;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.blocks.BlockInitializer;
import zaexides.steamworld.blocks.BlockObilisk;
import zaexides.steamworld.te.TileEntityObilisk;

public class TemplateProcessorCrypt implements ITemplateProcessor
{
	private static final float CUSTOM_HEAD_CHANCE = 0.0005f;
	
	@Override
	public BlockInfo processBlock(World worldIn, BlockPos pos, BlockInfo blockInfoIn) 
	{
		Block block = blockInfoIn.blockState.getBlock();
		
		if(block == Blocks.CHEST)
		{
			TileEntity tileEntity = worldIn.getTileEntity(pos);
			if(tileEntity != null && tileEntity instanceof TileEntityLockableLoot)
			{
				((TileEntityLockableLoot)tileEntity).setLootTable(LootTableInitializer.ANCITE_CRYPT, worldIn.rand.nextLong());
				
				int y = 250;
				BlockPos obiliskPos = new BlockPos(pos.getX(), y, pos.getZ());
				while(y > 0 && !BlockInitializer.OBILISK.canGenerateOn(worldIn.getBlockState(obiliskPos).getBlock(), worldIn, obiliskPos))
				{
					y--;
					obiliskPos = new BlockPos(pos.getX(), y, pos.getZ());
				}
				
				worldIn.setBlockState(obiliskPos.up(), BlockInitializer.OBILISK.getStateFromMeta(0));
				
				TileEntityObilisk.instantiateObiliskRandom(worldIn);
				TileEntity obiliskTileEntity = worldIn.getTileEntity(obiliskPos.up());
				if(obiliskTileEntity != null && obiliskTileEntity instanceof TileEntityObilisk)
					((TileEntityObilisk)obiliskTileEntity).generateText();
				
				return new BlockInfo(pos, blockInfoIn.blockState, tileEntity.serializeNBT());
			}
		}
		if(block == Blocks.SKULL && worldIn.rand.nextFloat() <= CUSTOM_HEAD_CHANCE)
		{
			TileEntity tileEntity = worldIn.getTileEntity(pos);
			if(tileEntity == null)
			{
				tileEntity = new TileEntitySkull();
				worldIn.setTileEntity(pos, tileEntity);
			}
			
			if(tileEntity instanceof TileEntitySkull)
			{
				((TileEntitySkull)tileEntity).setType(3);
				((TileEntitySkull)tileEntity).setPlayerProfile(new GameProfile(null, ModInfo.CREDIT_NAMES[worldIn.rand.nextInt(ModInfo.CREDIT_NAMES.length)]));
				return new BlockInfo(pos, blockInfoIn.blockState, tileEntity.serializeNBT());
			}
		}
		
		return blockInfoIn;
	}
}
