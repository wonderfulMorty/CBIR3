package CBIR3;


import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Thread1 implements Runnable{

	String labelnum[];
	JLabel label[];
	
	public Thread1(String labelnum[],JLabel label[]){
		this.label=label;
		this.labelnum=labelnum;
		
	}
	@Override
	public void run() {
		System.out.println("111111");
		for(int i=0;i<labelnum.length;i++){
           label[i].setText(labelnum[i]);
		}
	}
}
