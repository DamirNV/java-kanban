import static org.junit.jupiter.api.Assertions.*;

import entities.*;
import org.junit.jupiter.api.Test;
import services.Timetable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TimetableTest {

    @Test
    public void testGetTrainingSessionsForDaySingleSession() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        timetable.addNewTrainingSession(singleTrainingSession);

        Map<TimeOfDay, List<TrainingSession>> mondaySchedule = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        assertNotNull(mondaySchedule, "Расписание на понедельник не должно быть null");
        assertEquals(1, mondaySchedule.size(), "За понедельник вернулось не 1 занятие.");

        Map<TimeOfDay, List<TrainingSession>> tuesdaySchedule  = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        int actualTuesdaySize = (tuesdaySchedule == null) ? 0 : tuesdaySchedule.size();
        assertEquals(0, actualTuesdaySize, "За вторник вернулось не 0 занятий.");
    }

    @Test
    public void testGetTrainingSessionsForDayMultipleSessions() {
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

        Map<TimeOfDay, List<TrainingSession>> mondaySchedule = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        assertNotNull(mondaySchedule, "Расписание на понедельник не должно быть null");
        assertEquals(1, mondaySchedule.size(), "За понедельник вернулось не 1 занятие.");

        Map<TimeOfDay, List<TrainingSession>> thursdaySchedule = timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);
        assertNotNull(thursdaySchedule, "Расписание на четверг не должно быть null");
        assertEquals(2, thursdaySchedule.size(), "За четверг вернулось не 2 занятия.");
        List<TimeOfDay> times = new ArrayList<>(thursdaySchedule.keySet());
        assertEquals(new TimeOfDay(13, 0), times.get(0), "Первым должно быть время 13:00");
        assertEquals(new TimeOfDay(20, 0), times.get(1), "Вторым должно быть время 20:00");

        Map<TimeOfDay, List<TrainingSession>> tuesdaySchedule  = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        assertTrue(tuesdaySchedule == null || tuesdaySchedule.isEmpty(),
                "За вторник не должно быть занятий");
    }

    @Test
    public void testGetTrainingSessionsForDayAndTime() {
        Timetable timetable = new Timetable();

        TimeOfDay timeOfDay = new TimeOfDay(13, 0);

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, timeOfDay);

        timetable.addNewTrainingSession(singleTrainingSession);

        List<TrainingSession> mondayScheduleAt1300 = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.MONDAY, timeOfDay);
        assertNotNull(mondayScheduleAt1300, "Расписание на понедельник не должно быть null");
        assertEquals(1, mondayScheduleAt1300.size(), "За понедельник вернулось " +
                "не 1 занятие.");

        List<TrainingSession> mondayScheduleAt1400 = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.MONDAY, new TimeOfDay(14, 0));
        assertTrue(mondayScheduleAt1400 == null || mondayScheduleAt1400.isEmpty(),
                "За понедельник в 14:00 не должно быть занятий");

    }

    @Test
    public void testMultipleSessionsAtSameTime() {
        Timetable timetable = new Timetable();

        TimeOfDay time = new TimeOfDay(10, 0);
        DayOfWeek day = DayOfWeek.MONDAY;

        Coach coach1 = new Coach("Иванов", "Иван", "Иванович");
        Coach coach2 = new Coach("Петрова", "Ольга", "Владимировна");

        Group group1 = new Group("Йога", Age.ADULT, 60);
        Group group2 = new Group("Пилатес", Age.ADULT, 45);

        TrainingSession session1 = new TrainingSession(group1, coach1, day, time);
        TrainingSession session2 = new TrainingSession(group2, coach2, day, time);

        timetable.addNewTrainingSession(session1);
        timetable.addNewTrainingSession(session2);

        List<TrainingSession> sessions = timetable.getTrainingSessionsForDayAndTime(day, time);

        assertNotNull(sessions);
        assertEquals(2, sessions.size(), "В одно время должно быть 2 занятия");
        assertTrue(sessions.contains(session1), "Должно содержать первое занятие");
        assertTrue(sessions.contains(session2), "Должно содержать второе занятие");
    }

    @Test
    public void testAddDuplicateSessionIsPrevented() {
        Timetable timetable = new Timetable();

        Group group = new Group("Йога", Age.ADULT, 60);
        Coach coach = new Coach("Иванов", "Иван", "Иванович");
        TrainingSession session = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(session);
        timetable.addNewTrainingSession(session);

        Map<TimeOfDay, List<TrainingSession>> schedule = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        assertEquals(1, schedule.size());
        assertEquals(1, schedule.get(new TimeOfDay(10, 0)).size());
    }

    @Test
    public void testCoachCannotHaveTwoSessionsAtSameTime() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Иванов", "Иван", "Иванович");

        Group group1 = new Group("Йога", Age.ADULT, 60);
        Group group2 = new Group("Пилатес", Age.ADULT, 45);

        TrainingSession session1 = new TrainingSession(group1, coach,
                DayOfWeek.MONDAY, new TimeOfDay(10, 0));
        TrainingSession session2 = new TrainingSession(group2, coach,
                DayOfWeek.MONDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(session1);
        timetable.addNewTrainingSession(session2);

        List<TrainingSession> sessions = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY,
                new TimeOfDay(10, 0));
        assertEquals(1, sessions.size());
        assertEquals(session1, sessions.get(0));
    }

    @Test
    public void testTimeOverlapIsPrevented() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Иванов", "Иван", "Иванович");

        Group group1 = new Group("Йога", Age.ADULT, 60);
        TrainingSession session1 = new TrainingSession(group1, coach,
                DayOfWeek.MONDAY, new TimeOfDay(10, 0));

        Group group2 = new Group("Пилатес", Age.ADULT, 45);
        TrainingSession session2 = new TrainingSession(group2, coach,
                DayOfWeek.MONDAY, new TimeOfDay(10, 30));

        timetable.addNewTrainingSession(session1);
        timetable.addNewTrainingSession(session2);

        List<TrainingSession> sessions = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY,
                new TimeOfDay(10, 0));
        assertEquals(1, sessions.size());
        assertEquals(session1, sessions.get(0));
        assertTrue(timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY,
                new TimeOfDay(10, 30)) == null);
    }

    @Test
    public void testGetCountByCoaches() {
        Timetable timetable = new Timetable();

        Coach coach1 = new Coach("Иванов", "Иван", "Иванович");
        Coach coach2 = new Coach("Петрова", "Мария", "Сергеевна");

        Group group = new Group("Фитнес", Age.ADULT, 60);

        timetable.addNewTrainingSession(new TrainingSession(group, coach1, DayOfWeek.MONDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach1, DayOfWeek.WEDNESDAY, new TimeOfDay(10, 0)));

        timetable.addNewTrainingSession(new TrainingSession(group, coach2, DayOfWeek.FRIDAY, new TimeOfDay(11, 0)));

        Map<Coach, Integer> result = timetable.getCountByCoaches();

        assertEquals(2, result.size());
        assertEquals(2, result.get(coach1));
        assertEquals(1, result.get(coach2));
    }

    @Test
    public void testGetCountByCoachesEmptyTimetable() {
        Timetable timetable = new Timetable();

        Map<Coach, Integer> result = timetable.getCountByCoaches();

        assertNotNull(result, "Результат не должен быть null");
        assertTrue(result.isEmpty(), "Для пустого расписания статистика должна быть пустой");
    }

    @Test
    public void testGetCountByCoachesSingleCoachMultipleSessions() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Иванов", "Иван", "Иванович");

        Group group1 = new Group("Йога", Age.ADULT, 60);
        Group group2 = new Group("Пилатес", Age.ADULT, 45);

        timetable.addNewTrainingSession(new TrainingSession(group1, coach,
                DayOfWeek.MONDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group2, coach,
                DayOfWeek.WEDNESDAY, new TimeOfDay(14, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group1, coach,
                DayOfWeek.FRIDAY, new TimeOfDay(18, 0)));

        Map<Coach, Integer> result = timetable.getCountByCoaches();

        assertEquals(1, result.size(), "Должен быть только один тренер");
        assertEquals(3, result.get(coach), "У тренера должно быть 3 занятия");
    }

    @Test
    public void testGetCountByCoachesMultipleCoachesSortedDescending() {
        Timetable timetable = new Timetable();

        Coach coach1 = new Coach("Петров", "Петр", "Петрович");
        Coach coach2 = new Coach("Сидорова", "Мария", "Сергеевна");
        Coach coach3 = new Coach("Иванов", "Алексей", "");

        Group group = new Group("Фитнес", Age.ADULT, 60);

        timetable.addNewTrainingSession(new TrainingSession(group, coach2,
                DayOfWeek.MONDAY, new TimeOfDay(9, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach2,
                DayOfWeek.WEDNESDAY, new TimeOfDay(9, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach2,
                DayOfWeek.FRIDAY, new TimeOfDay(9, 0)));

        timetable.addNewTrainingSession(new TrainingSession(group, coach3,
                DayOfWeek.TUESDAY, new TimeOfDay(11, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach3,
                DayOfWeek.THURSDAY, new TimeOfDay(11, 0)));

        timetable.addNewTrainingSession(new TrainingSession(group, coach1,
                DayOfWeek.SATURDAY, new TimeOfDay(15, 0)));

        Map<Coach, Integer> result = timetable.getCountByCoaches();

        assertEquals(3, result.size(), "Должно быть 3 тренера");

        List<Map.Entry<Coach, Integer>> entries = new ArrayList<>(result.entrySet());

        assertEquals(coach2, entries.get(0).getKey(),
                "Первым должен быть тренер с наибольшим количеством занятий");
        assertEquals(3, entries.get(0).getValue(),
                "У первого тренера должно быть 3 занятия");

        assertEquals(coach3, entries.get(1).getKey(),
                "Вторым должен быть тренер со средним количеством занятий");
        assertEquals(2, entries.get(1).getValue(),
                "У второго тренера должно быть 2 занятия");

        assertEquals(coach1, entries.get(2).getKey(),
                "Третьим должен быть тренер с наименьшим количеством занятий");
        assertEquals(1, entries.get(2).getValue(),
                "У третьего тренера должно быть 1 занятие");
    }

    @Test
    public void testNoTimeOverlapAllowed() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Иванов", "Иван", "Иванович");

        Group group1 = new Group("Йога", Age.ADULT, 60);
        Group group2 = new Group("Пилатес", Age.ADULT, 60);

        TrainingSession session1 = new TrainingSession(group1, coach,
                DayOfWeek.MONDAY, new TimeOfDay(10, 0));
        TrainingSession session2 = new TrainingSession(group2, coach,
                DayOfWeek.MONDAY, new TimeOfDay(11, 0));

        timetable.addNewTrainingSession(session1);
        timetable.addNewTrainingSession(session2);

        assertEquals(2, timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY).size(), "Оба занятия " +
                "должны добавиться, так как время не пересекается (10:00-11:00 и 11:00-12:00)");
    }

}

