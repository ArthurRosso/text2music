package br.ufrgs.inf.aapp.text2music;

import java.util.Random;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

public class TimbreChange implements MusicInstruction {
    private final int increase;
    private final boolean absolute;
    
    public TimbreChange(int increase, boolean absolute) {
        this.increase = increase;
        this.absolute = absolute;
    }
    
    public void doInstruction(Player p) {
        if (this.absolute) {
            p.setTimbre((byte)this.increase);
        } else {
            p.setTimbre((byte)(p.getTimbre() + increase));
        }
    }

    @Override
    public long record(Player p, long tick, Track t) {
        this.doInstruction(p);
        byte timbre = p.getTimbre();
        
        try {
            ShortMessage m = new ShortMessage();
            m.setMessage(0xC0, timbre, 0);
            t.add(new MidiEvent(m, tick));
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return tick;
    }
}
