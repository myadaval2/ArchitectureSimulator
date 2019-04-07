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
    private int        prevPC;
    private int        SP;
    private int        RTN;
    private int        CMP;
    private int        BR;
    
    public static Register registers = new Register();
    
    private Register() {
        this.GP = new int[8];
        for (int i = 0; i < 8; i++) {
            this.GP[i] = (char) 0;
        }
//        this.GP[0] = (char) 0;
//        this.GP[1] = (char) 1;
//        this.GP[3] = (char) 3;
        this.PC        = 0;
        this.prevPC    = 0;
        this.SP        = 63;
        this.RTN       = 0;
        this.CMP       = 0;
        this.BR        = 0;
    }

    public void clearRegisterFile() {
        for (int i = 0; i < 8; i++) {
            this.GP[i] = (char) 0;
        }
        this.PC        = 0;
        this.prevPC    = 0;
        this.SP        = 63;
        this.RTN       = 0;
        this.CMP       = 0;
        this.BR        = 0;
    }
    
    public static void printRegisters() {
        Register register = Register.getRegisters();
        System.out.println("Registers: " +
                              " R0 " + register.getRegisterValue(0)
                            + " R1 " + register.getRegisterValue(1)
                            + " R2 " + register.getRegisterValue(2)
                            + " R3 " + register.getRegisterValue(3)
                            + " R4 " + register.getRegisterValue(4)
                            + " R5 " + register.getRegisterValue(5)
                            + " R6 " + register.getRegisterValue(6)
                            + " R7 " + register.getRegisterValue(7));
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
    public int getPrevPC() {
        return this.prevPC;
    }

    /**
     * @param NextPC the NextPC to set
     */
    public void setPrevPC(int prevPC) {
        this.prevPC = prevPC;
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
    
    /**
     * @return the BR
     */
    public int getBR() {
        return this.BR;
    }

    /**
     * @param BR the BR to set
     */
    public void setBR(int BR) {
        this.BR = BR;
    }
    
}
