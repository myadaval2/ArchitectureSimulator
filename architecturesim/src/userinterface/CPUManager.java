/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package userinterface;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import src.Memory;
import src.NoSuchMemoryLocationException;
import src.Pipeline;
import src.Register;

/**
 *
 * @author jjaikumar
 */

public class CPUManager {
    public Pipeline pipeline;
    public Memory memory;
    public Register register;
    private static int clockCycles;
    public boolean isFinished;
    public Map<String, Integer> lookup;
    
    public String[] fullProgram;
    public Map<String,Integer> functionCallOffsets;
    

    public static CPUManager driver = new CPUManager();
    
    private CPUManager(){
        isFinished = false;
        memory = Memory.getMemory();
        register = Register.getRegisters();
        pipeline = Pipeline.getPipeline();
        clockCycles = 0;
        register.clearRegisterFile();
        
    }
    public void setClockCycles(int cycles) {
        clockCycles = cycles;
    }
    public static CPUManager getCPUManager() {
        return driver;
    }
    int i;
    public void Stepper(){
            if (Pipeline.isIshalted()){
                isFinished = true;
            } 
            else {
                pipeline.step(i);
                clockCycles = i + memory.getMemoryCycleCount();
                i++;
            }   
            // System.out.println("Number of clock cycles: " + clockCycles);
    } 
    
    public static int getClockCycles() {
        return clockCycles;
    }
    
    public void readIn(String filename){
        
//         for (int i = 0; i < 10; i++) {
//             try {
//                 memory.writeAddressInMemory(i, i); // ST 0-9 in Mem[0] - Mem[9]
//                 memory.writeAddressInMemory(i*10, i + 10); // ST 11-19 in Mem[10] - Mem[90]
//             } catch (NoSuchMemoryLocationException ex) {
//             }
//            }
        lookup = new HashMap<>();
        register.setPC(0); // where instructions begin
        i = register.getPC();
        int pc = register.getPC();
        functionCallOffsets = new HashMap<>();
        
        if (filename.equals("")){
            //TODO
        }
        else {
            try{
                boolean isInstruction = false;
                String[] program = readLines(filename);
                this.fullProgram = program;
                int i = 0;
                while(!isInstruction){
                    // System.out.println(program[i]);
                    if (program[i].equals("data:") || program[i].length() == 0 || program[i].charAt(0) == '#' ){
                        
                    } 
                    else if (program[i].equals("instructions:")){
                        isInstruction = true;
                    }
                    else {
                        String[] dataAddress = program[i].split("\\,"); 
                        memory.writeAddressInMemory(Integer.parseInt(dataAddress[0]), Integer.parseInt(dataAddress[1]));
                    }
                    i++;
                }
                int offset = 0;
                boolean functionSave = false;
                String funcName = "";
                for (int j = i; j < program.length; j++){
                    String[] line = program[j].split(" "); 
                    //System.out.println(line[0]);
                    if (line[0].equals("")) {
                        continue;
                    } else {
                    if (program[j].charAt(0) == '#' || program[j].length() == 0){
                        
                    } else if (line[0].equals("FUNC")){
                        functionCallOffsets.put(line[1],pc+offset);
                        offset++;
                    }
                        else if (line[0].equals("function:")){
                        functionSave = true;
                        funcName = line[1];
                    }
                    else{
                        String binString = convert(program[j]);
                        int instruction = Integer.parseInt(binString,2);
                        // System.out.println("Instruction: " + program[j] + " Address: " + (pc+offset));
                        // System.out.println(binString);
                        memory.writeAddressInMemory(instruction, pc+offset); 
                        if (functionSave){
                            lookup.put(funcName, pc+offset);
                            functionSave = false;
                            funcName = "";
                        }
                        offset++; 
                    }
                    }
                }  
            }
            catch (NoSuchMemoryLocationException | IOException e){
                System.out.println("Bad File - LINE 139");
                System.exit(1);
            }
        }
        // System.out.println("SECOND PASS");
        for (String inst : this.fullProgram){
            String[] line = inst.split(" "); 
            if (line[0].equals("FUNC")){
                // System.out.println(lookup.keySet());
                int address = lookup.get(line[1]);
                String binString = "11111";
                binString += String.format("%11s", Integer.toBinaryString(address)).replace(" ", "0");
                
                int putback = functionCallOffsets.get(line[1]);
                int instruction = Integer.parseInt(binString,2);
                
                // System.out.println("Function: " + line[1] + " At Address: " + address + " goes at line: " + putback);
                // System.out.println("Binary string: " + binString);

                try {
                    memory.writeAddressInMemory(instruction, putback); 
                } catch (Exception e){
                    
                }
            }
        }
    }
    
    public String[] readLines(String filename) throws IOException {
        FileReader fileReader = new FileReader(filename);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        List<String> lines = new ArrayList<String>();
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            lines.add(line);
        }
        bufferedReader.close();
        return lines.toArray(new String[lines.size()]);
    }
    
    public String convert(String programLine){
        String[] instructions = programLine.split(" "); 
        if (instructions[0].equals("HLT")){
            return "1011100000000000";
        }

        if (instructions[0].equals("LD")){
            String binString = "";
            try {
                binString += String.format("%5s", Integer.toBinaryString(opCode(instructions[0]))).replace(" ", "0");
            } catch (BadInstructionException e){
                System.out.println("Please load a different program");
            }
            try {
                binString += String.format("%3s", Integer.toBinaryString(registerNumber(instructions[1]))).replace(" ", "0");
            } catch (BadInstructionException e){
                System.out.println("Please load a different program");
            }
            
            binString += String.format("%8s", Integer.toBinaryString(Integer.parseInt(instructions[2]))).replace(" ", "0");
            return binString;
        }

        if (instructions[0].equals("PUSH")){
            String binString = "11000";
            binString += String.format("%11s", Integer.toBinaryString(Integer.parseInt(instructions[1]))).replace(" ", "0");
            return binString;
        }

        if (instructions[0].equals("POP")){
            String binString = "11001";
            binString += String.format("%11s", Integer.toBinaryString(Integer.parseInt(instructions[1]))).replace(" ", "0");
            return binString;
        }
        
        if (instructions[0].equals("ADD")){
            String binString = "";
            try {
                binString += String.format("%5s", Integer.toBinaryString(opCode(instructions[0]))).replace(" ", "0");
            } catch (BadInstructionException e){
                System.out.println("Please load a different program");
            }
            try {
                binString += String.format("%3s", Integer.toBinaryString(registerNumber(instructions[1]))).replace(" ", "0");
            } catch (BadInstructionException e){
                System.out.println("Please load a different program");
            }
            try {
                binString += String.format("%3s", Integer.toBinaryString(registerNumber(instructions[2]))).replace(" ", "0");
            } catch (BadInstructionException e){
                System.out.println("Please load a different program");
            }
            try {
                binString += String.format("%3s", Integer.toBinaryString(registerNumber(instructions[3]))).replace(" ", "0");
            } catch (BadInstructionException e){
                System.out.println("Please load a different program");
            }
            binString += "00";
            return binString;
        }
        
        if (instructions[0].equals("MUL")){
            String binString = "";
            try {
                binString += String.format("%5s", Integer.toBinaryString(opCode(instructions[0]))).replace(" ", "0");
            } catch (BadInstructionException e){
                System.out.println("Please load a different program");
            }
            try {
                binString += String.format("%3s", Integer.toBinaryString(registerNumber(instructions[1]))).replace(" ", "0");
            } catch (BadInstructionException e){
                System.out.println("Please load a different program");
            }
            try {
                binString += String.format("%3s", Integer.toBinaryString(registerNumber(instructions[2]))).replace(" ", "0");
            } catch (BadInstructionException e){
                System.out.println("Please load a different program");
            }
            try {
                binString += String.format("%3s", Integer.toBinaryString(registerNumber(instructions[3]))).replace(" ", "0");
            } catch (BadInstructionException e){
                System.out.println("Please load a different program");
            }
            binString += "00";
            return binString;
        }

        String binString = "";
           // String in = String.format("%16s", Integer.toBinaryString(inst)).replace(" ", "0");
           try {
               binString += String.format("%5s", Integer.toBinaryString(opCode(instructions[0]))).replace(" ", "0");
           } catch (BadInstructionException e){
               System.out.println("Please load a different program");
           }
           try {
               binString += String.format("%3s", Integer.toBinaryString(registerNumber(instructions[1]))).replace(" ", "0");
           } catch (BadInstructionException e){
               System.out.println("Please load a different program");
           }
           try {
               binString += String.format("%3s", Integer.toBinaryString(registerNumber(instructions[2]))).replace(" ", "0");
           } catch (BadInstructionException e){
               System.out.println("Please load a different program");
           }

           binString += String.format("%5s", Integer.toBinaryString(Integer.parseInt(instructions[3]))).replace(" ", "0");

        return binString;
    }
    private int opCode(String inst) throws BadInstructionException{
        switch(inst){
            case "ADD": return 1;
            case "SUB": return 2;
            case "MUL": return 3;
            case "DIV": return 4;
            case "MOD": return 5;
            case "AND": return 6;
            case "OR": return 7;
            case "XOR": return 8;
            case "CMP": return 9;
            case "LDR": return 10;
            case "STR": return 11;
            case "ADDI": return 12;
            case "SUBI": return 13;
            case "ASL": return 14;
            case "ASR": return 15;
            case "ROT": return 16;
            case "LDI": return 17;
            case "STI": return 18;
            case "BGT": return 19;
            case "BLT": return 20;
            case "BOE": return 21;
            case "JI": return 22;
            case "HLT": return 23;
            case "PUSH": return 24;
            case "POP": return 25;
            case "LD": return 26;
            case "ST": return 27;
            case "FUNC": return 31;
            default: throw new BadInstructionException(inst);
        }
    }
    private int registerNumber(String register) throws BadInstructionException{
        switch(register){
            case "-": return 0;
            case "R0": return 0;
            case "R1": return 1;
            case "R2": return 2;
            case "R3": return 3;
            case "R4": return 4;
            case "R5": return 5;
            case "R6": return 6;
            case "R7": return 7;
            default: throw new BadInstructionException(register);
        }
    }
}
class BadInstructionException extends Exception {
    String instruction;
    public BadInstructionException(String instruction){
        this.instruction = instruction;
        System.out.println("No such instruction exists: "+ instruction);
    }
}