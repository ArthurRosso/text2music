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

import javax.swing.JButton;
import javax.swing.JFrame;
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
    private JButton loadButton = new JButton("Load");
    private JButton saveButton = new JButton("Save");
    
    private Player player = new Player();
    
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
        
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                Decoder d = new Decoder();
                MusicInstructionList l = d.decode(textArea.getText());

                player.reset();
                MusicInstruction i = l.getNextInstruction();
                while (i != null){
                    i.doInstruction(player);
                    i = l.getNextInstruction();
                }
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
