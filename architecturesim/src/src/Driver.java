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


    public Driver(){}
    
    public static void main(String[] args){}
    
    public static void memoryDemo1(){
        Memory memory = new Memory(true);
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
        try {
            System.out.println("Read address 0x7fff: " + memory.readAddressInMemory(0x7fff));
            System.out.println("Cycle count: " + memory.getMemoryCycleCount());
            System.out.println("Read address 0x1ffff: " + memory.readAddressInMemory(0x1ffff));
            System.out.println("Cycle count: " + memory.getMemoryCycleCount());
        } catch (NoSuchMemoryLocationException e){
            System.out.println("Test Failed");
        }
    }
    public static void memoryDemo2(){
        Memory memory = new Memory(false);
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
        try {
            System.out.println("Read address 0x7fff: " + memory.readAddressInMemory(0x7fff));
            System.out.println("Cycle count: " + memory.getMemoryCycleCount());
            System.out.println("Read address 0x1ffff: " + memory.readAddressInMemory(0x1ffff));
            System.out.println("Cycle count: " + memory.getMemoryCycleCount());
        } catch (NoSuchMemoryLocationException e){
            System.out.println("Test Failed");
        }
    }
}
