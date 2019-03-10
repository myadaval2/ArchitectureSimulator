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
public class Pipeline {
    public PipeStage[] pipeline;
    private final PipeHazard hazardValues;
    
    public Pipeline() {
        // make a linked list?
        // hardcoding an array makes it easy to keep track of each index as a specific stage in the pipeline
        this.pipeline = new PipeStage[6];
        for (int i = 0; i < 6; i++) {
            pipeline[i] = new PipeStage();
        }
        // get rewritten every clock cycle
        this.hazardValues = new PipeHazard();
        // step(this.pipeline);
    }
    
    public void step(int instruction) {
        for (int i = 5; i > 0; i--) {
            if (isEmpty(this.getPipeline()[i])) {
                switch (i) {
                    case 5:
                        System.out.println("got to WB stage");
                        stepWB();
                        break;
                    case 4:
                        System.out.println("got to MEM stage");
                        stepMEM();
                        updateMEMHazard();
                        break;
                    case 3:
                        System.out.println("got to EX stage");
                        stepEX();
                        updateEXHazard();
                        break;
                    case 2:
                        System.out.println("got to ID stage");
                        stepID();
                        updateIDHazard();
                        break;
                    case 1:
                        System.out.println("got to IF stage");
                        stepIF(instruction);
                        break;
                    default:
                        break;
                }
                // OR use 5 pointers for 5 different stages
                copyStage(this.getPipeline()[i-1], this.getPipeline()[i]);
                clearStage(this.getPipeline()[i-1]);
            }
        }
    }
    
    private void stepWB() {
        // only certain instructions write back... (ALU operations, Load)
        // if (opcode == ALU operations)
        // write to register#(pipeline[5].getALUOutput)
        // If (opcode == Load))
        // write to register#(pipeline[5].getMemoryOutput)
        // write to register file pipeline[5]
    }
    
    private void stepMEM() {
        // if (opcode == load) 
        // readAddressInMemory((char) address);
        // if (opcode == store)
        // writeAddressInMemory((char) address, (char) data);
    }
    
    private void stepEX() {
        // assign correct operands/values
        int RSValue, RTValue = 0;
        // int RSValue = get register value of pipeline[2].RS;
        // int RTValue = get register value of pipeline[2].RT;
        switch (checkEXHazard()) {
            case 0:
                // RSValue = get register value of pipeline[2].RS
                break;
            case 1:
                RSValue = this.hazardValues.getALUOutput();
                break;
            case 2:
                RTValue = this.hazardValues.getALUOutput();
                break;
            default:
                break;
        }
        
        switch (checkMEMHazard()) {
            case 0:
                break;
            case 1:
                RSValue = this.hazardValues.getALUOutput();
                break;
            case 2:
                RTValue = this.hazardValues.getALUOutput();
                break;
            default:
                break;
        }
        
        // do ALU operations
        // switch statement for ALU operations
        // store ALUOutput in pipeline[2].setALUOutput(ALUOutput);
    }
    
    private void stepID() {
        // decode instruction
    }
    
    private void stepIF(int instruction) {
        // fetch instruction
        // readAddressInMemory(PC);
        // NextPC = PC + 1;
        this.getPipeline()[0].setTestValue(instruction);
    }
    
    // may need to return something to indicate how to change RS/RT
    private int checkEXHazard() {
        // if writing to register Rd (ALU operations, etc.) AND
        if (this.hazardValues.getEX_MEM_RegisterRd().equals(this.hazardValues.getID_EX_RegisterRs())) {
            return 1;
        }
        
        else if (this.hazardValues.getEX_MEM_RegisterRd().equals(this.hazardValues.getID_EX_RegisterRt())) {
            return 2;
        }
        
        else {
            return 0;
        }
    }
    
    // may need to return something to indicate how to change RS/RT
    private int checkMEMHazard() {
        // more conditions to check
        // if writing to register Rd (ALU operations, etc.) AND
        if (this.hazardValues.getMEM_WB_RegisterRd().equals(this.hazardValues.getID_EX_RegisterRs())) {
            return 1;
        }
        
        else if (this.hazardValues.getMEM_WB_RegisterRd().equals(this.hazardValues.getID_EX_RegisterRt())) {
            return 2;
        }
        else {
            return 0;
        }
    }
    
    private void updateIDHazard() {
        // update whatever starts with ID
        this.hazardValues.setID_EX_RegisterRs(pipeline[1].getRS());
        this.hazardValues.setID_EX_RegisterRt(pipeline[1].getRT());
    }
    
    private void updateEXHazard() {
        // update whatever starts with EX
        this.hazardValues.setEX_MEM_RegisterRd(pipeline[2].getRD());
        this.hazardValues.setALUOutput(pipeline[2].getALUOutput());
    }
    
    private void updateMEMHazard() {
        // update whatever starts with MEM
        this.hazardValues.setMEM_WB_RegisterRd(pipeline[3].getRD());
        this.hazardValues.setMemoryOutput(pipeline[3].getMemoryOutput());
    }
    
    private void copyStage(PipeStage oldStage, PipeStage newStage) {
        newStage.setStage(oldStage.getStage());
        newStage.setInstruction(oldStage.getInstruction());
        newStage.setRD(oldStage.getRD());
        newStage.setRS(oldStage.getRS());
        newStage.setRT(oldStage.getRT());
        newStage.setImmediate(oldStage.getImmediate());
        newStage.setOpcode(oldStage.getOpcode());
        newStage.setTestValue(oldStage.getTestValue());
    }
    
    private void clearStage(PipeStage stage) {
        stage.setStage("");
        stage.setInstruction(0x0000);
        stage.setRD("");
        stage.setRS("");
        stage.setRT("");
        stage.setImmediate((char) 0x0000);
        stage.setOpcode((char) 0x0000);
        stage.setTestValue(0);
    }
    
    private Boolean isEmpty(PipeStage stage) {
        return "".equals(stage.getStage());
    }

    /**
     * @return the pipeline
     */
    public PipeStage[] getPipeline() {
        return pipeline;
    }

    /**
     * @param pipeline the pipeline to set
     */
    public void setPipeline(PipeStage[] pipeline) {
        this.pipeline = pipeline;
    }
}
