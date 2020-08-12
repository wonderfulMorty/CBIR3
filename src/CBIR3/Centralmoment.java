package CBIR3;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.imageio.ImageIO;


public class Centralmoment {
	
	public static int toAlpha( int rgb ){
		int temp	=	0xff000000;
		rgb	=	rgb & temp;
		return (rgb >> 24);
	}
	
	public static int toRed( int rgb ){
		return (( rgb >> 16 ) & 0xff);
	}
	
	public static int toGreen( int rgb ){
		return(( rgb >> 8 ) & 0xff);
	}
	
	public static int toBlue( int rgb ){
		return (rgb & 0xff);
	}
	
	public static double[] toHSV( BufferedImage img ){//RGB坐标系转为HSV坐标系，并取得图像的颜色矩值
		double temp[]=new double[9];
		int width;
		int height;
		double vHSV[][];
		double hHSV[][];
		double sHSV[][];
		int rgb;
		int red;
		int green;
		int blue;
		int	i,j;
		width=img.getWidth();
		height=img.getHeight();
		vHSV = new double[width][height];
		hHSV = new double[width][height];
		sHSV = new double[width][height];
		for( i = 0 ; i < width ; i++ ){
			for( j = 0 ; j < height ; j++ ){
				
				rgb= img.getRGB(i, j) ;
				red=toRed(rgb);
				green=toGreen(rgb);
				blue=toBlue(rgb);
				
				vHSV[i][j]	=	( red + green + blue ) / 3.0;
				if( red == green && green == blue ){
					hHSV[i][j]	=	0;
					sHSV[i][j]	=	0;
				}
				else if( red > blue && green >= blue ){
					hHSV[i][j]	=	( green - blue ) / ( (vHSV[i][j] - blue) * 3.0 );
					sHSV[i][j]	=	1 - blue / vHSV[i][j];
				}
				else if( green > red && blue >= red ){
					hHSV[i][j]	=	( blue - red ) / ( (vHSV[i][j] - red) * 3.0 )	+	1;
					sHSV[i][j]	=	1 - red / vHSV[i][j];
				}
				else if( blue > green && red >= green ){
					hHSV[i][j]	=	( red - green ) / ( (vHSV[i][j] - green) * 3.0 )	+	2;
					sHSV[i][j]	=	1 - green / vHSV[i][j];
				}
				
			}
		}
		
		int 	k;
		double	sumh , sums , sumv;
		double[] colorJuH =	new double[4];
		double[] colorJuS =	new double[4];
		double[] colorJuV =	new double[4];
		
		colorJuH[1]	=	0.0;
		colorJuS[1]	=	0.0;
		colorJuV[1]	=	0.0;
		
		for( k = 1 ; k <= 3 ; k++ ){
			sumh = 0.0;
			sums = 0.0;
			sumv = 0.0;
			for( i = 0 ; i < width ; i++ ){
				for( j = 0 ; j < height ; j++ ){

					sumh += Math.pow(	( hHSV[i][j] - colorJuH[1] )	,	k	);
					sums += Math.pow(	( sHSV[i][j] - colorJuS[1] )	,	k	);
					sumv += Math.pow(	( vHSV[i][j] - colorJuV[1] )	,	k	);
				}
			}
			
			colorJuH[k] = sumh / ( width * height );				
			if( k == 2 )
				colorJuH[k]	=	Math.sqrt(colorJuH[k]);
			if( k == 3 )
				colorJuH[k]	=	Math.cbrt(colorJuH[k]);
			
			colorJuS[k] = sums / ( width * height );
			if( k == 2 )
				colorJuS[k]	=	Math.sqrt(colorJuH[k]);
			if( k == 3 )
				colorJuS[k]	=	Math.cbrt(colorJuH[k]);
			
			colorJuV[k] = sumv / ( width * height );
			if( k == 2 )
				colorJuV[k]	=	Math.sqrt(colorJuH[k]);
			if( k == 3 )
				colorJuV[k]	=	Math.cbrt(colorJuH[k]);
		}
		
		for(k=1;k<4;k++){
			temp[k-1]=colorJuH[k];
		}
		for(k=1;k<4;k++){
			temp[k+2]=colorJuS[k];
		}
		for(k=1;k<4;k++){
			temp[k+5]=colorJuV[k];
		}
		
		return temp;
		
	}
	
	public static  double tempValue(double [] colorJu1,double [] colorJu2){
		double sum=0;
		for(int i=1;i<4;i++){
			sum+=Math.pow((colorJu2[i]-colorJu1[i]), 2);
		}

		return (sum/3);
	}

	public static double Metri(double [] colorJu1,double [] colorJu2){   //中心矩法，匹配值越小，越相似
		double[] colorJuH1 =	new double[4];
		double[] colorJuS1 =	new double[4];
		double[] colorJuV1 =	new double[4];
		double[] colorJuH2 =	new double[4];
		double[] colorJuS2 =	new double[4];
		double[] colorJuV2 =	new double[4];
		int k;
		double metriresult;
		for(k=1;k<4;k++){
			colorJuH1[k]=colorJu1[k-1];
			colorJuH2[k]=colorJu2[k-1];
		}
		for(k=1;k<4;k++){
			colorJuS1[k]=colorJu1[k+2];
			colorJuS2[k]=colorJu2[k+2];
		}
		for(k=1;k<4;k++){
			colorJuV1[k]=colorJu1[k+5];
			colorJuV2[k]=colorJu2[k+5];
		}
		
		metriresult=Math.sqrt(tempValue(colorJuH1,colorJuH2)+tempValue(colorJuS1,colorJuS2)+tempValue(colorJuV1,colorJuV2));
		return metriresult;
		
	}
	
}
