package CBIR3;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class HelpDialog extends JDialog  {

    private JPanel browsepane;
    private JLabel help;
    private JPanel basket;
    private JTextPane   textpane;
  
    @SuppressWarnings("unchecked")
	public HelpDialog(JFrame f,boolean b){
    	super(f,b);
    	setTitle("HELP");

        LayoutManager ly = new BorderLayout();
        setLayout(ly);
        
        help=new JLabel("You can see the following instructions ");
        help.setSize(1000, 100);
        browsepane = new JPanel();
        browsepane.setLayout(new FlowLayout());
        browsepane.add(help);
    
        textpane=new JTextPane();
        gui();
        
        basket=new JPanel();
        basket.setLayout( new BorderLayout());
        basket.add(textpane,BorderLayout.CENTER);

        this.add(browsepane,BorderLayout.NORTH);
        this.add(basket,BorderLayout.CENTER);

        this.setSize(1000, 700);
    }


    public   void   insert(String   str,   AttributeSet   attrSet)   {   
        Document   doc   =   textpane.getDocument();   
        try   {   
        	
            doc.insertString(doc.getLength(),   str,   attrSet);   
        }   
        catch   (BadLocationException   e)   {   
            System.out.println("BadLocationException:   "   +   e);   
        }   
    }   
  
    public   void   setDocs(String   str,Color   col,boolean   bold,int   fontSize)   {   
        SimpleAttributeSet   attrSet   =   new   SimpleAttributeSet();   
        StyleConstants.setForeground(attrSet,   col);   
        //颜色   
        if(bold==true){   
            StyleConstants.setBold(attrSet,   true);   
        }//字体类型   
        StyleConstants.setFontSize(attrSet,   fontSize);   
        //字体大小   
        insert(str,   attrSet);   
    }   
  
    public   void   gui()   {    
        setDocs("本软件操作分为三个步骤：选择任意目录作为图像库idb，从图像库中提取所有图片特征值建立图像特征库icdb和选择目标图片，使用图像特征提取算法和匹配算法从图库找出相似图片显示\n\n",Color.BLACK,false,25); 
        setDocs("本软件图像处理算法主要有：\n1.传统的直方图相交法，统计RGB，归一化；\n2.直方图相交欧式距离法；\n3.巴氏系数法；\n4.RGB坐标系转为HSV坐标系，取得图像的颜色矩值，即中心矩法；\n5.形状不变矩法.\n\n",Color.BLACK,false,22); 
        setDocs("主界面介绍：",Color.BLACK,true,25); 
        setDocs("选择目标图片，选择图像特征提取算法和匹配算法从图库找出相似图片显示，另外也提供了选择图像库和图像特征库的入口菜单\n",Color.BLACK,false,22);  
        setDocs("IDB界面介绍：",Color.BLACK,true,25); 
        setDocs("Browse按钮为选择任意目录作为图库，Confirm按钮为识别并提取已选目录下所有图片在工程目录IDB下建立文件路径的txt文件，Import按钮为将已选图库导入图像数据库idb\n",Color.BLACK,false,22);
        setDocs("ICDB界面介绍：",Color.BLACK,true,25); 
        setDocs("选择图像数据库内任意一个图库作为待检索图库，选择图像处理方法提取已选图库中所有图片特征值在工程目录ICDB下建立图像特征的txt文件，并导入图像特征数据库icdb\n",Color.BLACK,false,22);
        setDocs("HELP界面介绍：",Color.BLACK,true,25); 
        setDocs("对此软件功能的简要描述，方便用户使用",Color.BLACK,false,22);
        
    }   
}
