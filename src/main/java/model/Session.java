package model;

public class Session {

    private final String sessionId;
    private final String userId;
    private final String userName;

    public Session(String sessionId, String userId, String userName) {
        this.sessionId = sessionId;
        this.userId = userId;
        this.userName = userName;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }
}
