package com.vonzhou.tankwar2;
import java.awt.*;

/**
 * ¹¹½¨Ñª¿éÀà
 * @author vonzhou
 *
 */
public class Blood {
	int x,y,w=15,h=15;
	int step=0;
	boolean live=true;
	TankClient tc;
	int[][] pos={{400,400},{420,400},{430,410},{425,405},{400,405},
			{405,410},{400,400},{410,400},{405,425},{425,410}};
	public Blood(){
		x=pos[0][0];
		y=pos[0][1];
	}
	
	public void draw(Graphics g){
		if(!live)return;
		Color c=g.getColor();
		g.setColor(Color.MAGENTA);
		g.fillRect(x, y, w, h);
		g.setColor(c);
		move();
	}

	private void move() {
		step++;
		if(step==pos.length) step=0;
		x=pos[step][0];
		y=pos[step][1];
		
	}
	
	public Rectangle getRec(){
		return new Rectangle(x,y,w,h);
	}

	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}

}
