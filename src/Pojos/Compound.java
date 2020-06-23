/*
 * This file contains the necessary method to create the different Compound object needed throughout the program.
 */

package Pojos;

import java.util.ArrayList;
import java.util.List;

/*
 *
 * @author enriquetunon
 */
public class Compound {
    
    private int compound_id;
    private double mass;
    private double molecular_weight;
    private String formula;
    private double kendrick_mass;
    private double kmd;
    private List<Integer> id_list;
    private String lipid_type;
    private int double_bonds;
    
    public Compound() {
        super();
        this.compound_id = 0;
        this.mass = 0.00;
        this.molecular_weight = 0.00;
        this.formula = "/";
        this.kendrick_mass = 0.00;
        this.kmd = 0.00;
        this.lipid_type = "/";
        this.double_bonds = 0;
        this.id_list = new ArrayList<>();
    }
    
    // Compound constructor
    public Compound(int compound_id, double mass, double molecular_weight, String formula, List<Integer> id_list) {
        super();
        this.compound_id = compound_id;
        this.mass = mass;
        this.molecular_weight = molecular_weight;
        this.formula = formula;
        this.id_list = id_list;
    }
    
    // Kendrick data constructor
    public Compound(int compound_id, double kendrick_mass, double kmd, double molecular_weight) {
        super();
        this.compound_id = compound_id;
        this.kendrick_mass = kendrick_mass;
        this.kmd = kmd;
        this.molecular_weight = molecular_weight;
    }
    
    // Full constructor
    public Compound(int compound_id, double mass, double molecular_weight, String formula, double kendrick_mass, double kmd, List<Integer> id_list) {
        super();
        this.compound_id = compound_id;
        this.mass = mass;
        this.molecular_weight = molecular_weight;
        this.formula = formula;
        this.kendrick_mass = kendrick_mass;
        this.kmd = kmd;
        this.id_list = id_list;
    }
    
    public int getCompound_id() {
        return compound_id;
    }

    public void setCompound_id(int compound_id) {
        this.compound_id = compound_id;
    }

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public double getMolecular_weight() {
        return molecular_weight;
    }

    public void setMolecular_weight(double molecular_weight) {
        this.molecular_weight = molecular_weight;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public double getKendrick_mass() {
        return kendrick_mass;
    }

    public void setKendrick_mass(double kendrick_mass) {
        this.kendrick_mass = kendrick_mass;
    }

    public double getKmd() {
        return kmd;
    }

    public void setKmd(double kmd) {
        this.kmd = kmd;
    }
    
    public List<Integer> getId_list() {
        return id_list;
    }

    public void setId_list(List<Integer> id_list) {
        this.id_list = id_list;
    }
    
    public void addIdToIdList(int compound_id) {
        id_list.add(compound_id);
    }

    public String getLipid_type() {
        return lipid_type;
    }

    public void setLipid_type(String lipid_type) {
        this.lipid_type = lipid_type;
    }

    public int getDouble_bonds() {
        return double_bonds;
    }

    public void setDouble_bonds(int double_bonds) {
        this.double_bonds = double_bonds;
    }
    
    @Override
    public String toString() {
        return "Compound data [ id: " + this.compound_id +  ", Mass: " + this.mass + ", Molecular weight: " + this.molecular_weight + 
                ", Formula: " + this.formula + ", Kendrick mass: " + this.kendrick_mass + ", Kendrick mass defect: " + this.kmd + 
                ", id list: " + this.id_list + "]";
    }    

    public String toString2() {
        return "Kendrick data [ id: " + this.compound_id + ", Kendrick mass: " + this.kendrick_mass + ", Kendrick mass defect: " + this.kmd + "]";
    }

    public String toString3() {
        return "Compound data [ Mass: " +this.mass+ ", Formula: " +this.formula+ ", Molecular weight: " +this.molecular_weight+ ", Kendrick mass: " +this.kendrick_mass+ 
                ", KMD: " +this.kmd+ ", lipid type: " + this.lipid_type + ", double bonds: " + this.double_bonds + "]";
    }
}