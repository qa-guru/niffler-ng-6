package guru.qa.niffler.ex;

public class UnknownIssueStatusException extends RuntimeException {
    public UnknownIssueStatusException(String message) {
        super(message);
    }
}