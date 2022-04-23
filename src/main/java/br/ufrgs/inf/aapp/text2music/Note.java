package br.ufrgs.inf.aapp.text2music;

import javax.sound.midi.Track;


/**
 *
 */
public class Note implements MusicInstruction {
    char note;

    public Note(char note) {
        this.note = note;
    }
    public void doInstruction(Player p){
        p.playNote(this.note);
    }

    @Override
    public long record(Player p, long tick, Track t) {
        try {
            p.recordNote(tick, t, note);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tick + 4;
    }
}
