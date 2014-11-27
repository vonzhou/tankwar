package com.vonzhou.tankwar2;
import java.awt.*;



public class Explode {
	int x,y;
	private boolean live=true;
	private static Toolkit tk=Toolkit.getDefaultToolkit();
	private static Image[] imgs=new Image[]{
		tk.createImage(Explode.class.getClassLoader().getResource("images/0.gif")),
		tk.createImage(Explode.class.getClassLoader().getResource("images/1.gif")),
		tk.createImage(Explode.class.getClassLoader().getResource("images/2.gif")),
		tk.createImage(Explode.class.getClassLoader().getResource("images/3.gif")),
		tk.createImage(Explode.class.getClassLoader().getResource("images/4.gif")),
		tk.createImage(Explode.class.getClassLoader().getResource("images/5.gif")),
		tk.createImage(Explode.class.getClassLoader().getResource("images/6.gif")),
		tk.createImage(Explode.class.getClassLoader().getResource("images/7.gif")),
		tk.createImage(Explode.class.getClassLoader().getResource("images/8.gif")),
		tk.createImage(Explode.class.getClassLoader().getResource("images/9.gif")),
		tk.createImage(Explode.class.getClassLoader().getResource("images/10.gif")),
	};
	int step=0;
	
	private TankClient tc;
	private static boolean init=false;
	
	public Explode(int x,int y,TankClient tc){
		this.x=x;
		this.y=y;
		this.tc=tc;
	}
	
	public void draw(Graphics g){
		if(!init){
			for (int j = 0; j < imgs.length; j++) {
				g.drawImage(imgs[j], -100, -100, null);
			}
			init=true;
		}
		
		if(step==imgs.length ){
			live=false;
			step=0;
			return;
		}
		if(!live) {
			tc.explodes.remove(this);
			return;
		}
		g.drawImage(imgs[step], x, y, null);
		step++;
		
	}

}
