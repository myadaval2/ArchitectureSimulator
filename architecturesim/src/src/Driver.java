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
    public Memory memoryEnabled;
    public Memory memoryDisabled;
    public int counter;
    char[] values = {0xdead, 0xeeee, 0x3333, 0x6666, 0x1111, 0xffff, 0xaced, 0xbbbb, 0xaaaa, 0x2222, 0xaced};
    int[] address = {0x00000, 0x00001, 0x2000, 0x4000, 0xc000, 0x4002, 0xc002, 0x11000, 0xd000, 0x6000, 0x1ffff};
        

    public Driver(){
        memoryEnabled = new Memory(true);
        memoryDisabled = new Memory(false);
        this.counter = 0;
        // memoryDemo(memoryEnabled, memoryDisabled);
    }
    
    public static void main(String[] args){
       // directCompare(memoryEnabled, memoryDisabled);
       CLI();
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
    
    public void memoryStep(Memory memoryEnabled) {
        try {
            memoryEnabled.writeAddressInMemory(this.values[this.counter], this.address[this.counter]); 
            this.counter += 1;
        }
        catch (NoSuchMemoryLocationException e){
            System.out.println("Test Failed");
        }
    }
    
    public void allCacheDemoReal(Memory memory) {
        try {
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
            memory.writeAddressInMemory((char) 0xdead, 0b00000000000000000); 
            memory.writeAddressInMemory((char) 0xeeee, 0b00000000000000001);
            memory.writeAddressInMemory((char) 0x3333, 0b00010000000000000);
            memory.writeAddressInMemory((char) 0x6666, 0b00100000000000000);
            memory.writeAddressInMemory((char) 0x1111, 0b01100000000000000);
            memory.writeAddressInMemory((char) 0xffff, 0b00100000000000010); 
            memory.writeAddressInMemory((char) 0xaced, 0b01100000000000010); 
            memory.writeAddressInMemory((char) 0xbbbb, 0b10001000000000000);
            memory.writeAddressInMemory((char) 0xaaaa, 0b01101000000000000);
            memory.writeAddressInMemory((char) 0x2222, 0b00110000000000000); 
            memory.writeAddressInMemory((char) 0xabcd, 0b11111111111111111); 
        }
        catch (NoSuchMemoryLocationException e){
            System.out.println("Test Failed");
        }
       }
    
    
    public static void CLI(){
        Scanner scan = new Scanner(System.in);
        System.out.println("Architecture Memory Interface\nCache enabled? y/n\n");
        String ans1 = scan.next();
        Memory mem;
        if (ans1.charAt(0) == 'y'){
            mem = new Memory(true);
        } else {
            mem = new Memory(false);
        }
        ArrayList<Integer> written = new ArrayList<Integer>();
        while(true){
            System.out.println("Available commands: r --Read, w --Write, s --show memory, x --exit");
            System.out.println("Enter a command");
            char ans2 = scan.next().charAt(0);
            if (ans2 == 'x'){
                System.out.println("Final state of memory: ");
                System.out.println("Address     Data");
                for (int i = 0; i < written.size(); ++i){
                    try {
                        System.out.println(written.get(i) + "    " + mem.readAddressInMemory(written.get(i)));
                    } catch (Exception e){
                        System.out.println("Operation failed");
                    }        
                }
                System.out.println("Total entries: " + written.size());
                System.out.println("Cycles: "+mem.getMemoryCycleCount());
                break;
            }
            if (ans2 == 'r'){
                if (written.isEmpty()){
                    System.out.println("No address to read");
                } else {
                    System.out.println("Enter an address to read");
                    int address = Integer.parseInt(scan.next());
                    try{
                        int data = mem.readAddressInMemory(address);
                        System.out.println("Memory at " + address + " is " + data);
                        System.out.println("Cycles: "+mem.getMemoryCycleCount());
                    } catch(Exception e){
                        System.out.println("Invalid address");
                    }
                }
            }
            if (ans2 == 'w'){
                System.out.println("Enter an address to write to");
                int address = Integer.parseInt(scan.next());
                System.out.println("Enter data to write");
                int data = Integer.parseInt(scan.next());
                
                try{
                    mem.writeAddressInMemory((char) data, address);
                    written.add(address);
                    System.out.println("Cycles: "+mem.getMemoryCycleCount());
                } catch(Exception e){
                    System.out.println("Invalid address");
                }
            }
            if (ans2 == 's'){
                if (written.isEmpty()){
                    System.out.println("No address to show");
                } else {
                    System.out.println("Address     Data");
                    for (int i = 0; i < written.size(); ++i){
                        try {
                            System.out.println(written.get(i) + "    " + mem.readAddressInMemory(written.get(i)));
                        } catch (Exception e){
                            System.out.println("Operation failed");
                        }
                        
                    }
                    System.out.println("Total entries: " + written.size());
                    System.out.println("Cycles: "+mem.getMemoryCycleCount());
                }
            }
            
        }
        
        
    }
    
    public static void nSetAssociativityDemo(Memory memoryEnabled) {
        Memory memory = memoryEnabled;
        try {
            // TEST 1
            // Write multiple consecutive memories
            // Read same multiple consecutive memories
            
            
            // TEST 2
            // Write multiple locations that map to same L1Cache
            // Read same multiple locations that map to same L1Cache
//            memory.writeAddressInMemory((char) 0x0101, 0x00000); // 0
//            //memory.writeAddressInMemory((char) 0x1010, 0x00000); // 0
//            memory.writeAddressInMemory((char) 0x0101, 0x00001);
//            System.out.println("Read address 0x00000: " + memory.readAddressInMemory(0x00000)); // 0
//            System.out.println("Read address 0x00001: " + memory.readAddressInMemory(0x00001)); // 0
//            memory.writeAddressInMemory((char) 0xffff, 0x0002); // 0
//            //memory.writeAddressInMemory((char) 0x1010, 0x00000); // 0
//            memory.writeAddressInMemory((char) 0xaaaa, 0x00003);
//            
////            memory.writeAddressInMemory((char) 0x1cccc, 0x00000); // 0
////            memory.writeAddressInMemory((char) 0xffff, 0x00001); 
//            memory.writeAddressInMemory((char) 0xdddd, 0x10001);
//            memory.writeAddressInMemory((char) 0x0101, 0x1f000); // 0
//            memory.writeAddressInMemory((char) 0xffff, 0x00010); // 0
//            memory.writeAddressInMemory((char) 0xaaaa, 0x17000); // 0 displayed
//
//            memory.writeAddressInMemory((char) 0xdead, 0x1ffff); // 0 displayed
//            memory.writeAddressInMemory((char) 0xeeee, 0x1ffff); // 0 displayed
//            memory.writeAddressInMemory((char) 0x3333, 0x10fff); // 0 displayed
//            memory.writeAddressInMemory((char) 0x1111, 0x1fffe); // 0 displayed
            memory.writeAddressInMemory((char) 0x6666, 0x00fff); // 0 displayed
            

//            memory.writeAddressInMemory((char) 0xdead, 0x1ffff); // 0 displayed

            
            // TEST 3
            // Write multiple locations that map to same L2Cache
            // Read same multiple locations that map to same L2Cache
//            memory.writeAddressInMemory((char) 0x0101, 0x03fff);
//            memory.writeAddressInMemory((char) 0xaaaa, 0x13fff);
//            memory.writeAddressInMemory((char) 0xaaaa, 0x17fff);
//            memory.writeAddressInMemory((char) 0xffff, 0x1ffff);
//            memory.readAddressInMemory(0x03fff);
//            memory.writeAddressInMemory((char) 0x00ff, 0x03fff); // left
//            memory.writeAddressInMemory((char) 0xaaaa, 0x17fff);
            
            // TEST 4
            // Write to location (all map to same set)
            // Read same location
            // Write different location
            // Read first location
            // Write different location
            

            
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
