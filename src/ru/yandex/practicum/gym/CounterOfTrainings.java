package ru.yandex.practicum.gym;

public class CounterOfTrainings {
    private final Coach coach;
    private int trainingCount;


    public CounterOfTrainings(Coach coach) {
        this.coach = coach;
        this.trainingCount = 0;
    }

    public void incrementCount() {
        trainingCount++;
    }

    public Coach getCoach() {
        return coach;
    }

    public int getTrainingCount() {
        return trainingCount;
    }
}
