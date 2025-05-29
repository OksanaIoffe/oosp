import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


class User {
    private String username;
    private String password;
    private String role;
    private String fullName;
    private LocalDate birthDate;
    private String snils;
    private List<Voting> participatedVotings = new ArrayList<>();

    public User(String username, String password, String role, String fullName, LocalDate birthDate, String snils) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.snils = snils;
    }

    // Геттеры и сеттеры
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public String getFullName() { return fullName; }
    public LocalDate getBirthDate() { return birthDate; }
    public String getSnils() { return snils; }
    public List<Voting> getParticipatedVotings() { return participatedVotings; }

    public void addParticipatedVoting(Voting voting) {
        participatedVotings.add(voting);
    }
}
