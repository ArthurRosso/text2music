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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
    private JButton expoButton = new JButton("Export");
    private JButton helpButton = new JButton("Help");
    private JDialog helpDialog = new JDialog(this);
    
    private Decoder decoder = new Decoder();
    private Player player = new Player();
    
    private boolean isPlaying = false;
    
    public Window() {
        super("text2music");
        this.setLayout(null);
        this.setSize(WIDTH, HEIGHT + 30);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });
        
        playButton.setBounds(0, HEIGHT - BT_HEIGHT, WIDTH, BT_HEIGHT);
        loadButton.setBounds(0, 0, WIDTH/2, BT_HEIGHT);
        saveButton.setBounds(0, BT_HEIGHT, WIDTH/2, BT_HEIGHT);
        expoButton.setBounds(0, BT_HEIGHT*2, WIDTH/2, BT_HEIGHT);
        helpButton.setBounds(0, BT_HEIGHT*3, WIDTH/2, BT_HEIGHT);
        
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
        
        saveButton.addActionListener((ActionEvent ae) -> {
            try {
                if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                    decoder.SaveFile(
                            fileChooser.getSelectedFile().getAbsolutePath(),
                            textArea.getText()
                    );
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "error: " + e.getMessage());
            }
        });
        
        expoButton.addActionListener((ActionEvent ae) -> {
            try {
                if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                    MusicInstructionList l = decoder.decode(textArea.getText());
                    player.export(fileChooser.getSelectedFile().getAbsolutePath(), l);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        
        helpButton.addActionListener((ActionEvent ae) -> {
            helpDialog.setVisible(true);
        });
        
        
        JLabel helpText = new JLabel();
        helpText.setText("<html><body>"
            + "Functionamento do programa:<br>"
            + "1. Carregue uma partitura com o botão Load ou componha uma música com a caixa de texto.<br>"
            + "2. Clique em play para ouvir sua composição.<br>"
            + "3. Caso queira salvar sua partitura, clique em Save. Para salvar o som, clique em Export.<br><br>"
            + "Notas:<br>"
            + "A: Lá | B: Si | C: Dó | D: Ré | E: Mi | F: Fá | G: Sol<br><br>"
            + "Comandos:<br>"
            + "Letras H a M: Nota vira bemol | Letras N a S: Nota não é alterada | Letras T a Z: Nota vira sustenida | "
            + "Letras abcdef: Repete nota anterior | Dígito 1 a 9: Troca instrumento | Dígito 0: Volta para instrumento padrão | "
            + "+: Dobra volume | -: Volume volta a ser padrão | !: Sobe oitava | ?: Desce oitava | .: Reseta oitava | "
            + "Letra não precedida de nota: Pausa | ;: Flauta | ,: Órgão | Espaço: Piano | Nova Linha: Bells | BPM+: BPM+50 | "
            + "BPM-: BPM-50 | else: Repete nota anterior ou pausa"
        + "</body></html>");
        helpDialog.add(helpText);
        helpDialog.setSize(WIDTH, HEIGHT);
        
        this.add(playButton);
        this.add(saveButton);
        this.add(loadButton);
        this.add(expoButton);
        this.add(helpButton);
        this.add(textArea);
    }
    
    public void display() {
        this.setVisible(true);
    }
}
