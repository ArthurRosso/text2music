package br.ufrgs.inf.aapp.text2music;

import javax.sound.midi.Sequencer;
import javax.sound.midi.Track;

/**
 *
 */
public class OctaveChange implements MusicInstruction {
    private int increase;

    public OctaveChange(int increase) {
        this.increase = increase;
    }
    
    public void doInstruction(Player p){
        if (this.increase == 0) {
            p.setOctave(p.INITIAL_OCTAVE);
        } else {
            p.setOctave(p.getOctave() + this.increase);
        }
    }

    @Override
    public long record(Player p, long tick, Track t) {
        this.doInstruction(p);
        return tick;
    }
}
