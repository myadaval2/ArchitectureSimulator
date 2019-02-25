/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package userinterface;
import src.*;
import java.util.*;

/**
 *
 * @author megha
 */
public class DisplayMemory {
    public String[] displayLabels;
    public DisplayMemory() {
        int count = 0;
        for (int numberOfRows = 0; numberOfRows < Math.pow(2, 17); numberOfRows += Math.pow(2, 6)) {
            count++;
        }
        displayLabels = new String[count];
        for (int index = 0; index < count; index++) {
            this.displayLabels[index] = "Page " + Integer.toString(index);
        }   
    }
    
    public Object[][] getMemoryPage(char[] fullMemoryArray, int pageNum) {
        //Object[] fma = (Object[]) fullMemoryArray;
        int arraySize = 64;
        //Object y = (Object) arraySize;
        int startIndex = pageNum*arraySize;
        Object[][] memoryArraySegment = new Object[arraySize][2];
        for (int i = startIndex; i < startIndex+arraySize; i++) {
            for (int j = 0; j < 2; j++) {
                if (j == 0) {
                    memoryArraySegment[i-startIndex][j] = (Object) i;
                }
                else {
                    memoryArraySegment[i-startIndex][j] = (Object) Integer.toBinaryString(fullMemoryArray[i]);
                }
            }
        }
        return memoryArraySegment;
    }
    
    public String[] getMemoryPageLabelsForMemoryType(String memoryType) {
        if ("DRAM".equals((String) memoryType)) {
            return Arrays.copyOfRange(this.displayLabels, 0, 2048);
        }
        else if ("L2Cache".equals((String) memoryType)) {
            return Arrays.copyOfRange(this.displayLabels, 0, 1024);
        }
        else if ("L1Cache".equals((String) memoryType)) {
            return Arrays.copyOfRange(this.displayLabels, 0, 512);
        }
        else {
            return Arrays.copyOfRange(this.displayLabels, 0, 512);
        }
    }
}
