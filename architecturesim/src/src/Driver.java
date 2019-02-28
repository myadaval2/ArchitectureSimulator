/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src;

/**
 *
 * @author jjaikumar
 */

public class Driver {
    public Memory memoryEnabled;
    public Memory memoryDisabled;

    public Driver(){
        memoryEnabled = new Memory(true);
        memoryDisabled = new Memory(false);
        memoryDemo(memoryEnabled, memoryDisabled);
    }
    
    public void main(String[] args){
        directCompare(memoryEnabled, memoryDisabled);
    }
    
    public void memoryDemo(Memory memoryEnabled, Memory memoryDisabled){
//        System.out.println("Demo 1: Cache disabled");
//        memoryDemo1(memoryDisabled);
        
        System.out.println("Demo 2: Cache enabled");
        memoryDemo2(memoryEnabled);
        
//        System.out.println("DirectCompare Test");
//        directCompare(memoryEnabled, memoryDisabled);
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
            
            memory.writeAddressInMemory((char) 0x0101, 0x00ffc);
            memory.writeAddressInMemory((char) 0xffff, 0x1fffc);
            memory.writeAddressInMemory((char) 0xaaaa, 0x0fffc);
            memory.writeAddressInMemory((char) 0xdead, 0x03ffc);
            
            memory.writeAddressInMemory((char) 0x0101, 0x03fff); 
            memory.writeAddressInMemory((char) 0xffff, 0x07fff);
            memory.readAddressInMemory(0x03fff);
            memory.writeAddressInMemory((char) 0x00ff, 0x03fff); // left
            memory.writeAddressInMemory((char) 0xaaaa, 0x17fff);
            System.out.println("Cycle count: " + memory.getMemoryCycleCount());
            
            // test case for write and read same address
            
//            System.out.println("Cycle count: " + memory.getMemoryCycleCount());
//            memory.writeAddressInMemory((char) 0xaaaa, 0x7fff);
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
