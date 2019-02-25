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
    
    public Cache L1Cache;
    public Cache L2Cache;
    public Cache DRAM;
    
    private Cache currPointer;
    
    public Memory() {
        DRAM    = new Cache(Utils.size_DRAM , null      , Utils.wait_DRAM); 
        L2Cache = new Cache(Utils.size_l2   , DRAM      , Utils.wait_l2); 
        L1Cache = new Cache(Utils.size_l1   , L2Cache   , Utils.wait_l1);
        
        currPointer = L1Cache;
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
    
    private Boolean addressInCurrMemoryLevel(int address) throws NoSuchMemoryLocationException{
        // if address is in array, return true, else return false
        if (this.levelsFromMain() == 2){
            final char MASK_TAG = 49152; // BINARY 1100 0000 0000 0000
            final char MASK_INDEX = 16383; // BINARY 0011 1111 1111 1111
                
            int tag_bit = address & MASK_TAG;
            int index_bit = address & MASK_INDEX;
            
            return tag_bit == tag_array[index_bit];
        }
        else if (this.levelsFromMain == 1){          
            final char MASK_TAG = 32768; // hex: 8000, binary 1000 0000 0000 0000
            final char MASK_INDEX = 32767; // hex: 7FFF, binary 0111 1111 1111 1111
        
            int tag_bit = address & MASK_TAG;
            int index_bit = address & MASK_INDEX;
                
            return tag_bit == tag_array[index_bit];
        } else {
            if (isDRAM)
                return true;   
            else 
                throw new NoSuchMemoryLocationException(address);
        }
    }
    
}