package br.ufrgs.inf.aapp.text2music;

public class InvalidFileException extends Exception {
    public InvalidFileException() {
        super("File is not recognized by the decoder");
    }
}
