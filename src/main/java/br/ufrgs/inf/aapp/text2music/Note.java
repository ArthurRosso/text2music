package br.ufrgs.inf.aapp.text2music;


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
}
