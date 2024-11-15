package server.repository;

public interface Repository {
    String loadHistory();
    void saveHistory(String text);
}
