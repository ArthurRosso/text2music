package br.ufrgs.inf.aapp.text2music;

import javax.sound.midi.Track;

public class Note implements MusicInstruction {
    private final NoteStyle style;
    private final char note;

    public Note(char note, NoteStyle style) {
        this.note = note;
        this.style = style;
    }
    
    public void doInstruction(Player p) {
        p.playNote(this.note, style);
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
