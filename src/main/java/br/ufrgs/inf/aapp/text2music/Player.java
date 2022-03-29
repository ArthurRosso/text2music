package br.ufrgs.inf.aapp.text2music;

/**
 *
 */
public class Player {
    private int bpm;
    private int octave;
    private int timbre;
    private int volume;
    
    public void playNote(char note){
        // TODO: implementar
        System.out.println("Tocando a nota " + note);
    }

    public int getBpm() {
        return bpm;
    }

    public void setBpm(int bpm) {
        System.out.println("Definindo o bpm como " + bpm);
        this.bpm = bpm;
    }

    public int getOctave() {
        return octave;
    }

    public void setOctave(int octave) {
        System.out.println("Definindo a oitava como " + octave);
        this.octave = octave;
    }

    public int getTimbre() {
        return timbre;
    }

    public void setTimbre(int timbre) {
        System.out.println("Definindo o timbre como " + timbre);
        this.timbre = timbre;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        System.out.println("Definindo o volume como " + volume);
        this.volume = volume;
    }
}
