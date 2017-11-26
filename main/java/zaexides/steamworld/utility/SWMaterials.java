package zaexides.steamworld.utility;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class SWMaterials 
{
	public static final Material ENDRITCH = new Material(MapColor.MAGENTA)
			{
				@Override
				protected Material setRequiresTool() { return super.setRequiresTool(); };
			};
}
