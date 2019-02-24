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
public class Memory {
    public Cache DRAM; 
    public Cache L1Cache;
    public Cache L2Cache;
    public Cache currMemoryLevel;
    public Cache topLevelMemory;
    
    
    public Memory() {
        DRAM = new Cache(65536, null, 100); // 2^16 Bytes
        L2Cache = new Cache(32768,DRAM,30); // 2^15 Bytes
        L1Cache = new Cache(16384,L2Cache,4); // 2&14 Bytes
        currMemoryLevel = L1Cache;
        topLevelMemory = L1Cache;
        
        // read data in L1Cache
        // write data in L1Cache
        // read data in DRAM
        // write data in DRAM
    }
    
}
