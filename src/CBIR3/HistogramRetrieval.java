package CBIR3;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;


public class HistogramRetrieval {
		//  传统的直方图相交法    统计RGB  归一化
	public static double [][] GetHistogram1(BufferedImage img)
	{
		double [][] histgram=new double [3][256];
		int width=img.getWidth();//图片宽度
		int height=img.getHeight();//图片高度
		int pix[]= new int [width*height];//像素个数
		int r,g,b;//记录R、G、B的值
		pix = img.getRGB(0, 0, width, height, pix, 0, width);//将图片的像素值存到数组里
		for(int i=0; i<width*height; i++) 
		{  
			r = pix[i]>>16 & 0xff; //提取R 
			g = pix[i]>>8 & 0xff;  
			b = pix[i] & 0xff;   
			histgram[0][r] ++;  
			histgram[1][g] ++;  
			histgram[2][b] ++;  
		}  
		double red =0,green=0,blue=0;
		for(int j=0;j<256;j++){
			red+=histgram[0][j];
			green+=histgram[1][j];
			blue+=histgram[2][j];
		}
		for(int j=0;j<256;j++)//将直方图每个像素值的总个数进行量化
		{
			histgram[0][j]/=red;
			histgram[1][j]/=green;
			histgram[2][j]/=blue;
		}
		   return histgram;
	}
	////  传统的直方图相交法  统计RGB  归一化 后用交来求两个图片的相似度
	public static double GetSimilarity1(double [][] Rhistgram,double  [][] Dhistgram)
	{
		double similar=(double)0.0;//相似度
		for(int i=0;i<3;i++){
			for(int j=0;j<Rhistgram[i].length;j++){
				similar+=Math.min(Rhistgram[i][j], Dhistgram[i][j]);
			}
		}
		similar=similar/3;
		return similar;
	}
	//欧式距离求图片的相似度 
	public static double GetSimilarity2(double [][] Rhistgram,double  [][] Dhistgram)//欧式距离法，匹配值越小，越相似
	{
		double similar=(double)0.0;//相似度
		for(int i=0;i<3;i++){
			for(int j=0;j<Rhistgram[i].length;j++){
				similar+=(Rhistgram[i][j]-Dhistgram[i][j])*(Rhistgram[i][j]-Dhistgram[i][j]);
			}
		}
		similar=similar/6;
		similar=Math.sqrt(similar);
		return similar;
	}
	// 巴士系数法
	public static double [][] GetHistogram(BufferedImage img){
		double [][] histgram=new double [3][256];
		int width=img.getWidth();//图片宽度
		int height=img.getHeight();//图片高度
		int pix[]= new int [width*height];//像素个数
		int r,g,b;//记录R、G、B的值
		pix = img.getRGB(0, 0, width, height, pix, 0, width);//将图片的像素值存到数组里
		for(int i=0; i<width*height; i++) {  
			r = pix[i]>>16 & 0xff; //提取R 
			g = pix[i]>>8 & 0xff;  
			b = pix[i] & 0xff;   
			histgram[0][r] ++;  
			histgram[1][g] ++;  
			histgram[2][b] ++;  
		}  
		for(int j=0;j<256;j++)//将直方图每个像素值的总个数进行量化
		{
			for(int i=0;i<3;i++)
			{
				histgram[i][j]=histgram[i][j]/(width*height);
			}
		}
		   return histgram;
	}
	
	public static double GetSimilarity(double [][] Rhistgram,double  [][] Dhistgram)
	{
		double similar=(double)0.0;//相似度
		for(int i=0;i<3;i++){
			for(int j=0;j<Rhistgram[i].length;j++){
				similar+=Math.sqrt(Rhistgram[i][j]*Dhistgram[i][j]);
			}
		}
		similar=similar/3;
		return similar;
	}
}

