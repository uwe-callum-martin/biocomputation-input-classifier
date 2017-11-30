/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biocomputation;

import static biocomputation.classification64.N;

/**
 *
 * @author Callum
 */
public class individual64 {
    
    int [] genes;
    int fitness;
    
    public individual64 () {
        
        genes = new  int [N];
                
            
        }
    
    //Getters and setters
    public int getFitness (){
    
    //System.out.println(fitness);
    return fitness;
}
    
    public void setFitness(int nfitness){
        fitness = nfitness;
        
    }
    
    
    public int getGenes(int index){
        return genes[index];
    }
    public void setGenes(int index, int value){
        genes[index] = value;
    }
    public int getGenesLength(){
        return genes.length;
    }
    
    
}
