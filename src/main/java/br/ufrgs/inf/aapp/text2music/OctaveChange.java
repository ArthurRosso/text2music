package br.ufrgs.inf.aapp.text2music;


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
}
