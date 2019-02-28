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
    
    public Object[][] getMemoryPage(char[] fullMemoryArray, int[] tagArray, int pageNum) {
        int tableSize = 64;
        int addressValue;
        
        int startIndex = pageNum*tableSize;
        Object[][] memoryArraySegment = new Object[tableSize][2];
        for (int i = startIndex; i < startIndex+tableSize; i++) {
            for (int j = 0; j < 2; j++) {
                if (j == 0) {
                    addressValue = tagArray[i] | i;
//                    System.out.println(pageNum);
//                    System.out.println(i);
//                    System.out.println(tagArray[i]);
//                    System.out.println(addressValue);
                    memoryArraySegment[i-startIndex][j] = (Object) Integer.toHexString(addressValue);
                    // memoryArraySegment[i-startIndex][j] = (Object) Integer.toBinaryString(addressValue);
                }
                else {
                    // (Object) Integer.toHexString(fullMemoryArray[i]); // to display hex
                    // (Object) Integer.toBinaryString(fullMemoryArray[i]); // to display binary
                    memoryArraySegment[i-startIndex][j] = (Object) Integer.toBinaryString(fullMemoryArray[i]);
                }
            }
        }
        return memoryArraySegment;
    }
    
    public String[] getMemoryPageLabelsForMemoryType(String memoryType) {
        if (null == (String) memoryType) {
            return Arrays.copyOfRange(this.displayLabels, 0, 512);
        }
        else switch ((String) memoryType) {
            case "DRAM":
                return Arrays.copyOfRange(this.displayLabels, 0, 2048);
            case "L2Cache":
                return Arrays.copyOfRange(this.displayLabels, 0, 512);
            case "L1Cache":
                return Arrays.copyOfRange(this.displayLabels, 0, 128);
            default:
                return Arrays.copyOfRange(this.displayLabels, 0, 128);
        }
    }
}
