package CBIR3;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

public class ICDBMDialog extends JDialog  {

	private int sum;

	private Vector colorways;
	private Vector<String> shapeways;
	private Vector<String> textureways;
	private String idbtablename;
	protected String icdbtablename;
	protected String icdb_shapetablename;
	
	
	protected String rsstring[];
	protected int rsint[];
	protected int count;
	
	protected MysqlConnect mqlconnect2;
	protected File tempfile1;
	protected File tempfile2;
	protected File tempfile3;
	protected File tempfile4;
	protected File tempfile5;
	
    private JPanel browsepane;
    private JPanel inputimagepane;
    private JTextArea area;
    private JLabel jlabel;
    private JPanel outputimagepane;
    private JPanel basket;
    private XLabel querylabel;
    private JComboBox choosetable;
    private JComboBox choosecolorways;
    private JComboBox chooseshapeways;
    private JComboBox choosetextureways;
    public ICDBMDialog(JFrame f,boolean b){
    	super(f,b);
    	setTitle("ICDBM");

        LayoutManager ly = new BorderLayout();
        setLayout(ly);
     
        mqlconnect2=new MysqlConnect();
        mqlconnect2.getConnectionAndStatement("icdb");
        
    	colorways=new Vector();
    	shapeways=new Vector<String>();
    	textureways=new Vector<String>();
    	colorways.addElement("colorways");
    	colorways.addElement("Bhattacharyya");
    	colorways.addElement("RGBHistogram_intersection");
    	colorways.addElement("Euclidean_distance");
    	colorways.addElement("Central_moment");
    	
    	shapeways.addElement("shapeways");
    	shapeways.addElement("Invariant_moment");
    	
    	textureways.addElement("textureways");
    	
        choosetable = new JComboBox(TablesList.tablelist);
        choosetable.setBorder(BorderFactory.createTitledBorder("IDB tables"));
        choosecolorways=new JComboBox(colorways);
        choosecolorways.setBorder(BorderFactory.createTitledBorder("colorways"));
        chooseshapeways=new JComboBox(shapeways);
        chooseshapeways.setBorder(BorderFactory.createTitledBorder("shapeways"));
        choosetextureways=new JComboBox(textureways);
        choosetextureways.setBorder(BorderFactory.createTitledBorder("textureways"));

        browsepane = new JPanel();
        browsepane.setLayout(new FlowLayout());
        browsepane.add(choosetable);
        browsepane.add(choosecolorways);
        browsepane.add(chooseshapeways);
        browsepane.add(choosetextureways);
        
        inputimagepane = new JPanel();
        inputimagepane.setLayout(new BorderLayout());
        inputimagepane.setBorder (new LineBorder(Color.YELLOW, 3));
        inputimagepane.setSize(256,256);
        area =new JTextArea(1000,25);
        jlabel=new JLabel("No images got in the IDB");
        area.setLineWrap(true);

        inputimagepane.add(jlabel,BorderLayout.NORTH);
        inputimagepane.add(new JScrollPane(area),BorderLayout.CENTER);

        outputimagepane = new JPanel();
        outputimagepane.setBorder(new LineBorder(Color.BLACK,3));
        
        basket = new JPanel();
        basket.setLayout( new BorderLayout());

        this.add(browsepane,BorderLayout.NORTH);
        this.add(basket);

        basket.add(inputimagepane,BorderLayout.WEST); 
        basket.add(new JScrollPane(outputimagepane),BorderLayout.CENTER);

        choosetable.addActionListener(new ActionListener(){

    		@Override
    		public void actionPerformed(ActionEvent e) {  			   		
    			MysqlConnect mqlconnect1=new MysqlConnect();
    	        mqlconnect1.getConnectionAndStatement("idb");
    			sum=0;
            	count=0;
				jlabel.setText(sum+" images got in the IDB");
				area.setText(null);
				outputimagepane.removeAll();
		    	
    			int index=choosetable.getSelectedIndex();
    			if(index!=0){
    				idbtablename=choosetable.getSelectedItem().toString();
    				String sql;
    				try {
						sql ="select * from "+idbtablename;
						ResultSet rs =mqlconnect1.getStatement().executeQuery(sql);
						rsstring=new String[10000];
						rsint=new int[10000];
						while(rs.next()){
							rsint[count]=rs.getInt("id");
							rsstring[count]=rs.getString("path");
							count++;
						}
						sum=count;
						jlabel.setText(sum+" images got in the IDB");
						
						int rows=getRows(sum);
		            	outputimagepane.setLayout(new GridLayout(rows,4,2,2));
		            	int j;
		            	
		            	String temp[]=new String[4*rows];
		            	String labelnum[]=new String[4*rows];
		            	for(j=0;j<4*rows;j++){
		            		if(j<count){
		            		temp[j]=rsstring[j].replace("\\\\", "\\");
		            		area.append(rsint[j]+"-->"+temp[j]+"\n");
		            		labelnum[j]=String.valueOf(rsint[j]);
		            		}
		            		else{
		            			temp[j]=null;
		            			area.append(null);
		            			labelnum[j]="";
		            		}
		            	}
		            	int flag=JOptionPane.showConfirmDialog(choosetable, "you got "+sum+" pictures,showing overmany pictures may make it running slowly,will you show them?", "choose one", JOptionPane.YES_NO_OPTION); 
		    			if(flag==1){
		    				System.out.println("no");
		    			}else{
		    				System.out.println("yes");	
		            	

		        		XLabel label[] = new XLabel[4*rows]; 
		        		for(int i=0;i<4*rows;i++){
		        			label[i]=new XLabel();
		        			
		        			}
		        		JLabel l[] = new JLabel[4*rows]; 
		        		for(int i=0;i<4*rows;i++){
		        			l[i]=new JLabel();
		        			
		        			}
		        		Thread2 t1=new Thread2(temp,label);
		        		Thread t11=new Thread(t1);
		                Thread1 t2=new Thread1(labelnum,l);
		        		Thread t22=new Thread(t2);
		        		
		        		t11.start();
		        		t22.start();
		        		
		        		for( j=0;j<4*rows;j++){
		            		
		            		JPanel jp=new JPanel();
		            		jp.setSize(156, 180);
		            		jp.setLayout(new BorderLayout());
		            		jp.add(label[j],BorderLayout.NORTH);
		            		jp.add(l[j],BorderLayout.CENTER);
		            		outputimagepane.add(jp);		            				            			            		
		            	}
		            	outputimagepane.repaint();		            		
		    			}
						
		    			}catch (SQLException e1) {
						e1.printStackTrace();
					}				
   				System.out.println(idbtablename);  			  			
    		}   			   				
    			mqlconnect1.closeMysql();
    			mqlconnect1=null;    			   			
    		}
    		
        });
        
        choosecolorways.addActionListener(new ActionListener(){

    		@Override
    		public void actionPerformed(ActionEvent e) {
    			int index=0;
    			index=choosecolorways.getSelectedIndex();
    			double hist[][]; 			
    				if(index==1){    					    					    					
		            	File f=new File("ICDB");
		            	if(!f.exists())
		            		f.mkdir();
		            	FileWriter fw = null;		            	
		            	tempfile1=new File(f.getAbsolutePath()+"\\BH"+idbtablename+".txt");
		            	
					  try { 
						  if(!tempfile1.exists()){
							  fw=new FileWriter(tempfile1,true);
	    				      	for(int i=0;i<count;i++){   						    														      							
									hist=HistogramRetrieval.GetHistogram(ImageIO.read(new FileInputStream(rsstring[i])));								
										for(int j=0;j<hist[0].length;j++){											
											fw.append(hist[0][j]+" "+hist[1][j]+" "+hist[2][j]+"\r\n");																				
										}																			
										hist=null;
	    				      	}
	    				      	fw.close();
	    				      	JOptionPane.showMessageDialog(null, "File opened already written!", "prompt", JOptionPane.INFORMATION_MESSAGE);
							}
					  }catch (FileNotFoundException e1) {
								e1.printStackTrace();
							} catch (IOException e1) {
								e1.printStackTrace();
					        }  				   					
    				}
    				else if(index==2){   					   					   					  					   					
		            	File f=new File("ICDB");
		            	if(!f.exists())
		            		f.mkdir();
		            	FileWriter fw = null;		            	
		            	tempfile2=new File(f.getAbsolutePath()+"\\RGBH"+idbtablename+".txt");
    					try {
    						if(!tempfile2.exists()){
    							fw=new FileWriter(tempfile2,true);
	    						for(int i=0;i<count;i++){							    	
									hist=HistogramRetrieval.GetHistogram1(ImageIO.read(new FileInputStream(rsstring[i])));
										for(int j=0;j<hist[0].length;j++){
											fw.append(hist[0][j]+" "+hist[1][j]+" "+hist[2][j]+"\r\n");
										}
										hist=null;
	    						}		
	    						fw.close();
	    						JOptionPane.showMessageDialog(null, "File opened already written!", "prompt", JOptionPane.INFORMATION_MESSAGE);
    						} 
    					    }catch (FileNotFoundException e1) {
								e1.printStackTrace();
							} catch (IOException e1) {
								e1.printStackTrace();
							}					
    				}
    				else if(index==3){
		            	File f=new File("ICDB");
		            	if(!f.exists())
		            		f.mkdir();
		            	FileWriter fw = null;
		            	tempfile3=new File(f.getAbsolutePath()+"\\ED"+idbtablename+".txt");
    					try {
    						if(!tempfile3.exists()){
    							fw=new FileWriter(tempfile3,true);
	    						for(int i=0;i<count;i++){
									hist=HistogramRetrieval.GetHistogram1(ImageIO.read(new FileInputStream(rsstring[i])));
										for(int j=0;j<hist[0].length;j++){
											fw.append(hist[0][j]+" "+hist[1][j]+" "+hist[2][j]+"\r\n");
										}
										hist=null;
	    						}
	    						fw.close();
	    						JOptionPane.showMessageDialog(null, "File opened already written!", "prompt", JOptionPane.INFORMATION_MESSAGE);
							}
    					}catch (FileNotFoundException e1) {
								e1.printStackTrace();
    					} catch (IOException e1) {
								e1.printStackTrace();
    					}
    				}
    				else if(index==4){
		            	File f=new File("ICDB");
		            	if(!f.exists())
		            		f.mkdir();
		            	FileWriter fw = null;
		            	double tempcolorJu[]=new double[9];
		            	tempfile4=new File(f.getAbsolutePath()+"\\CM"+idbtablename+".txt");
    					try {
    						if(!tempfile4.exists()){
    							fw=new FileWriter(tempfile4,true);
    						for(int i=0;i<count;i++){
    							tempcolorJu=Centralmoment.toHSV(ImageIO.read(new FileInputStream(rsstring[i])));
    								for(int j=0;j<tempcolorJu.length;j++){
    									fw.append(tempcolorJu[j]+" ");
    								}
    								fw.append("\r\n");	
				            	}
    							fw.close();
    							JOptionPane.showMessageDialog(null, "File opened already written!", "prompt", JOptionPane.INFORMATION_MESSAGE);
							}
    					}catch (FileNotFoundException e1) {
								e1.printStackTrace();
    					} catch (IOException e1) {
								e1.printStackTrace();
    					}

    					FileReader fr =null;
    	            	BufferedReader br=null;
    	            	String existedtablename="";
    	            	String temprsstring[]=null;
    	            	boolean flags=false;
    	            	String path1=tempfile4.getAbsolutePath();
    	            	String splitname[]=path1.split("\\\\");
    	            	icdbtablename="color_";
    	        		for(int i=0;i<splitname.length;i++)
    	        			icdbtablename+=splitname[i];
    	        		
    	        		icdbtablename=icdbtablename.replace(":", "");
    	        		icdbtablename=icdbtablename.replace(".txt", "");
    	        		icdbtablename=icdbtablename.toLowerCase();
    	        		
    	        		//icdbtablename = icdbtablename.substring(0,icdbtablename.indexOf("."));
    	        		System.out.println("icdbtablename-----zz:"+icdbtablename);
    	            	String sql;
    					try {
    						sql ="select table_name from information_schema.TABLES where TABLE_SCHEMA='icdb' ";
    						ResultSet rs =mqlconnect2.getStatement().executeQuery(sql);
    						temprsstring=new String[1000];
    						int i=0;
    						
    						while(rs.next()){
    							temprsstring[i]=rs.getString("table_name");
    							System.out.println("temprsstring[i]:"+temprsstring[i]);
    							i++;
    					}
    					if(i>0){
    							for(int m=0;m<i;m++){
    							if(icdbtablename.indexOf(temprsstring[m])!=-1){
    								existedtablename=temprsstring[m];
    								flags=true;
    								area.setText("File opened already imported to the db 'icdb'");
    								System.out.println("File opened already imported to the db 'icdb'");
    	    				      	JOptionPane.showMessageDialog(null, "File opened already existed in the db !", "prompt", JOptionPane.INFORMATION_MESSAGE);

    								break;
    							}
    						}
    					}
    					rs.close();
    					if(flags==false){
    						area.setText("File opened to be imported to the db 'icdb'");
    						System.out.println("File opened to be imported to the db 'icdb'");
    						sql="create table "+icdbtablename+" (id int(25) primary key,name char(250),color double,path char(255))";
    						System.out.println(sql);
    						mqlconnect2.getStatement().execute(sql);
    							
    						fr=new FileReader(tempfile4);
    						br=new BufferedReader(fr);
    						String toimportpath="";
    						String splitstring[]=null;
    						int m=0;
    						while((toimportpath=br.readLine())!=null){		
    							splitstring=toimportpath.split(" ");
    							rsstring[m]=rsstring[m].replace("\\", "\\\\");
    							for(int l=1;l<=splitstring.length;l++){	   									
    								sql="insert into "+icdbtablename+" (id,name,color,path) values("+(l+m*9)+","+'"'+new File(rsstring[m]).getName()+'"'+","+Double.parseDouble(splitstring[l-1])+","+'"'+rsstring[m]+'"'+")";
    								mqlconnect2.getStatement().execute(sql);				
    							}		
    								m++;
    						}
    						fr.close();
    						br.close();
    						JOptionPane.showMessageDialog(null, "File opened already imported to the db!", "prompt", JOptionPane.INFORMATION_MESSAGE); 
    					}
    					if(flags==true){
    						icdbtablename=existedtablename;	
    						sql="select * from "+ icdbtablename;
    						System.out.println(sql);
    						ResultSet rs1=mqlconnect2.getStatement().executeQuery(sql);
    						while(rs1.next()){
    							int tempid=rs1.getInt("id");
    							String name=rs1.getString("name");
    							double color=rs1.getDouble("color");
    							String tempname=rs1.getString("path"); 
    						}
    						rs1.close();
    					}			
    					} catch (SQLException e1) {
    						e1.printStackTrace();
    					} catch (FileNotFoundException e1) {
    						e1.printStackTrace();
    					} catch (IOException e1) {
    						e1.printStackTrace();
    					}
    					mqlconnect2.closeMysql();
    				}		
    		}
        });

        chooseshapeways.addActionListener(new ActionListener(){

      		@Override
      		public void actionPerformed(ActionEvent e) {
      			int index=0;
      			index=chooseshapeways.getSelectedIndex();
      			if(index==1){ 
      		MysqlConnect tempmysqlconnect=new MysqlConnect();
      		tempmysqlconnect.getConnectionAndStatement("icdb");
            	File f=new File("ICDB");
            	if(!f.exists())
            		f.mkdir();
            	FileWriter fw = null;
            	double invariantmoment[]=new double[9];
            	tempfile5=new File(f.getAbsolutePath()+"\\IM"+idbtablename+".txt");
				try {
					if(!tempfile5.exists()){
						fw=new FileWriter(tempfile5,true);
						for(int i=0;i<count;i++){
							for(int j=1;j<invariantmoment.length;j++){
								fw.append(invariantmoment[j]+" ");
							}
							fw.append("\r\n");
						}
						fw.close();
						JOptionPane.showMessageDialog(null, "File opened already written!", "prompt", JOptionPane.INFORMATION_MESSAGE);
					}
				}catch (FileNotFoundException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				
            	FileReader fr =null;
            	BufferedReader br=null;
            	String existedtablename="";
            	String temprsstring[]=null;
            	boolean flags=false;

            	String path1=tempfile5.getAbsolutePath();
            	String splitname[]=path1.split("\\\\");
            	icdb_shapetablename="shape_";
        		for(int i=0;i<splitname.length;i++)
        			icdb_shapetablename+=splitname[i];
        		icdb_shapetablename=icdb_shapetablename.replace(":", "");
        		icdb_shapetablename=icdb_shapetablename.replace(".txt", "");
        		icdb_shapetablename=icdb_shapetablename.toLowerCase();
            	String sql;
				try{
					sql ="select table_name from information_schema.TABLES where TABLE_SCHEMA='icdb' ";
					ResultSet rs =tempmysqlconnect.getStatement().executeQuery(sql);
					temprsstring=new String[1000];
					int i=0;
					while(rs.next()){
						temprsstring[i]=rs.getString("table_name");
						i++;
					}
					if(i>0){
						for(int m=0;m<i;m++){
							if(icdb_shapetablename.indexOf(temprsstring[m])!=-1){
								existedtablename=temprsstring[m];
								flags=true;
								area.setText("File opened already imported to the db 'icdb'");
								System.out.println("File opened already imported to the db 'icdb'");
	    				      	JOptionPane.showMessageDialog(null, "File opened already existed in the db !", "prompt", JOptionPane.INFORMATION_MESSAGE);
								break;
							}
						}
					}
					rs.close();
					if(flags==false){
						area.setText("File opened to be imported to the db 'icdb'");
						System.out.println("File opened to be imported to the db 'icdb'");
						sql="create table "+icdb_shapetablename+" (id int(25) primary key,name char(250),shape1 double,shape2 double,shape3 double,shape4 double,shape5 double,shape6 double,shape7 double,path char(255))";
					    System.out.println(sql);
					    tempmysqlconnect.getStatement().execute(sql);
						fr=new FileReader(tempfile5);
						br=new BufferedReader(fr);
						String toimportpath="";
						String splitstring[]=null;
						int m=0;
						double tempshape[]=new double[8];
						while((toimportpath=br.readLine())!=null){		
							splitstring=toimportpath.split(" ");
							rsstring[m]=rsstring[m].replace("\\", "\\\\");
							for(int l=0;l<splitstring.length;l++){
								tempshape[l]=Double.parseDouble(splitstring[l]);
							}								
							sql="insert into "+icdb_shapetablename+" (id,name,shape1,shape2,shape3,shape4,shape5,shape6,shape7,path) values("+m+","+'"'+new File(rsstring[m]).getName()+'"'+","+tempshape[0]+","+tempshape[1]+","+tempshape[2]+","+tempshape[3]+","+tempshape[4]+","+tempshape[5]+","+tempshape[6]+","+'"'+rsstring[m]+'"'+")";
							tempmysqlconnect.getStatement().execute(sql);						
							m++;
						}
						fr.close();
						br.close();
						JOptionPane.showMessageDialog(null, "File opened already imported to the db!", "prompt", JOptionPane.INFORMATION_MESSAGE); 
					}	
				} catch (SQLException e1) {
					e1.printStackTrace();
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				try {
					if(flags==true){
						icdb_shapetablename=existedtablename;
						sql="select * from "+ icdb_shapetablename;
						System.out.println(sql);
						 ResultSet rs1=tempmysqlconnect.getStatement().executeQuery(sql);
						 while(rs1.next()){
							 int id=rs1.getInt("id");
							 String name=rs1.getString("name");
							 double shape1=rs1.getDouble("shape1");
							 double shape2=rs1.getDouble("shape2");
							 double shape3=rs1.getDouble("shape3");
							 double shape4=rs1.getDouble("shape4");
							 double shape5=rs1.getDouble("shape5");
							 double shape6=rs1.getDouble("shape6");
							 double shape7=rs1.getDouble("shape7");
							 String path=rs1.getString("path"); 
						 }
						 rs1.close();
					}
					tempmysqlconnect.closeMysql();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
      		}
      	}
        });
        setSize(1000, 700);
    }

        public int getRows(int sum){
        	if(sum%4==0){
        		return sum/4;
        	}
        	else
        		return (sum/4)+1;
        }

}
