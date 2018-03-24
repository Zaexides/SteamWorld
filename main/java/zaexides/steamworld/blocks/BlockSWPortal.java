package zaexides.steamworld.blocks;

import java.util.Random;

import org.apache.logging.log4j.Level;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import zaexides.steamworld.ConfigHandler;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.utility.SteamWorldTeleporter;

public class BlockSWPortal extends SteamWorldBlock
{
	public static final float POSITION_MULTIPLIER = 4;
	public static final float REVERSED_POSITION_MULTIPLIER = 1 / POSITION_MULTIPLIER;
	
	public BlockSWPortal()
	{
		super("portal", Material.PORTAL, 100);
	}
	
	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) 
	{
		if(worldIn.isRemote)
			return;
		
		int lastDimension = entityIn.getEntityWorld().provider.getDimension();
		int targetDimension = ConfigHandler.dimensionId;
		BlockPos spawnPos = new BlockPos(16, 80, 16);
		
		if(lastDimension == targetDimension)
		{
			targetDimension = 0;
			spawnPos = new BlockPos(pos.getX() * REVERSED_POSITION_MULTIPLIER, pos.getY(), pos.getZ() * REVERSED_POSITION_MULTIPLIER);
		}
		World targetWorld = worldIn.getMinecraftServer().getWorld(targetDimension);
		
		if(entityIn instanceof EntityPlayer)
			worldIn.getMinecraftServer().getPlayerList().transferPlayerToDimension((EntityPlayerMP)entityIn, targetDimension, new SteamWorldTeleporter((WorldServer)targetWorld, spawnPos, true));
		else
			worldIn.getMinecraftServer().getPlayerList().transferEntityToWorld(entityIn, lastDimension, (WorldServer)worldIn, (WorldServer)targetWorld, new SteamWorldTeleporter((WorldServer)targetWorld, spawnPos, true));
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) 
	{
		return NULL_AABB;
	}
}
