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
public class CPU {
    public Memory DRAM; 
    public Memory L1Cache;
    public Memory L2Cache;
    public Memory currMemoryLevel;
    public Memory topLevelMemory;
    
    public CPU() {
        DRAM = new Memory(65536, null, 100); // 2^16 Bytes
        L2Cache = new Memory(32768,DRAM,30); // 2^15 Bytes
        L1Cache = new Memory(16384,L2Cache,4); // 2&14 Bytes
        currMemoryLevel = L1Cache;
        topLevelMemory = L1Cache;
        // initialize DRAM
        // initialize L1Cache
        // read data in L1Cache
        // write data in L1Cache
        // read data in DRAM
        // write data in DRAM
    }
    
}
