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

    private Memory  memory;
    private int     clock;

    public CPU() {      this(true);     }
    
    public CPU(boolean cacheEnabled){
        //this.memory     = new Memory(cacheEnabled);
        this.clock      = 0;
    }
    public CPU(Memory memory){
        this.memory     = memory;
        this.clock      = memory.getMemoryCycleCount();
    }
    
    public Memory getMemory()   {       return this.memory;     }
    public int getClock()       {       return this.clock;      }
    public void resetClock()    {       this.clock = 0;         }
    
}
