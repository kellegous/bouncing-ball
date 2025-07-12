/*
    Copyright (C) 2006 Kelly Norton.
    
    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package com.kellegous.nmm;
/*
 * Created on Feb 8, 2005
 *
 */

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;

/**
 * @author knorton
 */
public class Ball {
    private static final boolean    SHOW_EDGE_PATH=false;
    private static final boolean    SHOW_CENTER_PATH=true;
    
	private Vector2D		position;
	private Vector2D		velocity;
	private float		angularVel;
	private float		orientation;
	
    private float		r;
    private Image		dyImage = null;
    private Image		stImage = null;
    
    private GeneralPath	cxPath = null;
    private GeneralPath	rxPath = null;
    
    public Ball() {
        this.position = new Vector2D(0f,0f);
        this.velocity = new Vector2D(0f,0f);
        this.angularVel = 0f;
        this.orientation = 0f;
        this.r = 5f;
    }
    
    public Ball(float x, float y, float r) {
    		this();
    		this.position.set(x,y);
    		this.r = r;
    }
    
    public Ball(float x, float y, float r, float vx, float vy) {
        this(x,y,r);
        this.velocity.set(vx,vy);
    }
    
    public void draw(Graphics2D g) {
        
        if (stImage == null || dyImage == null)
            return;
        
        if (SHOW_EDGE_PATH || SHOW_CENTER_PATH) {
            Graphics2D gc = (Graphics2D)g.create();
            gc.setComposite(
                AlphaComposite.getInstance(
                        AlphaComposite.SRC_OVER,0.25f));
            
            if (SHOW_CENTER_PATH && cxPath != null) {
                gc.setColor(Color.RED);
                gc.draw(cxPath);
            }

            if (SHOW_EDGE_PATH && rxPath != null) {
                gc.setColor(Color.GREEN);
                gc.draw(rxPath);
            }
        }
        
        Graphics2D gg = (Graphics2D)g.create();
        gg.transform(
                AffineTransform.getTranslateInstance(
                        position.i,position.j));
        
        Graphics2D gr = (Graphics2D)gg.create();
        gr.transform(
                AffineTransform.getRotateInstance(orientation));        
        gr.drawImage(dyImage,(int)-r,(int)-r,null);
        
        if (stImage != null)
            gg.drawImage(stImage,(int)-r,(int)-r,null);
    }
    
    public float getX() {
    		return position.i;
    }
    
    public void setX(float x) {
    		position.i = x;
    }
    
    public float getY() {
    		return position.j;
    }
    
    public void setY(float y) {
    		position.j = y;
    }
    
    public float getVx() {
    		return velocity.i;
    }
    
    public void setVx(float vx) {
    		velocity.i = vx;
    }
    
    public float getVy() {
    		return velocity.j;
    }
    
    public void setVy(float vy) {
    		velocity.j = vy;
    }
   
    public void setAngularVelocity(float av) {
        angularVel = av;
    }
    
    public float getRadius() {
    		return r;
    }
    
    public void setRadius(float r) {
        this.r = r;
    }
    
    public Image getStaticImage() {
        return this.stImage;
    }
    
    public void setStaticImage(Image stImage) {
        this.stImage = stImage;
    }
    
    public Image getDynamicImage() {
        return this.dyImage;
    }
    
    public void setDynamicImage(Image dyImage) {
        this.dyImage = dyImage;
        this.setRadius(this.dyImage.getWidth(null)/2);
    }
    
    public void computeFrictionlessCollisions(int width, int height) {
		// Collisions
		if ((position.j+r) >= height) {
			//collide with floor
			velocity.j = -Constants.DAMPENING * velocity.j;
			position.j = height-r;
		}
		else if ((position.j+r) < 0) {
			//collide with ceiling
			velocity.j = -Constants.DAMPENING * velocity.j;
			position.j = r;
		}
		if ((position.i+r) >= width) {
			//collide with right wall
			velocity.i = -Constants.DAMPENING * velocity.i;
			position.i = width-r;
		}
		else if ((position.i+r) <= 0) {
			//collide with left wall
			velocity.i = -Constants.DAMPENING * velocity.i;
			position.i = r;
		}    	
    }
    
    public static float computeVxGarwin(float vx, float av, float r) {
		return ((1-Constants.GARWIN_ALPHA*Constants.Ex)*vx
		        		+ Constants.GARWIN_ALPHA*(1+Constants.Ex)*r*av)
		        / (1+Constants.GARWIN_ALPHA);
    }
    
    public static float computeVyGarwin(float vy) {
        return -Constants.Ey*vy;
    }
    
    public static float computeAVGarwin(float vx,float av, float r) {
        return ((1+Constants.Ex) * vx
		        		+ (Constants.GARWIN_ALPHA - Constants.Ex)*r*av)
		        /(r*(1+Constants.GARWIN_ALPHA));        
    }
    
    public void computeGarwinCollisions(int width, int height) {
        if ((position.j+r) >= height) {
            velocity.i = computeVxGarwin(velocity.i,this.angularVel,r);
            angularVel = computeAVGarwin(velocity.i,this.angularVel,r);
            velocity.j = computeVyGarwin(velocity.j);
            position.j = height-r;
        }
        
		if ((position.i+r) >= width) {
		    velocity.i = computeVyGarwin(velocity.i);
		    velocity.j = computeVxGarwin(velocity.j,angularVel,r);
		    angularVel = -computeAVGarwin(velocity.j,angularVel,r);
			position.i = width-r;
		}
		else if ((position.i-r) <= 0) {
		    velocity.i = computeVyGarwin(velocity.i);
		    velocity.j = computeVxGarwin(velocity.j,angularVel,r);
		    angularVel = computeAVGarwin(velocity.j,angularVel,r);
			position.i = r;
		}
    }
    
    public void reset() {
        cxPath = rxPath = null;
        this.orientation = 0f;
        this.angularVel = 0f;
        this.velocity.set(0f,0f);
    }
    
    public void update(float dt, int width, int height) {
    		velocity.j  = velocity.j + Constants.G * dt;
    		position.i += velocity.i*dt;
    		position.j += velocity.j*dt;
    		orientation += angularVel*dt;			
    		this.computeGarwinCollisions(width,height);
    		
        if (cxPath == null) {
            cxPath = new GeneralPath();
            cxPath.moveTo(position.i,position.j);
        }
        else {
    			cxPath.lineTo(position.i,position.j);                
        }
            
        if (rxPath == null) {
            rxPath = new GeneralPath();
            rxPath.moveTo(
                    position.i+r*(float)Math.cos(orientation),
                    position.j+r*(float)Math.sin(orientation));
        }
        else {

            rxPath.lineTo(
                    position.i+r*(float)Math.cos(orientation),
                    position.j+r*(float)Math.sin(orientation));
        }
    }    
}
