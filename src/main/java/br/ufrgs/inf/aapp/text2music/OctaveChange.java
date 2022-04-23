package br.ufrgs.inf.aapp.text2music;

import javax.sound.midi.Sequencer;
import javax.sound.midi.Track;

/**
 *
 */
public class OctaveChange implements MusicInstruction {
    boolean isIncrease;

    public OctaveChange(boolean isIncrease) {
        this.isIncrease = isIncrease;
    }
    
    public void doInstruction(Player p){
        if (this.isIncrease) {
            p.setOctave(p.getOctave() + 1);
        } else {
            p.setOctave(p.getOctave() - 1);
        }
    }

    @Override
    public long record(Player p, long tick, Track t) {
        this.doInstruction(p);
        return tick;
    }
}
