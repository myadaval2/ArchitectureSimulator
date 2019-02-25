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
    public static int size_l1           =   32768;
    public static int size_l2           =   65536;
    public static int size_DRAM         =   131072;
    
    public static int wait_l1           =   4;
    public static int wait_l2           =   30;
    public static int wait_DRAM         =   100;
    
    public static char tagMask_l1       =   49152;
    public static char indexMask_l1     =   16383; // BINARY 0011 1111 1111 1111
    
    public static char tagMask_l2       =   32768;
    public static char indexMask_l2     =   32767; // BINARY 0111 1111 1111 1111
    
}
