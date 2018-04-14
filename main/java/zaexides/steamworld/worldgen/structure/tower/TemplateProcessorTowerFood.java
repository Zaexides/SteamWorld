package zaexides.steamworld.worldgen.structure.tower;

import java.util.Random;
import java.util.UUID;

import org.apache.logging.log4j.Level;

import com.mojang.authlib.GameProfile;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.ITemplateProcessor;
import net.minecraft.world.gen.structure.template.Template.BlockInfo;
import zaexides.steamworld.LootTableInitializer;
import zaexides.steamworld.ModInfo;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.blocks.BlockObilisk;
import zaexides.steamworld.init.BlockInitializer;
import zaexides.steamworld.te.TileEntityObilisk;

public class TemplateProcessorTowerFood implements ITemplateProcessor
{	
	@Override
	public BlockInfo processBlock(World worldIn, BlockPos pos, BlockInfo blockInfoIn) 
	{
		if(blockInfoIn.blockState.getBlock() == Blocks.HAY_BLOCK)
		{
			int blockType = worldIn.rand.nextInt(17);
			IBlockState blockState;
			
			NBTTagCompound compound = null;
			
			if(blockType < 8)
				blockState = Blocks.HAY_BLOCK.getDefaultState().withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.values()[worldIn.rand.nextInt(EnumFacing.Axis.values().length)]);
			else if(blockType < 12)
				blockState = Blocks.MELON_BLOCK.getDefaultState();
			else if (blockType < 16)
				blockState = Blocks.PUMPKIN.getDefaultState().withProperty(BlockHorizontal.FACING, EnumFacing.values()[worldIn.rand.nextInt(4) + 2]);
			else
			{
				blockState = Blocks.CHEST.getDefaultState().withProperty(BlockChest.FACING, EnumFacing.values()[worldIn.rand.nextInt(4) + 2]);
				worldIn.setBlockState(pos, blockState);
				TileEntity tileEntity = worldIn.getTileEntity(pos);
				if(tileEntity != null && tileEntity instanceof TileEntityLockableLoot)
				{
					((TileEntityLockableLoot)tileEntity).setLootTable(LootTableInitializer.TOWER_FOOD, worldIn.rand.nextLong());
					compound = tileEntity.serializeNBT();
				}
			}
			
			blockInfoIn = new BlockInfo(pos, blockState, compound);
		}
		
		return blockInfoIn;
	}
}
