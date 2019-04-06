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
    private PipeHazard hazardValues;
    private static boolean pipelineEnabled;
    private static boolean ishalted;
    private String lastInstruction;
    private String englishLastInstruction;

    public String getLastInstruction() {
        return lastInstruction;
    }
    // public static Pipeline pipeline1 = new Pipeline();
    Register register = Register.getRegisters();
    Memory memory = Memory.getMemory();
    GshareBranchPredictor gshareBP = GshareBranchPredictor.getGshareBP();
    BranchTargetAddress BTA = BranchTargetAddress.getBranchTargetAddress();
    PrefetchQueue prefetchQueue = PrefetchQueue.getPrefetchQueue();
    
    public static Pipeline pipelineMain = new Pipeline();
    
    private Pipeline() {
    //private Pipeline() {
        lastInstruction = "";
        englishLastInstruction = "";
        this.pipeline = new PipeStage[6];
        for (int i = 0; i < 6; i++) {
            pipeline[i] = new PipeStage();
        }
        // get rewritten every clock cycle
        this.hazardValues = new PipeHazard();
        // this.pipelineEnabled = pipelineEnabled;
        // step(this.pipeline);
    }
    public void reset(){
        ishalted = false;
        lastInstruction = "";
        englishLastInstruction = "";
        this.pipeline = new PipeStage[6];
        for (int i = 0; i < 6; i++) {
            pipeline[i] = new PipeStage();
        }
        // get rewritten every clock cycle
        this.hazardValues = new PipeHazard();
        // this.pipelineEnabled = pipelineEnabled;
        // step(this.pipeline);
    }
    
    public static void setPipelineEnabled(boolean enabled) {
        pipelineEnabled = enabled;
        System.out.println("*** " + pipelineEnabled);
    }
    
    public static boolean getPipelineEnabled() {
        return pipelineEnabled;
        
    }
    
    public static Pipeline getPipeline() {
        return pipelineMain;
    }
    
    public void step(int instruction) {
        if (pipelineEnabled) {
            // System.out.println("Pipeline Enabled");
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
                            // System.out.println("got to prefetch stage");
                            prefetchQueue.prefetch();
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
            System.out.println("HELLO WORLD");
            int pcOffset = 100;
            int i = ((instruction - pcOffset) % 5) + 1;
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
    public String getEnglishInstruction(){
        return englishLastInstruction;
    }
    
    private void setEnglishInstruction(){
        if (lastInstruction.equals("No instruction")){
            return;
        } else {
            String opCode = lastInstruction.substring(0,5);
            String reg1 = lastInstruction.substring(5,8);
            String reg2 = lastInstruction.substring(8,11);
            String imm = lastInstruction.substring(11);
            englishLastInstruction = englishLastInstruction + opCode(Integer.parseInt(opCode, 2)) + " ";
            englishLastInstruction = englishLastInstruction + registerNumber(Integer.parseInt(reg1, 2)) + " ";
            englishLastInstruction = englishLastInstruction + registerNumber(Integer.parseInt(reg2, 2)) + " ";
            englishLastInstruction = englishLastInstruction + Integer.parseInt(imm,2);
            
        }
    }
    private void stepWB() {
        // WHAT INSTRUCTIONS WRITE BACK?
        // write to register file pipeline[5]
        // NEED TO CAST AS CHAR ?? NEED TO GET THIS STRAIGHT
        
        int inst = pipeline[4].getInstruction();
        if (inst == 0){
            System.out.println("No instruction");
            lastInstruction = "No instruction";
        }
        else {
            String in = String.format("%16s", Integer.toBinaryString(inst)).replace(" ", "0");
            if (in.length() == 16){
                lastInstruction = in;
            } else {
                lastInstruction = in.substring(16);
            }
            // System.out.println("Instruction: " + in + " ");
        }
        englishLastInstruction = "";
        setEnglishInstruction();
        
        int writeDataToRegister;
        int registerWrite = pipeline[4].getRD();
        int opcode  = pipeline[4].getOpcode();
        
        // LDs
        if (opcode == OpcodeDecoder.LDR || opcode == OpcodeDecoder.LDI || opcode == OpcodeDecoder.LD) {
            writeDataToRegister = pipeline[4].getMemoryOutput();
            register.setRegisterValue(registerWrite, writeDataToRegister);
            // System.out.println("Register was loaded" + writeDataToRegister + "register " + registerWrite );

        }      
        // ALU Ops
        else if (opcode >= OpcodeDecoder.ADD && opcode <= OpcodeDecoder.CMP || opcode >= OpcodeDecoder.ADDI && opcode <= OpcodeDecoder.ROT) {
            // System.out.println("writing data " + pipeline[4].getALUOutput() + "to register " + registerWrite);
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
            if (pipelineEnabled) {
                updateMEMHazard();
            }
        }
        // Only STR for sure works, need to check ST and STI
        else if (opcode == OpcodeDecoder.STR || opcode == OpcodeDecoder.STI || opcode == OpcodeDecoder.ST) {
            try {
                data = register.getRegisterValue(RD);
                memory.writeAddressInMemory(data, address);
                // System.out.println("writing data " + data + "to memory " + address);
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
        int predictedBranchOutcome;
        Boolean predictedBranchOutcomeBool = pipeline[2].getPredictedBranchOutcome();
        if (predictedBranchOutcomeBool) {
            predictedBranchOutcome = 1;
        }
        else {
            predictedBranchOutcome = 0;
        }
        // System.out.println(opcode);
        
        int ALUOutput = 0;
        int memoryOutput = 0;
        
        // memoryOutput is for load/store, ALUOutput is for new address calculations
        // do we need both??
        if (pipelineEnabled) {
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
                // System.out.println("In SUBI");
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
                    ALUOutput = register.getPC() - immediate;
                    System.out.println("\t BRANCH TAKEN " + ALUOutput);
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
                // System.out.println("Entered Halt stage");
                setIshalted(true);
                lastInstruction = "Entered halt";
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
        if (pipelineEnabled) {
            if (opcode >= OpcodeDecoder.BGT && opcode <= OpcodeDecoder.BOE) {
                // branch should not have been taken but predicted it should
                if (branchTaken == 0 && predictedBranchOutcome == 1) {
                    clearHazards();
                    clearStage(pipeline[0]);
                    clearStage(pipeline[1]);
                    System.out.println("\n Real branch NO, predicted branch YES: " + register.getPrevPC());
                    register.setPC(register.getPrevPC()); // reset PC for prefetch queue
                    pipeline[2].setALUOutput(register.getPrevPC()); // finish rest of branch instruction
                }
                // branch should have been taken but predicted it didn't
                else if (branchTaken == 1 && predictedBranchOutcome == 0) {
                    clearHazards();
                    clearStage(pipeline[0]);
                    clearStage(pipeline[1]);
                    int targetAddress = ALUOutput - 1;
                    System.out.println("\n Real branch YES, predicted branch NO: " + targetAddress);
                    register.setPC(targetAddress); // reset PC for prefetch queue
                    pipeline[2].setALUOutput(targetAddress); // finish rest of branch instruction
                    BTA.addAddress(register.getBR(), targetAddress);
                }
                else {
                    System.out.println("\t Branch predicted correctly");
                }
                if (branchTaken == 1) {
                    gshareBP.updateGsharePredictorTable(true, gshareBP.getIndex());
                }
                else {
                    gshareBP.updateGsharePredictorTable(false, gshareBP.getIndex());
                }
            }
            // else: predicted correctly => we are fine
            else {
                pipeline[2].setALUOutput(ALUOutput);
                pipeline[2].setMemoryOutput(memoryOutput);
            }
            updateEXHazard();
        }
        else { // don't need branch prediction if pipeline is disabled
            if (branchTaken == 1) {
                register.setPC(ALUOutput);
                pipeline[2].setALUOutput(ALUOutput);
            }
        }
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
        // pipeline[1].setBranchTaken();
        if (pipelineEnabled) {
            updateIDHazard();
        }
    }
    
    private void stepIF() {
        // fetch instruction
        // System.out.println("Fetch PC Value: " + register.getPC());
        
        // if pipeline is disabled, fetch from memory
        if (!pipelineEnabled) {
            try {
                // set instruction from prefetch queue
                pipeline[0].setInstruction(memory.readAddressInMemory(register.getPC()));
            }
            catch (NoSuchMemoryLocationException e){
                System.out.println("Test Failed");
            }
            register.setPC(register.getPC() + 1);
        }
        // if pipelineis enabled (meaning branch prediction), fetch from prefetch queue
        else {
            pipeline[0].setInstruction(0x0000FFFF & prefetchQueue.getPrefetchedInstruction());
            pipeline[0].setPredictedBranchOutcome(prefetchQueue.getPrefectchedInstructionPrediction());
        }
        System.out.println("PC value: " + register.getPC());
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
        newStage.setPredictedBranchOutcome(oldStage.getPredictedBranchOutcome());
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
        stage.setPredictedBranchOutcome(false);
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
     * @return the ishalted
     */
    public static boolean isIshalted() {
        return ishalted;
    }

    /**
     * @param aIshalted the ishalted to set
     */
    public static void setIshalted(boolean aIshalted) {
        ishalted = aIshalted;
    }
    
    private String opCode(int inst) {
        switch(inst){
            case 1: return "ADD";
            case 2: return "SUB";
            case 3: return "MUL";
            case 4: return "DIV";
            case 5: return "MOD";
            case 6: return "AND";
            case 7: return "OR";
            case 8: return "XOR";
            case 9: return "CMP";
            case 10: return "LDR";
            case 11: return "STR";
            case 12: return "ADDI";
            case 13: return "SUBI";
            case 14: return "ASL";
            case 15: return "ASR";
            case 16: return "ROT";
            case 17: return "LDI";
            case 18: return "STI";
            case 19: return "BGT";
            case 20: return "BLT";
            case 21: return "BOE";
            case 22: return "JI";
            case 23: return "HLT";
            case 24: return "PUSH";
            case 25: return "POP";
            case 26: return "LD";
            case 27: return "ST";
            default: return "";
        }
    }
    private String registerNumber(int register) {
        switch(register){
            case 0: return "R0";
            case 1: return "R1";
            case 2: return "R2";
            case 3: return "R3";
            case 4: return "R4";
            case 5: return "R5";
            case 6: return "R6";
            case 7: return "R7";
            default: return "R0";
        }
    }
}
