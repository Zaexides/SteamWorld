package zaexides.steamworld.utility;

import net.minecraftforge.common.property.IUnlistedProperty;

public class UnlistedPropertyCanConnect implements IUnlistedProperty<Boolean>
{
	private final String name;
	
	public UnlistedPropertyCanConnect(String name) 
	{
		this.name = name;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean isValid(Boolean value) {
		return true;
	}

	@Override
	public Class<Boolean> getType() {
		return Boolean.class;
	}

	@Override
	public String valueToString(Boolean value) {
		return value.toString();
	}
	
}
