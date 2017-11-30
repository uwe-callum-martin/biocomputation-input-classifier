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
public class row64 {
    
    
     int[] var = new int [6];
    int predicted;
    
    public row64(int [] v, int p){
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
