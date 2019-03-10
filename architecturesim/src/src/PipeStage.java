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
public class PipeStage {
    private String stage;
    private int instruction;
    private String RD;
    private String RS;
    private String RT;
    private char immediate;
    private char opcode;
    
    public PipeStage() {
        this.stage = "";
        this.instruction = 0;
        this.RD = "";
        this.RS = "";
        this.RT = "";
        this.immediate = 0;
        this.opcode = 0;
    }

    /**
     * @return the stage
     */
    public String getStage() {
        return stage;
    }

    /**
     * @param stage the stage to set
     */
    public void setStage(String stage) {
        this.stage = stage;
    }

    /**
     * @return the instruction
     */
    public int getInstruction() {
        return instruction;
    }

    /**
     * @param instruction the instruction to set
     */
    public void setInstruction(int instruction) {
        this.instruction = instruction;
    }

    /**
     * @return the RD
     */
    public String getRD() {
        return RD;
    }

    /**
     * @param RD the RD to set
     */
    public void setRD(String RD) {
        this.RD = RD;
    }

    /**
     * @return the RS
     */
    public String getRS() {
        return RS;
    }

    /**
     * @param RS the RS to set
     */
    public void setRS(String RS) {
        this.RS = RS;
    }

    /**
     * @return the RT
     */
    public String getRT() {
        return RT;
    }

    /**
     * @param RT the RT to set
     */
    public void setRT(String RT) {
        this.RT = RT;
    }

    /**
     * @return the immediate
     */
    public char getImmediate() {
        return immediate;
    }

    /**
     * @param immediate the immediate to set
     */
    public void setImmediate(char immediate) {
        this.immediate = immediate;
    }

    /**
     * @return the opcode
     */
    public char getOpcode() {
        return opcode;
    }

    /**
     * @param opcode the opcode to set
     */
    public void setOpcode(char opcode) {
        this.opcode = opcode;
    }
    
    
}
