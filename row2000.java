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
//-------------------
//Would it be easier, for this class alone, to have var be a double or a float?
//?????????????????????????????????????

public class row2000 {
    
     double [] var;
    int predicted;
    
    public row2000(double [] v, int p){
    var = v;
    predicted = p;
}
    public double [] getInputVariable(){
        return var;
    }
    public int getPredicted(){
        return predicted;
    }
}
