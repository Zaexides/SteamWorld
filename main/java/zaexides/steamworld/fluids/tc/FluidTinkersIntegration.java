package zaexides.steamworld.fluids.tc;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

public class FluidTinkersIntegration extends Fluid
{
	private int color;
	
	private static final ResourceLocation STILL_TEXTURE = new ResourceLocation("tconstruct", "blocks/fluids/molten_metal");
	private static final ResourceLocation FLOW_TEXTURE = new ResourceLocation("tconstruct", "blocks/fluids/molten_metal_flow");
	
	public FluidTinkersIntegration(String fluidName, int color, ResourceLocation stillTex, ResourceLocation flowTex)
	{
		super(fluidName, stillTex, flowTex);
		this.color = color;
	}
	
	public FluidTinkersIntegration(String fluidName, int color) 
	{
		this(fluidName, color, STILL_TEXTURE, FLOW_TEXTURE);
	}

	@Override
	public int getColor() 
	{
		return color;
	}
}
