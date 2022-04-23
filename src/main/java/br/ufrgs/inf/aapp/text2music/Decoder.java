package br.ufrgs.inf.aapp.text2music;

import java.io.FileWriter;
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
            if (this.index < str.length()) {
                return str.charAt(this.index) + "";
            }
            return "\0";
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
        final String notes = "ABCDEFG";
        final String flat = "HIJKLM";
        final String normal = "NOPQRS";
        final String sharp = "TUVWXYZ";
        final String digits = "0123456789";
        
        Random random = new Random();
        MusicInstructionList res = new MusicInstructionList();
        SpecialString str = new SpecialString(text);

        while (str.length() > 0) {
            
            if (str.compareAdvance("BPM+")){
                res.add(new BPMChange(50));
                
            } else if (str.compareAdvance("BPM-")) {
                res.add(new BPMChange(-50));
                
            } else if (str.compareAdvance("!")) {
                res.add(new OctaveChange(1));
                
            } else if (str.compareAdvance("?")) {
                res.add(new OctaveChange(-1));
            } else if (str.compareAdvance(".")) {
                res.add(new OctaveChange(0));
            } else if (str.compareAdvance("+")) {
                res.add(new VolumeChange(true));
            } else if (str.compareAdvance("-")) {
                res.add(new VolumeChange(false));
                
            } else if (str.compareAdvance("?") || str.compareAdvance(".")) {
                int randomIndex = random.nextInt(notes.length());
                char note = notes.toLowerCase().charAt(randomIndex);
                res.add(new Note(note, NoteStyle.Normal));
            } else if (str.compareAdvance(";")) {
                res.add(new TimbreChange(76, true));
            } else if (str.compareAdvance(",")) {
                res.add(new TimbreChange(20, true));
            } else if (str.compareAdvance(" ")) {
                res.add(new TimbreChange(1, true));
            } else if (str.compareAdvance("\n")) {
                res.add(new TimbreChange(15, true));
            } else if (notes.contains(str.getCurr())) {
                char note = str.getCurr().toLowerCase().charAt(0);
                NoteStyle style = NoteStyle.Normal;
                str.skip();
                
                String next = str.getCurr().toUpperCase();
                if (flat.contains(next)) {
                    style = NoteStyle.Flat;
                    str.skip();
                } else if (normal.contains(next)) {
                    style = NoteStyle.Normal;
                    str.skip();
                } else if (sharp.contains(next)) {
                    style = NoteStyle.Sharp;
                    str.skip();
                }

                res.add(new Note(note, style));
            } else if (digits.contains(str.getCurr())) {
                int digit = digits.indexOf(str.getCurr());
                res.add(new TimbreChange(digit, digit == 0));
                str.skip();
            } else {
                if (notes.contains(str.getPrev())) {
                    res.add(new Note(str.getPrev().toLowerCase().charAt(0), NoteStyle.Normal));
                } else {
                    res.add(new SilentNoteInstruction());
                }
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
            int version = Integer.parseInt(matcher.group(1));
            
            if (version <= VERSION) {
                return matcher.replaceFirst("");
            }
        }
        
        throw new InvalidFileException();
    }
    
    public void SaveFile(String path, String music) throws IOException {
        FileWriter f = new FileWriter(path);
        f.write("t2m" + VERSION + " " + music);
        f.close();
    }
}