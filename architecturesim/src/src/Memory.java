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
    
    public Cache    L1Cache;
    public Cache    L2Cache;
    public Cache    DRAM;
    private Cache   headPointer;
    private boolean cacheEnabled;
    private int     memoryCycleCount;
    
    public static Memory memory = new Memory(true);
    
    public Memory() {   this(true);     }
    
    private Memory(boolean cacheEnabled) {
        this.memoryCycleCount = 0;
        this.cacheEnabled = cacheEnabled;
        if (cacheEnabled){
            DRAM    = new Cache(Utils.SIZE_DRAM , null      , Utils.WAIT_DRAM   , 0); 
            L2Cache = new Cache(Utils.SIZE_L2   , DRAM      , Utils.WAIT_L2     , 1); 
            L1Cache = new Cache(Utils.SIZE_L1   , L2Cache   , Utils.WAIT_L1     , 2);
        
            headPointer = L1Cache;
        } else {
            DRAM = new Cache(Utils.SIZE_DRAM, null, Utils.WAIT_DRAM, 0);
            headPointer = DRAM;
        }
    }
    
    public static Memory getMemory() {
        return memory;
    }
    
    public void writeAddressInMemory(int data, int address) throws NoSuchMemoryLocationException{
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
            int readData = DRAM.getMemArray()[address];
            if (cacheEnabled){
                writeToL1(readData, address);
                writeToL2(readData, address);  
            }
            return readData;  
        }  
        else {
            int MASK_INDEX = 0;
            int MASK_TAG = 0;
            if (pointer.getHeirarchy() == 2) { // L1 Cache
                MASK_TAG = Utils.TAG_MASK_L1; 
                MASK_INDEX = Utils.INDEX_MASK_L1;
                
            } else { //  L2 Cache
                MASK_TAG = Utils.TAG_MASK_L2; 
                MASK_INDEX = Utils.INDEX_MASK_L2;
            }
            int tag_bit = address & MASK_TAG;
            int index_bit = address & MASK_INDEX;
            int cacheIndex = index_bit * 2;
            for (int i = cacheIndex; i < cacheIndex + Utils.N_SET; i++) {
                if (tag_bit == pointer.getTagArray()[cacheIndex]) {
                    pointer.updateHistoryArray(cacheIndex, 1);
                    int word = 0;
                    if (address % 2 == 0) {
                        word = (pointer.getMemArray()[cacheIndex] & Utils.WORD_0) >> 16;
                    }
                    else if (address % 2 == 1) {
                        word =  (pointer.getMemArray()[cacheIndex] & Utils.WORD_1);
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
        for (int cacheIndex = i; cacheIndex < i + Utils.N_SET; cacheIndex++) {
            if (tag == cacheLevel.getTagArray()[cacheIndex]) {
                cacheIndexToReplace = cacheIndex;
                cacheLevel.updateHistoryArray(cacheIndexToReplace, 1);

                return cacheIndexToReplace;
            }
        }
        for (int cacheIndex = i; cacheIndex < i + Utils.N_SET; cacheIndex++) {
            if (min > cacheLevel.getHistoryArray()[cacheIndex]) {
                min = cacheLevel.getHistoryArray()[cacheIndex];
                cacheIndexToReplace = cacheIndex;      
            }
        }

        cacheLevel.updateHistoryArray(cacheIndexToReplace, 0);
        return cacheIndexToReplace;
    }
    
    private void writeToL1(int data, int address) {
        int tag_bit = address & Utils.TAG_MASK_L1;
        int index_bit = (address & Utils.INDEX_MASK_L1) >> 1;
        int dataToWrite = 0;
        int cacheIndexToReplace = checkCacheHistoryForReplacement(index_bit, tag_bit, L1Cache);
        if (address % 2 == 0x0) {
            dataToWrite =  ((((int) data << 16) & Utils.WORD_0) | (L1Cache.getMemArray()[cacheIndexToReplace] & Utils.WORD_1));
        }
        else {
            dataToWrite =  ((int) (data & Utils.WORD_1) | (L1Cache.getMemArray()[cacheIndexToReplace] & Utils.WORD_0));
        }
        L1Cache.setTagArray(tag_bit, cacheIndexToReplace);
        L1Cache.setData(dataToWrite, cacheIndexToReplace);
        this.memoryCycleCount += L1Cache.getWaitCycles();
    }
    private void writeToL2(int data, int address) {
        int tag_bit = address & Utils.TAG_MASK_L2;
        int index_bit = (address & Utils.INDEX_MASK_L2) >> 1;
        int dataToWrite = 0;
        int cacheIndexToReplace = checkCacheHistoryForReplacement(index_bit, tag_bit, L2Cache);
        if (address % 2 == 0x0) {
            dataToWrite =  ((((int) data << 16) & Utils.WORD_0) | (L2Cache.getMemArray()[cacheIndexToReplace] & Utils.WORD_1));
        }
        else {
            dataToWrite =  ((int) (data & Utils.WORD_1) | (L2Cache.getMemArray()[cacheIndexToReplace] & Utils.WORD_0));
        }
        L2Cache.setTagArray(tag_bit, cacheIndexToReplace);
        L2Cache.setData(dataToWrite, cacheIndexToReplace);
        this.memoryCycleCount += L2Cache.getWaitCycles();
    }
    private void writeToDRAM(int data, int address){
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
            int tag_bit = address & Utils.TAG_MASK_L1;
            int index_bit = address & Utils.INDEX_MASK_L1;
            int cacheIndex = index_bit * 2;
            for (int i = cacheIndex; i < cacheIndex + Utils.N_SET; i++) {
                if (tag_bit == currPointer.getTagArray()[cacheIndex])
                    return currLevel;
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
            int cacheIndex = index_bit * 2;
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
    
    public int getMemoryCycleCount()    {   return this.memoryCycleCount;   }
    
}