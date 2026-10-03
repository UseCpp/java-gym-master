package ru.yandex.practicum.gym;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Timetable {

    private static final TreeMap<TimeOfDay, List<TrainingSession>> EMPTY_TREE = new TreeMap<>();

    private final Map<DayOfWeek, TreeMap<TimeOfDay, List<TrainingSession>>> timetable = new HashMap<>();

    private final Map<Coach, CounterOfTrainings> trainingCountPerCoach = new HashMap<>();

    public void addNewTrainingSession(TrainingSession trainingSession) {
        var trainingSessions = timetable.computeIfAbsent(trainingSession.dayOfWeek(), k -> new TreeMap<>())
                .computeIfAbsent(trainingSession.timeOfDay(), k -> new ArrayList<>());

        if (!containsCoachCollision(trainingSessions, trainingSession)) {
            trainingSessions.add(trainingSession);
            trainingCountPerCoach.computeIfAbsent(trainingSession.coach(), CounterOfTrainings::new).incrementCount();
        }
    }

    /// Проверяет, что нет коллизий в расписании по группам у тренеров.
    private boolean containsCoachCollision(Collection<TrainingSession> trainingSessionsPerDayAndTime, TrainingSession newSession) {
        return trainingSessionsPerDayAndTime.stream().anyMatch(x -> x.coach().equals(newSession.coach()));
    }

    public TreeMap<TimeOfDay, List<TrainingSession>> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        return timetable.getOrDefault(dayOfWeek, EMPTY_TREE);
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        return timetable.getOrDefault(dayOfWeek, EMPTY_TREE).getOrDefault(timeOfDay, List.of());
    }

    public Collection<CounterOfTrainings> getCountByCoaches() {
        return trainingCountPerCoach.values()
                .stream()
                .sorted(Comparator.comparing(CounterOfTrainings::getTrainingCount).reversed())
                .toList();
    }
}
