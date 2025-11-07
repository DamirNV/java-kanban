package entities;

import java.util.Objects;

public class TrainingSession {

    private Group group;
    private Coach coach;
    private DayOfWeek dayOfWeek;
    private TimeOfDay timeOfDay;

    public TrainingSession(Group group, Coach coach, DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        this.group = group;
        this.coach = coach;
        this.dayOfWeek = dayOfWeek;
        this.timeOfDay = timeOfDay;
    }

    public Group getGroup() {
        return group;
    }

    public Coach getCoach() {
        return coach;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public TimeOfDay getTimeOfDay() {
        return timeOfDay;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrainingSession that = (TrainingSession) o;
        return Objects.equals(group, that.group) &&
                Objects.equals(coach, that.coach) &&
                dayOfWeek == that.dayOfWeek &&
                Objects.equals(timeOfDay, that.timeOfDay);
    }

    @Override
    public int hashCode() {
        return Objects.hash(group, coach, dayOfWeek, timeOfDay);
    }

    @Override
    public String toString() {
        return "entities.TrainingSession{" +
                "group=" + group.getTitle() +
                ", coach=" + coach +
                ", dayOfWeek=" + dayOfWeek +
                ", timeOfDay=" + timeOfDay +
                '}';
    }

}
