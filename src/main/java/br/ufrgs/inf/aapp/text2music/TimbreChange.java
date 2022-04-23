package br.ufrgs.inf.aapp.text2music;

import javax.sound.midi.Track;

public class TimbreChange implements MusicInstruction {
    public void doInstruction(Player p) {
        // TODO: Mudar timbre de uma maneira aleatória
    }

    @Override
    public long record(Player p, long tick, Track t) {
        // TODO: Emitir mensagem meta para mudar instrumento?
        return tick;
    }
}
