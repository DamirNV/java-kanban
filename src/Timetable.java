import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.TreeMap;

public class Timetable {

    private Map<DayOfWeek, Map<TimeOfDay, List<TrainingSession>>> timetable = new HashMap<>();

    public void addNewTrainingSession(TrainingSession trainingSession) {
        DayOfWeek day = trainingSession.getDayOfWeek();
        TimeOfDay time = trainingSession.getTimeOfDay();

        Map<TimeOfDay, List<TrainingSession>> daySchedule = timetable.get(day);
        if (daySchedule == null) {
            daySchedule = new TreeMap<>();
            timetable.put(day, daySchedule);
        }

        List<TrainingSession> sessionsAtTime = daySchedule.get(time);
        if (sessionsAtTime == null) {
            sessionsAtTime = new ArrayList<>();
            daySchedule.put(time, sessionsAtTime);
        }

        sessionsAtTime.add(trainingSession);
    }

    public Map<TimeOfDay, List<TrainingSession>> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        return timetable.get(dayOfWeek);
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        return getTrainingSessionsForDay(dayOfWeek).get(timeOfDay);
    }

    public HashMap<Coach, Integer> getCountByCoaches() {
        HashMap<Coach, Integer> counterOfTrainings = new HashMap<>();

        for (DayOfWeek day : timetable.keySet()) {
            Map<TimeOfDay, List<TrainingSession>> daySchedule = getTrainingSessionsForDay(day);
            if (daySchedule != null) {
                for (TimeOfDay time : daySchedule.keySet()) {
                    List<TrainingSession> sessions = getTrainingSessionsForDayAndTime(day, time);
                    if (sessions != null) {
                        for (TrainingSession session : sessions) {
                            Coach coach = session.getCoach();
                            if (counterOfTrainings.containsKey(coach)) {
                                int currentCount = counterOfTrainings.get(coach);
                                counterOfTrainings.put(coach, currentCount + 1);
                            } else {
                                counterOfTrainings.put(coach, 1);
                            }
                        }
                    }
                }
            }
        }
        return counterOfTrainings;
    }

}

