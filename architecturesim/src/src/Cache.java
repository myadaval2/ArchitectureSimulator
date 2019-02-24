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
public class Cache {
    // public int lineLength;
    private int sizeOfMemory;
    private Cache lowerLevelMemory;
    private int waitCycles;
    private int counter;
    private boolean fetchingMemory;
    private boolean isDRAM;
    private char[] mem_array;
    private int[] tag_array;
    private int levelsFromMain;
    
    // how do we want memory to look? vector of arrays? 2D array with row = 4 words, and 128 rows for DRAM?

    
    public Cache(int sizeOfMemory, Cache lowerLevelMemory, int waitCycles) {
        this.sizeOfMemory = sizeOfMemory;
        this.lowerLevelMemory = lowerLevelMemory;
        this.waitCycles = waitCycles;
        this.counter = waitCycles;
        this.isDRAM = false;
        
        this.mem_array = new char[sizeOfMemory];
        this.tag_array = new int[sizeOfMemory];
        
        if (lowerLevelMemory == null){
            this.isDRAM = true;
        }
        this.levelsFromMain = levelsFromMain();
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
    
    
    private void writeToL1(char data, int address){
        char MASK_INDEX = 16383;
        
        int index = address & MASK_INDEX;
        
        mem_array[index] = data;
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
        
    private int levelsFromMain(){
        if (this.isDRAM){
            return 0;
        }
        int temp = 0;
        Cache pointer = this;
        while (pointer.lowerLevelMemory != null){
            pointer = pointer.lowerLevelMemory;
            temp++;
        }
        return temp;
    }
    
    private void delayCounter() {
        this.counter = this.counter--;
    }
    
    public int getSizeOfMemory()        {   return sizeOfMemory;        }
    public Cache getLowerLevelMemory() {   return lowerLevelMemory;    }
    public int getWaitCycles()          {   return waitCycles;          }
    public int getCounter()             {   return counter;             }
    public Boolean getFetchingMemory()  {   return fetchingMemory;      }
    public char[] getMemArray()         {   return mem_array;           }
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
