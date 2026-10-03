package ru.yandex.practicum.gym;

/**
 * @param title    название группы
 * @param age      тип (взрослая или детская)
 * @param duration длительность (в минутах)
 */
public record Group(String title, Age age, int duration) {
}
