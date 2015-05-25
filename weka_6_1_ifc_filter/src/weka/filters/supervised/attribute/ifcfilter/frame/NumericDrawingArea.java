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
 * NumericDrawingArea.java
 * Copyright (C) 2010 University of Fribourg, Fribourg, Switzerland
 *
 */
package weka.filters.supervised.attribute.ifcfilter.frame;

import java.awt.*;
import java.text.DecimalFormat;
import javax.swing.*;

/**
 * Class to generate graphical object for numeric attribute panel.
 *
 * @author Cédric Graf
 */
class NumericDrawingArea extends JPanel {

    /**
     * Color of the stroke.
     */
    private Color middleColor;

    /**
     * Color of the rectangle.
     */
    private Color rectangleColor;

    /**
     * String array containing original values.
     */
    private String[] featureNameToString;

    /**
     * Double array containing the fuzzy values of featureName.
     */
    private double[] featureValue;

    /**
     * Paramter to resize the graphical object.
     */
    private double panelWidth = 721;

    /**
     * Paramter to resize the graphical object.
     */
    private double panelHeight = 299;

    /**
     * Double array containing the NLR.
     */
    private double tableNLR[][];

    /**
     * Double array containing original values.
     */
    private double[] featureName;
    private final double[] featureType;

    /**
     * Generates class to generate a graphical object panel.
     *
     * @param tableNLR double array containing the NLR.
     * @param featureName double array containing original values.
     * @param featureValue double array containing the fuzzy values of
     * featureName.
     */
    public NumericDrawingArea(double[][] tableNLR, double[] featureName, double[] featureValue, double[] featureType) {
        this.featureNameToString = new String[featureName.length];
        for (int i = 0; i < featureName.length; i++) {
            this.featureNameToString[i] = Double.toString(featureName[i]);
        }
        this.featureName = featureName;
        this.featureValue = featureValue;
        this.featureType = featureType;
        this.tableNLR = tableNLR;
        middleColor = Color.BLUE;
        rectangleColor = Color.BLACK;
    }

    /**
     * Generates graphical object.
     *
     * @param g graphical object.
     */
    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        int width = getWidth();
        int height = getHeight();
        double graphWidth = 580;
        double graphHeight = 80;
        double graphLeftBorder = 70;
        double graphUperBorder = 200;
        int distanceToHorizontalLine = 15;
        g2d.setColor(rectangleColor);
        String family = "Lucida Sans Typewriter";
        int style = Font.PLAIN;
        int size = 10;
        Font font = new Font(family, style, size);
        g2d.setFont(font);
        double adaptedWidth = width / panelWidth;
        double adaptedHeight = height / panelHeight;
        g2d.scale(adaptedWidth, adaptedHeight);

        double numberOfUnit = featureName[featureName.length - 1] - featureName[0];
        double intervalLength = graphWidth / numberOfUnit;

        int xAxesRight = (int) (graphLeftBorder + intervalLength * (featureName[featureName.length - 1] - featureName[0]));
        int xAxesLeft = (int) (graphLeftBorder);
        int yAxesUper = (int) (graphUperBorder);
        int yAxesUnder = (int) (graphHeight + graphUperBorder);

        int yHalfAxesUnder = (int) (graphHeight / 2 + graphUperBorder);
        g2d.setStroke(new BasicStroke(1));

        /*
         double invertValue = 1-featureValue[0];
         double invertValue2 = 1-featureValue[1];
         int xPositionLeft = (int)(intervalLength* (featureName[0]-featureName[0])+ graphLeftBorder);
         int xPositionRight = (int)((featureName[1]-featureName[0])*intervalLength +graphLeftBorder);
         int columnHeigt = (int) (graphHeight * invertValue + graphUperBorder);
         int columnHeigt2 = (int) (graphHeight * invertValue2 + graphUperBorder);
         g2d.draw3DRect(xPositionLeft-2, columnHeigt-2, 5, 5, true); */
        for (int i = 0; i < featureValue.length - 1; i++) {
            double invertValue = 1 - featureValue[i];
            double invertValue2 = 1 - featureValue[i + 1];
            int columnHeigt = (int) (graphHeight * invertValue + graphUperBorder);
            int columnHeigt2 = (int) (graphHeight * invertValue2 + graphUperBorder);
            int xPositionLeft = (int) (intervalLength * (featureName[i] - featureName[0]) + graphLeftBorder);
            int xPositionRight = (int) ((featureName[i + 1] - featureName[0]) * intervalLength + graphLeftBorder);
            g2d.drawLine(xPositionLeft, columnHeigt, xPositionRight, columnHeigt2);
            if (featureType[i+1] == 1) {
                g2d.draw3DRect(xPositionRight - 2, columnHeigt2 - 2, 5, 5, true);
            }

        }

        //Qantile
        int firstGraphUperLimit = 40;
        int yFirstGraphAxesUnder = (int) (graphHeight + firstGraphUperLimit);

        for (int i = 1; i <= tableNLR[0].length; i++) {
            double interval = graphWidth / (2 * tableNLR[0].length);
            int columnYPosition = (int) (graphHeight * (1 - tableNLR[2][i - 1]) + firstGraphUperLimit);
            int columnHeigt = (int) (yFirstGraphAxesUnder - columnYPosition);
            int columnWidth = (int) (interval);
            int columnXPosition = (int) (interval * i * 2 - interval + graphLeftBorder);

            g2d.setColor(Color.black);
            g2d.drawRect(columnXPosition, columnYPosition, columnWidth, columnHeigt);
            g2d.rotate(Math.PI / 4, columnXPosition + 10, yFirstGraphAxesUnder + 5);
            DecimalFormat dec = new DecimalFormat("#0.00");
            String temp = dec.format(tableNLR[1][i - 1]);
            g2d.drawString(temp, (int) (columnXPosition + 10), yFirstGraphAxesUnder + 10);
            g2d.rotate(-Math.PI / 4, columnXPosition + 10, yFirstGraphAxesUnder + 5);
        }

        g2d.drawLine(xAxesLeft, yAxesUper, xAxesLeft, yAxesUnder);//vertical
        g2d.setColor(Color.black);
        g2d.setStroke(new BasicStroke(1));
        g2d.drawLine(xAxesLeft, yFirstGraphAxesUnder, xAxesRight, yFirstGraphAxesUnder);//horizontal
        g2d.drawLine(xAxesLeft, firstGraphUperLimit, xAxesLeft, yFirstGraphAxesUnder);//vertical
        g2d.drawString("0.00", xAxesLeft - 25, yFirstGraphAxesUnder);
        g2d.drawString("1.00", xAxesLeft - 25, firstGraphUperLimit);
        String family2 = "Lucida Sans Typewriter";
        int style2 = Font.BOLD;
        int size2 = 12;
        Font font2 = new Font(family2, style2, size2);
        g2d.setFont(font2);
        g2d.drawString("NLR and average values of the q-quantile", xAxesLeft - 30, firstGraphUperLimit - 20);
        g2d.drawString("Resulting piecewise affine function", xAxesLeft - 30, yAxesUper - 20);
        g2d.setFont(font);
        g2d.setColor(Color.blue);
        g2d.drawLine(xAxesLeft, yFirstGraphAxesUnder - (int) (graphHeight / 2), xAxesRight, yFirstGraphAxesUnder - (int) (graphHeight / 2));//horizontal
        g2d.drawString("0.50", xAxesLeft - 25, yFirstGraphAxesUnder - (int) (graphHeight / 2));
        g2d.setColor(Color.black);
        g2d.drawLine(xAxesLeft, yAxesUnder, xAxesRight, yAxesUnder);//horizontal
        g2d.setColor(Color.blue);
        g2d.drawLine(xAxesLeft, yHalfAxesUnder, xAxesRight, yHalfAxesUnder);//horizontal
        g2d.drawString("0.50", xAxesLeft - 25, yHalfAxesUnder);
        g2d.setColor(Color.black);
        g2d.drawLine(xAxesLeft, yAxesUper, xAxesLeft, yAxesUnder);//vertical
        g2d.drawString(featureNameToString[0], xAxesLeft, yAxesUnder + distanceToHorizontalLine);
        g2d.drawString(featureNameToString[featureNameToString.length - 1], xAxesRight, yAxesUnder + distanceToHorizontalLine);
        g2d.drawString("0.00", xAxesLeft - 25, yAxesUnder);
        g2d.drawString("1.00", xAxesLeft - 25, yAxesUper);
    }
}
