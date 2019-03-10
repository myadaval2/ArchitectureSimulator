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
    public static final int SIZE_L1           =   0x02000;  // 0x08000
    public static final int SIZE_L2           =   0x08000;  // 0x10000
    public static final int SIZE_DRAM         =   0x20000; // 0x20000
    
    public static final int WAIT_L1           =   4;
    public static final int WAIT_L2           =   30;
    public static final int WAIT_DRAM         =   100;
    
/////////// 2-WAY SET ASSOCIATIVITY ////////////////////////////////////////
    
//    public static final int N_SET              = 2;
//    public static final int TAG_MASK_L1        =   0x1F000; //
//    public static final int INDEX_MASK_L1     =   0x0FFF; // BINARY 0001 1111 1111 1111
//    
//    public static final int TAG_MASK_L2        =   0x1C000;
//    public static final int INDEX_MASK_L2     =   0x3FFF; // BINARY 1111 1111 1111 1111
    
/////////// 1-WAY SET ASSOCIATIVITY  ////////////////////////////////////////
    
//    public static final int N_SET             = 1;
//    
//    public static final int TAG_MASK_L1       =   0x1E000; //
//    public static final int INDEX_MASK_L1     =   0x1FFF; // BINARY 0001 1111 1111 1111
//    
//    public static final int TAG_MASK_L2       =   0x18000;
//    public static final int INDEX_MASK_L2     =   0x7FFF; // BINARY 1111 1111 1111 1111
    
/////////// 2-WAY SET ASSOCIATIVITY WITH 2 WORDS PER LINE ////////////////////////////////////////
    
    public static final int N_SET              = 2;
    public static final int N_WORDS_PER_LINE    = 2;
    
    public static final int TAG_MASK_L1        =   0x1F000; //
    public static final int INDEX_MASK_L1     =   0x0FFE; // BINARY 0001 1111 1111 1111
    
    
    public static final int TAG_MASK_L2        =   0x1C000;
    public static final int INDEX_MASK_L2     =   0x3FFE; // BINARY 1111 1111 1111 1111
    
    public static final int WORD_0    = 0xFFFF0000;
    public static final int WORD_1    = 0x0000FFFF;
    
    enum Opcodes
    {
      ADD, SUB, NONE
    }
}
