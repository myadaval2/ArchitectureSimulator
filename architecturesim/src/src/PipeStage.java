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
    private int RD;
    private int RS;
    private int RT;
    private int immediate;
    private int offset;
    private int opcode;
    private int testValue;
    private int ALUOutput;
    private int memoryOutput;
    private int dataFromMemory;
    private int regWrite;
    private Boolean predictedBranchOutcome;
    // private int registerValue; OR should this be ALUOutput? ALUOutput is not used in LD/ST
    
    public PipeStage() {
        this.stage = "";
        this.instruction = 0;
        this.RD = 0;
        this.RS = 0;
        this.RT = 0;
        this.immediate = 0;
        this.opcode = 0;
        this.testValue = 0;
        this.ALUOutput = 0;
        this.memoryOutput = 0;
        this.regWrite = 0;
        this.predictedBranchOutcome = false;
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
    public int getRD() {
        return RD;
    }

    /**
     * @param RD the RD to set
     */
    public void setRD(int RD) {
        this.RD = RD;
    }

    /**
     * @return the RS
     */
    public int getRS() {
        return RS;
    }

    /**
     * @param RS the RS to set
     */
    public void setRS(int RS) {
        this.RS = RS;
    }

    /**
     * @return the RT
     */
    public int getRT() {
        return RT;
    }

    /**
     * @param RT the RT to set
     */
    public void setRT(int RT) {
        this.RT = RT;
    }

    /**
     * @return the immediate
     */
    public int getImmediate() {
        return immediate;
    }

    /**
     * @param immediate the immediate to set
     */
    public void setImmediate(int immediate) {
        this.immediate = immediate;
    }

    /**
     * @return the opcode
     */
    public int getOpcode() {
        return opcode;
    }

    /**
     * @param opcode the opcode to set
     */
    public void setOpcode(int opcode) {
        this.opcode = opcode;
    }

    /**
     * @return the testValue
     */
    public int getTestValue() {
        return testValue;
    }

    /**
     * @param testValue the testValue to set
     */
    public void setTestValue(int testValue) {
        this.testValue = testValue;
    }

    /**
     * @return the ALUOutput
     */
    public int getALUOutput() {
        return ALUOutput;
    }

    /**
     * @param ALUOutput the ALUOutput to set
     */
    public void setALUOutput(int ALUOutput) {
        this.ALUOutput = ALUOutput;
    }

    /**
     * @return the memoryOutput
     */
    public int getMemoryOutput() {
        return memoryOutput;
    }

    /**
     * @param memoryOutput the memoryOutput to set
     */
    public void setMemoryOutput(int memoryOutput) {
        this.memoryOutput = memoryOutput;
    }

    /**
     * @return the offset
     */
    public int getOffset() {
        return offset;
    }

    /**
     * @param offset the offset to set
     */
    public void setOffset(int offset) {
        this.offset = offset;
    }

    /**
     * @return the dataFromMemory
     */
    public int getDataFromMemory() {
        return dataFromMemory;
    }

    /**
     * @param dataFromMemory the dataFromMemory to set
     */
    public void setDataFromMemory(int dataFromMemory) {
        this.dataFromMemory = dataFromMemory;
    }

    /**
     * @return the regWrite
     */
    public int getRegWrite() {
        return regWrite;
    }

    /**
     * @param regWrite the regWrite to set
     */
    public void setRegWrite(int regWrite) {
        this.regWrite = regWrite;
    }
    
    public Boolean getPredictedBranchOutcome() {
        return this.predictedBranchOutcome;
    }
    
    public void setPredictedBranchOutcome(Boolean branchPrediction) {
        this.predictedBranchOutcome = branchPrediction;
    }
}
