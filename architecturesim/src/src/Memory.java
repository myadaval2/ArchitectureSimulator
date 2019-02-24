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
    // public int lineLength;
    public int sizeOfMemory;
    public Memory lowerLevelMemory;
    public int waitCycles;
    public int counter;
    public Boolean fetchingMemory;
    
    // how do we want memory to look? vector of arrays? 2D array with row = 4 words, and 128 rows for DRAM?
    
    public Memory(int sizeOfMemory, Memory lowerLevelMemory, int waitCycles) {
        this.sizeOfMemory = sizeOfMemory;
        this.lowerLevelMemory = lowerLevelMemory;
        this.waitCycles = waitCycles;
        this.counter = waitCycles;
        this.fetchingMemory = false;
    }
    
    public void getAddressInMemory(Memory currMemoryLevel, int address) {
        if (!addressInCurrMemoryLevel(address)) {
            currMemoryLevel = currMemoryLevel.lowerLevelMemory;
        }
        else {
            delayCounter();
        }
        if (this.counter == 0) {
            getDataFromMemory(address);
        }
    }
    
    public Boolean addressInCurrMemoryLevel(int address) {
        // if address is in array, return true, else return false
        if (true) {
            return true;
        }
        else {
            return false;
        }
    }
    
    public void delayCounter() {
        this.counter = this.counter--;
        this.fetchingMemory = true;
    }
    
    public int getDataFromMemory(int address) {
        // access array and return data from memory address
        return address;
        // write to topLevelMemory
    }
    
}
