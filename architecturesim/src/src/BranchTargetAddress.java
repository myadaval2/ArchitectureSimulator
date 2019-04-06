/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src;
import java.util.*;

/**
 *
 * @author megha
 */
public class BranchTargetAddress {
    private int MAX_ENTRIES = 10;
    private LinkedHashMap<Integer,Integer> branchTargetAddress;
    
    public static BranchTargetAddress BTA = new BranchTargetAddress();
    
    private BranchTargetAddress() {
        branchTargetAddress = new LinkedHashMap<Integer,Integer>(MAX_ENTRIES + 1, .75F, false) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<Integer,Integer> eldest) {
               return size() > MAX_ENTRIES;
            }
        };   
    }
    
    public static BranchTargetAddress getBranchTargetAddress() {
        return BTA;
    }
    
    public void addAddress(Integer branchInstructionAddress, Integer branchTargetAddress) {
        this.branchTargetAddress.put(branchInstructionAddress, branchTargetAddress);
    }
    
    public int getTargetAddress(Integer branchInstructionAddress) {
        if (this.branchTargetAddress.get(branchInstructionAddress) == null) {
            return -1;
        }
        else {
            return this.branchTargetAddress.get(branchInstructionAddress);
        }
    }

}
