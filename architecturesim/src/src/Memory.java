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
    
    public Memory() {
        // Need to check memory sizes
        DRAM = new Cache(131072, null, 100); // 2^16 Bytes
        L2Cache = new Cache(65536,DRAM,30); // 2^15 Bytes
        L1Cache = new Cache(32768,L2Cache,4); // 2&14 Bytes  
    }
}
