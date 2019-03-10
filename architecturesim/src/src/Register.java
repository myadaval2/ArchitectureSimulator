/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src;

/**
 *
 * @author megha
 */
public class Register {
    private char[]      Registers;
    private char        PC;
    private char        NextPC;
    private char        SP;
    private char        RTN;
    private char        CMP;
    
    public Register() {
        this.Registers = new char[8];
        this.PC        = 0;
        this.NextPC    = 0;
        this.SP        = 0;
        this.RTN       = 0;
        this.CMP       = 0;
    }
    
}
