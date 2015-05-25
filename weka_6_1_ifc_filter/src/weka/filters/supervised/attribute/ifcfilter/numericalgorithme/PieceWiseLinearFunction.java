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
 * PieceWiseLinearFunction.java
 * Copyright (C) 2010 University of Fribourg, Fribourg, Switzerland
 *
 */
package weka.filters.supervised.attribute.ifcfilter.numericalgorithme;


/**
 * Class to computes piece wise linear function.
 *
 * @author Cédric Graf
 */
public class PieceWiseLinearFunction implements IFCNumericAlgorithmInterface {
    
    /** Double array containing the target values, numeric values und apporximates fuzzy values (NLR).*/
    double[][] fuzzyTable;
    
    /** Double array containing the NLR.*/
    double[][] pwlfaInfoArray;
    public NLR nlr;

    /**
     * Constructor of the class.
     */
    public PieceWiseLinearFunction(){}

    /**
     * Computes the approximation of the NLR piece wise.
     * At firtst the n-tile are computed.
     *
     * @param doubleDat     double array containing the target values and numeric values.
     * @param leftNtilnr    int number of n-tile.
     * @param rightClassy   int equal 0 (is irrelevant here).
     * @param arrayIndex    int row to be sorted in quick sort (is irrelevant here).
     */
    public void set(double[][] doubleDat, int leftNtilnr, int rightClassy, int arrayIndex){
        int ntilnr = leftNtilnr;
        double[][] doubleData;
        doubleData = doubleDat;
        //Computes n-tile index
        double[][] numData = new double [3][doubleData[1].length];
        Ntil ntil = new Ntil();
        //INFO here the N-Tiles get calculated
        ntil.set(doubleDat, ntilnr);
        //INFO numData contains X, Y and n-tile
        numData = ntil.get();

        //table: average Q(x) - NLR - slope - b
        pwlfaInfoArray = new double [6][ntilnr];
        //table[0]: average value of an n-tile
        //table[1]: NLR of n-tile
        //table[2]: slope i, i-1 (A)
        //table[3]: Axis intercept (B)
        //table[4]: delta X
        //table[5]: n-tile index
        
        contingencyCrosstable numericTable = new contingencyCrosstable();
        numericTable.set(numData, ntilnr);


        //INFO returns crossTable from NumericCrosstable
        double[][] tempTable = numericTable.get();
        


        nlr = new NLR();
        //INFO here the NLR is calculated
        nlr.set(numericTable.contingencyTable, ntilnr);

        //INFO table[1] gets the NLR
        //DEBUG !! bis hier ok; test in window, check mit mehr NTIL; jetzt Pause mit Family
        // es gibt kein zurück mehr
        // deal with NaN
        
        //table[0] gets averag value of an n-tile
        pwlfaInfoArray[0] = nlr.contingencyTableNLR[8];
        // NLR
        pwlfaInfoArray[1] = nlr.contingencyTableNLR[11];
        //table[5] gets the n-tile index
        pwlfaInfoArray[5] = nlr.contingencyTableNLR[0];
        

        //INFO this is where slope et al are computed
        // A = delta y / delta x
        // B = Y -AX
        pwlfaInfoArray[2][0] = 0;
        for(int i = 1; i < pwlfaInfoArray[0].length; i++){
            //average of the values in a n-tile
            double intervalx = pwlfaInfoArray[0][i]-pwlfaInfoArray[0][i-1] ;
            //NLR
            double intervaly = pwlfaInfoArray[1][i]-pwlfaInfoArray[1][i-1] ;

            if(intervalx !=0){
                //computes slope crossTable[2][i]
                pwlfaInfoArray[2][i] = intervaly/intervalx;
                //interval of the n-tile
                pwlfaInfoArray[4] [i]= intervalx;
            }
            else{
                pwlfaInfoArray[2][i] = 0;
                pwlfaInfoArray[4] [i] = 0;
            }
        }


        
        for(int i = 1; i < pwlfaInfoArray[0].length; i++){
            //Computes the axis intercepts at table[3]
            pwlfaInfoArray[3][i] = pwlfaInfoArray[1][i]-pwlfaInfoArray[0][i]*pwlfaInfoArray[2][i];
        }

// INFO this is where the inductive Fuzzification takes place
        //INFO compute predictions
        double[][] numData2  =new double [3][doubleData[1].length];
        //value of origin
        numData2[0]= doubleData[1];
        //target value
        numData2[1]= doubleData[0]; 
        //piece wise linear apporximaion
        
        for(int i = 0; i < numData2[0].length; i++){
            for(int j = 0; j < pwlfaInfoArray[0].length; j++){
                if(j==0 && numData2[0][i] < pwlfaInfoArray[0][0]){
                    numData2[2][i] = numData2[0][i]*pwlfaInfoArray[2][1]+pwlfaInfoArray[3][1];
                    if (numData2[2][i] < 0){
                        numData2[2][i] = 0;
                    }
                    else if(numData2[2][i] > 1){
                        numData2[2][i] = 1;
                    }
                    break;
                }
                else if(j>0 && pwlfaInfoArray[0][j-1] <= numData2[0][i] && numData2[0][i] < pwlfaInfoArray[0][j] ){
                    numData2[2][i] = numData2[0][i]*pwlfaInfoArray[2][j]+pwlfaInfoArray[3][j];
                    break;
                }
                else if(j>0 &&  pwlfaInfoArray[0][pwlfaInfoArray[0].length-1] <= numData2[0][i]){
                    numData2[2][i] = numData2[0][i]*pwlfaInfoArray[2][pwlfaInfoArray[0].length-1]+pwlfaInfoArray[3][pwlfaInfoArray[0].length-1];
                    if (numData2[2][i] < 0){
                        numData2[2][i] = 0;
                    }
                    else if(numData2[2][i] > 1){
                        numData2[2][i] = 1;
                    }
                    break;
                }
            }
        }
        fuzzyTable = numData2;
    }

    /**
     * Returns double array containing the target values, numeric values und apporximates fuzzy values (NLR).
     *
     * @return      double array containing the target values, numeric values und apporximates fuzzy values (NLR).
     */
    public double[][] getCrossTable(){
        return fuzzyTable;
    }

    /**
     * Returns double array containing the NLR.
     *
     *  average Q(x) - NLR - slope - b
     *
     * @return      double array containing average of an n-tile at row[0], the NLR at row[1], the slope of an n-tile at row[2],
     *              the axis intercepts for each n-tile at row[3], interval for each n-tile at row[4] and the n-tile index at row[5].
     */
    public double[][] getNLR(){
        return pwlfaInfoArray;
    }


    /**
     * Returns correlation.
     *
     * @return      double containing correlation 0 (is irrelevant here).
     */
    public double getCorr() {
        return 0;
    }
    
}
