package ru.yandex.practicum.gym;

/**
 * @param group     группа
 * @param coach     тренер
 * @param dayOfWeek день недели
 * @param timeOfDay время начала занятия
 */
public record TrainingSession(Group group, Coach coach, DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
}

