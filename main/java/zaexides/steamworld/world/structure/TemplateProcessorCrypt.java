package zaexides.steamworld.world.structure;

import com.mojang.authlib.GameProfile;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.ITemplateProcessor;
import net.minecraft.world.gen.structure.template.Template.BlockInfo;
import zaexides.steamworld.ModInfo;
import zaexides.steamworld.entity.EntityAnciteGolem;
import zaexides.steamworld.init.BlockInitializer;
import zaexides.steamworld.init.LootTableInitializer;
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
				EnumFacing facing = blockInfoIn.blockState.getValue(BlockChest.FACING);
				SpawnGolem(worldIn, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, facing);
				
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
	
	public void SpawnGolem(World worldIn, double x, double y, double z, EnumFacing facing)
	{
		EntityAnciteGolem anciteGolem = new EntityAnciteGolem(worldIn);
		anciteGolem.setPositionAndRotation(x, y, z, facing.getHorizontalAngle(), 0);
		worldIn.spawnEntity(anciteGolem);
	}
}
