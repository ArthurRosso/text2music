package br.ufrgs.inf.aapp.text2music;

import java.util.ArrayList;

/**
 *
 */
public class MusicInstructionList {
    
    private ArrayList<MusicInstruction> instructions = new ArrayList<>();
    private int i = 0;

    public MusicInstruction getNextInstruction(){
        if (i >= this.instructions.size()) {
            return null;
        }
        MusicInstruction result = this.instructions.get(i);
        this.i++;

        return result;
    }
    
    public void add(MusicInstruction ins) {
        this.instructions.add(ins);
    }
}
