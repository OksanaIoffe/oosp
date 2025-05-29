import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


public class Voting {
    private String id;
    private String title;
    private LocalDate endDate;
    private List<Candidate> candidates = new ArrayList<>();
    private Map<Candidate, Integer> votes = new HashMap<>();
    private List<User> voters = new ArrayList<>();

    public Voting(String title, LocalDate endDate) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.endDate = endDate;
    }

    // Геттеры и сеттеры
    public String getId() { return id; }
    public String getTitle() { return title; }
    public LocalDate getEndDate() { return endDate; }
    public List<Candidate> getCandidates() { return candidates; }
    public Map<Candidate, Integer> getVotes() { return votes; }
    public List<User> getVoters() { return voters; }

    public void addCandidate(Candidate candidate) {
        candidates.add(candidate);
        votes.put(candidate, 0);
    }

    public void vote(User user, Candidate candidate) {
        if (voters.contains(user)) {
            throw new IllegalStateException("User already voted");
        }
        if (!candidates.contains(candidate)) {
            throw new IllegalArgumentException("Invalid candidate");
        }
        votes.put(candidate, votes.get(candidate) + 1);
        voters.add(user);
        user.addParticipatedVoting(this);
    }

    public boolean isActive() {
        return LocalDate.now().isBefore(endDate);
    }
}

