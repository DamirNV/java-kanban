package ui;

import entities.*;
import services.Timetable;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class GymManagementApp {

    private static final Scanner scanner = new Scanner(System.in);
    private static Timetable timetable = new Timetable();

    public static void main(String[] args) {
        boolean running = true;
        while (running) {
            showMenu();
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    addTrainingSession();
                    break;
                case 2:
                    showTrainingSessionsForDay();
                    break;
                case 3:
                    showTrainingSessionsForDayAndTime();
                    break;
                case 4:
                    CountByCoaches();
                    break;
                case 5:
                    running = false;
                    System.out.println("До свидания!");
                    break;
                default:
                    System.out.println("Неверный выбор, попробуйте снова.");
            }
        }
    }

    private static void showMenu() {
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("          Программа для учета гимнастического зала              ");
        System.out.println("                          Gym master                            ");
        System.out.println("Выберите действие:");
        System.out.println("1 — \uD83D\uDCC5 Добавить тренировку в недельное расписание.");
        System.out.println("2 — \uD83D\uDC40 Показать все тренировки на выбранный день.");
        System.out.println("3 — \u23F0 Показать все тренировки на выбранный день и время.");
        System.out.println("4 — \uD83D\uDCCA Показать, сколько занятий у тренера на этой неделе.");
        System.out.println("5 — \uD83D\uDEAA Выйти из программы.");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
    }

    private static void addTrainingSession() {
        System.out.println("Введите название группы:");
        String title = scanner.nextLine();
        System.out.println("Выберите тип группы:");
        System.out.println("1 - Детская");
        System.out.println("2 - Взрослая");
        System.out.print("Ваш выбор: ");
        int ageChoice = scanner.nextInt();
        Age age;
        if (ageChoice == 1) {
            age = Age.CHILD;
        } else if (ageChoice == 2) {
            age = Age.ADULT;
        } else {
            System.out.println("Неверный выбор, установлено значение по умолчанию: Взрослая");
            age = Age.ADULT;
        }
        System.out.println("Введите продолжительность занятия (в минутах):");
        int duration = scanner.nextInt();
        Group group = new Group(title, age, duration);

        scanner.nextLine();

        System.out.print("Введите фамилию тренера: ");
        String surname = scanner.nextLine().trim();
        System.out.print("Введите имя тренера: ");
        String name = scanner.nextLine().trim();
        System.out.print("Введите отчество тренера: ");
        String middleName = scanner.nextLine().trim();
        Coach coach = new Coach(surname, name, middleName);

        System.out.println("Введите день недели:");
        System.out.println("1 - Понедельник");
        System.out.println("2 - Вторник");
        System.out.println("3 - Среда");
        System.out.println("4 - Четверг");
        System.out.println("5 - Пятница");
        System.out.println("6 - Суббота");
        System.out.println("7 - Воскресенье");
        System.out.print("Ваш выбор: ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        DayOfWeek dayOfWeek;
        switch (choice) {
            case 1: dayOfWeek = DayOfWeek.MONDAY; break;
            case 2: dayOfWeek = DayOfWeek.TUESDAY; break;
            case 3: dayOfWeek = DayOfWeek.WEDNESDAY; break;
            case 4: dayOfWeek = DayOfWeek.THURSDAY; break;
            case 5: dayOfWeek = DayOfWeek.FRIDAY; break;
            case 6: dayOfWeek = DayOfWeek.SATURDAY; break;
            case 7: dayOfWeek = DayOfWeek.SUNDAY; break;
            default:
                System.out.println("Неверный выбор, установлен понедельник по умолчанию");
                dayOfWeek = DayOfWeek.MONDAY;
        }

        System.out.println("Введите время тренировки:");
        System.out.print("Часы (0-23): ");
        int hours = scanner.nextInt();
        System.out.print("Минуты (0-59): ");
        int minutes = scanner.nextInt();
        scanner.nextLine();
        TimeOfDay timeOfDay = new TimeOfDay(hours, minutes);

        TrainingSession trainingSession = new TrainingSession(group, coach, dayOfWeek, timeOfDay);

        timetable.addNewTrainingSession(trainingSession);

        System.out.println("Занятие успешно добавлено в расписание!");
    }

    private static void showTrainingSessionsForDay() {
        System.out.println("Введите день недели:");
        System.out.println("1 - Понедельник");
        System.out.println("2 - Вторник");
        System.out.println("3 - Среда");
        System.out.println("4 - Четверг");
        System.out.println("5 - Пятница");
        System.out.println("6 - Суббота");
        System.out.println("7 - Воскресенье");
        System.out.print("Ваш выбор: ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        DayOfWeek dayOfWeek;
        switch (choice) {
            case 1: dayOfWeek = DayOfWeek.MONDAY; break;
            case 2: dayOfWeek = DayOfWeek.TUESDAY; break;
            case 3: dayOfWeek = DayOfWeek.WEDNESDAY; break;
            case 4: dayOfWeek = DayOfWeek.THURSDAY; break;
            case 5: dayOfWeek = DayOfWeek.FRIDAY; break;
            case 6: dayOfWeek = DayOfWeek.SATURDAY; break;
            case 7: dayOfWeek = DayOfWeek.SUNDAY; break;
            default:
                System.out.println("Неверный выбор, установлен понедельник по умолчанию");
                dayOfWeek = DayOfWeek.MONDAY;
        }
        Map<TimeOfDay, List<TrainingSession>> daySchedule = timetable.getTrainingSessionsForDay(dayOfWeek);
        System.out.println("\n=== Расписание на " + dayOfWeek.getRussianName() + " ===");
        if (daySchedule == null || daySchedule.isEmpty()) {
            System.out.println("Занятий нет");
        } else {
            for (Map.Entry<TimeOfDay, List<TrainingSession>> entry : daySchedule.entrySet()) {
                System.out.println("Время " + entry.getKey() + ":");
                for (TrainingSession session : entry.getValue()) {
                    System.out.println("  - " + sessionToString(session));
                }
            }
        }

    }

    private static void showTrainingSessionsForDayAndTime() {
        System.out.println("Введите день недели:");
        System.out.println("1 - Понедельник");
        System.out.println("2 - Вторник");
        System.out.println("3 - Среда");
        System.out.println("4 - Четверг");
        System.out.println("5 - Пятница");
        System.out.println("6 - Суббота");
        System.out.println("7 - Воскресенье");
        System.out.print("Ваш выбор: ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        DayOfWeek dayOfWeek;
        switch (choice) {
            case 1: dayOfWeek = DayOfWeek.MONDAY; break;
            case 2: dayOfWeek = DayOfWeek.TUESDAY; break;
            case 3: dayOfWeek = DayOfWeek.WEDNESDAY; break;
            case 4: dayOfWeek = DayOfWeek.THURSDAY; break;
            case 5: dayOfWeek = DayOfWeek.FRIDAY; break;
            case 6: dayOfWeek = DayOfWeek.SATURDAY; break;
            case 7: dayOfWeek = DayOfWeek.SUNDAY; break;
            default:
                System.out.println("Неверный выбор, установлен понедельник по умолчанию");
                dayOfWeek = DayOfWeek.MONDAY;
        }
        System.out.println("Введите время тренировки:");
        System.out.print("Часы (0-23): ");
        int hours = scanner.nextInt();
        System.out.print("Минуты (0-59): ");
        int minutes = scanner.nextInt();
        scanner.nextLine();
        TimeOfDay timeOfDay = new TimeOfDay(hours, minutes);

        List<TrainingSession> sessions = timetable.getTrainingSessionsForDayAndTime(dayOfWeek, timeOfDay);
        System.out.println("\n=== Занятия в " + dayOfWeek.getRussianName() + " в " + timeOfDay + " ===");
        if (sessions == null || sessions.isEmpty()) {
            System.out.println("Занятий не найдено");
        } else {
            for (int i = 0; i < sessions.size(); i++) {
                TrainingSession session = sessions.get(i);
                System.out.println((i + 1) + ". " + sessionToString(session));
            }
            System.out.println("Всего занятий: " + sessions.size());
        }

    }

    private static void CountByCoaches() {
        Map<Coach, Integer> coachStats = timetable.getCountByCoaches();
        System.out.println("\n=== Статистика тренеров за неделю ===");
        if (coachStats.isEmpty()) {
            System.out.println("Нет данных о тренировках");
        } else {
            int position = 1;
            for (Map.Entry<Coach, Integer> entry : coachStats.entrySet()) {
                Coach coach = entry.getKey();
                System.out.printf("%d. %s: %d тренировок%n",
                        position++,
                        coach.toString(),
                        entry.getValue());
            }
        }
    }

    private static String sessionToString(TrainingSession session) {
        return String.format("Группа: %s (%s, %d мин.), Тренер: %s",
                session.getGroup().getTitle(),
                session.getGroup().getAge().getRussianName(),
                session.getGroup().getDuration(),
                session.getCoach().toString());
    }

}
