package br.ufrgs.inf.aapp.text2music;

import java.io.File;
import java.io.IOException;
import javax.sound.midi.*;
import java.util.HashMap;

/**
 *
 */
public class Player {
    public static final int INITIAL_VOLUME = 100;
    public static final int INITIAL_OCTAVE = 5;
    
    private float bpm;
    private byte timbre;
    private int octave;
    private int volume;
    private Synthesizer midiSynth;
    private Instrument[] instr;
    private MidiChannel[] mChannels;
    private HashMap<Character, Integer> notes = new HashMap<Character, Integer>();
    
    public Player(){
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
            this.reset();
        } catch( MidiUnavailableException e ) {
            System.out.println("Player MIDI exception");    
        }
    }
    
    public void reset() {
        bpm = 60;
        octave = INITIAL_OCTAVE;
        volume = INITIAL_VOLUME;
        setTimbre((byte)0);
    }
    
    public void playNote(char note, NoteStyle style) {
        int n = notes.get(note) + octave*12 + pitch(style);
        mChannels[0].noteOn(n, volume); 
        try { 
            Thread.sleep(Math.round(1000/(bpm/60)));
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        mChannels[0].noteOff(n);
    }
    
    private int pitch(NoteStyle style) {
        if (style == NoteStyle.Flat) {
            return -1;
        }
        if (style == NoteStyle.Sharp) {
            return 1;
        }
        return 0;
    }
    
    public void playSilence() {
        try { 
            Thread.sleep(Math.round(1000/(bpm/60)));
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void export(String path, MusicInstructionList l) throws MidiUnavailableException, InvalidMidiDataException, IOException {
        this.reset();
        
        Sequence seq = new Sequence(Sequence.PPQ, 4);
        
        Track t = seq.createTrack();
        new BPMChange(0).record(this, 0, t); // Initialize BPM
        
        MusicInstruction i = l.getNextInstruction();
        long tick = 0;
        while (i != null) {
            tick = i.record(this, tick, t);
            i = l.getNextInstruction();
        }
        
        File out = new File(path);
        MidiSystem.write(seq, MidiSystem.getMidiFileTypes(seq)[0], out);
    }
    
    public void recordNote(long tick, Track t, char note) throws InvalidMidiDataException {
        int n = notes.get(note)+octave*12;
        t.add(new MidiEvent(new ShortMessage(ShortMessage.NOTE_ON, 0, n, volume), tick));
        t.add(new MidiEvent(new ShortMessage(ShortMessage.NOTE_OFF, 0, n, volume), tick+4));
    }
    
    public float getBpm() {
        return bpm;
    }

    public void setBpm(float bpm) {
        System.out.println("Definindo o bpm como " + bpm);
        this.bpm = bpm >= 60 ? bpm : 60;
    }

    public int getOctave() {
        return octave;
    }

    public void setOctave(int octave) {
        System.out.println("Definindo a oitava como " + octave);
        this.octave = octave;
    }

    public byte getTimbre() {
        return timbre;
    }

    public void setTimbre(byte timbre) {
        System.out.println("Definindo o timbre como " + timbre);
        this.timbre = timbre;
        mChannels[0].programChange(this.timbre);
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        System.out.println("Definindo o volume como " + volume);
        this.volume = volume;
    }
}
