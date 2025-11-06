import java.util.*;

public class Timetable {

    private Map<DayOfWeek, Map<TimeOfDay, List<TrainingSession>>> timetable = new HashMap<>();

    public void addNewTrainingSession(TrainingSession trainingSession) {
        if (!isValidTrainingSession(trainingSession)) {
            return;
        }

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

    private boolean isValidTrainingSession(TrainingSession newSession) {
        DayOfWeek day = newSession.getDayOfWeek();
        TimeOfDay time = newSession.getTimeOfDay();
        Coach coach = newSession.getCoach();
        Group group = newSession.getGroup();
        if (isDuplicateSession(newSession)) {
            return false;
        }
        if (isCoachBusyAtTime(day, time, coach)) {
            return false;
        }
        if (hasTimeOverlap(day, time, coach, group.getDuration())) {
            return false;
        }
        return true;
    }

    private boolean isDuplicateSession(TrainingSession newSession) {
        Map<TimeOfDay, List<TrainingSession>> daySchedule = timetable.get(newSession.getDayOfWeek());
        if (daySchedule != null) {
            for (List<TrainingSession> sessions : daySchedule.values()) {
                for (TrainingSession existingSession : sessions) {
                    if (existingSession.equals(newSession)) {
                        System.out.println("Такое занятие уже существует в расписании");
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isCoachBusyAtTime(DayOfWeek day, TimeOfDay time, Coach coach) {
        Map<TimeOfDay, List<TrainingSession>> daySchedule = timetable.get(day);
        if (daySchedule != null) {
            List<TrainingSession> sessionsAtTime = daySchedule.get(time);
            if (sessionsAtTime != null) {
                for (TrainingSession session : sessionsAtTime) {
                    if (session.getCoach().equals(coach)) {
                        System.out.println("Тренер " + session.getCoach() + " уже ведет занятие в это время");
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean hasTimeOverlap(DayOfWeek day, TimeOfDay newTime, Coach coach, int duration) {
        Map<TimeOfDay, List<TrainingSession>> daySchedule = timetable.get(day);
        if (daySchedule != null) {
            for (Map.Entry<TimeOfDay, List<TrainingSession>> entry : daySchedule.entrySet()) {
                TimeOfDay existingTime = entry.getKey();
                for (TrainingSession session : entry.getValue()) {
                    if (session.getCoach().equals(coach) &&
                            isTimeOverlap(newTime, duration, existingTime, session.getGroup().getDuration())) {
                        System.out.println("У тренера " + session.getCoach() + " пересекается время занятий");
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isTimeOverlap(TimeOfDay time1, int duration1, TimeOfDay time2, int duration2) {
        int start1 = time1.getHours() * 60 + time1.getMinutes();
        int end1 = start1 + duration1;

        int start2 = time2.getHours() * 60 + time2.getMinutes();
        int end2 = start2 + duration2;

        return start1 < end2 && start2 < end1;
    }

    public Map<TimeOfDay, List<TrainingSession>> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        return timetable.get(dayOfWeek);
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        Map<TimeOfDay, List<TrainingSession>> daySchedule = getTrainingSessionsForDay(dayOfWeek);
        return daySchedule != null ? daySchedule.get(timeOfDay) : null;
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

