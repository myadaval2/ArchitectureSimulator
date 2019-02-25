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

    public CPU() {
        memory = new Memory();
    }
    
    public Memory getMemory(){      return memory;      }
    
}
