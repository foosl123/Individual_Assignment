package com.example.individualassignment;
public class Score implements Comparable<Score> {
    private String name;
    private int score;

    public Score(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    @Override
    public int compareTo(Score otherScore) {
        // Compare scores in descending order
        return Integer.compare(otherScore.score, this.score);
    }
}