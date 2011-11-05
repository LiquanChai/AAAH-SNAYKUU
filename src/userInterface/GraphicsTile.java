package userInterface;

import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.Image;
import java.net.URL;
import gameLogic.Direction;
import gameLogic.Position;

enum GraphicsTile
{
	SNAKEHEAD("snake_head.png"),
	SNAKETAIL("snake_tail.png"),
	SNAKEBODY("snake_body.png"),
	SNAKELEFT("snake_left.png"),
	SNAKERIGHT("snake_right.png"),
	SNAKEMONAD("snake_monad.png"),
	FRUIT("fruit.png"),
	WALL("wall.png");
	
	private Image image;
	private float imgHeight;
	private float imgWidth;
	
	GraphicsTile(String s)
	{
		try
		{
			URL temp = getClass().getResource("/img/"+s);
			image = ImageIO.read(temp);
			imgHeight = image.getHeight(null);
			imgWidth = image.getWidth(null);
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			
		}
	}
	
	Image getImage()
	{
		return image;
		
	}
	
	AffineTransform getTransformation(Direction dir, Position pos, int pixelsPerXUnit, int pixelsPerYUnit)
	{
		
		//~ See the java6 API spec for AffineTransform for details on syntax.
		
		float[] flatmatrix = {0.0f, 0.0f, 0.0f, 0.0f, (float)(pos.getX()), (float)(pos.getY())};
		float[] translationCorrector = {0, 0};
		float[] scaleAdjuster = {0, 0};
		
		
		//~ Rotate around the image's own center and set a 
		//  few variables for later fine-tuning.
		if(dir != null)
		{
			switch(dir)
			{
				case NORTH:
					flatmatrix[1] += -1;
					flatmatrix[2] += 1;
					flatmatrix[5] += 1;
					
					scaleAdjuster[1] = 1;
					break;
					
				case WEST:
					flatmatrix[0] += -1;
					flatmatrix[3] += -1;
					flatmatrix[4] += 1;
					flatmatrix[5] += 1;
					
					translationCorrector[1] = -1;
					
					scaleAdjuster[0] = 1;
					break;
					
				case SOUTH:
					flatmatrix[1] += 1;
					flatmatrix[2] += -1;
					flatmatrix[4] += 1;
					
					translationCorrector[0] = -1;
					translationCorrector[1] = -1;
					
					scaleAdjuster[1] = 1;
					break;
					
				default:
					flatmatrix[0] += 1;
					flatmatrix[3] += 1;
					
					translationCorrector[0] = -1;
					
					scaleAdjuster[0] = 1;
					break;
					
			}
		}
		else
		{
			flatmatrix[0] += 1;
			flatmatrix[3] += 1;
		}
		
		//~ Scale the image according to current window size.
		for(int i = 0; i < 4; ++i)
		{
			if(i%2 == 0)
			{
				flatmatrix[i] = flatmatrix[i]*((pixelsPerXUnit+scaleAdjuster[0])/imgWidth);
			}
			else
			{
				flatmatrix[i] = flatmatrix[i]*((pixelsPerYUnit+scaleAdjuster[1])/imgHeight);
			}
		}
		
		//~ Translate to the correct point.
		flatmatrix[4] = 1+flatmatrix[4]+flatmatrix[4]*pixelsPerXUnit;
		flatmatrix[5] = 1+flatmatrix[5]+flatmatrix[5]*pixelsPerYUnit;
		
		
		//~ Adjust for rotational positioning artifacts.
		flatmatrix[4] += translationCorrector[0];
		flatmatrix[5] += translationCorrector[1];
		
		return new AffineTransform(flatmatrix);
		
	}
}
