package CBIR3;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
//import org.opencv.highgui.Highgui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

public class LBPDialog extends JDialog  {
	private JButton lbpbutton;
	private JLabel image;
	private JPanel nor,sou;
    
    @SuppressWarnings("unchecked")
    public LBPDialog(JFrame f,boolean b) {
    	super(f,b);
    	setTitle("人脸识别");  
    	this.setSize(1000, 700);
        
        LayoutManager ly = new BorderLayout();
        setLayout(ly);
        
        final String str="D:\\ecs_work\\mycode\\code\\";
        ImageIcon icon = new ImageIcon(str+"shekhar.JPG");
        image = new JLabel(icon);
        icon.setImage(icon.getImage().getScaledInstance(512,343,Image.SCALE_DEFAULT)); 
        nor = new JPanel();
        nor.setLayout(new BorderLayout());
        nor.add(image,BorderLayout.CENTER);
        
        lbpbutton = new JButton("人脸识别");
	    lbpbutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		        CascadeClassifier faceDetector = new CascadeClassifier(LBPDialog.class.getResource("haarcascade_frontalface_alt.xml").getPath().substring(1));
		        Mat Image = Imgcodecs.imread(LBPDialog.class.getResource("shekhar.JPG").getPath().substring(1));
		        MatOfRect faceDetections = new MatOfRect();
		        faceDetector.detectMultiScale(Image, faceDetections);
		        for (Rect rect : faceDetections.toArray()) {
//		            Core.rectangle(Image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
//		            		new Scalar(0, 255, 0));
		        	Imgproc.rectangle(Image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
		            		new Scalar(0, 255, 0));
		        }
		        String filename = "ouput.png";
		        Imgcodecs.imwrite(filename, Image);
		        ImageIcon icon2 = new ImageIcon(str+filename);
		        image.setIcon(icon2);
			}
	    });
	    
        sou = new JPanel();  
        sou.setLayout(new FlowLayout());  
        sou.add(lbpbutton); 
        
        this.add(nor,BorderLayout.NORTH);
        this.add(sou,BorderLayout.SOUTH);
        
    }
    
}
    

