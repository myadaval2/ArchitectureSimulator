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
        DRAM = new Cache(65536, null, 100); // 2^16 Bytes
        L2Cache = new Cache(32768,DRAM,30); // 2^15 Bytes
        L1Cache = new Cache(16384,L2Cache,4); // 2&14 Bytes
        
    }
       public int getAddressInMemory(int address) throws NoSuchMemoryLocationException{
        
        boolean exists = false;
        try {
            exists = addressInCurrMemoryLevel(address);
        } catch (NoSuchMemoryLocationException e) {
            throw e;
        }
        
        if (!exists) {
            return this.lowerLevelMemory.getAddressInMemory(address);
        } 
        while (this.counter != 0){
            delayCounter(); 
        } 
 
        if (isDRAM){
           char temp = mem_array[address];
           writeToL1(temp, address);
           return temp;  
        }  
        else {
            char MASK_INDEX = 0;
            if (this.levelsFromMain == 2) { // L1 Cache
                MASK_INDEX = 16383; // BINARY 0011 1111 1111 1111
                
            } else { // levelsFromMain = 1, L2 Cache
                MASK_INDEX = 32767; // BINARY 0111 1111 1111 1111
            }
            int index_bit = address & MASK_INDEX;
                
            return mem_array[index_bit];
        }
    }
    public void writeAddressInMemory(char data, int address) throws NoSuchMemoryLocationException {
        boolean exists = false;
        try {
            exists = addressInCurrMemoryLevel(address);
        } catch (NoSuchMemoryLocationException e) {
            throw e;
        }
        
        if (!exists) {
            this.lowerLevelMemory.writeAddressInMemory(data, address);
        } 
        while (this.counter != 0){
            delayCounter(); 
        } 
 
        if (isDRAM) {
            mem_array[address] = data; 
        }
        else {
            char MASK_INDEX = 0;
            if (this.levelsFromMain == 2) { // L1 Cache
                MASK_INDEX = 16383; // BINARY 0011 1111 1111 1111
                
            } else { // levelsFromMain = 1, L2 Cache
                MASK_INDEX = 32767; // BINARY 0111 1111 1111 1111
            }
            int index_bit = address & MASK_INDEX;
            mem_array[index_bit] = data;
        }
    }
    
}
class NoSuchMemoryLocationException extends Exception {
    int address;
    public NoSuchMemoryLocationException(int address){
        this.address = address;
        System.out.println("No such memory location exists: "+ address);
    }
    public NoSuchMemoryLocationException(){
        System.out.println("No such memory location exists");
    }
}