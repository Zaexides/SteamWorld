package zaexides.steamworld.world.structure.tower;

import java.util.Random;
import java.util.UUID;

import org.apache.logging.log4j.Level;

import com.mojang.authlib.GameProfile;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCarpet;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockFlower.EnumFlowerType;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBed;
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
import zaexides.steamworld.blocks.machines.BlockMachine;
import zaexides.steamworld.init.BlockInitializer;
import zaexides.steamworld.init.LootTableInitializer;
import zaexides.steamworld.te.TileEntityObilisk;

public class TemplateProcessorTowerResidence implements ITemplateProcessor, IInitializableProcessor
{	
	private IBlockState carpet;
	private EnumDyeColor bedColor;
	
	@Override
	public void Init(Random rand) 
	{
		carpet = Blocks.CARPET.getDefaultState().withProperty(BlockCarpet.COLOR, EnumDyeColor.values()[rand.nextInt(EnumDyeColor.values().length)]);
		bedColor = EnumDyeColor.values()[rand.nextInt(EnumDyeColor.values().length)];
	}
	
	@Override
	public BlockInfo processBlock(World worldIn, BlockPos pos, BlockInfo blockInfoIn) 
	{
		if(blockInfoIn.blockState.getBlock() == Blocks.CHEST)
		{
			TileEntity tileEntity = worldIn.getTileEntity(pos);
			if(tileEntity != null && tileEntity instanceof TileEntityLockableLoot)
			{
				((TileEntityLockableLoot)tileEntity).setLootTable(LootTableInitializer.TOWER_RESIDENCE, worldIn.rand.nextLong());
				blockInfoIn = new BlockInfo(pos, blockInfoIn.blockState, tileEntity.serializeNBT());
			}
		}
		else if(blockInfoIn.blockState.getBlock() == Blocks.CARPET)
		{
			blockInfoIn = new BlockInfo(pos, carpet, blockInfoIn.tileentityData);
		}
		else if(blockInfoIn.blockState.getBlock() instanceof BlockFlower)
		{
			EnumFlowerType flowerType = worldIn.getBiomeForCoordsBody(pos).pickRandomFlower(worldIn.rand, pos);
			BlockFlower blockFlower = flowerType.getBlockType().getBlock();
			IBlockState blockState = blockFlower.getDefaultState().withProperty(blockFlower.getTypeProperty(), flowerType);
			blockInfoIn = new BlockInfo(pos, blockState, blockInfoIn.tileentityData);
		}
		else if(blockInfoIn.blockState.getBlock() == BlockInitializer.FURNACE_STEAITE)
		{
			EnumFacing facing = GetCorrectFace(worldIn, pos);
			IBlockState blockState = BlockInitializer.FURNACE_STEAITE.getDefaultState().withProperty(BlockMachine.FACING, facing).withProperty(BlockMachine.ACTIVE, false);
			blockInfoIn = new BlockInfo(pos, blockState, blockInfoIn.tileentityData);
		}
		else if(blockInfoIn.blockState.getBlock() == Blocks.BED)
		{
			TileEntity tileEntity = worldIn.getTileEntity(pos);
			if(tileEntity != null && tileEntity instanceof TileEntityBed)
			{
				((TileEntityBed)tileEntity).setColor(bedColor);
				blockInfoIn = new BlockInfo(pos, blockInfoIn.blockState, tileEntity.serializeNBT());
			}
		}
		
		return blockInfoIn;
	}
	
	private EnumFacing GetCorrectFace(World world, BlockPos pos)
	{
		for(int i = 2; i < 6; i++)
		{
			BlockPos checkingPos = pos.add(EnumFacing.VALUES[i].getDirectionVec());
			if(world.isAirBlock(checkingPos) || world.getBlockState(checkingPos).getBlock() == Blocks.CARPET)
				return EnumFacing.VALUES[i];
		}
		return EnumFacing.EAST;
	}
}
