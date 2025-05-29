
import java.time.LocalDate;

import java.util.*;

class Candidate {
    private String username;
    private String password;
    private String fullName;
    private String bio;
    private List<Voting> votings = new ArrayList<>();

    public Candidate(String username, String password, String fullName) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
    }

    // Геттеры и сеттеры
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getFullName() { return fullName; }
    public String getBio() { return bio; }
    public List<Voting> getVotings() { return votings; }

    public void setBio(String bio) { this.bio = bio; }
    public void addVoting(Voting voting) { votings.add(voting); }
}
