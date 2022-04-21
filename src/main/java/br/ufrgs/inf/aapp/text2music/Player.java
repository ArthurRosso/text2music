package br.ufrgs.inf.aapp.text2music;

import javax.sound.midi.*;
import java.util.HashMap;

/**
 *
 */
public class Player {
    private float bpm;
    private int octave;
    private int timbre;
    private int volume;
    private Synthesizer midiSynth;
    private Instrument[] instr;
    private MidiChannel[] mChannels;
    private HashMap<Character, Integer> notes = new HashMap<Character, Integer>();
    
    public Player(){
        this.reset();
        notes.put('a', 9);
        notes.put('b', 11);
        notes.put('c', 0);
        notes.put('d', 2);
        notes.put('e', 4);
        notes.put('f', 5);
        notes.put('g', 7);
        
        try{
            midiSynth = MidiSystem.getSynthesizer();
            instr = midiSynth.getDefaultSoundbank().getInstruments();
            mChannels = midiSynth.getChannels();
            midiSynth.open();

            midiSynth.loadInstrument(instr[this.timbre]);
            
        } catch( MidiUnavailableException e ) {
            System.out.println("Player MIDI exception");    
        }
    }
    
    public void reset() {
        bpm = 60;
        octave = 5;
        volume = 1;
        timbre = 0;
    }
    
    public void playNote(char note){
        int n = notes.get(note)+octave*12;
        mChannels[0].noteOn(n, 100); 
        try { 
            Thread.sleep(Math.round(1000/(bpm/60)));
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        mChannels[0].noteOff(n);
    }

    public float getBpm() {
        return bpm;
    }

    public void setBpm(float bpm) {
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
