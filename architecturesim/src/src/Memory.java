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
    
    private Cache headPointer;
    
    public Memory() {
        this(true);
    }
    
    public Memory(boolean cache_enabled) {
        if (cache_enabled){
            DRAM    = new Cache(Utils.size_DRAM , null      , Utils.wait_DRAM); 
            L2Cache = new Cache(Utils.size_l2   , DRAM      , Utils.wait_l2); 
            L1Cache = new Cache(Utils.size_l1   , L2Cache   , Utils.wait_l1);
        
            headPointer = L1Cache;
        } else {
            DRAM = new Cache(Utils.size_DRAM, null, Utils.wait_DRAM);
            headPointer = DRAM;
        }
        setHeirarchy();
    }
    
    private void setHeirarchy(){
        int counter = 0;
        Cache pointer = headPointer;
        while(pointer != null){
            pointer.setHeirarchy(counter);
            counter++;
            pointer = pointer.getNextCache();
        }
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
    
    private int addressInMemoryLevel(int address) throws NoSuchMemoryLocationException{
        // if address is in array, return true, else return false
        Cache currPointer = headPointer;
        int currLevel = currPointer.getHeirarchy();
        
        if (currLevel == 0){
            int tag_bit = address & Utils.tagMask_l1;
            int index_bit = address & Utils.indexMask_l1;
            
            if (tag_bit == currPointer.getTagArray()[index_bit]){
                return currLevel;
            }
        }
        currPointer = currPointer.getNextCache();
        if (currLevel == 1){          
            int tag_bit = address & Utils.tagMask_l2;
            int index_bit = address & Utils.indexMask_l2;
                
            if (tag_bit == currPointer.getTagArray()[index_bit]){
                return currLevel;
            }
        } else {
            if (isDRAM)
                return true;   
            else 
                throw new NoSuchMemoryLocationException(address);
        }
    }
    
}
