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
import src.NoSuchMemoryLocationException;
/**
 *
 * @author jjaikumar
 */
public class MemoryTest {
    
    public MemoryTest() {
    }
    @Before
    public void setup(){
        
    }
    /**
     * Test of trivial getClock example, of class CPU.
     */
    @Test
    public void testGetClockTrivial() {
        System.out.println("Trivial CPU test case...");
        CPU instance = new CPU();
        int expResult = 0;
        long result = instance.getClock();
        assertEquals("CPU did not initialize Clock correctly" ,expResult, result);
    }
    
    @Test
    public void testResetClock() {
        System.out.println("Test Reset clock...");
        Memory mem = Memory.getMemory();
        try{
            mem.writeAddressInMemory((char)0xffff, 0);
        } catch (Exception e){
            
        }
        
        CPU instance = new CPU(mem);
        instance.resetClock();
        int expResult = 0;
        long result = instance.getClock();
        assertEquals("CPU did not reset Clock correctly" ,expResult, result);
    }
    /**
     * Test of trivial getClock example, of class CPU.
     */
    @Test
    public void testWrite3LocationsClock() {
        System.out.println("Test write to address...");
        Memory mem = Memory.getMemory();
        try {
        mem.writeAddressInMemory((char) 0xffff, 0);
        mem.writeAddressInMemory((char) 0xffff, 0x1ffff);
        mem.writeAddressInMemory((char) 0xaaaa, 0);
        } catch (Exception e){
            
        }
        int expResult = 402;
        long result = mem.getMemoryCycleCount();
        assertEquals("CPU did not write correctly" ,expResult, result);
    }
    
    @Test
    public void testReadWriteClock1() {
        System.out.println("Test read/write to address...");
        Memory memory = Memory.getMemory();
        try {
            assertEquals("CPU did not write correctly" , 0, memory.getMemoryCycleCount());
            memory.writeAddressInMemory((char) 0xffff, 0);
            assertEquals("CPU did not write correctly" , 134, memory.getMemoryCycleCount());
            memory.writeAddressInMemory((char) 0xffff, 0x1ffff);
            assertEquals("CPU did not write correctly" , 268, memory.getMemoryCycleCount());
            memory.writeAddressInMemory((char) 0xffff, 0x1fffe);
            assertEquals("CPU did not write correctly" , 402, memory.getMemoryCycleCount());
            memory.writeAddressInMemory((char) 0xffff, 0x1fffd);
            assertEquals("CPU did not write correctly" , 536, memory.getMemoryCycleCount());
            memory.writeAddressInMemory((char) 0xffff, 0x1fffc);
            assertEquals("CPU did not write correctly" , 670, memory.getMemoryCycleCount());
            memory.writeAddressInMemory((char) 0xaaaa, 0x7fff);
            assertEquals("CPU did not write correctly" , 804, memory.getMemoryCycleCount());
            memory.writeAddressInMemory((char) 0xaaaa, 0);
            assertEquals("CPU did not write correctly" , 938, memory.getMemoryCycleCount());
            assertEquals("CPU did not read correctly" , 43690, memory.readAddressInMemory(0x7fff));
            assertEquals("CPU did not read correctly" , 942, memory.getMemoryCycleCount());
            assertEquals("CPU did not read correctly" , 65535, memory.readAddressInMemory(0x1ffff));
            assertEquals("CPU did not read correctly" , 972, memory.getMemoryCycleCount());
        } catch (NoSuchMemoryLocationException e){
            System.out.println("Test Failed");
        }
    }
}
