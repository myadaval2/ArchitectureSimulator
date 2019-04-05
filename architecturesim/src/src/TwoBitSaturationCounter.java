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
    
    public TwoBitSaturationCounter() {
        counter = 2;
    }
    
    public void updateState(Boolean wasTaken) {
        if ((counter >= 0 && counter < 3) && wasTaken) {
            counter++;
        }
        else if ((counter >= 0 && counter < 3) && !wasTaken) {
            counter--;
        }
    }
    
    public Boolean currStateIsTaken() {
        return (counter > 1);
    }
}
