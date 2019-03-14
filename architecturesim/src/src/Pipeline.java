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
    private static boolean pipelineEnabled;
    // public static Pipeline pipeline1 = new Pipeline();
    Register register = Register.getRegisters();
    Memory memory = Memory.getMemory();
    
    public static Pipeline pipelineMain = new Pipeline(true);
    
    private Pipeline(boolean pipelineEnabled) {
    //private Pipeline() {
        this.pipeline = new PipeStage[6];
        for (int i = 0; i < 6; i++) {
            pipeline[i] = new PipeStage();
        }
        // get rewritten every clock cycle
        this.hazardValues = new PipeHazard();
        // this.pipelineEnabled = pipelineEnabled;
        setPipelineEnabled(pipelineEnabled);
        // step(this.pipeline);
    }
    
    public static void setPipelineEnabled(boolean enabled) {
        pipelineEnabled = enabled;
    }
    
    public static Pipeline getPipeline() {
        return pipelineMain;
    }
    
    public void step(int instruction) {
        if (this.pipelineEnabled) {
            System.out.println("Pipeline Enabled");
            for (int i = 5; i > 0; i--) {
                if (isEmpty(pipeline[i])) {
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
                            stepIF();
                            break;
                        default:
                            break;
                    }
                    // OR use 5 pointers for 5 different stages
                    copyStage(pipeline[i-1], pipeline[i]);
                    clearStage(pipeline[i-1]);
                }
            }
        }
        else {
            System.out.println("Pipeline Disabled");
            int programStart = 15;
            int i = ((instruction - programStart) % 5) + 1;
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
                    stepIF();
                    break;
                default:
                    break;
            }
            // OR use 5 pointers for 5 different stages
            copyStage(pipeline[i-1], pipeline[i]);
            clearStage(pipeline[i-1]);
        }
    }
    
    private void stepWB() {
        // WHAT INSTRUCTIONS WRITE BACK?
        // write to register file pipeline[5]
        // NEED TO CAST AS CHAR ?? NEED TO GET THIS STRAIGHT
        int writeDataToRegister;
        int registerWrite = pipeline[4].getRD();
        int opcode  = pipeline[4].getOpcode();
        
        // LDs
        if (opcode == OpcodeDecoder.LDR || opcode == OpcodeDecoder.LDI || opcode == OpcodeDecoder.LD) {
            writeDataToRegister = pipeline[4].getMemoryOutput();
            register.setRegisterValue(registerWrite, writeDataToRegister);
        }      
        // ALU Ops
        else if (opcode == OpcodeDecoder.ADD && opcode <= OpcodeDecoder.CMP || opcode == OpcodeDecoder.ADDI && opcode <= OpcodeDecoder.ROT) {
            writeDataToRegister = pipeline[4].getALUOutput();
            register.setRegisterValue(registerWrite, writeDataToRegister);
        }
        // printRegisters();
    }
    
    private void stepMEM() {
        // get from ALUOutput or memoryOutput?
        int RD = pipeline[3].getRD();
        int address = pipeline[3].getMemoryOutput();

        int opcode = pipeline[3].getOpcode();
        int data;
        // Only need to updateHazard if load/store 
        if (opcode == OpcodeDecoder.LDR || opcode == OpcodeDecoder.LDI) {
            try {
                data = memory.readAddressInMemory(address);
                pipeline[3].setMemoryOutput(data);
                updateMEMHazard();

            }
            catch (NoSuchMemoryLocationException e){
                System.out.println("Test Failed");
            }
        } 
        else if (opcode == OpcodeDecoder.LD) {
            pipeline[3].setMemoryOutput(address);
            updateMEMHazard();
        }
        // Only STR for sure works, need to check ST and STI
        else if (opcode == OpcodeDecoder.STR || opcode == OpcodeDecoder.STI || opcode == OpcodeDecoder.ST) {
            try {
                data = register.getRegisterValue(RD);
                memory.writeAddressInMemory(data, address);
            }
            catch (NoSuchMemoryLocationException e){
                System.out.println("Test Failed");
            }
        }
        // do you only need to do this in the if/else statement?
        
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
                RSValue = this.hazardValues.getMemoryOutput();
                break;
            case 2:
                RTValue = this.hazardValues.getMemoryOutput();
                break;
            default:
                break;
        }
        
        // do ALU operations
        switch (opcode) {
            // REGISTER
            case OpcodeDecoder.ADD:
                //System.out.println("In ADD");
                ALUOutput = RSValue + RTValue;
                break;
            case OpcodeDecoder.SUB:
                ALUOutput = RSValue - RTValue;
                break;
            case OpcodeDecoder.MUL:
                ALUOutput = RSValue * RTValue;
                break;
            case OpcodeDecoder.DIV:
                ALUOutput = RSValue / (RTValue + 1);
                break;
            case OpcodeDecoder.MOD:
                ALUOutput = RSValue % RTValue;
                break;
            case OpcodeDecoder.AND:
                ALUOutput = RSValue & RTValue;
                break;
            case OpcodeDecoder.OR:
                ALUOutput = RSValue | RTValue;
                break;
            case OpcodeDecoder.XOR:
                ALUOutput = RSValue ^ RTValue;
                break;
            case OpcodeDecoder.CMP:
                // set CMP Register?
                if (RSValue == RTValue) {
                    ALUOutput = 1;
                }
                else {
                    ALUOutput = 0;
                }
                break;
            case OpcodeDecoder.LDR:
                // System.out.println("In LDR");
                memoryOutput = RSValue + RTValue;
                break;
            case OpcodeDecoder.STR:
                memoryOutput = RSValue + RTValue;
                break;
            // IMMEDIATES
            case OpcodeDecoder.ADDI:
                ALUOutput = RSValue + immediate;
                break;
            case OpcodeDecoder.SUBI:
                ALUOutput = RSValue - immediate;
                break;
            case OpcodeDecoder.ASL:
                ALUOutput = RSValue >> immediate;
                break;
            case OpcodeDecoder.ASR: 
                ALUOutput = RSValue << immediate;
                break;
            case OpcodeDecoder.ROT:
                ALUOutput = (RSValue >>> immediate) | (RSValue << (16 - immediate));
                break;
            case OpcodeDecoder.LDI:
                memoryOutput = RSValue + immediate;
                break;
            case OpcodeDecoder.STI:
                memoryOutput = RSValue + immediate;
                break;
            case OpcodeDecoder.BGT:
                // STATUS Register
                if (RSValue > RDValue) {
                    ALUOutput = register.getPC() - 1 - immediate;
                    branchTaken = 1;
                }
                break; 
            case OpcodeDecoder.BLT:
                if (RSValue < RDValue) {
                    ALUOutput = register.getPC() + immediate;
                    branchTaken = 1;
                }
                break;
            case OpcodeDecoder.BOE:
                if (RSValue == RDValue) {
                    ALUOutput = register.getPC() + immediate;
                    branchTaken = 1;
                }
                break;
            // CONTROL    
            case OpcodeDecoder.JI:
                ALUOutput = RDValue + offset;
            case OpcodeDecoder.HLT:
                break;
            case OpcodeDecoder.LD:
                memoryOutput = offset;
                break;
            case OpcodeDecoder.ST:
                memoryOutput = offset;
                break;
            case OpcodeDecoder.PUSH:
                // push
                // iterate through bit vectors
                // push is storing all of the registers onto the stack
                // and decrementing the stack pointer
                // where does the stack pointer point to begin with?
                // need to set up a stack area!
            case OpcodeDecoder.POP:
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
            clearStage(pipeline[0]);
            clearStage(pipeline[1]);
            register.setPC(ALUOutput);
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
        // between 1 and 11
        if ((opcode >= OpcodeDecoder.ADD && opcode <= OpcodeDecoder.STR)) {
            RD = (instruction >> 8) & 0x7;
            RS = (instruction >> 5) & 0x7;
            RT = (instruction >> 2) & 0x7;
            
        }
        // Immediate Type
        // between 12 and 21
        else if ((opcode >= OpcodeDecoder.ADDI && opcode <= OpcodeDecoder.BOE)) {
            RD = (instruction >> 8) & 0x7;
            RS = (instruction >> 5) & 0x7;
            immediate = instruction & 0x1F;
        }
        // Control Type
        // between 22 and 27
        else if ((opcode >= OpcodeDecoder.JI && opcode <= OpcodeDecoder.ST)) {
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
    
    private void stepIF() {
        // fetch instruction
        try {
            pipeline[0].setInstruction(memory.readAddressInMemory(register.getPC()));
        }
        catch (NoSuchMemoryLocationException e){
            System.out.println("Test Failed");
        }
        
        System.out.println("Fetch PC Value: " + register.getPC());
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
        
        if (opcode >= OpcodeDecoder.ADD && opcode <= OpcodeDecoder.LDR || opcode >= OpcodeDecoder.ADDI && opcode <= OpcodeDecoder.LDI) {
            this.hazardValues.setEX_MEM_RegWrite(1);
        }
        
    }
    
    private void updateMEMHazard() {
        // update whatever starts with MEM
        this.hazardValues.setMEM_WB_RegisterRd(pipeline[3].getRD());
        this.hazardValues.setMemoryOutput(pipeline[3].getMemoryOutput());
        int opcode = pipeline[3].getOpcode();
        
        if (opcode >= OpcodeDecoder.ADD && opcode <= OpcodeDecoder.LDR || opcode >= OpcodeDecoder.ADDI && opcode <= OpcodeDecoder.LDI) {
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
        newStage.setOffset(oldStage.getOffset());
        newStage.setTestValue(oldStage.getTestValue());
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
    
    public void printRegisters() {
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
}
