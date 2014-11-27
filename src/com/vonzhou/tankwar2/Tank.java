package com.vonzhou.tankwar2;
import java.awt.*;
import java.util.*;
import java.awt.event.*;

/**
 * 构建坦克类
 * @author vonzhou
 *
 */
public class Tank {
	int x,y;
	int oldX,oldY;
	public static final int XSPEED=5;
	public static final int YSPEED=5;
	public static final int WIDTH=30;
	public static final int HEIGHT=30;
	private static Random  r=new Random();
	private int step=r.nextInt(12)+3;
	private int life=100;
	private BloodBar bb=new BloodBar();
	
	private boolean bL=false,bR=false,bD=false,bU=false;
	
	private Direction dir=Direction.STOP;
	private Direction ptDir=Direction.D;
	private boolean good;
	private boolean live=true;
	TankClient tc;
	
	private static Toolkit tk=Toolkit.getDefaultToolkit();
	private static Image[] tankImages=null;
	private static Map<String,Image> imgs=new HashMap<String,Image>	();	
	static{
		tankImages=new Image[]{
				tk.createImage(Explode.class.getClassLoader().getResource("images/tankL.gif")),
				tk.createImage(Explode.class.getClassLoader().getResource("images/tankLU.gif")),
				tk.createImage(Explode.class.getClassLoader().getResource("images/tankU.gif")),
				tk.createImage(Explode.class.getClassLoader().getResource("images/tankRU.gif")),
				tk.createImage(Explode.class.getClassLoader().getResource("images/tankR.gif")),
				tk.createImage(Explode.class.getClassLoader().getResource("images/tankRD.gif")),
				tk.createImage(Explode.class.getClassLoader().getResource("images/tankD.gif")),
				tk.createImage(Explode.class.getClassLoader().getResource("images/tankLD.gif")),
			};
		imgs.put("L", tankImages[0]);
		imgs.put("LU", tankImages[1]);
		imgs.put("U", tankImages[2]);
		imgs.put("RU", tankImages[3]);
		imgs.put("R", tankImages[4]);
		imgs.put("RD", tankImages[5]);
		imgs.put("D", tankImages[6]);
		imgs.put("LD", tankImages[7]);
	}
	public boolean isGood() {
		return good;
	}

	public boolean isLive() {
		return live;
	}
	public void setLive(boolean live) {
		this.live = live;
	}
	
	public Tank(int x, int y,boolean good) {
		this.x = x;
		this.y = y;
		this.oldX=x;
		this.oldY=y;
		this.good=good;
	}
	public Tank(int x,int y,boolean good,Direction dir,TankClient tc){
		this(x,y,good);
		this.dir=dir;
		this.tc=tc;
	}
	public void draw(Graphics g){
		if(good&&this.life>0)bb.draw(g);
		if(!live){
			if(!good){
				tc.tanks.remove(this);
			}
			return;
		}
		
		switch(ptDir){
		case L:
			g.drawImage(imgs.get("L"), x, y, null);break;
		case LU:
			g.drawImage(imgs.get("LU"), x, y, null);break;
		case U:
			g.drawImage(imgs.get("U"), x, y, null);break;
		case RU:
			g.drawImage(imgs.get("RU"), x, y, null); break;
		case R:
			g.drawImage(imgs.get("R"), x, y, null);break;
		case RD:
			g.drawImage(imgs.get("RD"), x, y, null);break;
		case D:
			g.drawImage(imgs.get("D"), x, y, null);break;	
		case LD:
			g.drawImage(imgs.get("LD"), x, y, null);break;
		}
		move();
	}
	
	public void move(){
		this.oldX=x;
		this.oldY=y;
		
		switch(dir){
		case L:x-=XSPEED;break;
		case LU:x-=XSPEED; y-=XSPEED;break;
		case U:y-=YSPEED;break;
		case RU:x+=XSPEED;y-=YSPEED; break;
		case R:x+=XSPEED;break;
		case RD:x+=XSPEED;y+=YSPEED;break;
		case D:y+=XSPEED;break;	
		case LD:x-=XSPEED;y+=YSPEED;break;
		case STOP:break;
		}
		if(dir!=Direction.STOP){this.ptDir=dir;}
		if(x<0) x=0;
		if(y<30) y=30;
		if(x+Tank.WIDTH>TankClient.WIDTH)x=TankClient.WIDTH-Tank.WIDTH;
		if(y+Tank.HEIGHT>TankClient.HEIGHT)y=TankClient.HEIGHT-Tank.HEIGHT;
		
		if(!good){
			Direction[] dirs=dir.values();
			if(step==0){
				step=r.nextInt(12)+3;
				int rn=r.nextInt(dirs.length);
				dir=dirs[rn];
			}
			step--;
			if(r.nextInt(22)>20) fire();
		}
	}//end move
	
	
	
	public void keyPressed(KeyEvent e){
		int key=e.getKeyCode();
		switch(key){
		case KeyEvent.VK_F2:if(!this.live){
			                  this.live=true;
			                  this.life=100;
			                  }
		                      break;
		case KeyEvent.VK_RIGHT:bR=true;break;
		case KeyEvent.VK_LEFT:bL=true;break;
		case KeyEvent.VK_UP:bU=true;break;
		case KeyEvent.VK_DOWN:bD=true;break;
		}
		locateDirection();
	}//end keyPressed
	
	public void locateDirection(){
		if(bL && !bR && !bD && !bU) dir=Direction.L;
		else if(!bL && bR && !bD && !bU) dir=Direction.R;
		else if(!bL && !bR && bD && !bU) dir=Direction.D;
		else if(!bL && !bR && !bD && bU) dir=Direction.U;
		else if(bL && !bR && bD && !bU) dir=Direction.LD;
		else if(bL && !bR && !bD && bU) dir=Direction.LU;
		else if(!bL && bR && bD && !bU) dir=Direction.RD;
		else if(!bL && bR && !bD && bU) dir=Direction.RU;
		else if(!bL && !bR && !bD && !bU) dir=Direction.STOP;
	}

	public void keyReleased(KeyEvent e) {
		int key=e.getKeyCode();
		switch(key){
		case KeyEvent.VK_CONTROL:
			fire();break;
		case KeyEvent.VK_A:superFire();break;
		case KeyEvent.VK_RIGHT:bR=false;break;
		case KeyEvent.VK_LEFT:bL=false;break;
		case KeyEvent.VK_UP:bU=false;break;
		case KeyEvent.VK_DOWN:bD=false;break;
		}
		locateDirection();
	}
	
	public Missile fire(Direction dir){
		if(!live) return null;
		int x=this.x+Tank.WIDTH/2-Missile.WIDTH/2;
		int y=this.y+Tank.HEIGHT/2-Missile.HEIGHT/2;
		Missile m=new Missile(x,y,good,dir,this.tc);
		tc.missiles.add(m);
		return m;
	}
	
	public Missile fire(){
		if(!live) return null;
		int x=this.x+Tank.WIDTH/2-Missile.WIDTH/2;
		int y=this.y+Tank.HEIGHT/2-Missile.HEIGHT/2;
		Missile m=new Missile(x,y,good,ptDir,this.tc);
		tc.missiles.add(m);
		return m;
	}
	
	public void superFire(){
		Direction[] dirs=Direction.values();
		for(int i=0;i<8;i++){
			fire(dirs[i]);
		}
	}
	
	public Rectangle getRec(){
		return new Rectangle(x,y,WIDTH,HEIGHT);
	}
	
	public void stay(){
		x=this.oldX;
		y=this.oldY;
	}
	
	/**
	 * 
	 * @param w 所要撞得墙体
	 * @return 如果撞上墙就返回true 否则返回false
	 */
	public boolean collidesWithWall(Wall w){
		if(this.live &&this.getRec().intersects(w.getRect())){
			this.stay();
			return true;
		}
		return false;
	}
	
	public boolean collidesWithTanks(java.util.List<Tank> tanks){
		for(int i=0;i<tanks.size();i++){
			Tank t=tanks.get(i);
			if(this!=t){
				if(this.live && t.isLive()&&this.getRec().intersects(t.getRec())){
					this.stay();
					t.stay();
					return true;
			     }
			
		    }
			
	  }		
	return false;
    }

	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}
	
	
	private class BloodBar{
		public void draw(Graphics g){
			Color c=g.getColor();
			g.setColor(Color.RED);
			g.drawRect(x, y-10, WIDTH, 10);
			int w=WIDTH*life/100;
			g.fillRect(x, y-10, w, 10);
			g.setColor(c);
		}
	}
	
	public boolean eat(Blood b){
		
		if(this.live&&b.isLive()&&this.getRec().intersects(b.getRec())){
			this.life=100;
			b.setLive(false);
			return true;
		}
		return false;
	}

	
	
}
