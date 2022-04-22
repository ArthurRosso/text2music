/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.ufrgs.inf.aapp.text2music.ui;

import br.ufrgs.inf.aapp.text2music.Decoder;
import br.ufrgs.inf.aapp.text2music.MusicInstruction;
import br.ufrgs.inf.aapp.text2music.MusicInstructionList;
import br.ufrgs.inf.aapp.text2music.Player;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

/**
 *
 * @author pietro
 */
public class Window extends JFrame {
    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;
    private static final int BT_HEIGHT = 50;
    
    private JTextArea textArea = new JTextArea();
    private JButton playButton = new JButton("Play");
    private JFileChooser fileChooser = new JFileChooser();
    private JButton loadButton = new JButton("Load");
    private JButton saveButton = new JButton("Save");
    
    private Decoder decoder = new Decoder();
    private Player player = new Player();
    
    private boolean isPlaying = false;
    
    public Window() {
        this.setSize(WIDTH, HEIGHT);
        this.setLayout(null);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });
        
        playButton.setBounds(0, HEIGHT - BT_HEIGHT, WIDTH, BT_HEIGHT);
        loadButton.setBounds(0, 0, WIDTH/2, BT_HEIGHT);
        saveButton.setBounds(0, BT_HEIGHT, WIDTH/2, BT_HEIGHT);
        
        textArea.setBounds(WIDTH/2, 0, WIDTH/2, HEIGHT - BT_HEIGHT);
        textArea.setLineWrap(true);
        
        playButton.addActionListener((ActionEvent ae) -> {
            new Thread(() -> {
                if (this.isPlaying) {
                    this.isPlaying = false;
                } else {
                    playButton.setText("Stop");
                    this.isPlaying = true;
                    MusicInstructionList l = decoder.decode(textArea.getText());

                    player.reset();
                    MusicInstruction i = l.getNextInstruction();
                    while (i != null && isPlaying) {
                        i.doInstruction(player);
                        i = l.getNextInstruction();
                    }
                    playButton.setText("Play");
                    this.isPlaying = false;
                }
            }).start();
            
        });
        
        loadButton.addActionListener((ActionEvent ae) -> {
            try {
                if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                    String text = decoder.OpenFile(fileChooser.getSelectedFile().getAbsolutePath());
                    textArea.setText(text);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "error: " + e.getMessage());
            }
        });
        
        this.add(playButton);
        this.add(saveButton);
        this.add(loadButton);
        this.add(textArea);
    }
    
    public void display() {
        this.setVisible(true);
    }
}
