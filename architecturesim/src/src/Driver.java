/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src;

/**
 *
 * @author jjaikumar
 */

// NOT ENABLED BY DEFAULT
public class Driver {


    public Driver(){
        main(new String[0]);
        
        // if data is not in L1Cache, when it is found and returned from DRAM, write it into the correct spot in cache
        // access the same data again and should find it in the cache
        
        // how does L2 memory get populated? Does every L1Cache write just get trickled down to the lower levels?
    }
    
    public static void main(String[] args){
        CPU cpu = new CPU();
        System.out.println("Booting up...");
    }
}
