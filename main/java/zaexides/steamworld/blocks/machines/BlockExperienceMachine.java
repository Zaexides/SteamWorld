package zaexides.steamworld.blocks.machines;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.gui.GuiHandler;
import zaexides.steamworld.te.TileEntityExperienceMachine;

public class BlockExperienceMachine extends BlockMachine implements ITileEntityProvider
{
	public int efficiency = 1, efficiencyHT = 3;
	
	public BlockExperienceMachine(String name, float hardness, int efficiency, int efficiencyHT) 
	{
		super(name, Material.IRON, hardness);
		this.efficiency = efficiency;
		this.efficiencyHT = efficiencyHT;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) 
	{
		TileEntityExperienceMachine experienceMachine = new TileEntityExperienceMachine();
		setMachineStats(experienceMachine, IsHighTier(meta));
		return experienceMachine;
	}
	
	@Override
	public void setMachineStats(TileEntity tileEntity, boolean highTier) 
	{
		((TileEntityExperienceMachine)tileEntity).SetStats(highTier ? efficiencyHT : efficiency);
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) 
	{
		if(!worldIn.isRemote)
		{
			TileEntity tileEntity = worldIn.getTileEntity(pos);
			if(!(tileEntity instanceof TileEntityExperienceMachine))
				return false;
			playerIn.openGui(SteamWorld.singleton, GuiHandler.EXPERIENCE_MACHINE, worldIn, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
	}
}
