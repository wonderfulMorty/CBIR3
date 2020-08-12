package CBIR3;


import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Thread2 implements Runnable{

	String temp[];
	XLabel label[];
	
	public Thread2(String temp[],XLabel label[]){
		this.temp=temp;
		this.label=label;
	}
	@Override
	public void run() {
		for(int i=0;i<temp.length;i++){
				try {
					label[i].setimage(temp[i]);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				label[i].addMouseListener(label[i]);
				label[i]=null;
				temp[i]=null;
				System.gc();
		}
	}
}
