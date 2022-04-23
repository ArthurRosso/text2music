package br.ufrgs.inf.aapp.text2music;

import javax.sound.midi.Sequencer;
import javax.sound.midi.Track;

/**
 *
 */
public interface MusicInstruction {
    public void doInstruction(Player p);
    public long record(Player p, long tick, Track t);
}
