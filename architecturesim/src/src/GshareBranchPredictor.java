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
public class GshareBranchPredictor {
    public Register register;
    private TwoBitSaturationCounter[] ghr;
    public Boolean branchOutcome;
    
    public static GshareBranchPredictor gshareBP = new GshareBranchPredictor();
    
    private GshareBranchPredictor() {
        ghr = new TwoBitSaturationCounter[64];
        for (int i = 0; i < 64; i++) {
            ghr[i] = new TwoBitSaturationCounter();
        }
    }
    
    public static GshareBranchPredictor getGshareBP() {
        return gshareBP;
    }
    
    public int getIndex() {
        int index = (register.getPC() & 0b111111) ^ GlobalHistoryRegister.getGHR();
        return index;
    }
    
    public Boolean calculateBranchOutcome() {
        // mask bottom 6 bits of PC with 
        int index = getIndex();
        return ghr[index].currStateIsTaken();
    }
    
    public void updateGsharePredictorTable(Boolean wasTaken, int index) {
        ghr[index].updateState(wasTaken);
        GlobalHistoryRegister.updateGHR(wasTaken);
    }
    
}
