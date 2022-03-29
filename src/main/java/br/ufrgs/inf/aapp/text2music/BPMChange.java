package br.ufrgs.inf.aapp.text2music;


/**
 *
 */
public class BPMChange implements MusicInstruction {
    private boolean isIncrease;
    
    public BPMChange(boolean isIncrease) {
        this.isIncrease = isIncrease;
    }

    public void doInstruction(Player p) {
        p.setBpm(p.getBpm() + (this.isIncrease ? 50 : -50));
    }
}
