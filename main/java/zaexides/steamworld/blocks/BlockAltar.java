package zaexides.steamworld.blocks;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import zaexides.steamworld.SteamWorld;
import zaexides.steamworld.client.rendering.tile.AltarModel;
import zaexides.steamworld.te.TileEntityAltar;

public class BlockAltar extends SteamWorldBlock implements ITileEntityProvider
{
	public BlockAltar(String name) 
	{
		super(name, Material.ANVIL, 1000.0f, SteamWorld.CREATIVETAB_UTILITY);
		setBlockUnbreakable();
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) 
	{
		return false;
	}
	
	@Override
	public boolean isFullCube(IBlockState state) 
	{
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) 
	{
		return new TileEntityAltar();
	}
	
	@Override
	public void RegisterModels() 
	{
		super.RegisterModels();
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAltar.class, new AltarModel());
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) 
	{
		TileEntity tileEntity = worldIn.getTileEntity(pos);
		if(tileEntity instanceof TileEntityAltar)
		{
			ItemStack handStack = playerIn.getHeldItem(hand);
			ItemStack altarStack = ((TileEntityAltar)tileEntity).itemStackHandler.getStackInSlot(0);
			if(altarStack != ItemStack.EMPTY)
			{
				//Altar -> Player
				if(playerIn.inventory.addItemStackToInventory(altarStack.copy()))
				{
					playerIn.playSound(SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 1.0f, 1.0f);
					((TileEntityAltar)tileEntity).itemStackHandler.setStackInSlot(0, ItemStack.EMPTY);
				}
			}
			else if(handStack != ItemStack.EMPTY && altarStack == ItemStack.EMPTY)
			{
				//Player -> Altar
				((TileEntityAltar)tileEntity).itemStackHandler.setStackInSlot(0, new ItemStack(handStack.getItem(), 1, handStack.getItemDamage()));
				ItemStack newHandStack = handStack.copy();
				newHandStack.shrink(1);
				if(newHandStack.getCount() <= 0)
					newHandStack = ItemStack.EMPTY;
				playerIn.setHeldItem(hand, newHandStack);
			}
			
			return true;
		}
		return false;
	}
}
