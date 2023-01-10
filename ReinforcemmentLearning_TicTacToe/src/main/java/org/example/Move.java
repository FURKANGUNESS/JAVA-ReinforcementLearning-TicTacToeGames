package org.example;


import java.util.HashSet;
import java.util.Set;

/**
 * @author furkangunes
 */
public class Move {
    public int oldState;
    public int newState;
    public int action;
    public double reward;
    public Set<Integer> possibleActions = new HashSet<>();

    /**
     * @param oldState
     * @param action
     * @param newState
     * @param reward   Yukardidaki parametlere gore hareket yaratir.
     */
    public Move(int oldState, int action, int newState, double reward) {
        this.oldState = oldState;
        this.action = action;
        this.newState = newState;
        this.reward = reward;
    }

    /**
     * @param oldState
     * @param action
     * @param newState
     * @param reward
     * @param possibleActions Yukaridaki parametrelere gore hareket yaratir.
     */
    public Move(int oldState, int action, int newState, double reward, Set<Integer> possibleActions) {
        this.oldState = oldState;
        this.action = action;
        this.newState = newState;
        this.reward = reward;
        this.possibleActions = possibleActions;

    }
}
