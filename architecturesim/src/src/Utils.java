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
    public static final int size_l1           =   32768;  // 0x08000
    public static final int size_l2           =   65536;  // 0x10000
    public static final int size_DRAM         =   131072; // 0x20000
    
    public static final int wait_l1           =   4;
    public static final int wait_l2           =   30;
    public static final int wait_DRAM         =   100;
    
    public static final int tagMask_l1        =   0x18000; //49152 0b1100 0000 0000 0000
    public static final char indexMask_l1     =   0x7FFF; // BINARY 0011 1111 1111 1111
    
    public static final int tagMask_l2        =   0x10000;
    public static final char indexMask_l2     =   0xFFFF; // BINARY 0111 1111 1111 1111
    
}
