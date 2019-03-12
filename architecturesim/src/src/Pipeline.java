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
                        // System.out.println("got to WB stage");
                        stepWB();
                        break;
                    case 4:
                        // System.out.println("got to MEM stage");
                        stepMEM();
                        break;
                    case 3:
                        // System.out.println("got to EX stage");
                        stepEX();
                        break;
                    case 2:
                        // System.out.println("got to ID stage");
                        stepID();
                        break;
                    case 1:
                        // System.out.println("got to IF stage");
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
        int writeDataToRegister;
        int registerWrite = pipeline[4].getRD();
        int opcode  = pipeline[4].getOpcode();
        // ALU Ops
        if (opcode >= 1 && opcode <= 14) {
            writeDataToRegister = pipeline[4].getALUOutput();
            register.setRegisterValue(registerWrite, writeDataToRegister);
        }
        // LD
        else if (opcode == 15) {
            writeDataToRegister = pipeline[4].getDataFromMemory();
            register.setRegisterValue(registerWrite, writeDataToRegister);
        }        
    }
    
    private void stepMEM() {
        // get from ALUOutput or memoryOutput?
        int RD = pipeline[3].getRD();
        int address = pipeline[3].getMemoryOutput();
        int data = 0;
        // Only need to updateHazard if load/store 
        // LD
        if (pipeline[3].getOpcode() == 15) {
            
            try {
                data = memory.readAddressInMemory(address);
                pipeline[3].setDataFromMemory(data);
                
            }
            catch (NoSuchMemoryLocationException e){
                System.out.println("Test Failed");
            }
        } 
        // ST
        else if (pipeline[3].getOpcode() == 16) {
            try {
                data = register.getRegisterValue(RD);
                memory.writeAddressInMemory(data, address);
                //updateMEMHazard();
            }
            catch (NoSuchMemoryLocationException e){
                System.out.println("Test Failed");
            }
        }
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
        int branchTaken = 0;
        System.out.println("Current instruction: " + opcode);
        
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
                ALUOutput = RSValue / (RTValue + 1);
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
                // set CMP Register?
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
                System.out.println("In ADDI");
                break;
            // SUBI
            case 11:
                ALUOutput = RSValue - immediate;
                System.out.println("In SUBI");
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
                System.out.println("In BGT");
                if (RSValue > RDValue) {
                    ALUOutput = register.getPC() - 1 - immediate;
                    branchTaken = 1;
                }
                break; 
            // BLT
            case 18:
                if (RSValue < RDValue) {
                    ALUOutput = register.getPC() + immediate;
                    branchTaken = 1;
                }
                break;
            // BOE
            case 19:
                if (RSValue == RDValue) {
                    ALUOutput = register.getPC() + immediate;
                    branchTaken = 1;
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
            
        // if branch taken, pc = ALUOutput, flush previous stages pipeline[0] pipeline[1], clear IDHazard(), clearEXHazard, clear 
        // else pc=pc+1
        // switch statement for ALU operations
        if (branchTaken == 1) {
            clearHazards();
            clearStage(this.getPipeline()[0]);
            clearStage(this.getPipeline()[1]);
            register.setPC(ALUOutput);
            System.out.println("Branch taken pc value: " + register.getPC());
        }
        else {
            
            
            
        }
        
        pipeline[2].setALUOutput(ALUOutput);
        pipeline[2].setMemoryOutput(memoryOutput);
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
            pipeline[0].setInstruction(memory.readAddressInMemory(register.getPC()));
        }
        catch (NoSuchMemoryLocationException e){
            System.out.println("Test Failed");
        }
        this.getPipeline()[0].setTestValue(instruction);
        
        // System.out.println("Fetch PC Value: " + register.getPC());
        register.setPC(register.getPC() + 1);
    }
    
    // may need to return something to indicate how to change RS/RT
    private int checkEXHazard() {
        // if writing to register File
        if (this.hazardValues.getEX_MEM_RegWrite() == 1) {
            if (this.hazardValues.getEX_MEM_RegisterRd() == this.hazardValues.getID_EX_RegisterRs()) {
                return 1;
            }
            else if (this.hazardValues.getEX_MEM_RegisterRd() == this.hazardValues.getID_EX_RegisterRt()) {
                return 2;
            }
            
        }
        return 0;
    }
    
    // may need to return something to indicate how to change RS/RT
    private int checkMEMHazard() {
        // more conditions to check
        // if writing to register File
        if (this.hazardValues.getMEM_WB_RegWrite() == 1) {
            if (this.hazardValues.getMEM_WB_RegisterRd() == this.hazardValues.getID_EX_RegisterRs()) {
                return 1;
            }

            else if (this.hazardValues.getMEM_WB_RegisterRd() == this.hazardValues.getID_EX_RegisterRt()) {
                return 2;
            }
        }
        return 0;
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
        int opcode = pipeline[2].getOpcode();
        
        if (opcode >=1 && opcode <=8 || opcode >= 10 && opcode <= 15) {
            this.hazardValues.setEX_MEM_RegWrite(1);
        }
        
    }
    
    private void updateMEMHazard() {
        // update whatever starts with MEM
        this.hazardValues.setMEM_WB_RegisterRd(pipeline[3].getRD());
        this.hazardValues.setMemoryOutput(pipeline[3].getMemoryOutput());
        int opcode = pipeline[3].getOpcode();
        
        if (opcode >= 1 && opcode <= 8 || opcode >= 10 && opcode <= 15) {
            this.hazardValues.setMEM_WB_RegWrite(1);
        }
    }
    
    private void copyStage(PipeStage oldStage, PipeStage newStage) {
        newStage.setStage(oldStage.getStage());
        newStage.setInstruction(oldStage.getInstruction());
        newStage.setRD(oldStage.getRD());
        newStage.setRS(oldStage.getRS());
        newStage.setRT(oldStage.getRT());
        newStage.setImmediate(oldStage.getImmediate());
        newStage.setALUOutput(oldStage.getALUOutput());
        newStage.setMemoryOutput(oldStage.getMemoryOutput());
        newStage.setOpcode(oldStage.getOpcode());
        newStage.setTestValue(oldStage.getTestValue());
        newStage.setDataFromMemory(oldStage.getDataFromMemory());
    }
    
    private void clearStage(PipeStage stage) {
        stage.setStage("");
        stage.setInstruction(0x0000);
        stage.setRD(0);
        stage.setRS(0);
        stage.setRT(0);
        stage.setImmediate((char) 0x0000);
        stage.setALUOutput(0);
        stage.setMemoryOutput(0);
        stage.setOpcode((char) 0x0000);
        stage.setTestValue(0);
        stage.setDataFromMemory(0);
    }
    
    private Boolean isEmpty(PipeStage stage) {
        return "".equals(stage.getStage());
    }
    
    private void clearHazards() {
        this.hazardValues.setEX_MEM_RegWrite(0);
        this.hazardValues.setMEM_WB_RegWrite(0);
        this.hazardValues.setEX_MEM_RegisterRd(0);
        this.hazardValues.setMEM_WB_RegisterRd(0);
        this.hazardValues.setID_EX_RegisterRs(0);
        this.hazardValues.setID_EX_RegisterRt(0);
        this.hazardValues.setALUOutput(0);
        this.hazardValues.setMemoryOutput(0);
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
