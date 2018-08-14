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
import net.minecraft.block.material.Material;
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
import zaexides.steamworld.entity.villangler.EntityVillangler;
import zaexides.steamworld.entity.villangler.EntityVillangler.VillanglerVariant;
import zaexides.steamworld.init.BlockInitializer;
import zaexides.steamworld.init.LootTableInitializer;
import zaexides.steamworld.te.TileEntityObilisk;
import zaexides.steamworld.utility.interfaces.IResettable;

public class TemplateProcessorTowerAnimal implements ITemplateProcessor, IResettable
{	
	private static final float BREEDER_CHANCE = 0.04f;
	private static final int MAX_BREEDERS = 1;
	private int currentBreederCount = 0;
	
	@Override
	public BlockInfo processBlock(World worldIn, BlockPos pos, BlockInfo blockInfoIn) 
	{
		if(blockInfoIn.blockState.getMaterial() == Material.AIR && !worldIn.canSeeSky(pos))
		{
			if(currentBreederCount < MAX_BREEDERS && worldIn.rand.nextFloat() < BREEDER_CHANCE)
			{
				EntityVillangler villangler = new EntityVillangler(worldIn, VillanglerVariant.BREEDER);
				villangler.setPositionAndRotation(pos.getX(), pos.getY(), pos.getZ(), 0, 0);
				if(villangler.isNotColliding())
				{
					worldIn.spawnEntity(villangler);
					currentBreederCount++;
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
		currentBreederCount = 0;
	}
}
