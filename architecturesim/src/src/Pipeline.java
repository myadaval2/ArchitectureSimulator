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
    Register register = Register.getRegisters();
    Memory memory = Memory.getMemory();
    
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
                        
                        break;
                    case 3:
                        System.out.println("got to EX stage");
                        stepEX();
                        
                        break;
                    case 2:
                        System.out.println("got to ID stage");
                        stepID();
                        
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
        // WHAT INSTRUCTIONS WRITE BACK?
        // write to register file pipeline[5]
        // NEED TO CAST AS CHAR ?? NEED TO GET THIS STRAIGHT
        if (pipeline[3].getOpcode() >= 1 && pipeline[3].getOpcode() <= 14) {
            register.setRegisterValue(pipeline[3].getRD(), pipeline[3].getALUOutput());
        }
        else if (pipeline[3].getOpcode() == 16) {
            register.setRegisterValue(pipeline[3].getRD(), pipeline[3].getMemoryOutput());
        }
    }
    
    private void stepMEM() {
        // get from ALUOutput or memoryOutput?
        int address = 0;
        int data = 0;
        // LD
        if (pipeline[3].getOpcode() == 15) {
            try {
            data = memory.readAddressInMemory(address);
            }
            catch (NoSuchMemoryLocationException e){
                System.out.println("Test Failed");
            }
        } 
        // ST
        else if (pipeline[3].getOpcode() == 16) {
            try {
            memory.writeAddressInMemory(data, address);
            }
            catch (NoSuchMemoryLocationException e){
                System.out.println("Test Failed");
            }
        }
        // pipeline[3].setRegisterValue(data);
        updateMEMHazard();
    }
    
    private void stepEX() {
        // assign correct operands/values
        int RDValue = register.getRegisterValue(pipeline[2].getRD());
        int RSValue = register.getRegisterValue(pipeline[2].getRS());
        int RTValue = register.getRegisterValue(pipeline[2].getRT());
        int immediate = pipeline[2].getImmediate();
        int opcode = pipeline[2].getOpcode();
        int offset = pipeline[2].getOffset();
        int ALUOutput = 0;
        int memoryOutput = 0;
        // memoryOutput is for load/store, ALUOutput is for new address calculations
        // do we need both??
        
        switch (checkEXHazard()) {
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
        switch (opcode) {
            // ADD
            case 1:
                ALUOutput = RSValue + RTValue;
                break;
            // SUB
            case 2:
                ALUOutput = RSValue - RTValue;
                break;
            // MULT
            case 3:
                ALUOutput = RSValue * RTValue;
                break;
            // DIV
            case 4:
                ALUOutput = RSValue / RTValue;
                break;
            // MOD
            case 5:
                ALUOutput = RSValue % RTValue;
                break;
            // AND
            case 6:
                ALUOutput = RSValue & RTValue;
                break;
            // OR
            case 7:
                ALUOutput = RSValue | RTValue;
                break;
            // XOR
            case 8:
                ALUOutput = RSValue ^ RTValue;
                break;
            // CMP
            case 9:
                if (RSValue == RTValue) {
                    ALUOutput = 1;
                }
                else {
                    ALUOutput = 0;
                }
                break;
            // ADDI
            case 10:
                ALUOutput = RSValue + immediate;
                break;
            // SUBI
            case 11:
                ALUOutput = RSValue - immediate;
                break;
            // LLS
            case 12:
                ALUOutput = RSValue >> immediate;
                break;
            // LRS
            case 13: 
                ALUOutput = RSValue << immediate;
                break;
            // ROT
            case 14:
                ALUOutput = (RSValue >>> immediate) | (RSValue << (16 - immediate));
                break;
            // LD
            case 15:
                memoryOutput = RSValue + immediate;
                break;
            // ST
            case 16:
                memoryOutput = RSValue + immediate;
                break;
            // do we need the + 1 or can we use NextPC?
            // BGT    
            case 17:
                if (RSValue > RDValue) {
                    ALUOutput = register.getPC() + 1 + immediate;
                }
                break; 
            // BLT
            case 18:
                if (RSValue < RDValue) {
                    ALUOutput = register.getPC() + 1 + immediate;
                }
                break;
            // BOE
            case 19:
                if (RSValue == RDValue) {
                    ALUOutput = register.getPC() + 1 + immediate;
                }
                break;
            // JMP
            case 20:
                ALUOutput = RDValue + offset;
            // HLT
            case 21:
                break;
            // PUSH
            case 22:
                // push
                // iterate through bit vectors
                // push is storing all of the registers onto the stack
                // and decrementing the stack pointer
                // where does the stack pointer point to begin with?
                // need to set up a stack area!
            // POP
            case 23:
                // pop is loading all the registers and incrementing the stack
                // pointer
                // push and pop are both pretty complex, require a series
                // of loads/stores
        }
            
        
        // switch statement for ALU operations
        // store ALUOutput in pipeline[2].setALUOutput(ALUOutput);
        updateEXHazard();
    }
    
    private void stepID() {
        // decode instruction
        int instruction = this.pipeline[1].getInstruction();
        int opcode = (instruction >> 11) & 0x1F;
        int RD = 0;
        int RS = 0;
        int RT = 0;
        int immediate = 0;
        int offset = 0;
        // Register Type
        if (opcode >= 1 && opcode <= 9) {
            RD = (instruction >> 8) & 0x7;
            RS = (instruction >> 5) & 0x7;
            RT = (instruction >> 2) & 0x7;
            
        }
        // Immediate Type
        else if (opcode >= 10 && opcode <= 19) {
            RD = (instruction >> 8) & 0x7;
            RS = (instruction >> 5) & 0x7;
            immediate = instruction & 0x1F;
        }
        // Control Type
        else if (opcode >= 20 && opcode <= 23) {
            RD = (instruction >> 8) & 0x7;
            offset = instruction & 0xFF;
        }
        
        pipeline[1].setRD(RD);
        pipeline[1].setRS(RS);
        pipeline[1].setRT(RT);
        pipeline[1].setImmediate(immediate);
        pipeline[1].setOffset(offset);
        pipeline[1].setOpcode(opcode);
        updateIDHazard();
    }
    
    private void stepIF(int instruction) {
        // fetch instruction
        try {
            memory.readAddressInMemory(register.getPC());
        }
        catch (NoSuchMemoryLocationException e){
            System.out.println("Test Failed");
        }
        register.setNextPC(register.getPC() + 1);
        this.getPipeline()[0].setTestValue(instruction);
    }
    
    // may need to return something to indicate how to change RS/RT
    private int checkEXHazard() {
        // if writing to register Rd (ALU operations, etc.) AND
        if (this.hazardValues.getEX_MEM_RegisterRd() == this.hazardValues.getID_EX_RegisterRs()) {
            return 1;
        }
        
        else if (this.hazardValues.getEX_MEM_RegisterRd() == this.hazardValues.getID_EX_RegisterRt()) {
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
        if (this.hazardValues.getMEM_WB_RegisterRd() == this.hazardValues.getID_EX_RegisterRs()) {
            return 1;
        }
        
        else if (this.hazardValues.getMEM_WB_RegisterRd() == this.hazardValues.getID_EX_RegisterRt()) {
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
        stage.setRD(0);
        stage.setRS(0);
        stage.setRT(0);
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
