/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src;

/**
 *
 * @author megha
 */
public class OpcodeDecoder {
    public static final int ADD           =   1;
    public static final int SUB           =   2;
    public static final int MUL           =   3;
    public static final int DIV           =   4;
    public static final int MOD           =   5;
    public static final int AND           =   6;
    public static final int OR            =   7;
    public static final int XOR           =   8;
    public static final int CMP           =   9;
    public static final int LDR           =   10;
    public static final int STR           =   11;
    
    public static final int ADDI          =   12;
    public static final int SUBI          =   13;
    public static final int ASL           =   14;
    public static final int ASR           =   15;
    public static final int ROT           =   16;
    public static final int LDI           =   17;
    public static final int STI           =   18;
    public static final int BGT           =   19;
    public static final int BLT           =   20;
    public static final int BOE           =   21;
    
    public static final int JI            =   22;
    public static final int HLT           =   23;
    public static final int PUSH          =   24;
    public static final int POP           =   25;
    public static final int LD            =   26;
    public static final int ST            =   27;
    
    public static final int FUNC          =   31;

}
