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
public class PipeHazard {
    private int EX_MEM_RegWrite;
    private int MEM_WB_RegWrite;
    private int EX_MEM_RegisterRd;
    private int MEM_WB_RegisterRd;
    private int ID_EX_RegisterRs;
    private int ID_EX_RegisterRt;
    private int ALUOutput;
    private int memoryOutput;
    
    public PipeHazard() {
        this.EX_MEM_RegWrite = 0;
        this.MEM_WB_RegWrite = 0;
        this.EX_MEM_RegisterRd = 0;
        this.MEM_WB_RegisterRd = 0;
        this.ID_EX_RegisterRs = 0;
        this.ID_EX_RegisterRt = 0;
        this.ALUOutput = 0;
        this.memoryOutput = 0;
    }

    /**
     * @return the EX_MEM_RegWrite
     */
    public int getEX_MEM_RegWrite() {
        return EX_MEM_RegWrite;
    }

    /**
     * @param EX_MEM_RegWrite the EX_MEM_RegWrite to set
     */
    public void setEX_MEM_RegWrite(int EX_MEM_RegWrite) {
        this.EX_MEM_RegWrite = EX_MEM_RegWrite;
    }

    /**
     * @return the MEM_WB_RegWrite
     */
    public int getMEM_WB_RegWrite() {
        return MEM_WB_RegWrite;
    }

    /**
     * @param MEM_WB_RegWrite the MEM_WB_RegWrite to set
     */
    public void setMEM_WB_RegWrite(int MEM_WB_RegWrite) {
        this.MEM_WB_RegWrite = MEM_WB_RegWrite;
    }

    /**
     * @return the EX_MEM_RegisterRd
     */
    public int getEX_MEM_RegisterRd() {
        return EX_MEM_RegisterRd;
    }

    /**
     * @param EX_MEM_RegisterRd the EX_MEM_RegisterRd to set
     */
    public void setEX_MEM_RegisterRd(int EX_MEM_RegisterRd) {
        this.EX_MEM_RegisterRd = EX_MEM_RegisterRd;
    }

    /**
     * @return the MEM_WB_RegisterRd
     */
    public int getMEM_WB_RegisterRd() {
        return MEM_WB_RegisterRd;
    }

    /**
     * @param MEM_WB_RegisterRd the MEM_WB_RegisterRd to set
     */
    public void setMEM_WB_RegisterRd(int MEM_WB_RegisterRd) {
        this.MEM_WB_RegisterRd = MEM_WB_RegisterRd;
    }

    /**
     * @return the ID_EX_RegisterRs
     */
    public int getID_EX_RegisterRs() {
        return ID_EX_RegisterRs;
    }

    /**
     * @param ID_EX_RegisterRs the ID_EX_RegisterRs to set
     */
    public void setID_EX_RegisterRs(int ID_EX_RegisterRs) {
        this.ID_EX_RegisterRs = ID_EX_RegisterRs;
    }

    /**
     * @return the ID_EX_RegisterRt
     */
    public int getID_EX_RegisterRt() {
        return ID_EX_RegisterRt;
    }

    /**
     * @param ID_EX_RegisterRt the ID_EX_RegisterRt to set
     */
    public void setID_EX_RegisterRt(int ID_EX_RegisterRt) {
        this.ID_EX_RegisterRt = ID_EX_RegisterRt;
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
}
