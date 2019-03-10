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
    private final PipeStage[] pipeline;
    private final PipeHazard EX_Hazard;
    private final PipeHazard MEM_Hazard;
    
    public Pipeline() {
        // make a linked list?
        // hardcoding an array makes it easy to keep track of each index as a specific stage in the pipeline
        this.pipeline = new PipeStage[6];
        // get rewritten every clock cycle
        this.EX_Hazard = new PipeHazard();
        this.MEM_Hazard = new PipeHazard();
        // step(this.pipeline);
    }
    
    public void step() {
        stepWB();
        stepMEM();
        stepEX();
        stepID();
        stepIF();
        updatePipeline();
    }
    
    private void stepWB() {
        // write to register file
        clearStage(this.pipeline[4]);
    }
    
    private void stepMEM() {
        // check PipeStage[4] is empty
        // check this.MEM_Hazard for forwarding;
        // do memory access
        copyStage(this.pipeline[3], this.pipeline[4]);
        clearStage(this.pipeline[3]);
        ////// update stage with WriteBack
        
    }
    
    private void stepEX() {
        // check PipeStage[3] is empty
        // check this.EX_Hazard for forwarding;
        // do ALU operations
        // copy PipeStage[2] to PipeStage[3]
        // clear PipeStage[2]
        /////// update stage with Memory
        
    }
    
    private void stepID() {
        // check PipeStage[2] is empty
        // decode instruction
        // copy PipeStage[1] to PipeStage[2]
        // clear PipeStage[1]
        /////// update stage with Execute
    }
    
    private void stepIF() {
        // set stage to "Fetch"
        // check PipeStage[1] is empty
        // fetch instruction
        // copy PipeStage[0] to PipeStage[1]
        // clear PipeStage[0]
        ////// update stage with Decode
    }
    
    private void updatePipeline() {
        // update this.EX_Hazard;
        // update this.MEM_Hazard;
        // update each stage with new "stage"
    }
    
    private void copyStage(PipeStage oldStage, PipeStage newStage) {
        newStage.setStage(oldStage.getStage());
        newStage.setInstruction(oldStage.getInstruction());
        newStage.setRD(oldStage.getRD());
        newStage.setRS(oldStage.getRS());
        newStage.setRT(oldStage.getRT());
        newStage.setImmediate(oldStage.getImmediate());
        newStage.setOpcode(oldStage.getOpcode());
    }
    
    private void clearStage(PipeStage stage) {
        stage.setStage("");
        stage.setInstruction(0x0000);
        stage.setRD("");
        stage.setRS("");
        stage.setRT("");
        stage.setImmediate((char) 0x0000);
        stage.setOpcode((char) 0x0000);
    }
}
