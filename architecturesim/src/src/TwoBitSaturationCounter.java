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
public class TwoBitSaturationCounter {
    private int counter;
    private static final int predictionValue = 2;
    
    public TwoBitSaturationCounter() {
        counter = predictionValue;
    }
    
    public void updateState(Boolean wasTaken) {
        if ((counter >= 0 && counter < 3) && wasTaken) {
            counter++;
        }
        else if ((counter > 0 && counter <= 3) && !wasTaken) {
            counter--;
        }
    }
    
    public Boolean currStateIsTaken() {
        return (counter > 1);
    }
    
    public void resetState() {
        counter = predictionValue;
    }
}
