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
    public String format;
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
    
    public Object[][] getMemoryPage(String memoryLevel, int[] fullMemoryArray, int[] tagArray, int[] historyArray, int pageNum, String format) {
        int tableSize = 64;
        int addressValue = 0;
        
        int startIndex = pageNum*tableSize;
        Object[][] memoryArraySegment = new Object[tableSize][2];
        
        for (int i = startIndex; i < startIndex+tableSize; i++) {
            if (null != memoryLevel) switch (memoryLevel) {
                case "DRAM":
                    addressValue = i;
                    break;
                case "L2Cache":
                    addressValue = tagArray[i] | ((i / Utils.N_SET) << 1);
                    break;
                case "L1Cache":
                    addressValue = tagArray[i] | ((i / Utils.N_SET) << 1);
                    break;
                default:
                    break;
            }
            for (int j = 0; j < 2; j++) {
                if (j == 0) {
                    if (!"DRAM".equals(memoryLevel)) {
                        if (historyArray[i] != -1) {
                            memoryArraySegment[i-startIndex][j] = (Object) String.format("%05X", addressValue);
                        }
                        else {
                            memoryArraySegment[i-startIndex][j] = (Object) String.format("%05X", 0);
                        }
                    }
                        
                        // memoryArraySegment[i-startIndex][j] = (Object) Integer.toBinaryString(addressValue);
                    
                    else if ("DRAM".equals(memoryLevel)) {
                        memoryArraySegment[i-startIndex][j] = (Object) Integer.toHexString(addressValue);
                    }
                }
                else {
                    // if (historyArray[i] != -1) {
                        // (Object) Integer.toHexString(fullMemoryArray[i]); // to display hex
                        // (Object) Integer.toBinaryString(fullMemoryArray[i]); // to display binary
                        if ("Hex".equals(format)) {
                            memoryArraySegment[i-startIndex][j] = (Object) Integer.toHexString(fullMemoryArray[i]);
                        }
                        else if ("Binary".equals(format)) {
                            // memoryArraySegment[i-startIndex][j] = (Object) String.format("%016d", Integer.parseInt(Integer.toBinaryString(fullMemoryArray[i])));
                            memoryArraySegment[i-startIndex][j] = (Object) Integer.toBinaryString(fullMemoryArray[i]);
                        }
                        else {
                            memoryArraySegment[i-startIndex][j] = (Object) fullMemoryArray[i];
                        }
                        
                    // }
                    // else if ("DRAM".equals(memoryLevel)) {
                    //     memoryArraySegment[i-startIndex][j] = (Object) (String.format("%08X", (int) fullMemoryArray[i]));
                    // }
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
                return Arrays.copyOfRange(this.displayLabels, 0, 256);
            case "L1Cache":
                return Arrays.copyOfRange(this.displayLabels, 0, 64);
            default:
                return Arrays.copyOfRange(this.displayLabels, 0, 64);
        }
    }
    
    public String getFormat() {
        return format;
    }
    
    public void setFormat(String dropDownFormat) {
        format = dropDownFormat;
    }
}
