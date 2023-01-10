package org.example;

import org.example.bots.QBot;
import org.example.bots.RBot;
import org.example.bots.SarsaBot;
import org.example.dojos.DojoQ;
import org.example.dojos.DojoR;
import org.example.dojos.DojoSarsa;
import org.example.learning.qlearn.QLearner;
import org.example.learning.rlearn.RLearner;
import org.example.learning.sarsa.SarsaLearner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;
import java.util.function.Consumer;

/**
 * @author furkangunes
 */
public class Game extends JPanel {

    public static final int SCREEN_WIDTH = 700;
    public static final int SCREEN_HEIGHT = 700;

    private final Board board;

    private QBot bot_q;
    private RBot bot_r;
    private SarsaBot bot_sarsa;
    private QLearner model_q;
    private RLearner model_r;
    private SarsaLearner model_sarsa;
    private Boolean ıs_bot_thınkıng = false;
    private Font font = new Font("Arrial", Font.BOLD, 96);

    private static final Random random = new Random();
    private String message;

    /**
     * @param board Board tanimlanir.Programin acilir pencere olculeri belirlenir.
     *              Mouseeventler yakalanir.
     */
    public Game(Board board) {
        this.board = board;
        setSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                playerAct(e);
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

    /**
     * @param callback Q botu egitilir.
     */
    public void trainQ(Consumer<QLearner> callback) {
        new Thread(() -> {
            message = "Q Learn Started!";
            repaint();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.model_q = DojoQ.trainAgainstNaiveBot(board, 30000);
            this.bot_q = new QBot(1, board, model_q);
            message = null;
            board.reset();
            repaint();
            callback.accept(this.model_q);
        }).start();
    }

    /**
     * @param callback R bot egitilir.
     */
    public void trainR(Consumer<RLearner> callback) {
        new Thread(() -> {
            message = "R Learn Started!";
            repaint();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            this.model_r = DojoR.train_2(board, 30000);
            this.bot_r = new RBot(1, board, model_r);
            message = null;
            board.reset();
            repaint();
            callback.accept(this.model_r);
        }).start();
    }

    /**
     * @param callback Sarsa bot egitilir.
     */
    public void trainSarsa(Consumer<SarsaLearner> callback) {
        new Thread(() -> {
            message = "Sarsa Learn Started!";
            repaint();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            this.model_sarsa = DojoSarsa.trainAgainstNaiveBot(board, 30000);
            this.bot_sarsa = new SarsaBot(1, board, model_sarsa);
            message = null;
            board.reset();
            repaint();
            callback.accept(this.model_sarsa);
        }).start();
    }

    /**
     * Eski oyun sifirlanir.Yeni oyun yaratilir.
     * Sifirlanmis oyun tahtasinin gorsel update yapilir.
     */
    public void newGame() {
        message = null;
        board.reset();
        if (bot_q != null) {
            bot_q.clearHistory();
        } else if (bot_r != null) {
            bot_r.clearHistory();
        } else if (bot_sarsa != null) {
            bot_sarsa.clearHistory();
        }
        if (bot_q != null || bot_r != null || bot_sarsa != null) {
            if (random.nextBoolean()) {
                if (bot_q != null) {
                    bot_q.act();
                } else if (bot_r != null) {
                    bot_r.act();
                } else if (bot_sarsa != null) {
                    bot_sarsa.act();
                }

            }
        }
        repaint();
    }

    /**
     * @param e Mouseevent ile oyuncunun sectigi kutu yakalanir ve isaretlenir.
     *          2 saniye sonra botun tercihi ekrana yansitilir.
     *          Eger oynama kosullari bittiyse sonucumuz yazar.
     */
    private void playerAct(MouseEvent e) {

        if (ıs_bot_thınkıng)
            return;

        if (board.canBePlayed()) {

            int x = (int) (3.0 * e.getX() / SCREEN_WIDTH);
            int y = (int) (3.0 * e.getY() / SCREEN_HEIGHT);
            if (board.getCell(x, y) == 0) {
                board.move(new Position(x, y), 2);
            }


        }
        repaint();
        Thread botthread = new Thread(new Runnable() {
            @Override
            public void run() {

                if (board.canBePlayed()) {
                    try {
                        ıs_bot_thınkıng = true;
                        Thread.sleep(2000);
                    } catch (Exception r) {
                    }
                    if (bot_q != null) {

                        bot_q.act();
                    } else if (bot_r != null) {
                        bot_r.act();
                    } else if (bot_sarsa != null) {
                        bot_sarsa.act();
                    }

                }
                if (!board.canBePlayed()) {
                    int winner = board.getWinner();
                    if (winner == 1) {
                        message = "Bot Win!";
                    } else if (winner == 2) {
                        message = "You Win!";
                    } else {
                        message = "Game Ended!";
                    }

                    if (bot_q != null) {
                        bot_q.updateStrategy();
                        bot_q.clearHistory();
                    } else if (bot_r != null) {
                        bot_r.updateStrategy();
                        bot_r.clearHistory();
                    } else if (bot_sarsa != null) {
                        bot_sarsa.updateStrategy();
                        bot_sarsa.clearHistory();
                    }

                }
                ıs_bot_thınkıng = false;
                repaint();
            }
        });
        botthread.start();
    }

    /**
     * @param g the <code>Graphics</code> object to protect
     *          Oynama grafik yansitma bolumudur.
     */

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.black);
        g.drawLine(0, 0, 0, SCREEN_HEIGHT);
        g.drawLine(SCREEN_WIDTH / 3, 0, SCREEN_WIDTH / 3, SCREEN_HEIGHT);
        g.drawLine(SCREEN_WIDTH * 2 / 3, 0, SCREEN_WIDTH * 2 / 3, SCREEN_HEIGHT);
        g.drawLine(SCREEN_WIDTH, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

        g.drawLine(0, 0, SCREEN_WIDTH, 0);
        g.drawLine(0, SCREEN_HEIGHT / 3, SCREEN_WIDTH, SCREEN_HEIGHT / 3);
        g.drawLine(0, SCREEN_HEIGHT * 2 / 3, SCREEN_WIDTH, SCREEN_HEIGHT * 2 / 3);
        g.drawLine(0, SCREEN_HEIGHT, SCREEN_WIDTH, SCREEN_HEIGHT);


        int size = SCREEN_WIDTH / 6;

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                int cell = board.getCell(i, j);
                int x = i * SCREEN_WIDTH / 3 + size - 48;
                int y = j * SCREEN_HEIGHT / 3 + size + 48;
                if (cell == 0) {
                    continue;
                }
                String string_draw = cell == 1 ? "O" : "X";
                if (cell == 1) {
                    g.setColor(Color.red);
                } else {
                    g.setColor(Color.black);
                }
                //g.fillOval(x, y, size, size);
                g.setFont(font);
                g.drawString(string_draw, x, y);
            }
        }

        if (message != null) {
            Font small = new Font("Helvetica", Font.BOLD, 14);
            FontMetrics fm = getFontMetrics(small);

            g.setColor(Color.red);
            g.setFont(small);

            g.drawString(message, (SCREEN_WIDTH - fm.stringWidth(message)) / 2,
                    60);
        }
    }
}
