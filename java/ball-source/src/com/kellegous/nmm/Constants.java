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
public class Constants {
	// CoR from Garwin's Model
    public static float     Ex = 0.65f;
    public static float     Ey = 0.65f;
	
	// From Garwin's Model
    public static float     GARWIN_ALPHA = 2f/5f;
	 	
    public static float     Mball = 5f;         // kg
    public static float     G = 9.8f;           // m/sec^2
    public static float     DELTA_T = 0.04f;    // second
    public static float     DAMPENING	= 0.8f;   // ratio (this goes away)

    public static String	   STATIC_IMAGE 	= "images/lotto-hi.png";
    public static String	   DYNAMIC_IMAGE	= "images/lotto-lo.png";
}
