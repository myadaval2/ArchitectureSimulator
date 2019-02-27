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
public class Utils {
    public static final int SIZE_L1           =   32768;  // 0x08000
    public static final int SIZE_L2           =   65536;  // 0x10000
    public static final int SIZE_DRAM         =   131072; // 0x20000
    
    public static final int WAIT_L1           =   4;
    public static final int WAIT_L2           =   30;
    public static final int WAIT_DRAM         =   100;
    
    public static final int TAG_MASK_L1        =   0x18000; //
    public static final char INDEX_MASK_L1     =   0x7FFF; // BINARY 0011 1111 1111 1111
    
    public static final int TAG_MASK_L2        =   0x10000;
    public static final char INDEX_MASK_L2     =   0xFFFF; // BINARY 0111 1111 1111 1111
}
