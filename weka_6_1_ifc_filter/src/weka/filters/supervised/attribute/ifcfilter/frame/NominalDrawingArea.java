/*
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 2 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program; if not, write to the Free Software
 *    Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

/*
 * NominalDrawingArea.java
 * Copyright (C) 2010 University of Fribourg, Fribourg, Switzerland
 *
 */

package weka.filters.supervised.attribute.ifcfilter.frame;

import java.awt.*;
import javax.swing.*;

/**
 * Class to generate graphical object for categorical attribute panel.
 *
 * @author Cédric Graf
 */
class NominalDrawingArea extends JPanel {
    
    /** Color of the stroke.*/
    private Color middleColor;
    
    /** Color of the rectangle.*/
    private Color rectangleColor;
    
    /** String array containing the names of values.*/
    private String[] featureName;

    /** String array containing the values of featureName.*/
    private double[] featureValue;

    /**
     * Generates class to generate a graphical object panel.
     *
     * @param featureName       string array containing the names of values.
     * @param featureValue      string array containing the values of featureName.
     */
    public NominalDrawingArea(String[] featureName, double[] featureValue) {
        this.featureName = featureName;
        this.featureValue = featureValue;
        middleColor = Color.BLUE;
        rectangleColor = Color.BLACK;
    }

    /**
     * Generates graphical object.
     *
     * @param g     graphical object.
     */
    @Override public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;

        int width = getWidth();
        int height = getHeight();
        g2d.setColor(rectangleColor);


        int numberWidth = width/(2*featureValue.length+2);
        String family = "Lucida Sans Typewriter";
        int style = Font.PLAIN;
        int size = 10;
        Font font = new Font(family, style, size);
        g2d.setFont(font);

        int unityHeight = height - height/3-10 - height/15;//unity

        for(int i = 1; i <= featureValue.length; i++){
            double invertValue = 1-featureValue[i-1];
            int columnHeigt = (int) (unityHeight * invertValue);
            g2d.rotate(Math.PI/2, (numberWidth/2)+i*2*numberWidth+10, height - height/3);
            g2d.drawString(featureName[i-1], (numberWidth/2)+i*2*numberWidth+10, height - height/3);
            g2d.rotate(-Math.PI/2, (numberWidth/2)+i*2*numberWidth+10, height - height/3);
            g2d.drawRect(i*2*numberWidth +10, height/15 + columnHeigt, numberWidth, (height - height/3-10)-(height/15 + columnHeigt));// x-y des Rechtecks::beite-höhe der position
        }

        g2d.drawLine(2*numberWidth+5, height - height/3-10, width-2*numberWidth+10+5 , height - height/3-10);//Horizontal
        g2d.drawLine(2*numberWidth+5, height/15, 2*numberWidth+5, height - height/3-10);//Vertical
        g2d.setColor(middleColor);

        g2d.drawLine(2*numberWidth+5, ((height - height/3-10)-height/15)/2+height/15, width-2*numberWidth+10+5 , ((height - height/3-10)-height/15)/2+height/15);//Horizontal-midlle
        g2d.setColor(rectangleColor);

        g2d.drawString("0.5", 2*numberWidth-15 , ((height - height/3-10)-height/15)/2+height/15+5);
        g2d.drawString("  1", 2*numberWidth-15 , height/15+5);
    }
}
