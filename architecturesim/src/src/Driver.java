/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src;

import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author jjaikumar
 */

public class Driver {
    private Scanner scan;
    public Pipeline pipeline;
    public Memory memory;
    public Register register;
    private static int clockCycles;
    public boolean isFinished;

    public static Driver driver = new Driver();
    
    private Driver(){
        isFinished = false;
        scan = new Scanner(System.in);
        memory = Memory.getMemory();
        register = Register.getRegisters();
        pipeline = Pipeline.getPipeline();
        Memory.setCacheEnabled(true);
        Pipeline.setPipelineEnabled(true);
        clockCycles = 0;
//        Memory.setCacheEnabled(false);
//        Pipeline.setPipelineEnabled(false);
//        forLoopTest();
        register.clearRegisterFile();
        
    }

    
    public static void main(String[] args){
       // directCompare(memoryEnabled, memoryDisabled);
       // CLI();
       Driver driver = Driver.getDriver();
       
       // driver.allCacheDemoReal();
       // driver.pipelineTest();
       
    }
    
    public static Driver getDriver() {
        return driver;
    }
    int i;
    public void forLoopStepper(){
            if (Pipeline.isIshalted()){
                isFinished = true;
            } else {
                pipeline.step(i);
            //System.out.println(i);
            
            clockCycles = i + memory.getMemoryCycleCount();
            // System.out.println("instruction clock cycles " + i + " memory count " + memory.getMemoryCycleCount() + " PC Value: " + register.getPC());
            //System.out.println("i clock cycles" + i);
            i++;
            
            Register.printRegisters();
            }
            
            
        
    }
    public void forLoopTeardown(){
        System.out.println("i clock cycles " + i + " memory count " + memory.getMemoryCycleCount());
        System.out.println(getClockCycles());
        Register.printRegisters();
        try {
            for (i = 0; i < 30; i++) {
                System.out.println("Read address " + i + ": " + memory.readAddressInMemory(i));
            }
        }
        catch (NoSuchMemoryLocationException e){
            System.out.println("Test Failed");
        }
    }
    public void forLoopSetup(){
        Register.printRegisters();
        try {
            for (int i = 0; i < 10; i++) {
                memory.writeAddressInMemory(i, i); // ST 0-9 in Mem[0] - Mem[9]
                memory.writeAddressInMemory(i*10, i + 10); // ST 11-19 in Mem[10] - Mem[90]
            }
//            for (int i = 20; i < 25; i++) {
//                memory.writeAddressInMemory(0, i); // ST 0 in Mem[10] - Mem[14]
//            }
            
            int mem = 100;
        
            
            memory.writeAddressInMemory(0b1101000100000000, mem); // LD R1 with Arr1 (Load with const value)
            memory.writeAddressInMemory(0b1101001000001010, mem + 1); // LD R2 with Arr2 (Load with const value)
            memory.writeAddressInMemory(0b1101001100010100, mem + 2); // LD R3 with Arr3 (Load with const value)
            memory.writeAddressInMemory(0b0110011100001010, mem + 3); // ADDI R7, R0, 10 COUNTER
            memory.writeAddressInMemory(0b0101011000110000, mem + 4); // LDR R6 with Mem[R1+R4]
            memory.writeAddressInMemory(0b0101010101010000, mem + 5); // LDR R5 with Mem[R2+R4]
            memory.writeAddressInMemory(0b0000111010111000, mem + 6); // ADD R6, R5, R6
            memory.writeAddressInMemory(0b0101111001110000, mem + 7); // STR R6 at Mem[R3+R4]
            memory.writeAddressInMemory(0b0110010010000001, mem + 8); // ADDI R4, R4, 1
            memory.writeAddressInMemory(0b0110111111100001, mem + 9); // SUBI R7, R7, -1
            memory.writeAddressInMemory(0b1001100011100111, mem + 10); // BGT R0, R7 -7 
            memory.writeAddressInMemory(0b1011100000000000, mem + 11); // HLT
    }
        
        catch (NoSuchMemoryLocationException e){
            System.out.println("Test Failed");
        }
        
        register.setPC(100); // where instructions begin
        i = register.getPC();
        memory.resetMemoryCycleCount();
        int step = 0;
        
    }
    public void forLoopTest() {
        
        Register.printRegisters();
        try {
            for (int i = 0; i < 10; i++) {
                memory.writeAddressInMemory(i, i); // ST 0-9 in Mem[0] - Mem[9]
                memory.writeAddressInMemory(i*10, i + 10); // ST 11-19 in Mem[10] - Mem[90]
            }
            
            int mem = 100;
        
            
            memory.writeAddressInMemory(0b1101000100000000, mem); // LD R1 with Arr1 (Lwoad with const value)
            memory.writeAddressInMemory(0b1101001000001010, mem + 1); // LD R2 with Arr2 (Load with const value)
            memory.writeAddressInMemory(0b1101001100010100, mem + 2); // LD R3 with Arr3 (Load with const value)
            memory.writeAddressInMemory(0b0110011100001010, mem + 3); // ADDI R7, R0, 10 COUNTER
            memory.writeAddressInMemory(0b0101011000110000, mem + 4); // LDR R6 with Mem[R1+R4]
            memory.writeAddressInMemory(0b0101010101010000, mem + 5); // LDR R5 with Mem[R2+R4]
            memory.writeAddressInMemory(0b0000111010111000, mem + 6); // ADD R6, R5, R6
            memory.writeAddressInMemory(0b0101111001110000, mem + 7); // STR R6 at Mem[R3+R4]
            memory.writeAddressInMemory(0b0110010010000001, mem + 8); // ADDI R4, R4, 1
            memory.writeAddressInMemory(0b0110111111100001, mem + 9); // SUBI R7, R7, -1
            memory.writeAddressInMemory(0b1001100011100111, mem + 10); // BGT R0, R7 -7 
            memory.writeAddressInMemory(0b1011100000000000, mem + 11); // HLT
            /**
             * 
             *  input (R1, R2, R3)
             *  for (int R7 = 10; R7 > 0; R7--){
             *      R6 = R1 + R4
             *      R5 = R2 + R4
             *      R6 = R5 + R6
             *      
             *  }
             */
            
//            memory.writeAddressInMemory(0b0110011100000011, 15); // ADDI R7, R0, 5 COUNTER
//            memory.writeAddressInMemory(0b0110111111100001, 16); // SUBI R7, R7, -1
//            memory.writeAddressInMemory(0b1001100011100010, 17); // BGT R0, R7 -1
//            memory.writeAddressInMemory(0b1011100000000000, 18); // HLT

        }
        catch (NoSuchMemoryLocationException e){
            System.out.println("Test Failed");
        }
        
        
        
        register.setPC(100); // where instructions begin
        int i = register.getPC();
        memory.resetMemoryCycleCount();
        int step = 0;
        while(!Pipeline.isIshalted()) {
            // register.printRegisters();
            
            
            scan.nextLine();
            System.out.println("Step: " + ++step);
            pipeline.step(i);
            //System.out.println(i);
            
            clockCycles = i + memory.getMemoryCycleCount();
            // System.out.println("instruction clock cycles " + i + " memory count " + memory.getMemoryCycleCount() + " PC Value: " + register.getPC());
            //System.out.println("i clock cycles" + i);
            i++;
            
            Register.printRegisters();
            // register.printRegisters();
        }
        System.out.println("i clock cycles " + i + " memory count " + memory.getMemoryCycleCount());
        System.out.println(getClockCycles());
        Register.printRegisters();
        try {
            for (i = 0; i < 30; i++) {
                System.out.println("Read address " + i + ": " + memory.readAddressInMemory(i));
            }
        }
        catch (NoSuchMemoryLocationException e){
            System.out.println("Test Failed");
        }
    }
    
    public static int getClockCycles() {
        return clockCycles;
    }
    
    public void pipelineTest() {
        memory = Memory.getMemory();
        register = Register.getRegisters();
        try {
            // ADD ADD ADD hazards
//            memory.writeAddressInMemory(0b0000100101001100, 0); //2380
//            memory.writeAddressInMemory(0b0001001001110000, 1); //4720
//            memory.writeAddressInMemory(0b0001101110010100, 2); //7060
//            memory.writeAddressInMemory(0b0010010010111000, 3); //9400
//            memory.writeAddressInMemory(0b0010110111011100, 4); //11740
            
// checking LD ST ADD dependencies
            memory.writeAddressInMemory(0b0000101101000100, 0); //
            memory.writeAddressInMemory(0b0000110000101100, 1); //
            memory.writeAddressInMemory(0b0000111001110000, 2); //
            memory.writeAddressInMemory(0b1000101000100000, 3); // LD R2 with Mem[R1+0]
            memory.writeAddressInMemory(0b0000111000110000, 4); // ADD R6 = R1 + R4, R6 = 4
            memory.writeAddressInMemory(0b1001011000011111, 5); // ST R6 into Mem[R0+31] -> Mem[31]
            
// branch test
//            memory.writeAddressInMemory(0b0000101101000100, 0); // LD R1, 0
//            memory.writeAddressInMemory(0b0000110000101100, 1); // LD R3 5
//            memory.writeAddressInMemory(0b0101101101100001, 0); // SUBi R3 R3, -1
//            memory.writeAddressInMemory(0b1000100101100010, 1); // BGT R1 R3 -2
//            memory.writeAddressInMemory(0b0101001001000001, 2); // Addi R2 R2 +1
//            System.out.println("Read address 0: " + memory.readAddressInMemory(0));
//            // System.out.println("\n\n");
//            System.out.println("Read address 1: " + memory.readAddressInMemory(1));
//            System.out.println("Read address 2: " + memory.readAddressInMemory(2));
            
        }
        catch (NoSuchMemoryLocationException e){
            System.out.println("Test Failed");
        }
        
        Register.printRegisters();
        
        for (int i = 0; i < 30; i++) {
            pipeline.step(i);
            for (int j = 0; j < 6; j++) {
                // System.out.println( pipeline.getPipeline()[j].getInstruction());
//                            + " " + pipeline.getPipeline()[j].getOpcode()
//                            + " " + pipeline.getPipeline()[j].getRD()
//                            + " " + pipeline.getPipeline()[j].getRS()
//                            + " " + pipeline.getPipeline()[j].getRT()
//                            + " " + pipeline.getPipeline()[j].getImmediate()
//                            + " " + pipeline.getPipeline()[j].getALUOutput());
        
            }
        }  
        Register.printRegisters();
        try {
            System.out.println("Read address 0: " + memory.readAddressInMemory(0));
            System.out.println("Read address 1: " + memory.readAddressInMemory(1));
            System.out.println("Read address 2: " + memory.readAddressInMemory(2));
            System.out.println("Read address 3: " + memory.readAddressInMemory(3));
            System.out.println("Read address 4: " + memory.readAddressInMemory(4));
            System.out.println("Read address 5: " + memory.readAddressInMemory(5));
            System.out.println("Read address 31: " + memory.readAddressInMemory(31));
        }
        catch (NoSuchMemoryLocationException e){
            System.out.println("Test Failed");
        }
        
    }
    
    
    
    public void memoryDemo(Memory memoryEnabled){
        System.out.println("Real Demo for caching, n-set associative, and n-lines");
        // memoryStep(memoryEnabled);
//        System.out.println("Demo 1: Cache disabled");
//        memoryDemo1(memoryDisabled);
        
        // System.out.println("Demo 2: Cache enabled");
        // memoryDemo2(memoryEnabled);
        
//        System.out.println("Demo 2: N-Set Associativity");
//        nSetAssociativityDemo(memoryEnabled);
        
//        System.out.println("DirectCompare Test");
//        directCompare(memoryEnabled, memoryDisabled);
    }
    
    
    public void allCacheDemoReal() {
        Memory memory = Memory.getMemory();
        try {
            
            memory.writeAddressInMemory(0b0000100101001100, 0); //2380
            memory.writeAddressInMemory(0b0001001001110000, 1); //4720
            memory.writeAddressInMemory(0b0001101110010100, 2); //7060
            memory.writeAddressInMemory(0b0010010010111000, 3); //9400
            memory.writeAddressInMemory(0b0010110111011100, 4); //11740
            System.out.println("Read address 0: " + memory.readAddressInMemory(0));
            System.out.println("Read address 1: " + memory.readAddressInMemory(1));
            System.out.println("Read address 2: " + memory.readAddressInMemory(2));
            System.out.println("Read address 3: " + memory.readAddressInMemory(3));
            System.out.println("Read address 4: " + memory.readAddressInMemory(4));
            // L1 Page 0: 00000	DEADEEEE
            // L1 Page 0: 04002	FFFF0000
            // L1 Page 0: 0C002	ACED0000
            // L1 Page 0: 06000	22220000
            // L2 Page 0: 00000	DEADEEEE
            // L2 Page 0: 0C000	11110000
            // L2 Page 0: 04002	FFFF0000
            // L2 Page 0: 0C002	ACED0000
            // L2 Page 64: 11000 BBBB0000
            // L2 Page 64: 0D000 AAAA0000
            // L2 Page 128: 02000 33330000
            // L2 Page 128: 06000 22220000
//            memory.writeAddressInMemory((char) 0xdead, 0b00000000000000000); 
//            memory.writeAddressInMemory((char) 0xeeee, 0b00000000000000001);
//            memory.writeAddressInMemory((char) 0x3333, 0b00010000000000000);
//            memory.writeAddressInMemory((char) 0x6666, 0b00100000000000000);
//            memory.writeAddressInMemory((char) 0x1111, 0b01100000000000000);
//            memory.writeAddressInMemory((char) 0xffff, 0b00100000000000010); 
//            memory.writeAddressInMemory((char) 0xaced, 0b01100000000000010); 
//            memory.writeAddressInMemory((char) 0xbbbb, 0b10001000000000000);
//            memory.writeAddressInMemory((char) 0xaaaa, 0b01101000000000000);
//            memory.writeAddressInMemory((char) 0x2222, 0b00110000000000000); 
//            memory.writeAddressInMemory((char) 0xabcd, 0b11111111111111111); 
        }
        catch (NoSuchMemoryLocationException e){
            System.out.println("Test Failed");
        }
       }
    
    
//    public static void CLI(){
//        Scanner scan = new Scanner(System.in);
//        System.out.println("Architecture Memory Interface\nCache enabled? y/n\n");
//        String ans1 = scan.next();
//        Memory mem;
//        if (ans1.charAt(0) == 'y'){
//            mem = new Memory(true);
//        } else {
//            mem = new Memory(false);
//        }
//        ArrayList<Integer> written = new ArrayList<Integer>();
//        while(true){
//            System.out.println("Available commands: r --Read, w --Write, s --show memory, x --exit");
//            System.out.println("Enter a command");
//            char ans2 = scan.next().charAt(0);
//            if (ans2 == 'x'){
//                System.out.println("Final state of memory: ");
//                System.out.println("Address     Data");
//                for (int i = 0; i < written.size(); ++i){
//                    try {
//                        System.out.println(written.get(i) + "    " + mem.readAddressInMemory(written.get(i)));
//                    } catch (Exception e){
//                        System.out.println("Operation failed");
//                    }        
//                }
//                System.out.println("Total entries: " + written.size());
//                System.out.println("Cycles: "+mem.getMemoryCycleCount());
//                break;
//            }
//            if (ans2 == 'r'){
//                if (written.isEmpty()){
//                    System.out.println("No address to read");
//                } else {
//                    System.out.println("Enter an address to read");
//                    int address = Integer.parseInt(scan.next());
//                    try{
//                        int data = mem.readAddressInMemory(address);
//                        System.out.println("Memory at " + address + " is " + data);
//                        System.out.println("Cycles: "+mem.getMemoryCycleCount());
//                    } catch(Exception e){
//                        System.out.println("Invalid address");
//                    }
//                }
//            }
//            if (ans2 == 'w'){
//                System.out.println("Enter an address to write to");
//                int address = Integer.parseInt(scan.next());
//                System.out.println("Enter data to write");
//                int data = Integer.parseInt(scan.next());
//                
//                try{
//                    mem.writeAddressInMemory((char) data, address);
//                    written.add(address);
//                    System.out.println("Cycles: "+mem.getMemoryCycleCount());
//                } catch(Exception e){
//                    System.out.println("Invalid address");
//                }
//            }
//            if (ans2 == 's'){
//                if (written.isEmpty()){
//                    System.out.println("No address to show");
//                } else {
//                    System.out.println("Address     Data");
//                    for (int i = 0; i < written.size(); ++i){
//                        try {
//                            System.out.println(written.get(i) + "    " + mem.readAddressInMemory(written.get(i)));
//                        } catch (Exception e){
//                            System.out.println("Operation failed");
//                        }
//                        
//                    }
//                    System.out.println("Total entries: " + written.size());
//                    System.out.println("Cycles: "+mem.getMemoryCycleCount());
//                }
//            }
//            
//        }
//        
//        
//    }
    
    public static void nSetAssociativityDemo(Memory memoryEnabled) {
        Memory memory = memoryEnabled;
        try {
            memory.writeAddressInMemory((char) 0x6666, 0x00fff); // 0 displayed
            System.out.println("Cycle count: " + memory.getMemoryCycleCount());
        }
        catch (NoSuchMemoryLocationException e){
            System.out.println("Test Failed");
        }
        
    }
    
    public static void directCompare(Memory memoryEnabled, Memory memoryDisabled){
        
        try {
            memoryEnabled.writeAddressInMemory((char) 10, 0);
            memoryEnabled.writeAddressInMemory((char) 11, 1);
            memoryEnabled.writeAddressInMemory((char) 12, 2);
            memoryEnabled.writeAddressInMemory((char) 13, 3);
            memoryEnabled.writeAddressInMemory((char) 14, 4);

            memoryDisabled.writeAddressInMemory((char) 10, 0);
            memoryDisabled.writeAddressInMemory((char) 11, 1);
            memoryDisabled.writeAddressInMemory((char) 12, 2);
            memoryDisabled.writeAddressInMemory((char) 13, 3);
            memoryDisabled.writeAddressInMemory((char) 14, 4);
            
            int memEnabledCycles = memoryEnabled.getMemoryCycleCount();
            int memDisabledCycles = memoryDisabled.getMemoryCycleCount();
            
            System.out.println("Cache enabled write 5 pieces data, cycles: " + memEnabledCycles);
            System.out.println("Cache disabled write 5 pieces data, cycles: " + memDisabledCycles);
            
            for (int i = 0; i < 5; i++){
                System.out.println("Read data at " + i);
                
                System.out.println(
                        "Cache Enabled: " + "Data at " + i + " is " + 
                        memoryEnabled.readAddressInMemory(i) + 
                         ". Took " + (memoryEnabled.getMemoryCycleCount() - memEnabledCycles) + 
                         " cycles. Total cycles is: " + memoryEnabled.getMemoryCycleCount());
                memEnabledCycles = memoryEnabled.getMemoryCycleCount();
                
                System.out.println(
                        "Cache Disabled: " + "Data at " + i + " is " + 
                        memoryDisabled.readAddressInMemory(i) + 
                         ". Took " + (memoryDisabled.getMemoryCycleCount() - memDisabledCycles) + 
                         " cycles. Total cycles is: " +  memoryDisabled.getMemoryCycleCount());
                memDisabledCycles = memoryDisabled.getMemoryCycleCount();
            }
            
            System.out.println("Cache enabled write then read 5 pieces data, cycles: " + memEnabledCycles);
            System.out.println("Cache disabled write then read 5 pieces data, cycles: " + memDisabledCycles);
            
            
        } catch (Exception e){
            
        }
        
    }
    
    public static void memoryDemo2(Memory memoryEnabled){
        // Memory memory = new Memory(true);
        Memory memory = memoryEnabled; //new Memory(true);
        try {        
            
//            System.out.println("Cycle count: " + memory.getMemoryCycleCount());
//            memory.writeAddressInMemory((char) 0xffff, 0);
//            System.out.println("Cycle count: " + memory.getMemoryCycleCount());
//            memory.writeAddressInMemory((char) 0xffff, 0x1ffff);
//            System.out.println("Cycle count: " + memory.getMemoryCycleCount());
//            memory.writeAddressInMemory((char) 0xffff, 0x1fffe);
//            System.out.println("Cycle count: " + memory.getMemoryCycleCount());
//            memory.writeAddressInMemory((char) 0xffff, 0x1fffd);
//            System.out.println("Cycle count: " + memory.getMemoryCycleCount());
            
            
            
            // test case for write and read same address
            
            System.out.println("Cycle count: " + memory.getMemoryCycleCount());
            memory.writeAddressInMemory((char) 0xaaaa, 0x7fff);
//            System.out.println("Cycle count: " + memory.getMemoryCycleCount());
//            memory.writeAddressInMemory((char) 0xaaaa, 0);
//            System.out.println("Cycle count: " + memory.getMemoryCycleCount());
//            System.out.println("Read address 0x7fff: " + memory.readAddressInMemory(0x7fff));
//            System.out.println("Cycle count: " + memory.getMemoryCycleCount());
//            System.out.println("Read address 0x1ffff: " + memory.readAddressInMemory(0x1ffff));
//            System.out.println("Read address 0x1fffe: " + memory.readAddressInMemory(0x1fffe));
//            System.out.println("Cycle count: " + memory.getMemoryCycleCount());
        } catch (NoSuchMemoryLocationException e){
            System.out.println("Test Failed");
        }
    }
    public static void memoryDemo1(Memory memoryDisabled){
        // Memory memory = new Memory(false);
        Memory memory = memoryDisabled;
        try {
            System.out.println("Cycle count: " + memory.getMemoryCycleCount());
            memory.writeAddressInMemory((char) 0xffff, 0);
            System.out.println("Cycle count: " + memory.getMemoryCycleCount());
            memory.writeAddressInMemory((char) 0xffff, 0x1ffff);
            System.out.println("Cycle count: " + memory.getMemoryCycleCount());
            memory.writeAddressInMemory((char) 0xffff, 0x1fffe);
            System.out.println("Cycle count: " + memory.getMemoryCycleCount());
            memory.writeAddressInMemory((char) 0xffff, 0x1fffd);
            System.out.println("Cycle count: " + memory.getMemoryCycleCount());
            memory.writeAddressInMemory((char) 0xffff, 0x1fffc);
            System.out.println("Cycle count: " + memory.getMemoryCycleCount());
            memory.writeAddressInMemory((char) 0xaaaa, 0x7fff);
            System.out.println("Cycle count: " + memory.getMemoryCycleCount());
            memory.writeAddressInMemory((char) 0xaaaa, 0);
            System.out.println("Cycle count: " + memory.getMemoryCycleCount());
            System.out.println("Read address 0x7fff: " + memory.readAddressInMemory(0x7fff));
            System.out.println("Cycle count: " + memory.getMemoryCycleCount());
            System.out.println("Read address 0x1ffff: " + memory.readAddressInMemory(0x1ffff));
            System.out.println("Cycle count: " + memory.getMemoryCycleCount());
        } catch (NoSuchMemoryLocationException e){
            System.out.println("Test Failed");
        }
    }
}
