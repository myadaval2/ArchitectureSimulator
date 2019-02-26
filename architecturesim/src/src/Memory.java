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
    private boolean cacheEnabled;
    private int memoryCycleCount;
    
    public Memory() {   this(true); }
    
    public Memory(boolean cacheEnabled) {
        this.memoryCycleCount = 0;
        this.cacheEnabled = cacheEnabled;
        if (cacheEnabled){
            DRAM    = new Cache(Utils.size_DRAM , null      , Utils.wait_DRAM   , 0); 
            L2Cache = new Cache(Utils.size_l2   , DRAM      , Utils.wait_l2     , 1); 
            L1Cache = new Cache(Utils.size_l1   , L2Cache   , Utils.wait_l1     , 2);
        
            headPointer = L1Cache;
        } else {
            DRAM = new Cache(Utils.size_DRAM, null, Utils.wait_DRAM, 0);
            headPointer = DRAM;
        }
    }
    
    public void writeAddressInMemory(char data, int address) throws NoSuchMemoryLocationException{
        if (cacheEnabled){
            writeToL1(data, address);
            writeToL2(data, address);
            writeToDRAM(data, address);
        } else {
            writeToDRAM(data, address);
        }
    }
    
    public int readAddressInMemory(int address) throws NoSuchMemoryLocationException{
        int exists = -1;
        try {
            exists = addressInMemory(address);
        } catch (NoSuchMemoryLocationException e) {
            throw e;
        }
        Cache pointer = headPointer;
        while (pointer.getHeirarchy() != exists){
            pointer = pointer.getNextCache();
        }
        this.memoryCycleCount += pointer.getWaitCycles();
 
        if (pointer.getHeirarchy() == 0){
           char readData = DRAM.getMemArray()[address];
           if (cacheEnabled){
                writeToL1(readData, address);
                writeToL2(readData, address);  
           }
           return readData;  
        }  
        else {
            char MASK_INDEX = 0;
            if (pointer.getHeirarchy() == 2) { // L1 Cache
                MASK_INDEX = Utils.indexMask_l1; 
                
            } else { //  L2 Cache
                MASK_INDEX = Utils.indexMask_l2; 
            }
            int index_bit = address & MASK_INDEX;
                
            return pointer.getMemArray()[index_bit];
        }
    }
    
    public int addressInMemory(int address) throws NoSuchMemoryLocationException{
        if (cacheEnabled){
            return addressInCacheLevel(address);
        } else {
            return addressInDRAM(address);
        }
    }
    
    private void writeToL1(char data, int address) {
        int tag_bit = address & Utils.tagMask_l1;
        int index_bit = address & Utils.indexMask_l1;
        L1Cache.setTagArray(tag_bit, index_bit);
        L1Cache.setData(data, index_bit);
    }
    private void writeToL2(char data, int address) {
        int tag_bit = address & Utils.tagMask_l2;
        int index_bit = address & Utils.indexMask_l2;
        L2Cache.setTagArray(tag_bit, index_bit);
        L2Cache.setData(data, index_bit);
    }
    private void writeToDRAM(char data, int address){
        this.memoryCycleCount += DRAM.getWaitCycles();
        DRAM.setData(data, address);
    }
    
    private boolean isValidAddress(int address){
        return true;
    }
    
    private int addressInDRAM(int address) throws NoSuchMemoryLocationException{
        if (isValidAddress(address)){
            return 0;
        } else {
            throw new NoSuchMemoryLocationException(address);
        }
    }
    
    private int addressInCacheLevel(int address) throws NoSuchMemoryLocationException {
        // if address is in array, return true, else return false
        Cache currPointer = headPointer;
        int currLevel = currPointer.getHeirarchy();
        
        if (currLevel == 2){
            int tag_bit = address & Utils.tagMask_l1;
            int index_bit = address & Utils.indexMask_l1;
            
            if (tag_bit == currPointer.getTagArray()[index_bit]){
                return currLevel;
            }
        }
        currPointer = currPointer.getNextCache();
        currLevel = currPointer.getHeirarchy();
        if (currLevel == 1){          
            int tag_bit = address & Utils.tagMask_l2;
            int index_bit = address & Utils.indexMask_l2;
                
            if (tag_bit == currPointer.getTagArray()[index_bit]){
                return currLevel;
            }
        } 
        return(addressInDRAM(address));
    }
    
    public int getMemoryCycleCount()    {   return this.memoryCycleCount;   }
    
}