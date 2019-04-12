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
public class PrefetchQueue {
    BranchTargetAddress BTA = BranchTargetAddress.getBranchTargetAddress();
    GshareBranchPredictor gshareBP = GshareBranchPredictor.getGshareBP();
    Register register = Register.getRegisters();
    Memory memory = Memory.getMemory();
    public int[] instruction;
    public Boolean[] prediction;
    // prefetch queue has 2 parts
    // 0th element is where it is adding
    // 1st element is where pipeline fetch is getting information from
    
    public static PrefetchQueue prefetchQueue = new PrefetchQueue();
    
    private PrefetchQueue() {
        this.instruction = new int[2];
        this.prediction = new Boolean[2];
        this.instruction[0] = 0;
        this.prediction[0] = false;
        this.instruction[1] = 0;
        this.prediction[1] = false;
    }
    
    public void prefetch() {
        if (true) {
            try {
                // System.out.println("\tCheck after branch: " + register.getPC());
                instruction[0] = memory.readAddressInMemory(register.getPC());
            }
            catch (NoSuchMemoryLocationException e){
                System.out.println("Test Failed");
            }
            int opcode = (instruction[0] >> 11) & 0x1F;
            // System.out.println("opcode: " + opcode);
            // branch instruction
            if (opcode >= OpcodeDecoder.BGT && opcode <= OpcodeDecoder.BOE) {
                // System.out.println("Branch Instruction");
                register.setBR(register.getPC());
                register.setPrevPC(register.getPC()+1);
                // System.out.println("\tBR register: " + register.getBR());
                // get branch prediction output
                if (BTA.getTargetAddress(register.getBR()) != -1) {
                    // System.out.println("\tBranch target found");
                    // if predicted true, change pc
                    if (gshareBP.branchPredictedTaken()) {
                        // System.out.println("target address from file: " + "\t" + BTA.getTargetAddress(register.getBR()));
                        register.setPC(BTA.getTargetAddress(register.getBR()));
                        prediction[0] = gshareBP.branchPredictedTaken();
                    }
                    else {
                        register.setPC(register.getPC() + 1);
                    }
                }
                else {
                    register.setPC(register.getPC() + 1);
                    // System.out.println("Branch instruction had no match so PC incremented " + register.getPC());
                }
            }
            else {
                    register.setPC(register.getPC() + 1);
                    // System.out.println("PC incremented" + register.getPC());
            }
            copyAndClearStage();
        }
    }
    
    public static PrefetchQueue getPrefetchQueue() {
        return prefetchQueue;
    }
    public int getPrefetchedInstruction() {
        return instruction[1];
    }
    
    public Boolean getPrefectchedInstructionPrediction() {
        return prediction[1];
    }
    
    public void copyAndClearStage() {
        instruction[1] = instruction[0];
        prediction[1] = prediction[0];
        instruction[0] = 0;
        prediction[0] = false;
    }
}
