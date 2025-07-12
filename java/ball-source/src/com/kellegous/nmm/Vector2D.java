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

/**
 * @author knorton
 */
public class Vector2D {
    public	float i,j;
    
    public Vector2D() {
        this.i=0f;
        this.j=0f;
    }
    
    public Vector2D(float i, float j) {
        this.i = i;
        this.j = j;
    }
    
    public float getLength() {
        return (float)Math.sqrt(i*i + j*j);
    }
    
    public void normalize() {
        float l = getLength();
        i /= l;
        j /= l;
    }
    
    public void scale(float s) {
        i *= s;
        j *= s;
    }
    
    public void scale(float si, float sj) {
        i *= si;
        j *= sj;
    }
    
    public void translate(float di, float dj) {
        i += di;
        j += dj;
    }
    
    public void add(Vector2D v) {
    		i += v.i;
    		j += v.j;
    }
    
    public void set(float i,float j) {
    		this.i = i;
    		this.j = j;
    }
    
    public static float dotProduct(Vector2D v1, Vector2D v2) {
        return v1.i*v2.i + v1.j*v2.j;
    }
    
    public static Vector2D crossProduct(Vector2D v1, Vector2D v2) {
        return new Vector2D(v1.i*v2.i,v2.j*v2.j);
    }
    
    public String toString() {
        return "("+i+","+j+")";
    }
}
