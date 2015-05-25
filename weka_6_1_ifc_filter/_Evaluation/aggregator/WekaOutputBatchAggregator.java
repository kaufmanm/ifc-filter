/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kaum
 */
public class WekaOutputBatchAggregator {

    /**
     * @param args the command line arguments
     */
    
    public static String filename = "";
    public static String content = "";
    public static String output = "dataset\tclassifier\tifc\ttime1\ttime2\trmse\n";
            
    public static void main(String[] args) {

        String dirpath = "../output/";
        File dir = new File(dirpath);
        for (File f : dir.listFiles()) {
            if (f.isDirectory()) {
                continue;
            }
            String filepath = f.getAbsolutePath();
            filename = f.getName();
            content = read(filepath);
            
            // add methods for extraction of each measure
            
            //extract ifc/noninfc and classifier from filename
            output+= filename
                    .replace("test_", "")
                    .replace(".txt", "")
                    .replace("_", "\t") + "\t";
            
            getTime1();
            getTime2();
            getRMSE();
            
            output += "\n";
            
            System.out.print("");
        }
        
        PrintWriter out;
        try {
            out = new PrintWriter("output.txt");
            out.println(output);
            out.close();
        } catch (FileNotFoundException ex) {
           ex.printStackTrace();
        }
        
    }

    static String read(String filepath) {

        StringBuilder text = new StringBuilder();
        String nl = System.getProperty("line.separator");
        Scanner scanner;
        try {
            scanner = new Scanner(new FileInputStream(filepath), "UTF-8");
            while (scanner.hasNextLine()) {
                text.append(scanner.nextLine() + nl);
            }
            scanner.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        return text.toString();
    }



    private static void getTime1() {
        String[] split1 = content.split("Time taken to build model:");
        String[] split2 = split1[1].split("seconds");
        String time1 = split2[0] ;
        output += time1 + "\t";
        System.out.print("");
    }
    
     private static void getTime2() {
        String[] split1 = content.split("Time taken to test model on training data:");
        String[] split2 = split1[1].split("seconds");
        String time2 = split2[0] ;
        output += time2 + "\t";
        System.out.print("");
    }

    private static void getRMSE() {
        String[] split1 = content.split("=== Cross-validation ===");
        String[] split2 = split1[1].split("Root mean squared error");
        String[] split3 = split2[1].split("Relative absolute error");
        String RMSE = split3[0].trim();
        output += RMSE + "\t";
        System.out.print("");
    }
}
