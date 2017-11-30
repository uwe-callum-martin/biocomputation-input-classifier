/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biocomputation;

/**
 *
 * @author Callum
 */
//Changed var from string to int[] array. Allows for easier comparison 
public class row32 {
    
    int[] var = new int[5];
    int predicted;
    
    public row32(int[] v, int p){
    var = v;
    predicted = p;
}
    public int [] getInputVariable(){
        return var;
    }
    
    public int getPredicted(){
        return predicted;
    }
    
    
    
}
