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
public class GlobalHistoryRegister {
    private static int mRecentBranchOutcomes;
    
    public static GlobalHistoryRegister ghr = new GlobalHistoryRegister();
    
    private GlobalHistoryRegister() {
        mRecentBranchOutcomes = 0;
    }
    
    public static int getGHR() {
        return mRecentBranchOutcomes;
    }
    
    public static void updateGHR(Boolean wasTaken) {
        mRecentBranchOutcomes = mRecentBranchOutcomes << 1;
        mRecentBranchOutcomes = mRecentBranchOutcomes & 0x3F;
        if (wasTaken) {
            mRecentBranchOutcomes += 1;
        }
    }
}
