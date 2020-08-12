package CBIR3;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
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

public class IDBMDialog extends JDialog  {

	private String path;
	private File f;
	private int sum;
	private List filename=new ArrayList();
	protected MysqlConnect mqlconnect;
	
    private JButton browsebutton;
    private JTextField browsetextfield;
    private JButton confirmbutton;
    private JButton importbutton;
    
    private JPanel browsepane;
    private JPanel inputimagepane;
    private JTextArea area;
    private JLabel jlabel;
    private JPanel outputimagepane;
    private JPanel basket;

    private JComboBox choosetable;
    @SuppressWarnings("unchecked")
	public IDBMDialog(JFrame f,boolean b){
    	super(f,b);
    	setTitle("IDBM");

        LayoutManager ly = new BorderLayout();
        setLayout(ly);
        
        mqlconnect=new MysqlConnect();
    	mqlconnect.getConnectionAndStatement("idb");
    	
        browsebutton = new JButton("Browse");
        browsetextfield = new JTextField("enter path to input image");
        confirmbutton = new JButton("Confirm");
        importbutton = new JButton("Import");
        choosetable = new JComboBox(TablesList.tablelist);
        choosetable.setBorder(BorderFactory.createTitledBorder("IDB tables"));
        browsepane = new JPanel();
        
        browsepane.setLayout(new FlowLayout());
        browsepane.add(browsebutton);
        browsepane.add(browsetextfield);
        browsepane.add(confirmbutton);
        browsepane.add(importbutton);
        browsepane.add(choosetable);
        
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

        EvevtHandler handler = new EvevtHandler();
        browsebutton.addActionListener(handler);
        browsetextfield.addActionListener(handler);
        confirmbutton.addActionListener(handler);
        importbutton.addActionListener(handler);
        
        choosetable.addActionListener(new ActionListener(){

    		@Override
    		public void actionPerformed(ActionEvent e) {
    			sum=0;
				jlabel.setText(sum+" images got in the IDB");
				area.setText(null);
				outputimagepane.removeAll();
				
    			int index=choosetable.getSelectedIndex();
    			if(index!=0){
    				String s=choosetable.getSelectedItem().toString();
    				String sql,rsstring[];
    				int count=0,rsint[];
    				try {
						sql ="select * from "+s;
						ResultSet rs =mqlconnect.getStatement().executeQuery(sql);
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
		            }	
    				catch (SQLException e1) {
						e1.printStackTrace();
					}
   				System.out.println(s);
    		}
    	}
        });
        setSize(1000, 700);
    }

	private class EvevtHandler implements ActionListener{
		private File tempfile=null;
		private String tempname="";
        	
        public void actionPerformed(ActionEvent event){
            if(event.getSource() == browsebutton){
	            area.setText(null);
	            browsetextfield.setText("enter path to input image");
	            outputimagepane.removeAll();
	            JFileChooser chooser = new JFileChooser();
	            chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
	            
	            int returnvalue = chooser.showOpenDialog(null);
	
	            if(returnvalue == JFileChooser.APPROVE_OPTION){
	            	f=chooser.getSelectedFile();
	            	path=f.getAbsolutePath();
	            	browsetextfield.setText(path);
	          
	            }
            }

            else if(event.getSource() == browsetextfield)
            {
            	path = browsetextfield.getText();
            	browsetextfield.setText(path);
            	inputimagepane.removeAll();
            }
            else if(event.getSource() ==confirmbutton ){
            	sum=0;
            	tempname="";
            	jlabel.setText(sum+" images got in the IDB");
            	area.setText(null);
            	outputimagepane.removeAll();
            	
            	if(new File(path).isDirectory()){
					listFile(path);				
	            	sum=filename.size();
	            	
	            	File f=new File("IDB");
	            	if(!f.exists())
	            		f.mkdir();
	            	FileWriter fw = null;
	            	String splitname[]=null;
	            	try{
	            		splitname=path.split("\\\\");
	            		for(int i=0;i<splitname.length;i++)
	            			tempname+=splitname[i];
	            		tempname=tempname.replace(":", "");
	            		tempname=tempname.toLowerCase();
	            		tempfile=new File(f.getAbsolutePath()+"\\"+tempname+".txt");
	            		if(!tempfile.exists()){
		            		fw=new FileWriter(tempfile);
							for(int i=0;i<filename.size();i++){
								fw.write(i+"-->");
								fw.write((String)filename.get(i));
								fw.write("\r\n");
							}
							fw.close();
							JOptionPane.showMessageDialog(null, "File opened already written!", "prompt", JOptionPane.INFORMATION_MESSAGE);
	            		}
					} catch (IOException e) {
						e.printStackTrace();
					}
            	}
            	else
            	{
            		try {
            			File file=new File(path);
						FileReader fr =null;
						BufferedReader br=null;
						fr=new FileReader(file);
						br=new BufferedReader(fr);
						String toimportpath=null;
						String splitstring[];

						while((toimportpath=br.readLine())!=null){
							splitstring=toimportpath.split("-->");
							toimportpath=splitstring[1];
							filename.add(toimportpath);
						}
						sum=filename.size();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
            	}
            	jlabel.setText(sum+" images got in the IDB");
            	int count=sum;
            	int rows=getRows(sum);
            	outputimagepane.setLayout(new GridLayout(rows,4,2,2));
            	int j=0;
            	String temp[]=new String[4*rows];
            	String labelnum[]=new String[4*rows];

            	for(j=0;j<4*rows;j++){
            		if(j<count){
	            		temp[j]=(String) filename.get(j);
	            		area.append(j+"-->"+temp[j]+"\n");
	            		labelnum[j]=String.valueOf(j);
	            	}else{
	        			temp[j]=null;
	        			area.append(null);
	        			labelnum[j]="";
	        		}
            	}

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
        		
            	filename.clear();
            }
            	
            else if(event.getSource() ==importbutton)
            {
            	FileReader fr =null;
            	BufferedReader br=null;
            	String tablename="";
            	String existedtablename="";
            	String rsstring[]=null;
            	boolean flags=false;
            	if(new File(path).isDirectory()){
            		tablename=tempname;
            	}
            	else{
	            	String splitname[]=path.split("\\\\");
	        		for(int i=0;i<splitname.length;i++)
	        			tablename+=splitname[i];
	        		tablename=tablename.replace(":", "");
	        		tablename=tablename.replace(".txt", "");
            	}
            	tablename=tablename.toLowerCase();
            	
            	//tablename = tablename.substring(0,tablename.indexOf("."));
        		System.out.println("icdbtablename:"+tablename);
            	
            	String sql;
				try {
					sql ="select table_name from information_schema.TABLES where TABLE_SCHEMA='idb' ";
					ResultSet rs =mqlconnect.getStatement().executeQuery(sql);
					rsstring=new String[1000];
					int i=0;
					while(rs.next()){
						rsstring[i]=rs.getString("table_name");
						i++;
					}
					if(i>0){
						for(int m=0;m<i;m++){
							if(tablename.indexOf(rsstring[m])!=-1){
								existedtablename=rsstring[m];
								flags=true;
								area.setText("File opened already imported to the db 'idb'");
								System.out.println("File opened already imported to the db 'idb'");
	    				      	JOptionPane.showMessageDialog(null, "File opened already existed in the db !", "prompt", JOptionPane.INFORMATION_MESSAGE);
								break;
							}	
						}
					}
					rs.close();

					if(flags==false){
						area.setText("File opened to be imported to the db 'idb'");
						System.out.println("File opened to be imported to the db 'idb'");
						sql="create table "+tablename+" (id int(25) primary key,path char(250))";
					    System.out.println(sql);
						mqlconnect.getStatement().execute(sql);
						if(new File(path).isDirectory()){
							fr=new FileReader(tempfile);
						}
						else{
							fr=new FileReader(new File(path));
						}
						br=new BufferedReader(fr);
						String toimportpath=null;
						String splitstring[];
						int i1;
						while((toimportpath=br.readLine())!=null){		
							splitstring=toimportpath.split("-->");
							i1=Integer.parseInt(splitstring[0]);
							toimportpath=splitstring[1];
							toimportpath=toimportpath.replace("\\", "\\\\");
							sql="insert into "+tablename+"(id,path) values("+i1+","+'"'+toimportpath+'"'+")";
							mqlconnect.getStatement().execute(sql);				
						}
						JOptionPane.showMessageDialog(null, "File opened already imported to the db!", "prompt", JOptionPane.INFORMATION_MESSAGE); 
					}				
				} catch (SQLException e1) {
					e1.printStackTrace();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				try {
					if(flags==true)
						tablename=existedtablename;
					sql="select * from "+ tablename;
					System.out.println(sql);
					 ResultSet rs1=mqlconnect.getStatement().executeQuery(sql);
					 while(rs1.next()){
						 int tempid=rs1.getInt("id");
						 String tempname=rs1.getString("path");
						 System.out.println(tempid+" "+tempname);
					 }
					 rs1.close();
				}
				catch (SQLException e) {
					e.printStackTrace();
				}         	
            }
        }
        @SuppressWarnings("unchecked")
        private void listFile(String path){
        	File f=new File(path);
        	String []temp=f.list();
        	File tempfile;
        	for(int i=0;i<temp.length;i++){
        		tempfile=new File(path+"\\"+temp[i]);
        		if(tempfile.isFile() ){
        			if( tempfile.getName().endsWith(".jpg") 
            				||tempfile.getName().endsWith(".png")
            				||tempfile.getName().endsWith(".bmp")
            				||tempfile.getName().endsWith(".gif")
            				||tempfile.getName().endsWith(".jpeg")
            				||tempfile.getName().endsWith(".JPG")
        				    ||tempfile.getName().endsWith(".PNG")
        			    	||tempfile.getName().endsWith(".BMP")
        			    	||tempfile.getName().endsWith(".GIF")
        			    	||tempfile.getName().endsWith(".JPEG"))
        				filename.add(path+"\\"+temp[i]);
		
        		}
        		else
        			listFile(path+"\\"+temp[i]);
        	}
        }
    }
	
	private int getRows(int sum){
		if(sum%4==0){
			return sum/4;
		}
		else
			return (sum/4)+1;
        }
	}
