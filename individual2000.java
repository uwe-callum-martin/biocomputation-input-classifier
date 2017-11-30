/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biocomputation;

import static biocomputation.classification2000.N;

/**
 *
 * @author Callum
 */
public class individual2000 {
    
    double [] genes;
    int fitness;
    
    public individual2000 () {
        
        genes = new  double [130];
                
            
        }
    
    //Getters and setters
    public int getFitness (){
    
    //System.out.println(fitness);
    return fitness;
}
    
    public void setFitness(int nfitness){
        fitness = nfitness;
        
    }
    
    
    public double getGenes(int index){
        return genes[index];
    }
    public void setGenes(int index, double value){
        genes[index] = value;
    }
    public int getGenesLength(){
        return genes.length;
    }
    
    
}
