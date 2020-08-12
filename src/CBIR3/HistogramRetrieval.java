package CBIR3;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;


public class HistogramRetrieval {
		//  ��ͳ��ֱ��ͼ�ཻ��    ͳ��RGB  ��һ��
	public static double [][] GetHistogram1(BufferedImage img)
	{
		double [][] histgram=new double [3][256];
		int width=img.getWidth();//ͼƬ���
		int height=img.getHeight();//ͼƬ�߶�
		int pix[]= new int [width*height];//���ظ���
		int r,g,b;//��¼R��G��B��ֵ
		pix = img.getRGB(0, 0, width, height, pix, 0, width);//��ͼƬ������ֵ�浽������
		for(int i=0; i<width*height; i++) 
		{  
			r = pix[i]>>16 & 0xff; //��ȡR 
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
		for(int j=0;j<256;j++)//��ֱ��ͼÿ������ֵ���ܸ�����������
		{
			histgram[0][j]/=red;
			histgram[1][j]/=green;
			histgram[2][j]/=blue;
		}
		   return histgram;
	}
	////  ��ͳ��ֱ��ͼ�ཻ��  ͳ��RGB  ��һ�� ���ý���������ͼƬ�����ƶ�
	public static double GetSimilarity1(double [][] Rhistgram,double  [][] Dhistgram)
	{
		double similar=(double)0.0;//���ƶ�
		for(int i=0;i<3;i++){
			for(int j=0;j<Rhistgram[i].length;j++){
				similar+=Math.min(Rhistgram[i][j], Dhistgram[i][j]);
			}
		}
		similar=similar/3;
		return similar;
	}
	//ŷʽ������ͼƬ�����ƶ� 
	public static double GetSimilarity2(double [][] Rhistgram,double  [][] Dhistgram)//ŷʽ���뷨��ƥ��ֵԽС��Խ����
	{
		double similar=(double)0.0;//���ƶ�
		for(int i=0;i<3;i++){
			for(int j=0;j<Rhistgram[i].length;j++){
				similar+=(Rhistgram[i][j]-Dhistgram[i][j])*(Rhistgram[i][j]-Dhistgram[i][j]);
			}
		}
		similar=similar/6;
		similar=Math.sqrt(similar);
		return similar;
	}
	// ��ʿϵ����
	public static double [][] GetHistogram(BufferedImage img){
		double [][] histgram=new double [3][256];
		int width=img.getWidth();//ͼƬ���
		int height=img.getHeight();//ͼƬ�߶�
		int pix[]= new int [width*height];//���ظ���
		int r,g,b;//��¼R��G��B��ֵ
		pix = img.getRGB(0, 0, width, height, pix, 0, width);//��ͼƬ������ֵ�浽������
		for(int i=0; i<width*height; i++) {  
			r = pix[i]>>16 & 0xff; //��ȡR 
			g = pix[i]>>8 & 0xff;  
			b = pix[i] & 0xff;   
			histgram[0][r] ++;  
			histgram[1][g] ++;  
			histgram[2][b] ++;  
		}  
		for(int j=0;j<256;j++)//��ֱ��ͼÿ������ֵ���ܸ�����������
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
		double similar=(double)0.0;//���ƶ�
		for(int i=0;i<3;i++){
			for(int j=0;j<Rhistgram[i].length;j++){
				similar+=Math.sqrt(Rhistgram[i][j]*Dhistgram[i][j]);
			}
		}
		similar=similar/3;
		return similar;
	}
}

