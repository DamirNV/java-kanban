import java.util.*;

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
        HashMap<Coach, Integer> sortedCounterOfTrainings = new LinkedHashMap<>();
        HashMap<Coach, Integer> copyCounterOfTrainings = new HashMap<>(counterOfTrainings);
        for (int i = 0; i < counterOfTrainings.size(); i++) {
            int maxCount = 0;
            Coach maxValueCoach = null;
            for (Coach coach : copyCounterOfTrainings.keySet()) {
                if (copyCounterOfTrainings.get(coach) > maxCount) {
                    maxCount = copyCounterOfTrainings.get(coach);
                    maxValueCoach = coach;
                }
            }
            sortedCounterOfTrainings.put(maxValueCoach, maxCount);
            copyCounterOfTrainings.remove(maxValueCoach);
        }
        return sortedCounterOfTrainings;
    }

}

