package br.ufrgs.inf.aapp.text2music;

/**
 *
 */
public class Main {
    public static void main(String args[]){
        String partitura = "abc";
        
        Player p = new Player();
        Decoder d = new Decoder();
        MusicInstructionList l = d.decode(partitura);
        
        MusicInstruction i = l.getNextInstruction();
        while (i != null){
            i.doInstruction(p);
            i = l.getNextInstruction();
        }
        
    }
    
}
