/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import static junit.framework.Assert.assertEquals;
import org.junit.Test;
import org.junit.*;
import src.CPU;
import src.Memory;
import src.Cache;
import src.NoSuchMemoryLocationException;
/**
 *
 * @author jjaikumar
 */
public class MemoryTest {
    
    public MemoryTest() {
    }
   
    /**
     * Test of trivial getClock example, of class CPU.
     */
    @Test
    public void testGetClockTrivial() {
        System.out.println("Trivial CPU test case...");
        CPU instance = new CPU();
        int expResult = 0;
        int result = instance.getClock();
        assertEquals("CPU did not initialize Clock correctly" ,expResult, result);
    }
    /**
     * Test of trivial getClock example, of class CPU.
     */
    @Test
    public void testWrite3LocationsClock() {
        System.out.println("Test write to address...");
        CPU instance = new CPU();
        Memory mem = instance.getMemory();
        mem.writeAddressInMemory((char) 0xffff, 0);
        mem.writeAddressInMemory((char) 0xffff, 0x1ffff);
        mem.writeAddressInMemory((char) 0xaaaa, 0);
        int expResult = 402;
        int result = mem.getMemoryCycleCount();
        assertEquals("CPU did not write correctly" ,expResult, result);
    }
    
    @Test
    public void testReadWrite1() {
        System.out.println("Test read/write to address...");
        CPU instance = new CPU();
        Memory memory = instance.getMemory();
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
        try {
            System.out.println(memory.readAddressInMemory(0x7fff));
            System.out.println("Clock cycle count: " + memory.getMemoryCycleCount());
            System.out.println(memory.readAddressInMemory(0x1ffff));
            System.out.println("Clock cycle count: " + memory.getMemoryCycleCount());
        } catch (NoSuchMemoryLocationException e){
            System.out.println("Test Failed");
        }
    }
}
