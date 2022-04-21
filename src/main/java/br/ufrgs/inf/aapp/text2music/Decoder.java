package br.ufrgs.inf.aapp.text2music;

import java.util.Random;

/**
 *
 */
public class Decoder {
    /**
     * Representa uma string que pode ser "percorrida"
     */
    private class SpecialString {
        private final String str;
        private int index = 0;
        
        public SpecialString(String str) {
            this.str = str;
        }

        /**
         * Retorna o tamanho restante da string
         */
        public int length() {
            return this.str.substring(this.index).length();
        }

        /**
         * Testa se a string começa com um dado prefixo, e caso comece, avança a leitura
         */
        public boolean compareAdvance(String s) {
            if (this.str.substring(this.index).startsWith(s)) {
                this.index += s.length();
                return true;
            }

            return false;
        }

        /**
         * Retorna o caractere atual
         */
        public String getCurr() {
            return str.charAt(this.index) + "";
        }

        /**
         * Retorna o caractere anterior
         */
        public String getPrev() {
            if (this.index > 0 && this.str.length() > 0) {
                return str.charAt(this.index - 1) + "";
            }
            return "\0";
        }

        /**
         * Avança um caractere
         */
        public void skip() {
            this.index++;
        }
    }

    public MusicInstructionList decode(String text) {
        final String notes = "abcdefg";
        Random random = new Random();
        MusicInstructionList res = new MusicInstructionList();
        SpecialString str = new SpecialString(text);

        while (str.length() > 0) {
            
            if (str.compareAdvance("BPM+")){
                res.add(new BPMChange(true));
                
            } else if (str.compareAdvance("BPM-")) {
                res.add(new BPMChange(false));
                
            } else if (str.compareAdvance("T+")) {
                res.add(new OctaveChange(true));
                
            } else if (str.compareAdvance("T-")) {
                res.add(new OctaveChange(false));
                
            } else if (str.compareAdvance(" ")) {
                // TODO: Adicionar instrução de silêncio
            } else if (str.compareAdvance("\n")) {
                res.add(new TimbreChange());
                
            } else if (str.compareAdvance("+")) {
                res.add(new VolumeChange(true));
                
            } else if (str.compareAdvance("-")) {
                res.add(new VolumeChange(false));
                
            } else if (str.compareAdvance("?") || str.compareAdvance(".")) {
                int randomIndex = random.nextInt(notes.length());
                char note = notes.charAt(randomIndex);
                res.add(new Note(note));
                
            } else if (notes.contains(str.getCurr().toLowerCase())) {
                res.add(new Note(str.getCurr().toLowerCase().charAt(0)));
                str.skip();

            } else if("iou".contains(str.getCurr().toLowerCase())) {
                String nota = str.getPrev().toLowerCase();
                          
                if (notes.contains(nota)) {
                    res.add(new Note(nota.charAt(0)));
                } else {
                    // TODO: Adicionar instrução de silêncio
                }
                str.skip();

            } else {
                // Pular caractere inválido
                str.skip();
            }
        }

        return res;
    }

}
