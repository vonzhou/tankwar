package com.vonzhou.tankwar2;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Missile{
	
	public static final int XSPEED=10;
	public static final int YSPEED=10;
	public static final int WIDTH=10;
	public static final int HEIGHT=10;
	
	private boolean live=true;
	private boolean good;

	int x,y;
	Direction dir;
	private TankClient tc;
	private static Toolkit tk=Toolkit.getDefaultToolkit();
	private static Image[] tankImages=null;
	private static Map<String,Image> imgs=new HashMap<String,Image>	();	
	static{
		tankImages=new Image[]{
				tk.createImage(Explode.class.getClassLoader().getResource("images/missileL.gif")),
				tk.createImage(Explode.class.getClassLoader().getResource("images/missileLU.gif")),
				tk.createImage(Explode.class.getClassLoader().getResource("images/missileD.gif")),
				tk.createImage(Explode.class.getClassLoader().getResource("images/missileRU.gif")),
				tk.createImage(Explode.class.getClassLoader().getResource("images/missileR.gif")),
				tk.createImage(Explode.class.getClassLoader().getResource("images/missileRD.gif")),
				tk.createImage(Explode.class.getClassLoader().getResource("images/missileU.gif")),
				tk.createImage(Explode.class.getClassLoader().getResource("images/missileLD.gif")),
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
	public Missile(int x, int y, Direction dir) {
		this.x = x;
		this.y = y;
		this.dir = dir;
	}
	public Missile(int x,int y, boolean good,Direction dir,TankClient tc){
		this(x,y,dir);
		this.good=good;
		this.tc=tc;
	}
	
	public void draw(Graphics g){
		if(!live){
			tc.missiles.remove(this);
			return;
		}
		switch(dir){
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
	
	public boolean isLive() {
		return live;
	}
	public void setLive(boolean live) {
		this.live = live;
	}
	
	private void move() {
		switch(dir){
		case L:x-=XSPEED;break;
		case LU:x-=XSPEED; y-=XSPEED;break;
		case U:y-=YSPEED;break;
		case RU:x+=XSPEED;y-=YSPEED; break;
		case R:x+=XSPEED;break;
		case RD:x+=XSPEED;y+=YSPEED;break;
		case D:y+=XSPEED;break;	
		case LD:x-=XSPEED;y+=YSPEED;break;		
	   }
		if(x<0 || y<0|| x>TankClient.WIDTH || y>TankClient.HEIGHT){
			live=false;
		}
	}
	
	public Rectangle getRec(){
		return new Rectangle(x,y,WIDTH,HEIGHT);
	}
	public boolean hitTank(Tank t){
		if(this.live && this.getRec().intersects(t.getRec())&& t.isLive() && this.good!=t.isGood()){
			if(t.isGood()){
				t.setLife(t.getLife()-20);
				if(t.getLife()<=0){
					t.setLive(false);
				}
			}
			else t.setLive(false);
			
			//t.setLive(false);
			//this.setLive(false);
			Explode e=new Explode(x,y,tc);
			tc.explodes.add(e);
			return true;
			}
		return false;
	}
	
	
	public boolean hitTanks(List<Tank> tanks){
		for(int i=0;i<tanks.size();i++){	
		if( hitTank(tanks.get(i))){
			return true;
		}
	}
	return false;
	
	}
	
	public boolean hitWall(Wall w){
		if(this.live &&this.getRec().intersects(w.getRect())){
			this.live=false;
			return true;
		}
		return false;
	}
	
	
	
	
  }


