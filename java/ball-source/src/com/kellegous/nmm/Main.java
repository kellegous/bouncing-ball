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

import info.clearthought.layout.TableLayout;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextLayout;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class Main extends JApplet {
    private static final long   serialVersionUID = 1L;
    private static final float  RUN_UNTIL = 40f;
    
    private Image           stImage;
    private Image           dyImage;
    private float           time = 0f;
    private Timer           timer;
    private JLabel          guiTime;
    private Panel[]         guiPanels;
    
    public void init() {
        this.loadImages();
        this.timer = new Timer(20,
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        time += Constants.DELTA_T;
                        guiTime.setText(
                                getTimeLabelString());
                        if (time > RUN_UNTIL)
                            ((Timer)e.getSource()).stop();
                    }
                });
        
        SwingUtilities.invokeLater(
                new Runnable() {
                    public void run() {
                        JPanel gui = (JPanel)getContentPane();
                        gui.setBorder(
                                BorderFactory.createLineBorder(Color.black));
                        
                        gui.setLayout(new TableLayout(new double[][] {
                                {
                                    TableLayout.FILL,
                                    TableLayout.FILL,
                                    TableLayout.FILL
                                },
                                {
                                    TableLayout.PREFERRED,
                                    TableLayout.FILL
                                }
                        }));
                        
                        JPanel guiPanel = new JPanel();
                        guiPanel.setLayout(new TableLayout(new double[][] {
                                {
                                    0.5,
                                    0.5
                                },
                                {
                                    TableLayout.PREFERRED
                                }
                        }));
                        guiPanel.setBorder(
                                BorderFactory.createEmptyBorder(5,10,5,10));
                        guiPanel.setBackground(Color.black);
                        
                        JButton guiResetButton
                            = new JButton("Reset");
                        guiResetButton.setBackground(Color.black);
                        guiResetButton.addActionListener(
                                new ActionListener() {
                                    public void actionPerformed(ActionEvent e) {
                                        reset();
                                    }
                                });
                        
                        guiTime = new JLabel(
                                getTimeLabelString());
                        guiTime.setForeground(Color.white);
    
                        guiPanel.add(guiTime,       "0,0,l,c");
                        guiPanel.add(guiResetButton,"1,0,r,c");
                        
                        guiPanels = new Panel[] {
                                new Panel(new Vector2D(9f,5f), 0f,"No Spin"),
                                new Panel(new Vector2D(9f,5f), 1.0f,"Top Spin"),
                                new Panel(new Vector2D(9f,5f),-1.0f,"Back Spin")                                
                        };
                        
                        guiPanels[1].setBorder(BorderFactory.createMatteBorder(
                                0,1,0,1,Color.black));
                        
                        gui.add(guiPanel,       "0,0,2,0,f,f");
                        gui.add(guiPanels[0],   "0,1,f,f");
                        gui.add(guiPanels[1],   "1,1,f,f");
                        gui.add(guiPanels[2],   "2,1,f,f");
                        
                        timer.start();
                    }
                });
    }
    
    private String getTimeLabelString() {
        return "Time: "+new DecimalFormat("0.00").format(time);        
    }
    
    private void loadImages() {
        stImage = new ImageIcon(
                getClass().getResource(
                        Constants.STATIC_IMAGE)).getImage();
        dyImage = new ImageIcon(
                getClass().getResource(
                        Constants.DYNAMIC_IMAGE)).getImage();
    }
   
    private void reset() {
        time = 0f;
        for (int i=0;i<guiPanels.length;i++)
            guiPanels[i].reset();
        if (!timer.isRunning())
            timer.start();
    }
    
    class Panel extends JPanel {
        private static final long serialVersionUID = 1L;
        private Ball        ball;
        private String      label;
        private Vector2D    initVelocity;
        private float       initAngVelocity;
        
        Panel(Vector2D velocity, float angVelocity, String label) {
            setBackground(Color.white);
            this.label = label;
            this.initVelocity = velocity;
            this.initAngVelocity = angVelocity;
            
            ball = new Ball();
            ball.setStaticImage(stImage);
            ball.setDynamicImage(dyImage);
            reset();
            
            timer.addActionListener(
                    new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            ball.update(
                                    Constants.DELTA_T,
                                    getWidth(),
                                    getHeight());
                            repaint();
                        }
                    });
            
            this.addMouseListener(
                    new MouseAdapter() {
                        public void mouseClicked(MouseEvent e) {
                            repaint();
                        }
                    });
        }
        
        private void reset() {
            ball.reset();
            float r = ball.getRadius();
            ball.setX(r+10f);
            ball.setY(r+10f);
            ball.setAngularVelocity(
                    this.initAngVelocity);
            ball.setVx(
                    this.initVelocity.i);
            ball.setVy(
                    this.initVelocity.j);
        }
        
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (ball == null)
                return;
            
            Graphics2D  g2 = (Graphics2D)g;
            
            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);            
            
            ball.draw(g2);
            
            TextLayout ts = new TextLayout(
                    this.label,
                    this.getFont(),
                    g2.getFontRenderContext());
            g2.setColor(Color.black);
            ts.draw(g2,10,2+(float)ts.getAscent());
        }        
    }
}
