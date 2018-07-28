package zaexides.steamworld.world.structure.tower;

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
import zaexides.steamworld.ModInfo;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.blocks.BlockObilisk;
import zaexides.steamworld.init.BlockInitializer;
import zaexides.steamworld.init.LootTableInitializer;
import zaexides.steamworld.te.TileEntityObilisk;

public class TemplateProcessorTowerLibrary implements ITemplateProcessor
{	
	@Override
	public BlockInfo processBlock(World worldIn, BlockPos pos, BlockInfo blockInfoIn) 
	{
		if(blockInfoIn.blockState.getBlock() == Blocks.BOOKSHELF)
		{
			if(worldIn.rand.nextFloat() < 0.005f)
			{
				EnumFacing facing = GetCorrectFace(worldIn, pos);
				IBlockState blockState = Blocks.CHEST.getDefaultState().withProperty(BlockChest.FACING, facing);
				worldIn.setBlockState(pos, blockState);
				TileEntity tileEntity = worldIn.getTileEntity(pos);
				if(tileEntity != null && tileEntity instanceof TileEntityLockableLoot)
				{
					((TileEntityLockableLoot)tileEntity).setLootTable(LootTableInitializer.TOWER_LIBRARY, worldIn.rand.nextLong());
					blockInfoIn = new BlockInfo(pos, blockState, tileEntity.serializeNBT());
				}
			}
		}
		
		return blockInfoIn;
	}
	
	private EnumFacing GetCorrectFace(World world, BlockPos pos)
	{
		for(int i = 2; i < 6; i++)
		{
			if(world.isAirBlock(pos.add(EnumFacing.VALUES[i].getDirectionVec())))
				return EnumFacing.VALUES[i];
		}
		return EnumFacing.EAST;
	}
}
