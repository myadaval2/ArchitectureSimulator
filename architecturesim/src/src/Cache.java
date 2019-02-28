/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src;
import java.util.*;

/**
 *
 * @author jjaikumar
 */
public class Cache {
    
    private final int       sizeOfMemory;
    private final Cache     lowerLevelMemory;
    private final int       waitCycles;
    private final char[]    mem_array;
    private final int[]     tag_array;
    private final int[]     historyArray;
    private final int       heirarchy;
    
    public Cache(int sizeOfMemory, Cache lowerLevelMemory, int waitCycles, int heirarchy) {
        this.sizeOfMemory       = sizeOfMemory;
        this.lowerLevelMemory   = lowerLevelMemory;
        this.waitCycles         = waitCycles;
        this.heirarchy          = heirarchy;  
        this.mem_array          = new char[sizeOfMemory];
        this.tag_array          = new int[sizeOfMemory];
        this.historyArray       = new int[sizeOfMemory];
        Arrays.fill(this.historyArray, -1);
    }
    
    public int getHeirarchy()           {   return heirarchy;               }
    public int getSizeOfMemory()        {   return sizeOfMemory;            }
    public int getWaitCycles()          {   return waitCycles;              }
    public char[] getMemArray()         {   return mem_array;               }
    public int[] getTagArray()          {   return tag_array;               }
    public int[] getHistoryArray()      {   return historyArray;            }
    public Cache getNextCache()         {   return lowerLevelMemory;        }
    public char getData(int address)    {   return this.mem_array[address]; }
    
    public void setTagArray(int data, int address)   {   
        this.tag_array[address] = data; 
    }
    
    public void setData(char data, int address){
        this.mem_array[address] = data;
    }
    
    public void updateHistoryArray(int address, int value) {
        if (value == 0) {
            historyArray[address] = 0;
        }
        else {
            historyArray[address] += 1;
        }
    }
}


