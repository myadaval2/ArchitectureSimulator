/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src;
import java.util.Arrays;
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
        Arrays.fill(this.mem_array, 'z');
        this.mem_array[0] = 0b1010111100110000 & 0xFFFF;
        this.tag_array = new int[sizeOfMemory];
    }
    
    public char getData(int address){
        return this.mem_array[address];
    }
    
    public void setData(char data, int address){
        this.mem_array[address] = data;
    }
    
    private void delayCounter() {
        this.counter = this.counter--;
    }
    
    public int getSizeOfMemory()        {   return sizeOfMemory;        }
    public int getWaitCycles()          {   return waitCycles;          }
    public int getCounter()             {   return counter;             }
    public char[] getMemArray()         {   return mem_array;           }
    public int[] getTagArray()          {   return tag_array;           }
    public void setTagArray(int data, int address)   {   
        tag_array[address] = data; 
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