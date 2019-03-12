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
public class Register {
    private int[]      GP;
    private int        PC;
    private int        NextPC;
    private int        SP;
    private int        RTN;
    private int        CMP;
    
    public static Register registers = new Register();
    
    private Register() {
        this.GP = new int[8];
        for (int i = 0; i < 8; i++) {
            this.GP[i] = (char) 1;
        }
        this.GP[0] = (char) 0;
        this.GP[1] = (char) 0;
        this.GP[3] = (char) 5;
        this.PC        = 0;
        this.NextPC    = 0;
        this.SP        = 0;
        this.RTN       = 0;
        this.CMP       = 0;
    }

    public void clearRegisterFile() {
        for (int i = 0; i < 8; i++) {
            this.GP[i] = (char) 0;
        }
        this.PC        = 0;
        this.NextPC    = 0;
        this.SP        = 0;
        this.RTN       = 0;
        this.CMP       = 0;
    }
    
    public int getRegisterValue(int register) {
        return GP[register];
    }

    public void setRegisterValue(int register, int value) {
        this.GP[register] = value;
    }
    
    /**
     * @return the Registers
     */
    public static Register getRegisters() {
        return registers;
    }

    /**
     * @return the PC
     */
    public int getPC() {
        return PC;
    }

    /**
     * @param PC the PC to set
     */
    public void setPC(int PC) {
        this.PC = PC;
    }

    /**
     * @return the NextPC
     */
    public int getNextPC() {
        return NextPC;
    }

    /**
     * @param NextPC the NextPC to set
     */
    public void setNextPC(int NextPC) {
        this.NextPC = NextPC;
    }

    /**
     * @return the SP
     */
    public int getSP() {
        return SP;
    }

    /**
     * @param SP the SP to set
     */
    public void setSP(int SP) {
        this.SP = SP;
    }

    /**
     * @return the RTN
     */
    public int getRTN() {
        return RTN;
    }

    /**
     * @param RTN the RTN to set
     */
    public void setRTN(int RTN) {
        this.RTN = RTN;
    }

    /**
     * @return the CMP
     */
    public int getCMP() {
        return CMP;
    }

    /**
     * @param CMP the CMP to set
     */
    public void setCMP(int CMP) {
        this.CMP = CMP;
    }
    
}
