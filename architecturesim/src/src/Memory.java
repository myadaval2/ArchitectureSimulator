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
    
    public static Cache    L1Cache;
    public Cache    L2Cache;
    public static Cache    DRAM;
    private static Cache   headPointer;
    private static boolean cacheEnabled;
    private static long     memoryCycleCount;
    
    public static Memory memory = new Memory();
    
    private Memory() {
        memoryCycleCount = 0;
        DRAM    = new Cache(Utils.SIZE_DRAM , null      , Utils.WAIT_DRAM   , 0); 
        L2Cache = new Cache(Utils.SIZE_L2   , DRAM      , Utils.WAIT_L2     , 1); 
        L1Cache = new Cache(Utils.SIZE_L1   , L2Cache   , Utils.WAIT_L1     , 2);
    }
    
    public void reset(){
        DRAM.clear(Utils.SIZE_DRAM);
        L2Cache.clear(Utils.SIZE_L2);
        L1Cache.clear(Utils.SIZE_L1);
    }
    
    public static void resetMemoryCycleCount() {
        memoryCycleCount = 0;
    }
    
    public static void setCacheEnabled(boolean status) {
        if (status) {
            cacheEnabled = status;
            headPointer = L1Cache;
        }
        else {
            cacheEnabled = status;
            headPointer = DRAM;
        }
    }
    
    public static boolean getCacheEnabled() {
        return cacheEnabled;
    }
    
    public static Memory getMemory() {
        return memory;
    }
    
    public void writeAddressInMemory(int data, int address) throws NoSuchMemoryLocationException{
        if (cacheEnabled){
            // System.out.println("Cache is enabled");
            // write through no-allocate
            int exists = -1;
            try {
                exists = addressInMemory(address);
            } catch (NoSuchMemoryLocationException e) {
                throw e;
            }
            
            writeToDRAM(data, address);
            
            Cache pointer = headPointer;
            while (pointer.getHeirarchy() != exists){
                pointer = pointer.getNextCache();
            }
            
            if (pointer.getHeirarchy() != 0) {
                writeToL1(address);
                writeToL2(address);
            }            
        } 
        else {
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
        memoryCycleCount += pointer.getWaitCycles();
        if (pointer.getHeirarchy() == 0){
            int readData = DRAM.getMemArray()[address];
            if (cacheEnabled) {
                writeToL1(address);
                writeToL2(address);  
            }
            return readData;  
        }  
        else {
            int MASK_INDEX;
            int MASK_TAG;
            if (pointer.getHeirarchy() == 2) { // L1 Cache
                MASK_TAG = Utils.TAG_MASK_L1; 
                MASK_INDEX = Utils.INDEX_MASK_L1;
                
            } else { //  L2 Cache
                MASK_TAG = Utils.TAG_MASK_L2; 
                MASK_INDEX = Utils.INDEX_MASK_L2;
            }
            int tag_bit = address & MASK_TAG;
            int index_bit = address & MASK_INDEX;
            int cacheIndex = (index_bit >> 1) * 2;
            
            for (int i = cacheIndex; i < cacheIndex + Utils.N_SET; i++) {
                if (tag_bit == pointer.getTagArray()[cacheIndex]) {
                    pointer.updateHistoryArray(cacheIndex, 1);
                    int word = 0;
                    if (address % 2 == 0) {
                        word = (pointer.getMemArray()[cacheIndex] & Utils.WORD_0) >> 16;
                    }
                    else if (address % 2 == 1) {
                        word =  (pointer.getMemArray()[cacheIndex]) & Utils.WORD_1;
                    }
                    return word;
                }
            }
        }
        return 0;
    }
    
    public int addressInMemory(int address) throws NoSuchMemoryLocationException{
        if (cacheEnabled){
            return addressInCacheLevel(address);
        } else {
            return addressInDRAM(address);
        }
    }
    
    private int checkCacheHistoryForReplacement(int index, int tag, Cache cacheLevel) {
        // if address already in memory, need to replace that address regardless of replacement policy
        // tag matching
        int i = index * Utils.N_SET;
        int min = 99999;
        int cacheIndexToReplace = -1;
        // Already in cache
        for (int cacheIndex = i; cacheIndex < i + Utils.N_SET; cacheIndex++) {
            if (tag == cacheLevel.getTagArray()[cacheIndex]) {
                cacheIndexToReplace = cacheIndex;
                cacheLevel.updateHistoryArray(cacheIndexToReplace, 1);
                return cacheIndexToReplace;
            }
        }
        // Not in cache, need to bring it in
        for (int cacheIndex = i; cacheIndex < i + Utils.N_SET; cacheIndex++) {
            if (min > cacheLevel.getHistoryArray()[cacheIndex]) {
                min = cacheLevel.getHistoryArray()[cacheIndex];
                cacheIndexToReplace = cacheIndex;      
            }
        }
        // age all entries
        if (cacheLevel.getHeirarchy() == 2) {
            for (int cacheIndex = 0; cacheIndex < Utils.SIZE_L1; cacheIndex++) {
                if (cacheLevel.getHistoryArray()[cacheIndex] > 0) {
                    cacheLevel.updateHistoryArray(cacheIndex, -1);
                }
            }
        }
        if (cacheLevel.getHeirarchy() == 1) {
            for (int cacheIndex = 0; cacheIndex < Utils.SIZE_L2; cacheIndex++) {
                if (cacheLevel.getHistoryArray()[cacheIndex] > 0) {
                    cacheLevel.updateHistoryArray(cacheIndex, -1);
                }
            }
        }
        
        cacheLevel.updateHistoryArray(cacheIndexToReplace, 0);
        return cacheIndexToReplace;
    }
    
    private void writeToL1(int address) {
        int tag_bit = address & Utils.TAG_MASK_L1;
        int index_bit = (address & Utils.INDEX_MASK_L1) >> 1;
        int dataToWrite;
        int cacheIndexToReplace = checkCacheHistoryForReplacement(index_bit, tag_bit, L1Cache);
        if (address % 2 == 0x0) {
            dataToWrite = DRAM.getMemArray()[address] << 16 | DRAM.getMemArray()[address+1];
        }
        else {
            dataToWrite =  DRAM.getMemArray()[address-1] << 16 | DRAM.getMemArray()[address];
        }
        
        L1Cache.setTagArray(tag_bit, cacheIndexToReplace);
        L1Cache.setData(dataToWrite, cacheIndexToReplace);
        memoryCycleCount += L1Cache.getWaitCycles();
    }
    
    private void writeToL2(int address) {
        int tag_bit = address & Utils.TAG_MASK_L2;
        int index_bit = (address & Utils.INDEX_MASK_L2) >> 1;
        int dataToWrite;
        int cacheIndexToReplace = checkCacheHistoryForReplacement(index_bit, tag_bit, L2Cache);
        if (address % 2 == 0x0) {
            dataToWrite = DRAM.getMemArray()[address] << 16 | DRAM.getMemArray()[address+1];
        }
        else {
            dataToWrite =  DRAM.getMemArray()[address-1] << 16 | DRAM.getMemArray()[address];
        }
        L2Cache.setTagArray(tag_bit, cacheIndexToReplace);
        L2Cache.setData(dataToWrite, cacheIndexToReplace);
        memoryCycleCount += L2Cache.getWaitCycles();
    }
    
    private void writeToDRAM(int data, int address){
        memoryCycleCount += DRAM.getWaitCycles();
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
            int tag_bit = address & Utils.TAG_MASK_L1;
            int index_bit = address & Utils.INDEX_MASK_L1;
            int cacheIndex = (index_bit >> 1) * 2;
            for (int i = cacheIndex; i < cacheIndex + Utils.N_SET; i++) {
                if (tag_bit == currPointer.getTagArray()[cacheIndex]) {
                    return currLevel;
                }
                else {
                    return 0;
                }
            }
        }
        currPointer = currPointer.getNextCache();
        currLevel = currPointer.getHeirarchy();
        if (currLevel == 1){          
            int tag_bit = address & Utils.TAG_MASK_L2;
            int index_bit = address & Utils.INDEX_MASK_L2;
            int cacheIndex = (index_bit >> 1) * 2;
            for (int i = cacheIndex; i < cacheIndex + Utils.N_SET; i++) {
                if (tag_bit == currPointer.getTagArray()[cacheIndex])
                    return currLevel;
                else {
                    return 0;
                }
            }
        }
        return(addressInDRAM(address));
    }
    
    public long getMemoryCycleCount()    {   return memoryCycleCount;   }
    
}