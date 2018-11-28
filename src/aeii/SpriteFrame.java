package aeii;

// Decompiled by:       Fernflower v0.6
// Date:                22.01.2010 19:41:58
// Copyright:           2008-2009, Stiver
// Home page:           http://www.reversed-java.com

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public final class SpriteFrame
{

	public Image image;
	private boolean useTransform = false;
	private int frameX;
	private int frameY;
	public int imgwidth;
	public int imgheight;
	private int translateX;
	private int translateY;
	public int transform = 0;


	public SpriteFrame(SpriteFrame var1, int var2, int var3, int var4, int var5)
	{
		image = var1.image;
		imgwidth = var4;
		imgheight = var5;
		frameX = var2 * var4 + var1.frameX;
		frameY = var3 * var5 + var1.frameY;
		useTransform = true;
	}

	public SpriteFrame(SpriteFrame var1, int var2)
	{
		image = var1.image;
		imgwidth = var1.imgwidth;
		imgheight = var1.imgheight;
		frameX = var1.frameX;
		frameY = var1.frameY;
		translateX = var1.translateX;
		translateY = var1.translateY;
		useTransform = var1.useTransform;

		if((var2 & 1) != 0)
		{
			transform = 2;
		}
		else if((var2 & 2) != 0)
		{
			transform = 1;
		}
		else if((var2 & 4) != 0)
		{
			transform = 6;
		}
		else if((var2 & 8) != 0)
		{
			transform = 3;
		}
		else if((var2 & 16) != 0)
		{
			transform = 5;
		}
	}

	public SpriteFrame(String var1)
	{
		byte[] var2 = Renderer.getResource(var1 + ".png");
		image = Image.createImage(var2, 0, var2.length);
		imgwidth = (short)image.getWidth();
		imgheight = (short)image.getHeight();
		useTransform = false;
	}

	public final void align(int mode, int x, int y)
	{
		if(!useTransform)
		{
			int var4 = mode & 13;
			int var5 = mode & 50;
			
			if(transform == 2)
			{
				if((var4 & 4) != 0)
				{
					var4 = 8;
				}
				else if((var4 & 8) != 0)
				{
					var4 = 4;
				}
			}
			else if(transform == 1)
			{
				if((var5 & 16) != 0)
				{
					var4 = 32;
				}
				else if((var4 & 32) != 0)
				{
					var4 = 16;
				}
			}
			
			mode = var4 | var5;
			
			if((mode & 8) != 0)
			{
				translateX = x - imgwidth;
			}
			else if((mode & 1) != 0)
			{
				translateX = x - imgwidth >> 1;
			}

			if((mode & 32) != 0)
			{
				translateY = y - imgheight;
			}
			else if((mode & 2) != 0)
			{
				translateY = y - imgheight >> 1;
			}
		}
	}

	public final void translate(int var1, int var2)
	{
		translateX += var1;
		translateY += var2;
	}

	public final void paint(Graphics var1, int var2, int var3)
	{
		paintEx(var1, var2, var3, 20);
	}

	public final void paintEx(Graphics var1, int var2, int var3, int var4)
	{
		if(!useTransform && transform == 0)
		{
			var1.drawImage(image, var2 + translateX, var3 + translateY, var4);
		}
		else
		{
			var1.drawRegion(image, frameX, frameY, imgwidth, imgheight, transform, var2 + translateX, var3 + translateY, var4);
		}
	}

	public final Image getFrame()
	{
		return Image.createImage(image, frameX, frameY, imgwidth, imgheight, transform);
	}

	public SpriteFrame(String var1, int var2)
	{
		byte[] var3 = Renderer.getResource(var1 + ".png");
		if(var2 != 1)
		{
			byte[] var4 = new byte[var3.length];
			System.arraycopy(var3, 0, var4, 0, var3.length);
			replacePaletteColor(var4, var2);
			var3 = var4;
		}

		image = Image.createImage(var3, 0, var3.length);
		imgwidth = (short)image.getWidth();
		imgheight = (short)image.getHeight();
	}

	public static final void replacePaletteColor(byte[] var0, int var1)
	{
		try
		{
			int var2 = 33;
			int pltStartIndex = 0;

			int pltLength;
			for(pltLength = var0.length - 3; pltStartIndex < pltLength; ++pltStartIndex)
			{
				if(var0[pltStartIndex] == 80 && var0[pltStartIndex + 1] == 76 && var0[pltStartIndex + 2] == 84)
				{
					var2 = pltStartIndex - 4;
					break;
				}
			}

			pltLength = ((var0[var2] & 255) << 24 | (var0[var2 + 1] & 255) << 16 | (var0[var2 + 2] & 255) << 8 | var0[var2 + 3] & 255) & -1;
			pltStartIndex = var2 + 4;
			int var5 = -1;

			for(int var6 = 0; var6 < 4; ++var6)
			{
				var5 = updateCRC(var0[pltStartIndex + var6], var5);
			}

			pltStartIndex += 4;

			int pltIndex;

			for(pltIndex = pltStartIndex; pltIndex < pltStartIndex + pltLength; pltIndex += 3)
			{
				int cr = var0[pltIndex] & 255;
				int cg = var0[pltIndex + 1] & 255;
				int cb = var0[pltIndex + 2] & 255;

				if(var1 == 0)
				{
					if(cr != 244 || cg != 244 || cb != 230)
					{
						int var13;
						cr = var13 = (cr + cg + cb) / 3;
						var0[pltIndex] = (byte)cr;
						var0[pltIndex + 1] = (byte)var13;
						var0[pltIndex + 2] = (byte)var13;
					}
				}
				else if(var1 != 1)
				{
					int[][] repSourceColors = MainDisplayable.PALETTE_REPLACE_TABLE[1];
					int[][] repDestColors = MainDisplayable.PALETTE_REPLACE_TABLE[var1];

					for(int repColorIndex = 0; repColorIndex < repSourceColors.length; ++repColorIndex)
					{
						if(repSourceColors[repColorIndex][0] == cr && repSourceColors[repColorIndex][1] == cg && repSourceColors[repColorIndex][2] == cb)
						{
							var0[pltIndex] = (byte)repDestColors[repColorIndex][0];
							var0[pltIndex + 1] = (byte)repDestColors[repColorIndex][1];
							var0[pltIndex + 2] = (byte)repDestColors[repColorIndex][2];
							break;
						}
					}
				}

				var5 = updateCRC(var0[pltIndex], var5);
				var5 = updateCRC(var0[pltIndex + 1], var5);
				var5 = updateCRC(var0[pltIndex + 2], var5);
			}

			var5 = ~var5;
			pltIndex = var2 + 8 + pltLength;
			var0[pltIndex] = (byte)(var5 >> 24);
			var0[pltIndex + 1] = (byte)(var5 >> 16);
			var0[pltIndex + 2] = (byte)(var5 >> 8);
			var0[pltIndex + 3] = (byte)var5;
		}
		catch(Exception var16)
		{
			var16.printStackTrace();
		}
	}

	public static final int updateCRC(byte var0, int var1)
	{
		int var2 = var0 & 255;
		var1 ^= var2;

		for(int var3 = 0; var3 < 8; ++var3)
		{
			if((var1 & 1) != 0)
			{
				var1 = var1 >>> 1 ^ -306674912;
			}
			else
			{
				var1 >>>= 1;
			}
		}

		return var1;
	}
}
