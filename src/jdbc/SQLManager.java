/*
 * This file contains the necessary methods to support the necessary database operations.
 */
package jdbc;

import Pojos.Compound;
import java.util.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author enriquetunon
 */

public class SQLManager { //SQL Manager
	
    private Connection sql_connection;
    private Statement statement;

    public SQLManager() {
        /*
         * Empty constructor that will allow to create a Manager object in order to use the methods
         */
    }
    
    public Statement connectToDB(String bd, String username, String password) {
        statement = null;
        try {
            // MySQL driver registered
            DriverManager.registerDriver(new org.gjt.mm.mysql.Driver());

            // get DatabaseConnection--> Mysql bd = compounds, user = root, clave = password of mysql
            sql_connection = DriverManager.getConnection(bd, username, password);

            // Statement created to perform queries
            statement = sql_connection.createStatement();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            System.out.println("Error abriendo la conexión. Comprueba BBDD, usuario y clave");
        }
        return statement;
    }
    
    // -----> DELETE METHODS <-----
    
    // Deletes Kendrick table in order to then create in from scratch
    public boolean Delete_Kendrick_Data_table() {
        try {
            Statement statement_2 = this.sql_connection.createStatement();
            String KM_table_drop = "DROP TABLE Kendrick_data";
            statement_2.execute(KM_table_drop);
            statement_2.close();
            return true;
        } catch (SQLException kendrick_table_error) {
            kendrick_table_error.printStackTrace();
            return false;
        }
    }
    
    // -----> CREATE KENDRICK MASS TABLE <-----
    
    // Creates new Kendrick table connected to the already existing compounds table through a foreign key
    public boolean Create_Kendrick_Data_table() {
        try {
            Statement statement_1 = this.sql_connection.createStatement();
            String table_1 = "CREATE TABLE Kendrick_data "
                                    + "(compound_id INT REFERENCES compounds.compounds (compound_id), "
                                    + "kendrick_mass REAL NOT NULL, "
                                    + "kmd REAL NOT NULL,"
                                    + "molecular_weight REAL NOT NULL)";
            statement_1.execute(table_1);
            statement_1.close();
            return true;
        } catch (SQLException table_error) {
            table_error.printStackTrace();
            return false;
        }
    }
    
    // -----> INSERT METHODS <----- 
    
    // Insert Kendrick Mass instances in the previously created Kendrick Mass table.
    public boolean Insert_new_KendrickData(Compound compound) {
        try {
            String table = "INSERT INTO Kendrick_data (compound_id, kendrick_mass, kmd, molecular_weight) " + "VALUES (?,?,?,?);";
            PreparedStatement template_0 = this.sql_connection.prepareStatement(table);
            template_0.setInt(1, compound.getCompound_id());
            template_0.setDouble(2, compound.getKendrick_mass());
            template_0.setDouble(3, compound.getKmd());
            template_0.setDouble(4, compound.getMolecular_weight());
            template_0.executeUpdate();
            template_0.close();
            return true;
        } catch (SQLException new_kendrick_data_error) {
            new_kendrick_data_error.printStackTrace();
            return false;
        }
    }
    
    // -----> SELECT METHODS <----- 
    
    // Returns a ArrayList with all the compounds
    public ArrayList<Compound> List_all_Compounds() {
        try {
            Statement statement_3 = this.sql_connection.createStatement();
            String SQL_code = "SELECT compound_id, formula, mass FROM compounds.compounds WHERE formula LIKE 'C%H%' "
                    + "AND formula NOT LIKE '%)n%' AND formula NOT LIKE '%Mo%' AND formula NOT LIKE '%Na%' AND formula NOT LIKE '%R%' "
                    + "AND formula NOT LIKE '%X%' AND formula NOT LIKE '%L%'  AND formula NOT LIKE '%I%' AND formula NOT LIKE '%Hg%' "
                    + "AND formula NOT LIKE '%F%' AND formula NOT LIKE '%Te%' AND formula NOT LIKE '%.%' AND formula NOT LIKE '%Ca%' "
                    + "AND formula NOT LIKE '%D%' AND formula NOT LIKE '%e%' AND formula NOT LIKE '%As%' AND formula NOT LIKE '%Ag%' "
                    + "AND formula NOT LIKE '%B%' AND formula NOT LIKE '%-%' AND formula NOT LIKE '%Cu%'"
                    + "AND formula IS NOT NULL AND MASS IS NOT NULL";
            List<Compound> compound_list = new ArrayList<>();
            ResultSet result_set = statement_3.executeQuery(SQL_code);
            while (result_set.next()) {
                Compound compound = new Compound();  
                compound.setCompound_id(result_set.getInt("compound_id"));
                compound.setFormula(result_set.getString("formula"));
                compound.setMass(result_set.getDouble("mass"));
                compound_list.add(compound);
            }
            statement_3.close();
            return (ArrayList<Compound>) compound_list;
        } catch (SQLException list_compounds_error) {
            list_compounds_error.printStackTrace();
            return null;
        }
    }
    
    // Returns a ArrayList with all the Kendrick data
    public ArrayList<Compound> List_all_KendrickData() {
        try {
            Statement statement_4 = this.sql_connection.createStatement();
            String SQL_code = "SELECT compound_id, kendrick_mass, kmd FROM compounds.Kendrick_data";
            List<Compound> kendrick_list = new ArrayList<>();
            ResultSet result_set = statement_4.executeQuery(SQL_code);
            while (result_set.next()) {
                Compound compound = new Compound();  
                compound.setCompound_id(result_set.getInt("compound_id"));
                compound.setKendrick_mass(result_set.getDouble("kendrick_mass"));
                compound.setKmd(result_set.getDouble("kmd"));
                kendrick_list.add(compound);
            }
            statement_4.close();
            return (ArrayList<Compound>) kendrick_list;
        } catch (SQLException list_kendrick_data_error) {
            list_kendrick_data_error.printStackTrace();
            return null;
        }
    }
    
    // Returns a List with the selected lipid types as Strings.
    public List<String> List_Lipid_Types() {
        try {
            Statement statement_5 = this.sql_connection.createStatement();
            String SQL_code = "SELECT DISTINCT (lipid_type) FROM compounds_lipids_classification WHERE lipid_type = 'MG(' "
                    + "OR lipid_type = 'DG(' OR lipid_type = 'TG(' OR lipid_type = 'CE(' OR lipid_type = 'PA(' OR lipid_type = 'PC(' "
                    + "OR lipid_type = 'PE(' OR lipid_type = 'PI(' OR lipid_type = 'PG(' OR lipid_type = 'PS('";
            List<String> lipid_type_list = new ArrayList<>();
            ResultSet result_set = statement_5.executeQuery(SQL_code);
            while (result_set.next()) {
                String type = result_set.getString("lipid_type");
                lipid_type_list.add(type);
            }
            statement_5.close();
            return (List<String>) lipid_type_list;
        } catch (SQLException list_compounds_error) {
            list_compounds_error.printStackTrace();
            return null;
        }
    
    }
    
    // Returns an ArrayList with the lipid data according to lipid family and number of double bonds.
    public ArrayList<Compound> List_LipidData(String lipid_type, int double_bonds) {
        try {
            String SQL_code = "SELECT formula, mass, molecular_weight, kendrick_mass, kmd, lipid_type, double_bonds FROM compounds.compounds A "
                    + "INNER JOIN Kendrick_data B ON A.compound_id = B.compound_id "
                    + "INNER JOIN compounds_lipids_classification C ON A.compound_id = C.compound_id "
                    + "WHERE C.lipid_type=? AND double_bonds=?";
            PreparedStatement template_1 = this.sql_connection.prepareStatement(SQL_code);
            template_1.setString(1, lipid_type);
            template_1.setInt(2, double_bonds);
            List<Compound> lipid_list = new ArrayList<>();
            ResultSet result_set = template_1.executeQuery();
            while(result_set.next()){
                Compound compound = new Compound();  
                compound.setFormula(result_set.getString("formula"));
                compound.setMass(result_set.getDouble("mass"));
                compound.setMolecular_weight(result_set.getDouble("molecular_weight"));
                compound.setKendrick_mass(result_set.getDouble("kendrick_mass"));
                compound.setKmd(result_set.getDouble("kmd"));
                compound.setLipid_type(result_set.getString("lipid_type"));
                compound.setDouble_bonds(result_set.getInt("double_bonds"));
                lipid_list.add(compound);
            }
            template_1.close();
            return (ArrayList<Compound>) lipid_list;
        } catch (SQLException List_all_LipidData_error) {
            List_all_LipidData_error.printStackTrace();
            return null;
        }
    }
    
    // Returns max double_bond value with all the lipid data
    public int Max_double_bonds(String lipid_type) {
        try {
            int max_db = 0;
            String SQL_code = "SELECT MAX(double_bonds) AS max_db FROM compounds.compounds_lipids_classification WHERE lipid_type=?";
            PreparedStatement template_1 = this.sql_connection.prepareStatement(SQL_code);
            template_1.setString(1, lipid_type);
            ResultSet result_set = template_1.executeQuery();
            if (result_set.next()) {
            max_db = result_set.getInt(1);
            }
            template_1.close();
            return max_db;
        } catch (SQLException Max_double_bonds_error) {
            Max_double_bonds_error.printStackTrace();
            return 0;
        }
    }
    
    public boolean check_db(int double_bonds) {
        try {
            String SQL_code = "SELECT * FROM compounds.compounds_lipids_classification WHERE double_bonds=?";
            PreparedStatement template_1 = this.sql_connection.prepareStatement(SQL_code);
            template_1.setInt(1, double_bonds);
            ResultSet result_set = template_1.executeQuery();
            if (result_set.next()) {
                return true;
            }
            template_1.close();
            return false;
        } catch (SQLException Max_double_bonds_error) {
            Max_double_bonds_error.printStackTrace();
            return false;
        }
    }
    
    // -----> SEACRH METHODS <-----
    
    // Selects compound objects with the same compound_id from the kendrick mass table and returns them
    public Compound Search_compound_by_id(int compound_id) {
        try {
            String SQL_code = "SELECT compound_id, formula, mass FROM compounds.compounds WHERE compound_id LIKE ?";
            PreparedStatement template_3 = this.sql_connection.prepareStatement(SQL_code);
            template_3.setInt(1, compound_id);
            ResultSet result_set = template_3.executeQuery();
            result_set.next();
            Compound compound = new Compound();
            compound.setCompound_id(result_set.getInt("compound_id"));
            compound.setFormula(result_set.getString("formula"));
            compound.setMass(result_set.getDouble("mass"));
            template_3.close();
            return compound;
        } catch (SQLException search_compound_error) {
            search_compound_error.printStackTrace();
            return null;
        }
    }
    
    // -----> CLOSE CONNECTION METHOD <-----
    
    public boolean Close_connection() {
        try {
            this.sql_connection.close();
            return true;
        } catch (SQLException close_connection_error) {
            close_connection_error.printStackTrace();
            return false;
        }
    }
}