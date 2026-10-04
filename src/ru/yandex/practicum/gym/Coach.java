package ru.yandex.practicum.gym;

/**
 * @param surname    фамилия
 * @param name       имя
 * @param middleName отчество
 */
public record Coach(String surname, String name, String middleName) implements Comparable<Coach> {
    @Override
    public int compareTo(Coach o) {
        int surnameDifference = String.CASE_INSENSITIVE_ORDER.compare(this.surname, o.surname);
        if (surnameDifference != 0) {
            return surnameDifference;
        }

        int nameDifference = String.CASE_INSENSITIVE_ORDER.compare(this.name, o.name);
        return nameDifference != 0 ? nameDifference : String.CASE_INSENSITIVE_ORDER.compare(this.middleName, o.middleName);
    }
}
