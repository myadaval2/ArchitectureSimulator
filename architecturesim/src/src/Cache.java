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
    private int waitCycles;
    private int counter;
    private char[] mem_array;
    private int[] tag_array;
    
    public Cache(int sizeOfMemory, Cache lowerLevelMemory, int waitCycles) {
        this.sizeOfMemory = sizeOfMemory;
        this.waitCycles = waitCycles;
        this.counter = waitCycles;
        
        this.mem_array = new char[sizeOfMemory];
        this.tag_array = new int[sizeOfMemory];
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
    
    private void delayCounter() {
        this.counter = this.counter--;
    }
    
    public int getSizeOfMemory()        {   return sizeOfMemory;        }
    public int getWaitCycles()          {   return waitCycles;          }
    public int getCounter()             {   return counter;             }
    public char[] getMemArray()         {   return mem_array;           }
}


