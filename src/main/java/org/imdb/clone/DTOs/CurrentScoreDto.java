package org.imdb.clone.DTOs;

public class CurrentScoreDto {
    private int currentScore;

    public CurrentScoreDto(){}
    public CurrentScoreDto(int currentScore) {
        this.currentScore = currentScore;
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public void setCurrentScore(int currentScore) {
        this.currentScore = currentScore;
    }
}
