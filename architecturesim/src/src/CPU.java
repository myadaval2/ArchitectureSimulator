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
        memory.writeAddressInMemory((char) 0xffff, 0);
        memory.writeAddressInMemory((char) 0xffff, 0x1ffff);
        memory.writeAddressInMemory((char) 0xaaaa, 0);
        System.out.println(memory.getMemoryCycleCount());
    }
    
    // During MEM stage CPU.clock += memory.getmemoryCycleCount();
    
}
