import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

class VotingSystem {
    private List<User> users = new ArrayList<>();
    private List<Candidate> candidates = new ArrayList<>();
    private List<Voting> votings = new ArrayList<>();
    private User currentUser;

    // Методы для работы с пользователями
    public void registerUser(String username, String password, String fullName, LocalDate birthDate, String snils) {
        users.add(new User(username, password, "USER", fullName, birthDate, snils));
    }

    public boolean login(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                currentUser = user;
                return true;
            }
        }
        return false;
    }

    public void logout() {
        currentUser = null;
    }

    // Методы администратора
    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    public void deleteUser(String username) {
        users.removeIf(u -> u.getUsername().equals(username));
    }

    public void createCEC(String username, String password) {
        users.add(new User(username, password, "CEC", null, null, null));
    }

    public List<Candidate> getAllCandidates() {
        return new ArrayList<>(candidates);
    }

    public void deleteCandidate(String username) {
        candidates.removeIf(c -> c.getUsername().equals(username));
    }

    // Методы ЦИК
    public Voting createVoting(String title, LocalDate endDate) {
        Voting voting = new Voting(title, endDate);
        votings.add(voting);
        return voting;
    }

    public void addCandidateToVoting(Voting voting, Candidate candidate) {
        voting.addCandidate(candidate);
    }

    public void printResultsToPDF(Voting voting, String filePath) throws Exception {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();

        document.add(new Paragraph("Результаты голосования: " + voting.getTitle()));
        document.add(new Paragraph("Дата окончания: " + voting.getEndDate()));
        document.add(new Paragraph(" "));

        // Сортируем кандидатов по количеству голосов
        List<Map.Entry<Candidate, Integer>> sortedResults = voting.getVotes().entrySet().stream()
                .sorted(Map.Entry.<Candidate, Integer>comparingByValue().reversed())
                .collect(Collectors.toList());

        for (Map.Entry<Candidate, Integer> entry : sortedResults) {
            document.add(new Paragraph(entry.getKey().getFullName() + ": " + entry.getValue() + " голосов"));
        }

        document.close();
    }

    // Методы кандидата
    public void updateCandidateBio(String username, String bio) {
        for (Candidate candidate : candidates) {
            if (candidate.getUsername().equals(username)) {
                candidate.setBio(bio);
                return;
            }
        }
        throw new IllegalArgumentException("Candidate not found");
    }

    // Методы пользователя
    public List<Voting> getActiveVotings() {
        return votings.stream().filter(Voting::isActive).collect(Collectors.toList());
    }

    public List<Candidate> getCandidatesForVoting(Voting voting) {
        return new ArrayList<>(voting.getCandidates());
    }

    public void castVote(Voting voting, Candidate candidate) {
        voting.vote(currentUser, candidate);
    }

    // Методы для выгрузки результатов
    public void exportResults(List<Voting> votingsToExport, String directory, boolean singleFile) throws Exception {
        if (singleFile) {
            String fileName = directory + "/results_" + System.currentTimeMillis() + ".pdf";
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();

            for (Voting voting : votingsToExport) {
                document.add(new Paragraph("Результаты голосования: " + voting.getTitle()));
                document.add(new Paragraph("Дата окончания: " + voting.getEndDate()));
                document.add(new Paragraph(" "));

                List<Map.Entry<Candidate, Integer>> sortedResults = voting.getVotes().entrySet().stream()
                        .sorted(Map.Entry.<Candidate, Integer>comparingByValue().reversed())
                        .collect(Collectors.toList());

                for (Map.Entry<Candidate, Integer> entry : sortedResults) {
                    document.add(new Paragraph(entry.getKey().getFullName() + ": " + entry.getValue() + " голосов"));
                }

                document.add(new Paragraph(" "));
                document.add(new Paragraph("----------------------------------------"));
                document.add(new Paragraph(" "));
            }

            document.close();
        } else {
            for (Voting voting : votingsToExport) {
                String fileName = directory + "/results_" + voting.getId() + "_" + System.currentTimeMillis() + ".pdf";
                printResultsToPDF(voting, fileName);
            }
        }
    }
}
