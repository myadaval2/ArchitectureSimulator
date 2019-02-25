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
    
    public Object[][] getMemoryPage(char[] fullMemoryArray, int[] tagArray, int memoryLevel, int pageNum) {
        //Object[] fma = (Object[]) fullMemoryArray;
        int arraySize = 64;
        int tempTag;
        int addressValue;
        int startIndex = pageNum*arraySize;
        Object[][] memoryArraySegment = new Object[arraySize][2];
        for (int i = startIndex; i < startIndex+arraySize; i++) {
            for (int j = 0; j < 2; j++) {
                tempTag = 0;
                if (j == 0) {
                    // Close, but I think the shift of 15 is wrong for L2 and DRAM
                    tempTag = tagArray[i];
                    switch (memoryLevel) {
                        case 0:
                            tempTag = 0;
                            break;
                        case 1:
                            tempTag = tempTag << 16;
                            break;
                        case 2:
                            tempTag = tempTag << 15;
                            break;
                        default:
                            break;
                    }
                    addressValue = (tempTag & 0x18000) | i;
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
                return Arrays.copyOfRange(this.displayLabels, 0, 1024);
            case "L1Cache":
                return Arrays.copyOfRange(this.displayLabels, 0, 512);
            default:
                return Arrays.copyOfRange(this.displayLabels, 0, 512);
        }
    }
}
