package com.myapp.royalcounselling;

public class PersonalCounsellingBean {
    String personalCounsellingID;
    String requestTime;
    String startTime;
    String counsellingType;
    boolean accepted;

    public String getPersonalCounsellingID() {
        return personalCounsellingID;
    }

    public void setPersonalCounsellingID(String personalCounsellingID) {
        this.personalCounsellingID = personalCounsellingID;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getCounsellingType() {
        return counsellingType;
    }

    public void setCounsellingType(String counsellingType) {
        this.counsellingType = counsellingType;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }
}
