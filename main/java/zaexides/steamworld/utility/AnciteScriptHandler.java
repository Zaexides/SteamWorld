package zaexides.steamworld.utility;

public class AnciteScriptHandler 
{
	public static final char[] CHAR_TABLE =
		{
			'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', '-',
			'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', '.',
			'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', '.',
			'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', '.',
			'W', 'X', 'Y', 'Z', '1', '2', '3', '4', '5', '6', '7', '8', '.',
			'9', '0', '!', '.', '.', '.', '@', '#', '$', '.', '.', '.', '.',
			'%', '^', '&', '*', '(', ')', '-', '=', '_', '+', '/', '[', '.',
			']', '{', '}', ':', ',', '<', '>', '?', '|', '`', '\\', '~', '.'
		};
	
	public static int getIdFromChar(char c)
	{
		for(int i = 0; i < CHAR_TABLE.length; i++)
		{
			if(CHAR_TABLE[i] == c)
				return i;
		}
		return -1;
	}
}
