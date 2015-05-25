/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package weka.filters.supervised.attribute.ifcfilter.numericalgorithme;

/**
 * Class for computing the avarage of the n-tile and the sum of the target
 * values.
 *
 * @author cedric
 */
public class contingencyCrosstable implements PieceWiseLinearFunctionInterface {

    double[][] crossTable;
    double[][] contingencyTable;
    public static String[] labels = {"ntl", "n10", "n11", "n1*", "n*0", "n*1", "n**", "s1*", "a1*", "p10", "p11", "nlr"};

    /**
     * Computes the avarage of the n-tile values and the sum of the target
     * values.
     *
     * @param table double array containing the target values, numeric values
     * and index of the n-tile.
     * @param ntil int containing the number of n-tile.
     */
    //INFO called by PieceWiseLinearFunction
    @SuppressWarnings("empty-statement")
    public void set(double[][] table, int ntil) {

        //ntil = table[2].length;
        /**
         * double array containing the sum of the target values (0 at row[0]; 1
         * at row[1]), the average of the n-tile, the index of the n-tile.
         */
        
        // INPUT
        //table[0]: Y
        //table[1]: X
        //table[2]: n-tile
        
        //OUTPUT
        // count nXY
        //contingencyTable[0]: ntl
        //contingencyTable[1]: n10
        //contingencyTable[2]: n11
        //contingencyTable[3]: n1*
        //contingencyTable[4][0]: n*0
        //contingencyTable[5][0]: n*1
        //contingencyTable[6][0]: n**
        //contingencyTable[7]: s1*
        //contingencyTable[8]: a1*
        //contingencyTable[9]: p10 (later)
        //contingencyTable[10]: p11 (later)
        //contingencyTable[11]: nlr (later)
        crossTable = new double[4][ntil];
        contingencyTable = new double[12][ntil];
        int t = 0;
        double summ = 0;
        double sumcount = 0;
        for (int i = 0; i < table[0].length; i++) {
            int y = (int) table[0][i]; // get the Y target to store the 
            int x = (int) table[2][i]; // geth the X (N-Tile)
            double v = table[1][i];
            
            contingencyTable[0][x - 1] = x;  // store ntile for cross-check
            contingencyTable[y+1][x - 1] += 1; // count conditional to x and y
            contingencyTable[3][x - 1] += 1; // count conditional to x 
            contingencyTable[y + 4][0] += 1; // count conditional to y
            contingencyTable[6][0] += 1; // overall count
            contingencyTable[7][x - 1] += v;  // sum conditional to x
            
            
        }
        double[] avg = new double[ntil];
        for(int i = 0; i< ntil; i++){
            avg[i] = contingencyTable[7][i] / contingencyTable[3][i];
        }
        contingencyTable[8] = avg;
        
        //System.out.println(ar2s(contingencyTable, labels));

        System.out.print("");
        
        /*crossTable[0] =  contingencyTable[0];//: ? Anz. 1
        crossTable[1] = contingencyTable[1];//: ? Anz. 0
        crossTable[2] = avg;//: avg value of ntile
        crossTable[3] = contingencyTable[7];//: n-tile index
*/
    }

    /**
     * Returns cross table containing. The sum of the target values. The average
     * of the n-tile. The index of the n-tile.
     *
     * @return double array containing the sum of the target value 0 at row[0],
     * the sum of the target value 1 at row[1], the average of the n-tile at
     * row[2] and the index of the n-tileat row[4].
     *
     */
    public double[][] get() {
        return crossTable;
    }

    static String ar2s(double[][] in, String[] labels) {
        String r = "";
        for (int i = 0; i < in.length; i++) {
            r += labels[i];
            for (int j = 0; j < in[i].length; j++) {
                r += "\t" + in[i][j];
            }
            r += "\n";
        }
        return r;
    }

}
