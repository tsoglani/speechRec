/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jarvis;

import java.util.ArrayList;

/**
 *
 * @author tsoglani
 */
public class SH {

    static ArrayList<String> outputPowerCommands = new ArrayList<String>();

    public SH() {
        outputPowerCommands.add("kitchen lights");
        outputPowerCommands.add("room light");
        outputPowerCommands.add("bedroom lights");
        outputPowerCommands.add("living room lights");
        outputPowerCommands.add("tv");
        outputPowerCommands.add("garden");
        outputPowerCommands.add("bathroom lights");
    }

}
