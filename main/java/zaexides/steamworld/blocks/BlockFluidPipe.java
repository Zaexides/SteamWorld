package zaexides.steamworld.blocks;

import org.apache.logging.log4j.Level;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zaexides.steamworld.ModInfo;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.init.BlockInitializer;
import zaexides.steamworld.init.ItemInitializer;
import zaexides.steamworld.models.PipeBakedModel;
import zaexides.steamworld.te.TileEntityPipe;
import zaexides.steamworld.utility.UnlistedPropertyCanConnect;
import zaexides.steamworld.utility.interfaces.IModeledObject;

public class BlockFluidPipe extends Block implements IModeledObject, ITileEntityProvider
{
	public static final UnlistedPropertyCanConnect NORTH = new UnlistedPropertyCanConnect("north");
	public static final UnlistedPropertyCanConnect SOUTH = new UnlistedPropertyCanConnect("south");
	public static final UnlistedPropertyCanConnect WEST = new UnlistedPropertyCanConnect("west");
	public static final UnlistedPropertyCanConnect EAST = new UnlistedPropertyCanConnect("east");
	public static final UnlistedPropertyCanConnect UP = new UnlistedPropertyCanConnect("up");
	public static final UnlistedPropertyCanConnect DOWN = new UnlistedPropertyCanConnect("down");
	
	public static final AxisAlignedBB[] AXIS_ALIGNED_BBS_ADDITIONS = new AxisAlignedBB[]
			{
					new AxisAlignedBB(0.0D, 0.0D, -0.32D, 0.0D, 0.0D, 0.0D), //North
					new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.32D), //South
					new AxisAlignedBB(-0.32D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D), //West
					new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.32D, 0.0D, 0.0D), //East
					new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.0D, 0.32D, 0.0D), //Up
					new AxisAlignedBB(0.0D, -0.32D, 0.0D, 0.0D, 0.0D, 0.0D), //Down
			};
	
	private static final AxisAlignedBB AABB = new AxisAlignedBB(0.32D, 0.32D, 0.32D, 0.68D, 0.68D, 0.68D);
	
	public BlockFluidPipe() 
	{
		super(Material.IRON);
		setUnlocalizedName(ModInfo.MODID + ".block_pipe");
		setRegistryName("block_pipe");
		setCreativeTab(SteamWorld.CREATIVETAB_UTILITY);
		setHardness(2.0f);
		
		BlockInitializer.BLOCKS.add(this);
		ItemInitializer.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void RegisterModels() 
	{
		SteamWorld.proxy.RegisterItemRenderers(Item.getItemFromBlock(this), 0,
				"inventory",
				"item_block_pipe"
				);
		StateMapperBase ignoreState = new StateMapperBase() 
		{
			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState state) 
			{
				return PipeBakedModel.BAKED_MODEL;
			}
		};
		ModelLoader.setCustomStateMapper(this, ignoreState);
	}
	
	@Override
	public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) 
	{
		return false;
	}
	
	@Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }
    
	/**
     * Used to determine ambient occlusion and culling when rebuilding chunks for render
     */
	@Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }
	
	@Override
	protected BlockStateContainer createBlockState() 
	{
		IProperty[] properties = new IProperty[0];
		IUnlistedProperty[] unlistedProperties = new IUnlistedProperty[] { NORTH, SOUTH, WEST, EAST, UP, DOWN };
		return new ExtendedBlockState(this, properties, unlistedProperties);
	}
	
	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		IExtendedBlockState extendedBlockState = (IExtendedBlockState) state;
		
		boolean n = canConnectTo(world, pos.north(), EnumFacing.SOUTH);
		boolean s = canConnectTo(world, pos.south(), EnumFacing.NORTH);
		boolean w = canConnectTo(world, pos.west(), EnumFacing.EAST);
		boolean e = canConnectTo(world, pos.east(), EnumFacing.WEST);
		boolean u = canConnectTo(world, pos.up(), EnumFacing.DOWN);
		boolean d = canConnectTo(world, pos.down(), EnumFacing.UP);
		
		return extendedBlockState
				.withProperty(NORTH, n)
				.withProperty(SOUTH, s)
				.withProperty(WEST, w)
				.withProperty(EAST, e)
				.withProperty(UP, u)
				.withProperty(DOWN, d);
	}
	
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
		AxisAlignedBB ret_AABB = AABB;
		
		IExtendedBlockState extendedBlockState = (IExtendedBlockState) getExtendedState(state, source, pos);
		
		if(extendedBlockState.getValue(NORTH))
			ret_AABB = addToAABB(ret_AABB, AXIS_ALIGNED_BBS_ADDITIONS[0]);
		if(extendedBlockState.getValue(SOUTH))
			ret_AABB = addToAABB(ret_AABB, AXIS_ALIGNED_BBS_ADDITIONS[1]);
		if(extendedBlockState.getValue(WEST))
			ret_AABB = addToAABB(ret_AABB, AXIS_ALIGNED_BBS_ADDITIONS[2]);
		if(extendedBlockState.getValue(EAST))
			ret_AABB = addToAABB(ret_AABB, AXIS_ALIGNED_BBS_ADDITIONS[3]);
		if(extendedBlockState.getValue(UP))
			ret_AABB = addToAABB(ret_AABB, AXIS_ALIGNED_BBS_ADDITIONS[4]);
		if(extendedBlockState.getValue(DOWN))
			ret_AABB = addToAABB(ret_AABB, AXIS_ALIGNED_BBS_ADDITIONS[5]);
		
        return ret_AABB;
    }
	
	private AxisAlignedBB addToAABB(AxisAlignedBB original, AxisAlignedBB addition)
	{
		return new AxisAlignedBB(
				original.minX + addition.minX,
				original.minY + addition.minY,
				original.minZ + addition.minZ,
				original.maxX + addition.maxX,
				original.maxY + addition.maxY,
				original.maxZ + addition.maxZ
				);
	}
	
	private boolean canConnectTo(IBlockAccess world, BlockPos pos, EnumFacing facing)
	{
		IBlockState state = world.getBlockState(pos);
		if(state.getBlock() == this)
			return true;

		TileEntity tEntity = world.getTileEntity(pos);
		if(tEntity != null && tEntity.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing))
			return true;
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) 
	{
		return new TileEntityPipe();
	}
}
