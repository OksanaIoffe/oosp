import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class VotingSystemApp {
    public static void main(String[] args) {
        VotingSystem system = new VotingSystem();
        Scanner scanner = new Scanner(System.in);

        // Инициализация тестовых данных
        system.registerUser("user1", "pass1", "Иванов Иван Иванович", LocalDate.of(1990, 1, 1), "123-456-789 00");
        system.createCEC("cec1", "cecpass1");

        boolean running = true;
        while (running) {
            System.out.println("1. Вход");
            System.out.println("2. Регистрация");
            System.out.println("0. Выход");
            System.out.print("Выберите действие: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    System.out.print("Логин: ");
                    String username = scanner.nextLine();
                    System.out.print("Пароль: ");
                    String password = scanner.nextLine();

                    if (system.login(username, password)) {
                        showUserMenu(system, scanner);
                    } else {
                        System.out.println("Неверный логин или пароль");
                    }
                    break;
                case 2:
                    System.out.print("ФИО: ");
                    String fullName = scanner.nextLine();
                    System.out.print("Дата рождения (ГГГГ-ММ-ДД): ");
                    LocalDate birthDate = LocalDate.parse(scanner.nextLine());
                    System.out.print("СНИЛС: ");
                    String snils = scanner.nextLine();
                    System.out.print("Логин: ");
                    String newUsername = scanner.nextLine();
                    System.out.print("Пароль: ");
                    String newPassword = scanner.nextLine();

                    system.registerUser(newUsername, newPassword, fullName, birthDate, snils);
                    System.out.println("Регистрация успешна");
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("Неверный выбор");
            }
        }
    }

    private static void showUserMenu(VotingSystem system, Scanner scanner) {
        String role = system.getCurrentUser().getRole();

        switch (role) {
            case "ADMIN":
                showAdminMenu(system, scanner);
                break;
            case "CEC":
                showCECMenu(system, scanner);
                break;
            case "CANDIDATE":
                showCandidateMenu(system, scanner);
                break;
            case "USER":
                showUserRegularMenu(system, scanner);
                break;
            default:
                System.out.println("Неизвестная роль");
        }
    }

    private static void showAdminMenu(VotingSystem system, Scanner scanner) {
        boolean back = false;
        while (!back) {
            System.out.println("\nМеню администратора:");
            System.out.println("1. Просмотр пользователей");
            System.out.println("2. Удаление пользователя");
            System.out.println("3. Создание ЦИК");
            System.out.println("4. Просмотр кандидатов");
            System.out.println("5. Удаление кандидата");
            System.out.println("0. Выход");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    System.out.println("\nСписок пользователей:");
                    system.getAllUsers().forEach(u -> System.out.println(u.getUsername() + " - " + u.getRole()));
                    break;
                case 2:
                    System.out.print("Введите логин пользователя для удаления: ");
                    String usernameToDelete = scanner.nextLine();
                    system.deleteUser(usernameToDelete);
                    System.out.println("Пользователь удален");
                    break;
                case 3:
                    System.out.print("Введите логин ЦИК: ");
                    String cecUsername = scanner.nextLine();
                    System.out.print("Введите пароль ЦИК: ");
                    String cecPassword = scanner.nextLine();
                    system.createCEC(cecUsername, cecPassword);
                    System.out.println("ЦИК создан");
                    break;
                case 4:
                    System.out.println("\nСписок кандидатов:");
                    system.getAllCandidates().forEach(c -> System.out.println(c.getUsername() + " - " + c.getFullName()));
                    break;
                case 5:
                    System.out.print("Введите логин кандидата для удаления: ");
                    String candidateToDelete = scanner.nextLine();
                    system.deleteCandidate(candidateToDelete);
                    System.out.println("Кандидат удален");
                    break;
                case 0:
                    back = true;
                    system.logout();
                    break;
                default:
                    System.out.println("Неверный выбор");
            }
        }
    }

    // Аналогичные методы для других ролей (showCECMenu, showCandidateMenu, showUserRegularMenu)
    // ...
}