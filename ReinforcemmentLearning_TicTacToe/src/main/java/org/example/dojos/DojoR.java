package org.example.dojos;

import org.example.Board;
import org.example.bots.NaiveBot;
import org.example.bots.RBot;
import org.example.learning.rlearn.RLearner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author furkangunes
 * R ogrenme modelinin iterasyonlarinin hesaplanip degerlendirildigi kisimdir
 * 1,2,0 durumlarina gore board uzerindeki iterasyonlari hesaplanir.
 */
public class DojoR {

    private static final Logger logger = LoggerFactory.getLogger(DojoR.class);

    public static double test(Board board, RLearner model, int episodes) {

        RBot bot1 = new RBot(1, board, model);
        NaiveBot bot2 = new NaiveBot(2, board);

        int wins = 0;
        int loses = 0;
        for (int i = 0; i < episodes; ++i) {
            bot1.clearHistory();
            bot2.clearHistory();
            /**
             * iterasyon degerini gosterir
             */
            logger.info("Iteration: {} / {}", (i + 1), episodes);
            board.reset();
            while (board.canBePlayed()) {
                bot1.act();
                bot2.act();
            }
            /**
             * Kazanan oyuncunun ongorulen rakamini gosterir.
             */
            int winner = board.getWinner();
            logger.info("Winner: {}", winner);
            wins += winner == 1 ? 1 : 0;
            loses += winner == 2 ? 1 : 0;
        }

        return wins * 1.0 / episodes;

    }

    public static RLearner train(Board board, int episodes) {


        int stateCount = (int) Math.pow(3, board.size() * board.size());
        int actionCount = board.size() * board.size();

        RLearner learner = new RLearner(stateCount, actionCount);
        //learner.setActionSelection(SoftMaxActionSelectionStrategy.class.getCanonicalName());

        RBot bot1 = new RBot(1, board, learner);
        RBot bot2 = new RBot(2, board, learner);

        for (int i = 0; i < episodes; ++i) {
            bot1.clearHistory();
            bot2.clearHistory();

            logger.info("Iteration: {} / {}", (i + 1), episodes);
            board.reset();
            while (board.canBePlayed()) {
                bot1.act();
                bot2.act();
            }
            logger.info("winner: {}", board.getWinner());
            bot1.updateStrategy();
            bot2.updateStrategy();
            logger.info("board: \n{}", board);
        }

        return learner;
    }

    public static RLearner train_2(Board board, int episodes) {


        int stateCount = (int) Math.pow(3, board.size() * board.size());
        int actionCount = board.size() * board.size();

        RLearner learner = new RLearner(stateCount, actionCount);
        //learner.setActionSelection(SoftMaxActionSelectionStrategy.class.getCanonicalName());

        RBot bot1 = new RBot(1, board, learner);
        NaiveBot bot2 = new NaiveBot(2, board);

        int wins = 0;

        for (int i = 0; i < episodes; ++i) {
            bot1.clearHistory();
            bot2.clearHistory();

            logger.info("Iteration: {} / {}", (i + 1), episodes);
            board.reset();
            while (board.canBePlayed()) {
                bot1.act();
                bot2.act();
            }
            logger.info("winner: {}", board.getWinner());
            bot1.updateStrategy();
            logger.info("board: \n{}", board);

            /**
             * Botun basari oranini gosterir.
             */
            wins += board.getWinner() == 1 ? 1 : 0;
            logger.info("success rate: {} %", (wins * 100) / (i + 1));
        }

        return learner;
    }

    public static void main(String[] args) {

        Board board = new Board();
        RLearner model = train_2(board, 30000);

        /**
         * R botun random bota karsilik olarak ne kadar basarili oldugunu gosterir.
         */
        double cap = test(board, model, 1000);
        logger.info("R-Learn Bot beats Random Bot in {} % of the games", cap * 100);
    }

}
