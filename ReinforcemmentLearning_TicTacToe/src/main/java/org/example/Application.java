package org.example;


import org.apache.log4j.BasicConfigurator;

import javax.swing.*;
import java.awt.*;

/**
 * @author furkangunes
 */
public class Application extends JFrame {
    /**
     * Tasarlanmis UI ekrana yansitilmasi
     */
    public Application() {

        initUI();
    }

    /**
     * Oyunu yaratir.Bot egitim butonlarini ve yeni oyun baslat butonunu yerlestirir.
     */
    private void initUI() {

        Game game = new Game(new Board());
        add(game, BorderLayout.CENTER);

        JPanel commands = new JPanel(new BorderLayout());
        add(commands, BorderLayout.SOUTH);

        JButton btnTrainQ = new JButton("Q-Train");
        btnTrainQ.addActionListener(e -> {
            game.trainQ(learner -> {
                JOptionPane.showMessageDialog(Application.this,
                        "Training completed");
            });
        });
        commands.add(btnTrainQ, BorderLayout.WEST);

        JButton btnTrainR = new JButton("R-Train");
        btnTrainR.addActionListener(e -> {
            game.trainR(learner -> {
                JOptionPane.showMessageDialog(Application.this,
                        "Training completed");
            });
        });
        commands.add(btnTrainR, BorderLayout.CENTER);

        JButton btnTrainSarsa = new JButton("Sarsa-Train");
        btnTrainSarsa.addActionListener(e -> {
            game.trainSarsa(learner -> {
                JOptionPane.showMessageDialog(Application.this,
                        "Training completed");
            });
        });
        commands.add(btnTrainSarsa, BorderLayout.EAST);

        JButton btnNewGame = new JButton("New Game");
        btnNewGame.addActionListener(e -> game.newGame());
        commands.add(btnNewGame, BorderLayout.SOUTH);

        setTitle("Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setSize(new Dimension(Game.SCREEN_WIDTH + 20, Game.SCREEN_HEIGHT + 80));


    }

    /**
     * @param args
     * @throws Exception Java application baslaticidir.
     */
    public static void main(String[] args) throws Exception {

        BasicConfigurator.configure();
        EventQueue.invokeLater(() -> {
            Application ex = new Application();
            ex.setVisible(true);
        });
    }
}

