package br.ufrgs.inf.aapp.text2music;

public class SilentNoteInstruction implements MusicInstruction {
    @Override
    public void doInstruction(Player p) {
        p.playSilence();
    }
}
