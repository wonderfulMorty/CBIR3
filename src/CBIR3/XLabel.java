package CBIR3;

import java.awt.Color;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import java.awt.Graphics;

import java.awt.image.BufferedImage;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.imageio.ImageIO;

import java.io.File;
import javax.swing.border.LineBorder;

public class XLabel extends JLabel implements MouseListener{

    boolean select_flag = false;
    private String image_src = null;
    
    public boolean setimage ( String filepath){
    	if(filepath!=null)	{
	        LineBorder border = new LineBorder(Color.BLACK, 3);
	        this.setBorder(border);
	        image_src = filepath;
	        Image img;
	        try {
	            img = ImageIO.read(new File(filepath));
	            Icon icon=null;
	            Image imgins=img.getScaledInstance(156, 156,Image.SCALE_DEFAULT);
	            icon= new ImageIcon(imgins);
	            this.setIcon(icon);
	            return true;
	        }
	        catch (IOException ex) {
	            Logger.getLogger(XLabel.class.getName()).log(Level.SEVERE, null, ex);
	        }
	        
    	}
    	else{
    		this.setSize(156, 156);
    	}

    	return false;  
    }
 
    public void setflag(boolean flag){
        select_flag = flag;        
    }

    public boolean getflag(){ return select_flag; }
    
    public String getsource(){ return image_src; }

    public void mouseClicked(MouseEvent e) {       
    	if(getflag() == false){
    		LineBorder border = new LineBorder(Color.BLUE, 3);
    		this.setBorder(border);
    		this.setflag(true);
    		System.out.println(getsource());
    	}
    	else if(this.getflag() == true) {
    		LineBorder border = new LineBorder(Color.BLACK, 3);
    		this.setBorder(border);
    		this.setflag(false);
    	}  
	}

	public void mousePressed(MouseEvent e) {}

	public void mouseReleased(MouseEvent e) {}

	public void mouseEntered(MouseEvent e) {           
		if(this.getflag() == false){
			LineBorder border = new LineBorder(Color.RED, 3);
			this.setBorder(border);
		}   
	}

	public void mouseExited(MouseEvent e) {
		if(e.getComponent() == this){
			if(this.getflag() == false){
				LineBorder border = new LineBorder(Color.BLACK, 3);
				this.setBorder(border);
			}
		}
	}
}
