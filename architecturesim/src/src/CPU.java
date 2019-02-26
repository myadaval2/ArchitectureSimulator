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
    public int getClock()       {       return clock;       }
    
    // During MEM stage CPU.clock += memory.getmemoryCycleCount();
    
}
