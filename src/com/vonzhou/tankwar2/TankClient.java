
package com.vonzhou.tankwar2;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.util.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 坦克大战游戏主窗体
 * @author vonzhou
 *
 */
public class TankClient extends Frame{
	/**
	 * 主窗口的宽度
	 */
	public static  final int WIDTH=800;//添加血块类 可以增加生命值
	/**
	 * 主窗口的高度
	 */
	public static  final int HEIGHT=600;
	
	Tank myTank=new Tank(600,500,true,Direction.STOP,this);
	List<Tank> tanks=new ArrayList<Tank>();
	Wall w1=new Wall(100,200,20,150,this),w2=new Wall(300,200,300,20,this);
	Blood b=new Blood();
	
	List<Explode> explodes=new ArrayList<Explode>();
	List<Missile> missiles=new ArrayList<Missile>();
	Image offScreenImage = null;
	public void paint(Graphics g){
		
		g.drawString("炮弹数："+missiles.size(), 10, 50);
		g.drawString("爆炸数："+explodes.size(), 10, 70);
		g.drawString("坦克数："+tanks.size(), 10, 90);
		g.drawString("我方坦克生命值："+myTank.getLife(), 10, 110);
		
		if(tanks.size()<=0){
			for(int i=0;i<5;i++){
				tanks.add(new Tank(50+40*(i+1),50,false,Direction.D,this));
			}
		}
		
		for(int i=0;i<missiles.size();i++){
			Missile m=missiles.get(i);
			m.hitTanks(tanks);
			m.hitTank(myTank);
			m.hitWall(w1);
			m.hitWall(w2);
			m.draw(g);
		}
		for(int i=0;i<explodes.size();i++){
			Explode e=explodes.get(i);
			e.draw(g);
		}
		
		for(int i=0;i<tanks.size();i++){
			Tank t=tanks.get(i);
			t.collidesWithWall(w1);
			t.collidesWithWall(w2);
			t.collidesWithTanks(tanks);
			t.draw(g);
		}
		myTank.draw(g);
		myTank.eat(b);
		w1.draw(g);
		w2.draw(g);
		if(b.isLive()) b.draw(g);
	}
	
public void update(Graphics g) {
		
		if (offScreenImage == null) {
			offScreenImage = this.createImage(WIDTH,HEIGHT);
		}
		Graphics gOffScreen = offScreenImage.getGraphics();
		Color c = gOffScreen.getColor();
		gOffScreen.setColor(Color.BLACK);
		gOffScreen.fillRect(0, 0,WIDTH, HEIGHT);
		gOffScreen.setColor(c);
		paint(gOffScreen);
		g.drawImage(offScreenImage, 0, 0, null);
	} 
	/**
	 * 显示游戏的主窗口
	 */
	public void lanchFrame(){
		int tankCount=0;
		Properties props=new Properties();
		try {
			props.load(this.getClass().getClassLoader().getResourceAsStream("config/tankInitCount.properties"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		tankCount=Integer.parseInt(props.getProperty("tankInitCount"));
		
		
		for(int i=0;i<tankCount;i++){
			tanks.add(new Tank(50+40*(i+1),50,false,Direction.D,this));
		}
		this.setTitle("TankWar");
		this.setLocation(50,50);
		this.setSize(WIDTH, HEIGHT);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});
		this.setResizable(false);
		this.setBackground(Color.BLACK);
		this.addKeyListener(new KeyMonitor());
		this.setVisible(true);
		new Thread(new PaintThread()).start();
		
	}
	
   private class KeyMonitor extends KeyAdapter{
	public void keyReleased(KeyEvent e) {
		myTank.keyReleased(e);
	}

	public void keyPressed(KeyEvent e) {
		myTank.keyPressed(e);
	}
	   
   }

	public static void main(String[] args) {
		TankClient tc=new TankClient();
		tc.lanchFrame();
	}
	
	private class PaintThread implements Runnable{		
		public void run() {
			while(true){
                repaint();
				try {
					Thread.sleep(25);
				    } catch (InterruptedException e) {e.printStackTrace();}				
			}			
		}		
	}

}
