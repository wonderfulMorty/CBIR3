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
        //��ɫ   
        if(bold==true){   
            StyleConstants.setBold(attrSet,   true);   
        }//��������   
        StyleConstants.setFontSize(attrSet,   fontSize);   
        //�����С   
        insert(str,   attrSet);   
    }   
  
    public   void   gui()   {    
        setDocs("�����������Ϊ�������裺ѡ������Ŀ¼��Ϊͼ���idb����ͼ�������ȡ����ͼƬ����ֵ����ͼ��������icdb��ѡ��Ŀ��ͼƬ��ʹ��ͼ��������ȡ�㷨��ƥ���㷨��ͼ���ҳ�����ͼƬ��ʾ\n\n",Color.BLACK,false,25); 
        setDocs("�����ͼ�����㷨��Ҫ�У�\n1.��ͳ��ֱ��ͼ�ཻ����ͳ��RGB����һ����\n2.ֱ��ͼ�ཻŷʽ���뷨��\n3.����ϵ������\n4.RGB����ϵתΪHSV����ϵ��ȡ��ͼ�����ɫ��ֵ�������ľط���\n5.��״����ط�.\n\n",Color.BLACK,false,22); 
        setDocs("��������ܣ�",Color.BLACK,true,25); 
        setDocs("ѡ��Ŀ��ͼƬ��ѡ��ͼ��������ȡ�㷨��ƥ���㷨��ͼ���ҳ�����ͼƬ��ʾ������Ҳ�ṩ��ѡ��ͼ����ͼ�����������ڲ˵�\n",Color.BLACK,false,22);  
        setDocs("IDB������ܣ�",Color.BLACK,true,25); 
        setDocs("Browse��ťΪѡ������Ŀ¼��Ϊͼ�⣬Confirm��ťΪʶ����ȡ��ѡĿ¼������ͼƬ�ڹ���Ŀ¼IDB�½����ļ�·����txt�ļ���Import��ťΪ����ѡͼ�⵼��ͼ�����ݿ�idb\n",Color.BLACK,false,22);
        setDocs("ICDB������ܣ�",Color.BLACK,true,25); 
        setDocs("ѡ��ͼ�����ݿ�������һ��ͼ����Ϊ������ͼ�⣬ѡ��ͼ��������ȡ��ѡͼ��������ͼƬ����ֵ�ڹ���Ŀ¼ICDB�½���ͼ��������txt�ļ���������ͼ���������ݿ�icdb\n",Color.BLACK,false,22);
        setDocs("HELP������ܣ�",Color.BLACK,true,25); 
        setDocs("�Դ�������ܵļ�Ҫ�����������û�ʹ��",Color.BLACK,false,22);
        
    }   
}
