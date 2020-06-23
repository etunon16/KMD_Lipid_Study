/*
 * This is the main file of the program.
 */
package Main;

import Pojos.Compound;
import Utils.UtilsMain;
import static Utils.UtilsMain.DbToCSV;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import jdbc.SQLManager;

/**
 *
 * @author enriquetunon
 */
public class Main {
    
    public static void main(String[] args) {

        // 1. Create SQLManager object in order to access the SQL querying methods.
        SQLManager manager = new SQLManager();
        
        // 2. Establish connection with the CEU Mass Mediator database.
        Statement everything_ok = manager.connectToDB("jdbc:mysql://localhost/compounds?useSSL=false", "root", "et8040et");
        System.out.println("- Connection stablished.");
        
        // 3. Extract compounds from database as a list of Compound objects that contain the compound_id, formula and mass.
        List<Compound> compound_list = manager.List_all_Compounds(); // 173426 compounds
        System.out.println(compound_list.size());
        System.out.println("- Compounds list created.");
        /*
        // 4. Create a tree map with the 19861 distinct formulas and a representative Compound object in each entry. 
        // The Compound object that contains the formula, mass and a list of ids that have the same formula. 
        // Steps 4-7 only need to be executed once in order to create the Kendrick Data table in the databse.
        Map<String, Compound> compound_map = new TreeMap<>();
        Iterator<Compound> compoundIterator = compound_list.iterator();
        Compound comp, comp_map;
        while(compoundIterator.hasNext()){
            comp = compoundIterator.next();

            if (!(compound_map.containsKey(comp.getFormula()))){
                compound_map.put(comp.getFormula(), comp);
            }
            if (compound_map.containsKey(comp.getFormula())){ 
                comp_map = compound_map.get(comp.getFormula()); // Get the map value associated to the formula
                Integer id = comp.getCompound_id();
                comp_map.addIdToIdList(id); // add id of the compound to the compound on the map
            }
        }
        System.out.println(compound_map.size());
        System.out.println("- Treemap created.");

        // 5. Calculate the molecular weights from the formulas, which we need in order to calculate the Kendrick mass. Then
        // both the Kendrick Mass and Kendrick Mass Defect are calculated and assigned to the tree map entries.
        // System.out.println("Tree map data");
        Map.Entry<String, Compound> entry;
        Iterator<Map.Entry<String, Compound>> treemap_iterator = compound_map.entrySet().iterator();
        while(treemap_iterator.hasNext()){
            entry = treemap_iterator.next();
            double molecular_weight = UtilsMain.calculate_molecular_weight(entry.getKey());
            entry.getValue().setMolecular_weight(molecular_weight);
            double kendrick_mass = UtilsMain.calculate_kendrickmass(molecular_weight);
            entry.getValue().setKendrick_mass(kendrick_mass);
            double kmd = UtilsMain.calculate_kmd(entry.getValue().getKendrick_mass());
            entry.getValue().setKmd(kmd);
            //System.out.println( entry.getKey() + "=" + entry.getValue()); // To visualize the tree map data.
        }
        System.out.println("- Kendrick data calculated.");

        // 6. Create the Kendrick Data table and add the Kendrick data to the table.
        if (manager.Delete_Kendrick_Data_table()){ // First eliminate the Kendrick Data table from previous executions
        }
        if (manager.Create_Kendrick_Data_table()){ // Then create the table
            System.out.println("- Kendrick data table created.");
        }
        
        // 7. Assign the KM mass and KMD to the corresponding ids and then insert them in the database.
        treemap_iterator = compound_map.entrySet().iterator();
        List<Integer> id_list;
        Iterator<Integer> id_list_iterator;
        int kmd_id, i=0;
        while(treemap_iterator.hasNext()){
            entry = treemap_iterator.next();
            id_list = entry.getValue().getId_list(); // extract id list assigned to a formula in the map
            id_list_iterator = id_list.iterator();
            System.out.println(i);
            i++;
            while (id_list_iterator.hasNext()) {
                kmd_id = id_list_iterator.next();    
                comp = manager.Search_compound_by_id(kmd_id);
                comp.setKendrick_mass(entry.getValue().getKendrick_mass());
                comp.setKmd(entry.getValue().getKmd());
                comp.setMolecular_weight(entry.getValue().getMolecular_weight());
                Compound kmd_comp = new Compound(kmd_id, comp.getKendrick_mass(), comp.getKmd(), comp.getMolecular_weight());
                manager.Insert_new_KendrickData(kmd_comp);
            }
        }
        System.out.println("- Kendrick data inserted into table.");
        */
        // 8. Extract a list of lipid families as String objects, 10 to be more precise.
        List<String> lipid_type_list = manager.List_Lipid_Types();
        
        // 9. Create a series of lists according to lipid family and double bond amounts with the lipid_classification table from the 
        // database. A join operation is used to form a table with all the data (compound_id, formula, mass, molecular_weight, 
        // kendrick_mass, kmd, lipid_family and double_bonds). The csv files will appear in the folder named "csv_files" inside the project.
        Iterator<String> lipid_type_list_iterator = lipid_type_list.iterator();
        List<Compound> kendrick_lipid_list;
        boolean check;
        int max_db;
        
        while(lipid_type_list_iterator.hasNext()){
            String lipid_type = lipid_type_list_iterator.next();
            max_db = manager.Max_double_bonds(lipid_type);
            System.out.println(lipid_type);
            for(int counter=0; counter <= max_db; counter++){
                System.out.println(counter);
                if(manager.check_db(counter)){
                    kendrick_lipid_list = manager.List_LipidData(lipid_type, counter);
                    Iterator<Compound> dataIterator = kendrick_lipid_list.iterator();
                    while (dataIterator.hasNext()) {
                        System.out.println("Data:" + dataIterator.next().toString3());
                    }
                    check = DbToCSV(kendrick_lipid_list, lipid_type, counter);
                }
            }
        }
        System.out.println("- Lipid data lists and Csv files according to lipid types created.");

        // 10. Close connection
        manager.Close_connection();
        System.out.println("- Connection closed.");
    }   
}