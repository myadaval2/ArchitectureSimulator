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
public class CPU {

    private Memory memory;
    private int clock;

    public CPU() {
        memory = new Memory();
        clock = 0;
    }
    
    public Memory getMemory()   {       return memory;      }
    public int getClock()       {       return memory.getMemoryCycleCount();       }
    
    public void testCase1() {
        System.out.println("Clock cycle count: " + memory.getMemoryCycleCount());
        memory.writeAddressInMemory((char) 0xffff, 0);
        System.out.println("Clock cycle count: " + memory.getMemoryCycleCount());
        memory.writeAddressInMemory((char) 0xffff, 0x1ffff);
        System.out.println("Clock cycle count: " + memory.getMemoryCycleCount());
        memory.writeAddressInMemory((char) 0xffff, 0x1fffe);
        System.out.println("Clock cycle count: " + memory.getMemoryCycleCount());
        memory.writeAddressInMemory((char) 0xffff, 0x1fffd);
        System.out.println("Clock cycle count: " + memory.getMemoryCycleCount());
        memory.writeAddressInMemory((char) 0xffff, 0x1fffc);
        System.out.println("Clock cycle count: " + memory.getMemoryCycleCount());
        memory.writeAddressInMemory((char) 0xaaaa, 0x7fff);
        System.out.println("Clock cycle count: " + memory.getMemoryCycleCount());
        memory.writeAddressInMemory((char) 0xaaaa, 0);
        System.out.println("Clock cycle count: " + memory.getMemoryCycleCount());
        System.out.println(memory.readAddressInMemory(0x7fff));
        System.out.println("Clock cycle count: " + memory.getMemoryCycleCount());
        System.out.println(memory.readAddressInMemory(0x1ffff));
        System.out.println("Clock cycle count: " + memory.getMemoryCycleCount());
    }
    
    // During MEM stage CPU.clock += memory.getmemoryCycleCount();
    
}
