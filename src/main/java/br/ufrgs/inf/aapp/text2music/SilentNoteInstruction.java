package br.ufrgs.inf.aapp.text2music;

import javax.sound.midi.Sequencer;
import javax.sound.midi.Track;

public class SilentNoteInstruction implements MusicInstruction {
    @Override
    public void doInstruction(Player p) {
        p.playSilence();
    }

    @Override
    public long record(Player p, long tick, Track t) {
        return tick + 4;
    }
}
