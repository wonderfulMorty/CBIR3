package CBIR3;

import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.Graphics;

import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.LayoutManager;
import java.io.File;
import java.awt.image.BufferedImage;
import java.awt.Image;

import javax.imageio.ImageIO;
import javax.swing.SwingConstants;

public class MainFrame extends JFrame{

	private JMenuBar menubar;
	private JMenu browsemenu,dbmmenu,helpmenu,lbpmenu;
	private JMenuItem browseitem,idbmitem,icdbmitem,helpitem,lbpitem;
    private JTextField browsetextfield;
    private JPanel browsepane;
    private JPanel inputimagepane;
    private JPanel outputimagepane;
    private JPanel basket;
    private XLabel1 querylabel;//显示选择的图像
    
    private UserPane usrpane;

  
    private String filepath;//browse 的文件名
    
    private IDBMDialog idbmdialog;
    private ICDBMDialog icdbmdialog;
    private HelpDialog helpdialog;
    private LBPDialog lbpdialog;
    
    private JFrame thisf;

    private static final int QUERYLABEL_SIZE = 256;
   
    private static final int OUTPUTLABEL_SIZE = 156;
    
    public MainFrame(String str){
        super(str);
        LayoutManager ly = new BorderLayout();
        setLayout(ly);
    
        menubar=new JMenuBar();
        
        browsemenu=new JMenu("File");
        dbmmenu=new JMenu("DBmanager");
        lbpmenu=new JMenu("lbp");
        helpmenu=new JMenu("Help");
        
        browseitem =new JMenuItem("Browse");
        idbmitem=new JMenuItem("IDBM");
        icdbmitem=new JMenuItem("ICDBM");
        lbpitem=new JMenuItem("lbp");
        helpitem=new JMenuItem("Help");
        
        this.setJMenuBar(menubar);
        menubar.add(browsemenu);
        menubar.add(dbmmenu);
        menubar.add(lbpmenu);
        menubar.add(helpmenu);
        browsemenu.add(browseitem);
        dbmmenu.add(idbmitem);
        dbmmenu.add(icdbmitem);
        lbpmenu.add(lbpitem);
        helpmenu.add(helpitem);
       
        browsetextfield = new JTextField("enter path to input image");

        browsepane = new JPanel();
        browsepane.setLayout(new BorderLayout());
        browsepane.add(browsetextfield,BorderLayout.CENTER);

        inputimagepane = new JPanel();
        inputimagepane.setBorder (new LineBorder(Color.BLACK, 3));
        inputimagepane.setSize(256,256);

        outputimagepane = new JPanel();
        outputimagepane.setBorder(new LineBorder(Color.BLACK,3));
        
        basket = new JPanel();
        
        basket.setLayout( new BorderLayout());

        this.add(browsepane,BorderLayout.NORTH);
        this.add(basket);

        basket.add(inputimagepane,BorderLayout.WEST); 
        basket.add(new JScrollPane(outputimagepane),BorderLayout.CENTER);
       
        thisf=this;
        idbmitem.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				getTables();
				idbmdialog =new IDBMDialog(thisf,true);
				idbmdialog.addWindowListener(new WindowAdapter(){
					@Override
					public void windowClosing(WindowEvent e) {
						idbmdialog.mqlconnect.closeMysql();
						idbmdialog.mqlconnect=null;
						idbmdialog=null;
						System.gc();
					}
				});
				idbmdialog.setVisible(true);
			}
        });
        
        icdbmitem.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				getTables();
				icdbmdialog =new ICDBMDialog(thisf ,true);
				icdbmdialog.addWindowListener(new WindowAdapter(){
				@Override
				public void windowClosing(WindowEvent e) {
					// TODO Auto-generated method stub
				}
				});
				icdbmdialog.setVisible(true);
			}
        });
        
        lbpitem.addActionListener(new ActionListener(){
        	@Override
        	public void actionPerformed(ActionEvent e){
        		lbpdialog = new LBPDialog(thisf,true);
        		lbpdialog.setVisible(true);
        		lbpdialog.addWindowListener(new WindowAdapter(){
        			@Override
					public void windowClosing(WindowEvent e) {
						lbpdialog=null;
						System.gc();
					}
        		});
        	}       		
        });
        
        helpitem.addActionListener(new ActionListener(){
 			@Override
 			public void actionPerformed(ActionEvent e) {
 				helpdialog =new HelpDialog(thisf ,true);
 				helpdialog.setVisible(true);
 				helpdialog.addWindowListener(new WindowAdapter(){
 				@Override
				public void windowClosing(WindowEvent e) {
 					helpdialog=null;
 					System.gc();
 				}	
				});
 			}
         });
        
        BrowseHandler handler = new BrowseHandler();
        browseitem.addActionListener(handler);
        browsetextfield.addActionListener(handler);

        querylabel = new XLabel1(false);
       
        querylabel.addMouseListener(querylabel);
        querylabel.setimage("image_0003.jpg",QUERYLABEL_SIZE);

        inputimagepane.setLayout(new GridLayout(2, 1, 5, 5));

        usrpane = new UserPane();

        inputimagepane.add(querylabel,0);
        inputimagepane.add(usrpane,1);
        inputimagepane.repaint();
    }
    
    //选择数据库中已经存在的表  
    public void getTables(){
    	TablesList.tablelist.clear();
    	TablesList.tablelist.add("please choose a table");

		MysqlConnect mqlconnect=new MysqlConnect();
		String sql;
		String rsstring[]=null;
		int i=0;
		mqlconnect.getConnectionAndStatement("idb");
		try {
			sql ="select table_name from information_schema.TABLES where TABLE_SCHEMA='idb' ";
			ResultSet rs =mqlconnect.getStatement().executeQuery(sql);
			rsstring=new String[1000];
			while(rs.next()){
				rsstring[i]=rs.getString("table_name");
				i++;
			}
			if(i>0){
				for(int m=0;m<i;m++){
					TablesList.tablelist.add(rsstring[m]);
				}
			}
			rsstring=null;
			rs.close();
			mqlconnect.closeMysql();
			System.gc();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }

    private class BrowseHandler implements ActionListener{
    	public void actionPerformed(ActionEvent event){
    		if(event.getSource() == browseitem){
		        JFileChooser chooser = new JFileChooser();
		        int returnvalue = chooser.showOpenDialog(null);
		        if(returnvalue == JFileChooser.APPROVE_OPTION){
		        	filepath = (chooser.getSelectedFile()).getAbsolutePath();
		        	browsetextfield.setText(filepath);
		        	inputimagepane.remove(0);
	
		        	querylabel.setimage(filepath,QUERYLABEL_SIZE);
		        	inputimagepane.add(querylabel,0);
		        	querylabel.setIconTextGap(8);
		        	querylabel.setHorizontalTextPosition(SwingConstants.CENTER);
		        	querylabel.setVerticalTextPosition(SwingConstants.BOTTOM);
		        	querylabel.setText(filepath);               
	                
		        	inputimagepane.repaint();
	             
		        	outputimagepane.removeAll();
		        	XLabel labelnew = new XLabel();
		        	outputimagepane.add(labelnew);                
		        	labelnew.setimage(filepath);
		        	labelnew.addMouseListener(labelnew);             
		        }
    		}

    		else if(event.getSource() == browsetextfield)
    		{
	            String filepath;
	            filepath = browsetextfield.getText();
	            browsetextfield.setText(filepath);
	            inputimagepane.remove(0);
	
	            querylabel.setimage(filepath);
	            querylabel.setText(filepath);
	            querylabel.setHorizontalTextPosition(SwingConstants.CENTER);
	            querylabel.setVerticalTextPosition(SwingConstants.BOTTOM);
	               
	            inputimagepane.add(querylabel,0);
	            inputimagepane.repaint();
	
	            XLabel label = new XLabel();
	            outputimagepane.add(label);
	            label.setimage(filepath);
	            label.addMouseListener(label);
	        }
    	}
   }

public class XLabel1 extends JLabel implements MouseListener{

    boolean color_flag;
    boolean select_flag;
    private String image_src = null;

    public XLabel1 (){
        color_flag = true;
        select_flag = false;
        image_src = null;
    }

    public XLabel1 (boolean flag){
        color_flag = flag;
        select_flag = false;
        image_src = null;
    }

    public boolean setimage (String filepath){

       return setimage(filepath, OUTPUTLABEL_SIZE);
        
    }

    public boolean setimage ( String filepath, int size){
    	if(filepath!=null)	{
	        if(color_flag){
		        this.setBorder(new LineBorder(Color.BLACK, 5));
		        this.setBackground(Color.BLACK);
	        }
	        setPreferredSize(new Dimension(size, size));
	        image_src = filepath;
	        Image img;
	        
	        try {
	            img = ImageIO.read(new File(filepath));
	            Icon icon = new ImageIcon(img.getScaledInstance(OUTPUTLABEL_SIZE, OUTPUTLABEL_SIZE,Image.SCALE_DEFAULT));
	            this.setIcon(icon);
	            this.setHorizontalAlignment(SwingConstants.CENTER);
	            this.setVerticalAlignment(SwingConstants.CENTER);
	            return true;
	        }
	        catch (IOException ex) {
	            Logger.getLogger(XLabel.class.getName()).log(Level.SEVERE, null, ex);
	        }
    	}
    	else{
    		this.setSize(OUTPUTLABEL_SIZE, OUTPUTLABEL_SIZE);
    	}
        return true;
    }

    public void setflag(boolean flag){
        select_flag = flag;
    }

	public boolean getflag(){ return select_flag; }

	public void mouseClicked(MouseEvent e) {
		System.out.println(image_src);

		inputimagepane.remove(0);
		querylabel.setimage(image_src,QUERYLABEL_SIZE);
		querylabel.setText(image_src);
		browsetextfield.setText(image_src);
		querylabel.setHorizontalTextPosition(SwingConstants.CENTER);
		querylabel.setVerticalTextPosition(SwingConstants.BOTTOM);
		inputimagepane.add(querylabel,0);
		inputimagepane.repaint();
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
		if(color_flag){
			LineBorder border = new LineBorder(Color.RED,5);
			this.setBorder(border);
		}
	}

	public void mouseExited(MouseEvent e) {
		if(color_flag){
			LineBorder border = new LineBorder(Color.BLACK, 5);
			this.setBorder(border);                
		}
	}
}

public class UserPane extends javax.swing.JPanel {	 
	
	private JButton Button_Search;
	private JLabel Label_Color;
	private JLabel Label_Shape;
	private JLabel Label_Texture;
	private Vector colorways;
	private Vector<String> shapeways;
	private Vector<String> textureways;
    private JComboBox choosecolorways;
    private JComboBox chooseshapeways;
    private JComboBox choosetextureways;
    
    private double hist1[][];
    private double hist2[][];
    private double hist3[][];
    private double tempcolorJu[];
    private double tempinvariantmoment[];
    private double [][]hist=new double[3][256];

    private int index=0;
    private int index1=0;
    private Vector picHistograms;
	private BufferedImage srcpic;

    public UserPane() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        Button_Search = new JButton();
        Label_Color=new JLabel();
        Label_Shape=new JLabel();
        Label_Texture=new JLabel();
        colorways=new Vector();
        shapeways=new Vector();
        textureways=new Vector();
        
        picHistograms=new Vector();
        
        colorways.addElement("colorways");
    	colorways.addElement("Bhattacharyya");
    	colorways.addElement("RGBHistogram_intersection");
    	colorways.addElement("Euclidean_distance");
    	colorways.addElement("Central_moment");
    	
    	shapeways.addElement("shapeways");
    	shapeways.addElement("Invariant_moment");
    	
    	textureways.addElement("textureways");
    	
        choosecolorways=new JComboBox(colorways);
        choosecolorways.setBorder(BorderFactory.createTitledBorder("colorways"));
        chooseshapeways=new JComboBox(shapeways);
        chooseshapeways.setBorder(BorderFactory.createTitledBorder("shapeways"));
        choosetextureways=new JComboBox(textureways);
        choosetextureways.setBorder(BorderFactory.createTitledBorder("textureways"));

        Button_Search.setSize(5, 5);

        this.setBorder(javax.swing.BorderFactory.createEtchedBorder(java.awt.Color.darkGray, java.awt.Color.lightGray));
        this.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        GridLayout gridlayout =new GridLayout();
        this.setLayout(gridlayout);
        gridlayout.setColumns(1);
        gridlayout.setRows(7);
        gridlayout.setVgap(1);
        Label_Color.setText("Color");
        Label_Texture.setText("Texture");
        Label_Shape.setText("Shape");        
        Button_Search.setText("Search");
        this.add(Label_Color);
        this.add(choosecolorways);
        this.add(Label_Shape);
        this.add(chooseshapeways);
        this.add(Label_Texture);
        this.add(choosetextureways);
        this.add(Button_Search);
        
        choosecolorways.addActionListener(new ActionListener(){
    		@Override
    		public void actionPerformed(ActionEvent e) {
    			index=choosecolorways.getSelectedIndex();
    				try {
						if(index==1){
							srcpic=ImageIO.read(new FileInputStream(filepath));
							hist1=HistogramRetrieval.GetHistogram(srcpic);
						}
						else if(index==2){
									srcpic=ImageIO.read(new FileInputStream(filepath));
									hist2=HistogramRetrieval.GetHistogram1(srcpic);		
						}
						else if(index==3){
									srcpic=ImageIO.read(new FileInputStream(filepath));
									hist3=HistogramRetrieval.GetHistogram1(srcpic);
						}
						else if(index==4){
							        srcpic=ImageIO.read(new FileInputStream(filepath));
							        tempcolorJu=Centralmoment.toHSV(srcpic);

						}
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
		
    		}
        });
        
        chooseshapeways.addActionListener(new ActionListener(){
    		@Override
    		public void actionPerformed(ActionEvent e) {
    			
    			index1=chooseshapeways.getSelectedIndex();
    			System.out.println("index1:"+index1);
    				try {
						if(index1==1){
							srcpic=ImageIO.read(new FileInputStream(filepath));
						}else if(index1==0){
							srcpic=ImageIO.read(new FileInputStream(filepath));
						}
						
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					}		
    		}
        });

        Button_Search.addActionListener(new ActionListener() {
        	public void actionPerformed(java.awt.event.ActionEvent evt) {
        		Button_SearchactionPerformed();
        	}
        });
	}

    @SuppressWarnings("unchecked")
	public void Button_SearchactionPerformed(){
    	
    	if(index<4 && index>0){
    	picHistograms.clear();
    	
    	File tempfile=null;
    	double temphist[][]=null;
    	switch(index){
    	case 1:
    		   tempfile=icdbmdialog.tempfile1;
    	       temphist=hist1;
    	       hist1=null;
    	       break;
    	case 2:
    		   tempfile=icdbmdialog.tempfile2;
    		   temphist=hist2;
    		   hist2=null;
	           break;
    	case 3:
    		   tempfile=icdbmdialog.tempfile3;
    		   temphist=hist3;
    		   hist3=null;
	           break;
    	}
    	try {
    		FileReader fr=new FileReader(tempfile);
        	BufferedReader br=new BufferedReader(fr);
    		String splits[]=null;
    		int j=0,i=0,k=-1,l=0;
    		String readrgb;
			while((readrgb=br.readLine())!=null){
				splits=readrgb.split(" ");
				i=j%256;		
				hist[0][i]=Double.parseDouble(splits[0]);
				hist[1][i]=Double.parseDouble(splits[1]);
				hist[2][i]=Double.parseDouble(splits[2]);
				readrgb=null;
				l=j/256;
				if(l!=k && i==255  && index==1){
					picHistograms.add(HistogramRetrieval.GetSimilarity(hist,temphist ));
			    	for(int a=0;a<3;a++){
			    		for(int b=0;b<256;b++){
			    			hist[a][b]=0;
			    		}
			    	}
					k=l;
				}
                if(l!=k && i==255  && index==2){
					picHistograms.add(HistogramRetrieval.GetSimilarity1(hist,temphist ));
			    	for(int a=0;a<3;a++){
			    		for(int b=0;b<256;b++){
			    			hist[a][b]=0;
			    		}
			    	}
					k=l;
				}
                if(l!=k && i==255  && index==3){
					picHistograms.add(HistogramRetrieval.GetSimilarity2(hist,temphist ));
			    	for(int a=0;a<3;a++){
			    		for(int b=0;b<256;b++){
			    			hist[a][b]=0;
			    		}
			    	}
					k=l;
				}
                j++;  
		} 

			fr.close();
			br.close();
			
			 Map<String, Double> oriMap=new LinkedHashMap<String, Double>();
             for(int a=0;a<picHistograms.size();a++){
             	oriMap.put(icdbmdialog.rsstring[a], (Double)picHistograms.get(a));
             }
             
             Map<String, Double> sortedMap;
     		if(index==3){
     			sortedMap=MapSort1.sortMapByValue(oriMap);
     		}
     		else{
         		sortedMap =MapSort.sortMapByValue(oriMap);
     		}
     		oriMap=null;
             
			outputimagepane.removeAll();
			int rows=icdbmdialog.getRows(icdbmdialog.count);
			int n;
	            	
			Iterator it;
			it=sortedMap.entrySet().iterator();   
	            	
			int showsum=0;
			if(4*rows<1000){
				showsum=4*rows;
				outputimagepane.setLayout(new GridLayout(rows,4,2,2));
			}
			else{
				showsum=1000;
				outputimagepane.setLayout(new GridLayout(250,4,2,2));
			}
			String temp[]=new String[showsum];
			String labelnum[]=new String[showsum];
			for(n=0;n<showsum;n++){
				if(n<icdbmdialog.count){
					Map.Entry entry=(Map.Entry )it.next();
					temp[n]=((String)(entry.getKey()));
	            		labelnum[n]=String.valueOf(entry.getValue());	
				}
				else{
					temp[n]=null;			
					labelnum[n]="";
	            }
			}
			sortedMap=null;
			System.gc();            		
			XLabel label[] = new XLabel[showsum]; 
			for(int i1=0;i1<showsum;i1++){
				label[i1]=new XLabel();
			}
			JLabel l1[] = new JLabel[showsum]; 
			for(int i1=0;i1<showsum;i1++){
				l1[i1]=new JLabel();
	        }
			Thread2 t1=new Thread2(temp,label);
			Thread t11=new Thread(t1);
			Thread1 t2=new Thread1(labelnum,l1);
			Thread t22=new Thread(t2);
	        		
			t11.start();
			t22.start();
	        		
			for(int j1=0;j1<showsum;j1++){	
				JPanel jp=new JPanel();
				jp.setSize(156, 180);
				jp.setLayout(new BorderLayout());
				jp.add(label[j1],BorderLayout.NORTH);
				jp.add(l1[j1],BorderLayout.CENTER);
				outputimagepane.add(jp);
			}
			outputimagepane.repaint();
    	}catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	index=0;
    }
    	
    	else if(index==4){
    		MysqlConnect mqlconnect=new MysqlConnect();;
    		mqlconnect.getConnectionAndStatement("icdb");
    		String sql;

    		double tempcolor[]=new double[10000*9];;
    		String temppath[]=new String[10000];
    		int tempcount =0;
    		try {
    			System.out.println(icdbmdialog.icdbtablename);
    			sql ="select * from "+icdbmdialog.icdbtablename;
    			System.out.println(sql);
    			ResultSet rs =mqlconnect.getStatement().executeQuery(sql);
    			while(rs.next()){
    				tempcolor[tempcount]=rs.getDouble("color");
    				if(tempcount%9==0)
						temppath[tempcount/9]=rs.getString("path");
    				tempcount++;
    			}	
    			mqlconnect.closeMysql();
    			mqlconnect=null;
				} catch (SQLException e) {
					e.printStackTrace();
				}
				Vector picMoment=new Vector();
				double tempScolorJu[]=new double[9];
				for(int i=0;i<tempcount;i++){
					tempScolorJu[i%9]=tempcolor[i];
					if(i%9==8){
				        picMoment.add(Centralmoment.Metri(tempScolorJu, tempcolorJu));
				        for(int j=0;j<9;j++){
				        	tempScolorJu[j]=0;
				        }
					}
				}
				
				 Map<String, Double> oriMap=new LinkedHashMap<String, Double>();
	             for(int a=0;a<picMoment.size();a++){
	             	oriMap.put(temppath[a], (Double)picMoment.get(a));
	             }
	     		
	     		Map<String, Double> sortedMap =MapSort1.sortMapByValue(oriMap);
	     		oriMap=null;
				
				outputimagepane.removeAll();
				
				int imgcount=tempcount/9;
				int rows=icdbmdialog.getRows(imgcount);
            	
            	int n;
            	Iterator it;
            	it=sortedMap.entrySet().iterator();
             	int showsum=0;
            	if(4*rows<1000){
            		showsum=4*rows;
	            	outputimagepane.setLayout(new GridLayout(rows,4,2,2));
            	}
            	else{
            		showsum=1000;

	            	outputimagepane.setLayout(new GridLayout(250,4,2,2));
            	}
            	String temp[]=new String[showsum];
            	String labelnum[]=new String[showsum];
            	for(n=0;n<showsum;n++){
            		if(n<imgcount){
            			Map.Entry entry=(Map.Entry )it.next();
            			temp[n]=((String)(entry.getKey()));
            			labelnum[n]=String.valueOf(entry.getValue());
            			
            		}
            		else{
            			temp[n]=null;
            			labelnum[n]="";
            		}
            	}
            	sortedMap=null;
            	System.gc();
            		
            	XLabel label[] = new XLabel[showsum]; 
        		for(int i=0;i<showsum;i++){
        			label[i]=new XLabel();
        		}
        		JLabel l[] = new JLabel[showsum]; 
        		for(int i=0;i<showsum;i++){
        			l[i]=new JLabel();
        		}
        		Thread2 t1=new Thread2(temp,label);
        		Thread t11=new Thread(t1);
                Thread1 t2=new Thread1(labelnum,l);
        		Thread t22=new Thread(t2);
        		
        		t11.start();
        		t22.start();
        		
        		for(int j=0;j<showsum;j++){
            		
            		JPanel jp=new JPanel();
            		jp.setSize(156, 180);
            		jp.setLayout(new BorderLayout());
            		jp.add(label[j],BorderLayout.NORTH);
            		jp.add(l[j],BorderLayout.CENTER);
            		outputimagepane.add(jp);
            	}
            	outputimagepane.repaint();
            	index=0;
    	}
    	
    	if(index1==1){
    		MysqlConnect tempmysqlconnect=new MysqlConnect();
  			tempmysqlconnect.getConnectionAndStatement("icdb");
    		String sql;
    		double tempshape[]=new double[10000*7];;
    		String temppath[]=new String[10000];
    		int tempcount =0;
    		try {
    			System.out.println(icdbmdialog.icdb_shapetablename);
    			sql ="select * from "+icdbmdialog.icdb_shapetablename;
    			System.out.println(sql);
    			ResultSet rs =tempmysqlconnect.getStatement().executeQuery(sql);
    			while(rs.next()){
    				tempshape[tempcount*7]=rs.getDouble("shape1");
    				tempshape[tempcount*7+1]=rs.getDouble("shape2");
    				tempshape[tempcount*7+2]=rs.getDouble("shape3");
    				tempshape[tempcount*7+3]=rs.getDouble("shape4");
    				tempshape[tempcount*7+4]=rs.getDouble("shape5");
    				tempshape[tempcount*7+5]=rs.getDouble("shape6");
    				tempshape[tempcount*7+6]=rs.getDouble("shape7");
						
    				temppath[tempcount]=rs.getString("path");
    				tempcount++;
    			}
    			tempmysqlconnect.closeMysql();
    			tempmysqlconnect=null;
    		} catch (SQLException e) {
					e.printStackTrace();
    		}
    		Vector picMoment=new Vector();
    		double tempSinvariantmoment[]=new double[8];
    		for(int i=0;i<tempcount;i++){
    			tempSinvariantmoment[1]=tempshape[i*7];
    			tempSinvariantmoment[2]=tempshape[i*7+1];
    			tempSinvariantmoment[3]=tempshape[i*7+2];
    			tempSinvariantmoment[4]=tempshape[i*7+3];
    			tempSinvariantmoment[5]=tempshape[i*7+4];
    			tempSinvariantmoment[6]=tempshape[i*7+5];
    			tempSinvariantmoment[7]=tempshape[i*7+6];
    		}
				
    		Map<String, Double> oriMap=new LinkedHashMap<String, Double>();
    			for(int a=0;a<picMoment.size();a++){
	             	oriMap.put(temppath[a], (Double)picMoment.get(a));
	             }

	     		Map<String, Double> sortedMap =MapSort1.sortMapByValue(oriMap);
	     		oriMap=null;
				
				outputimagepane.removeAll();

				int rows=icdbmdialog.getRows(tempcount);
            	
            	int n;
            	Iterator it;
            	it=sortedMap.entrySet().iterator();
            	int showsum=0;
            	if(4*rows<1000){
            		showsum=4*rows;
	            	outputimagepane.setLayout(new GridLayout(rows,4,2,2));
            	}
            	else{
            		showsum=1000;

	            	outputimagepane.setLayout(new GridLayout(250,4,2,2));
            	}
            	String temp[]=new String[showsum];
            	String labelnum[]=new String[showsum];
            	for(n=0;n<showsum;n++){
            		if(n<tempcount){
            			
            			Map.Entry entry=(Map.Entry )it.next();
            		temp[n]=((String)(entry.getKey()));
            		
            		labelnum[n]=String.valueOf(entry.getValue());
            			
            		}
            		
            		else{
            			temp[n]=null;
            			
            			labelnum[n]="";
            		}
            		
            	}
            	sortedMap=null;
            	System.gc();
            		
            	XLabel label[] = new XLabel[showsum]; 
        		for(int i=0;i<showsum;i++){
        			label[i]=new XLabel();
        		}
        		JLabel l[] = new JLabel[showsum]; 
        		for(int i=0;i<showsum;i++){
        			l[i]=new JLabel();
        		}
        		Thread2 t1=new Thread2(temp,label);
        		Thread t11=new Thread(t1);
                Thread1 t2=new Thread1(labelnum,l);
        		Thread t22=new Thread(t2);
        		
        		t11.start();
        		t22.start();
        		
        		for(int j=0;j<showsum;j++){
            		JPanel jp=new JPanel();
            		jp.setSize(156, 180);
            		jp.setLayout(new BorderLayout());
            		jp.add(label[j],BorderLayout.NORTH);
            		jp.add(l[j],BorderLayout.CENTER);
            		outputimagepane.add(jp);
            	}
            	outputimagepane.repaint();
            	index1=0;
    	}
    }
}
}

