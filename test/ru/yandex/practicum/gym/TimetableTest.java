package ru.yandex.practicum.gym;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TimetableTest {

    private List<TrainingSession> getAllTrainings(Map<TimeOfDay, List<TrainingSession>> trainings) {
        return trainings.values().stream().flatMap(List::stream).collect(Collectors.toList());
    }

    private boolean containsCounterFor(Coach coach, int expectedValue, Collection<CounterOfTrainings> counters) {
        for (var counter : counters) {
            if (counter.getCoach().equals(coach)) {
                return counter.getTrainingCount() == expectedValue;
            } else {
                return false;
            }
        }
        return false;
    }

    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);
        Assertions.assertEquals(
                List.of(singleTrainingSession),
                getAllTrainings(timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY))
        );

        Assertions.assertTrue(timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY).isEmpty());
    }

    @Test
    void testGetTrainingSessionsForDayMultipleSessions() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));

        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);

        Assertions.assertEquals(
                List.of(mondayChildTrainingSession),
                getAllTrainings(timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY))
        );
        Assertions.assertEquals(
                List.of(thursdayChildTrainingSession, thursdayAdultTrainingSession),
                getAllTrainings(timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY))
        );


        Assertions.assertTrue(timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY).isEmpty());
    }

    @Test
    void testGetTrainingSessionsForDayAndTime() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        Assertions.assertEquals(
                List.of(singleTrainingSession),
                timetable.getTrainingSessionsForDayAndTime(
                        DayOfWeek.MONDAY,
                        new TimeOfDay(13, 0)
                )
        );

        Assertions.assertEquals(
                List.of(),
                timetable.getTrainingSessionsForDayAndTime(
                        DayOfWeek.MONDAY,
                        new TimeOfDay(14, 0)
                )
        );
    }

    @Test
    void testCoachCollisionResolving() {
        Timetable timetable = new Timetable();

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession childTraining = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        timetable.addNewTrainingSession(childTraining);

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession adultTraining = new TrainingSession(groupAdult, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        timetable.addNewTrainingSession(adultTraining);
        var trainings = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        Assertions.assertEquals(List.of(childTraining), trainings);
    }

    @Test
    void testEmptyTrainingCountTable() {
        Timetable timetable = new Timetable();
        Assertions.assertTrue(timetable.getCountByCoaches().isEmpty());
    }

    @Test
    void testOneCoachCountTable() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        timetable.addNewTrainingSession(new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0)));

        Assertions.assertTrue(containsCounterFor(coach, 1, timetable.getCountByCoaches()));
        timetable.addNewTrainingSession(new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(14, 0)));

        Assertions.assertTrue(containsCounterFor(coach, 2, timetable.getCountByCoaches()));
    }


    @Test
    void testSortedCountTable() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach first = new Coach("Васильев", "Николай", "Сергеевич");
        Coach second = new Coach("Алексеев", "Николай", "Сергеевич");
        Coach third = new Coach("Иванов", "Николай", "Сергеевич");
        timetable.addNewTrainingSession(new TrainingSession(group, first, DayOfWeek.MONDAY, new TimeOfDay(13, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, first, DayOfWeek.TUESDAY, new TimeOfDay(13, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, first, DayOfWeek.WEDNESDAY, new TimeOfDay(13, 0)));

        timetable.addNewTrainingSession(new TrainingSession(group, second, DayOfWeek.THURSDAY, new TimeOfDay(13, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, second, DayOfWeek.FRIDAY, new TimeOfDay(13, 0)));

        timetable.addNewTrainingSession(new TrainingSession(group, third, DayOfWeek.SATURDAY, new TimeOfDay(13, 0)));

        Assertions.assertEquals(List.of(first, second, third), timetable.getCountByCoaches().stream().map(CounterOfTrainings::getCoach).toList());
    }

}
