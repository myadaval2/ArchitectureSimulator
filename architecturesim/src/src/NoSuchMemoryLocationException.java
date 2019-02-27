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
public class NoSuchMemoryLocationException extends Exception {
    int address;
    public NoSuchMemoryLocationException(int address){
        this.address = address;
        System.out.println("No such memory location exists: "+ address);
    }
    public NoSuchMemoryLocationException(){
        System.out.println("No such memory location exists");
    }
}