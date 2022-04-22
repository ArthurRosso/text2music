package br.ufrgs.inf.aapp.text2music;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class Decoder {
    private static final int VERSION = 1;
    
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
                res.add(new SilentNoteInstruction());
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
                    res.add(new SilentNoteInstruction());
                }
                str.skip();

            } else {
                // Pular caractere inválido
                str.skip();
            }
        }

        return res;
    }

    public String OpenFile(String path) throws InvalidFileException, IOException {
        String text = new String(Files.readAllBytes(Paths.get(path)));
        
        Pattern pattern = Pattern.compile("t2m(\\d+) ");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            String match = matcher.group(0);
            int version = Integer.parseInt(matcher.group(1));
            
            if (version <= VERSION) {
                return matcher.replaceFirst("");
            }
        }
        
        throw new InvalidFileException();
    }
}
