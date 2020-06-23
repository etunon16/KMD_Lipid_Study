/*
 * Here the operations to turn a list of Compound objects into a csv file 
 * and obtain the kendrick data and molecular_weight are included.
 */
package Utils;

import Pojos.Compound;
import java.io.File;
import java.io.FileWriter;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author enriquetunon
 */
public class UtilsMain {
    
    public UtilsMain() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    // Calulate Kendrick mass
    public static double calculate_kendrickmass (double molecular_weight){
        
        double kendrick_mass = (molecular_weight * 14.0)/14.01565;
        return kendrick_mass;
    }
    
    // Calulate Kendrick mass defect
    public static double calculate_kmd (double kendrick_mass){

        double kmd = kendrick_mass - (int)kendrick_mass;
        return kmd;   
    }       
    
    // Calulate molecular_weight from the formula
    public static double calculate_molecular_weight (String formula){
        
        double mass=0.00;
        String[] elements = new String[] {"C", "H", "N", "O", "P", "S"};
        double[] masses = new double[] {12.0107, 1.00784, 14.0067, 15.999, 30.973762, 32.065};
        
        for (int i=0; i<masses.length; i++){
            
            String element = elements[i];
            int index = formula.indexOf(elements[i]);
            String coef = "";
            
            if(formula.contains(element)){
                
                for(int k=1; k<4; k++) {
                   
                    if((index+k)<formula.length()){
                        
                        if(Character.isDigit(formula.charAt(index+k))){
                            coef = coef + formula.charAt(index+k);
                        }
                        else{
                            break;
                        }
                    }
                }
                if(coef.isEmpty()){
                    mass += masses[i];
                }else{
                    mass += Integer.parseInt(coef)*masses[i];
                }
            }
        }
        return mass;
    }
    
    // Create a csv file from a Compound list of a specific lipid family and double bond amount.
    public static boolean DbToCSV (List<Compound> kl_list, String lipid_type, int double_bonds) {
                
        try {
            String filename = "./csv_files/";
            filename += lipid_type;
            filename += "_lipid_family/";
            File file = new File(filename);
            boolean bool = file.mkdirs();
            String db = Integer.toString(double_bonds);
            filename += db;
            filename += "db.csv";
            FileWriter fw = new FileWriter(filename);
            fw.append("Formula,Mass,Kendrick_Mass,Kmd,Lipid_type,double_bonds\n");
            Iterator<Compound> kl_list_iterator = kl_list.iterator();
            while (kl_list_iterator.hasNext()) {
                Compound compound = kl_list_iterator.next();
                fw.append(compound.getFormula());
                fw.append(',');
                fw.append(Double.toString(compound.getMass()));
                fw.append(',');
                fw.append(Double.toString(compound.getKendrick_mass()));
                fw.append(',');
                fw.append(Double.toString(compound.getKmd()));
                fw.append(',');
                fw.append(compound.getLipid_type());
                fw.append(',');
                fw.append(Double.toString(compound.getDouble_bonds()));
                fw.append('\n');
               }
            fw.flush();
            fw.close();
            return true;
        } catch (Exception csv_error) {
            csv_error.printStackTrace();
        }
        return false;
    }
}