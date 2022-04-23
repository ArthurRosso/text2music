package br.ufrgs.inf.aapp.text2music;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.Track;

/**
 *
 */
public class BPMChange implements MusicInstruction {
    private static final byte MIDI_SETTEMPO = 0x51;
    
    private int change;
    
    public BPMChange(int change) {
        this.change = change;
    }

    public void doInstruction(Player p) {
        p.setBpm(p.getBpm() + this.change);
    }

    @Override
    public long record(Player p, long tick, Track seq) {
        this.doInstruction(p);
        float bpm = p.getBpm();
        int res = Math.round(60000000 / (bpm)); // Delay between notes in microseconds
        byte[] bytes = encodeInt(res);
        
        // BPM+abcBPM+abcBPM+abcBPM+abcBPM+abcBPM+abcBPM+abcBPM+abcBPM+abc 
        try {
            seq.add(new MidiEvent(new MetaMessage(MIDI_SETTEMPO, bytes, bytes.length), tick));
        } catch (InvalidMidiDataException ex) {
            ex.printStackTrace();
        }
        
        return tick;
    }
    
    private byte[] encodeInt(int i) {
        byte[] bytes = ByteBuffer
                .allocate(4)
                .order(ByteOrder.BIG_ENDIAN)
                .putInt(i)
                .array();
        
        List<Byte> byteList = new ArrayList<>();
        for (byte b : bytes) {
            byteList.add(b);
        }
        
        while (!byteList.isEmpty()) {
            if (byteList.get(0) == 0) {
                byteList.remove(0);
            } else {
                break;
            }
        }
        
        bytes = new byte[byteList.size()];
        for (int j = 0; j < byteList.size(); j++) {
            bytes[j] = byteList.get(j);
        }
        
        return bytes;
    }
}
