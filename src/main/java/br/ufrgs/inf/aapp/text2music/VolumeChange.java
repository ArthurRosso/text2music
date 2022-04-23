package br.ufrgs.inf.aapp.text2music;

import javax.sound.midi.Sequencer;
import javax.sound.midi.Track;

public class VolumeChange implements MusicInstruction {
    private boolean isIncrease;
    
    public VolumeChange(boolean isIncrease) {
        this.isIncrease = isIncrease;
    }

    public void doInstruction(Player p) {
        if (this.isIncrease) {
            p.setVolume(p.getVolume() * 2);
        } else {
            p.setVolume(p.INITIAL_VOLUME);
        }
    }

    @Override
    public long record(Player p, long tick, Track t) {
        this.doInstruction(p);
        return tick;
    }
}
