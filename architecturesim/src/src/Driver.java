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
    public Memory DRAM; 
    public Memory L1Cache;
    public Memory currMemoryLevel;
    public Memory topLevelMemory;
    
    public Driver(){
        System.out.println("Booting up...");
        DRAM = new Memory(128, null, 100);
        L1Cache = new Memory(64, DRAM, 4);
        currMemoryLevel = L1Cache;
        topLevelMemory = L1Cache;
        // initialize DRAM
        // initialize L1Cache
        // read data in L1Cache
        // write data in L1Cache
        // read data in DRAM
        // write data in DRAM
        
        // if data is not in L1Cache, when it is found and returned from DRAM, write it into the correct spot in cache
        // access the same data again and should find it in the cache
        
        // how does L2 memory get populated? Does every L1Cache write just get trickled down to the lower levels?
    }
}
