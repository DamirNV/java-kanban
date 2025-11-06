import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

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

        int expectedOnMonday = 1;
        Map<TimeOfDay, List<TrainingSession>> mondaySchedule = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        assertNotNull(mondaySchedule, "Расписание на понедельник не должно быть null");
        assertEquals(expectedOnMonday, mondaySchedule.size(), "За понедельник вернулось не 1 занятие.");

        int expectedOnTuesday = 0;
        Map<TimeOfDay, List<TrainingSession>> tuesdaySchedule  = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        int actualTuesdaySize = (tuesdaySchedule == null) ? 0 : tuesdaySchedule.size();
        assertEquals(expectedOnTuesday, actualTuesdaySize, "За вторник вернулось не 0 занятий.");
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

        int expectedOnMonday = 1;
        Map<TimeOfDay, List<TrainingSession>> mondaySchedule = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        assertNotNull(mondaySchedule, "Расписание на понедельник не должно быть null");
        assertEquals(expectedOnMonday, mondaySchedule.size(), "За понедельник вернулось не 1 занятие.");

        int expectedOnThursday = 2;
        Map<TimeOfDay, List<TrainingSession>> thursdaySchedule = timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);
        assertNotNull(thursdaySchedule , "Расписание на четверг не должно быть null");
        assertEquals(expectedOnThursday , thursdaySchedule.size(), "За четверг вернулось не 2 занятия.");
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

        int expectedOnMondayOn_13_00 = 1;
        List<TrainingSession> mondayScheduleOn_13_00 = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.MONDAY, timeOfDay);
        assertNotNull(mondayScheduleOn_13_00, "Расписание на понедельник не должно быть null");
        assertEquals(expectedOnMondayOn_13_00, mondayScheduleOn_13_00.size(), "За понедельник вернулось " +
                "не 1 занятие.");

        List<TrainingSession> mondayScheduleOn_14_00 = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.MONDAY, new TimeOfDay(14, 0));
        assertTrue(mondayScheduleOn_14_00 == null || mondayScheduleOn_14_00.isEmpty(),
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











}

