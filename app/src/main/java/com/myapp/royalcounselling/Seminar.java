package com.myapp.royalcounselling;

public class Seminar {

    String seminarDescription;
    String seminarEnd;
    String seminarFees, acceptingRegistration;
    String seminarId, seminarName, seminarRegistrationEnd, seminarRegistrationStart, seminarStart, seminarType, seminarZoomLink, whatsappLink;

    public Seminar() {
    }

    public Seminar(String seminarName) {
        this.seminarName = seminarName;
    }

    public String getSeminarDescription() {
        return seminarDescription;
    }

    public String getAcceptingRegistration() {
        return acceptingRegistration;
    }

    public void setAcceptingRegistration(String acceptingRegistration) {
        this.acceptingRegistration = acceptingRegistration;
    }

    public void setSeminarDescription(String seminarDescription) {
        this.seminarDescription = seminarDescription;
    }

    public String getSeminarEnd() {
        return seminarEnd;
    }

    public void setSeminarEnd(String seminarEnd) {
        this.seminarEnd = seminarEnd;
    }

    public String getSeminarFees() {
        return seminarFees;
    }

    public void setSeminarFees(String seminarFees) {
        this.seminarFees = seminarFees;
    }

    public String getSeminarId() {
        return seminarId;
    }

    public void setSeminarId(String seminarId) {
        this.seminarId = seminarId;
    }

    public String getSeminarName() {
        return seminarName;
    }

    public void setSeminarName(String seminarName) {
        this.seminarName = seminarName;
    }

    public String getSeminarRegistrationEnd() {
        return seminarRegistrationEnd;
    }

    public void setSeminarRegistrationEnd(String seminarRegistrationEnd) {
        this.seminarRegistrationEnd = seminarRegistrationEnd;
    }

    public String getSeminarRegistrationStart() {
        return seminarRegistrationStart;
    }

    public void setSeminarRegistrationStart(String seminarRegistrationStart) {
        this.seminarRegistrationStart = seminarRegistrationStart;
    }

    public String getSeminarStart() {
        return seminarStart;
    }

    public void setSeminarStart(String seminarStart) {
        this.seminarStart = seminarStart;
    }

    public String getSeminarType() {
        return seminarType;
    }

    public void setSeminarType(String seminarType) {
        this.seminarType = seminarType;
    }

    public String getSeminarZoomLink() {
        return seminarZoomLink;
    }

    public void setSeminarZoomLink(String seminarZoomLink) {
        this.seminarZoomLink = seminarZoomLink;
    }

    public String getWhatsappLink() {
        return whatsappLink;
    }

    public void setWhatsappLink(String whatsappLink) {
        this.whatsappLink = whatsappLink;
    }
}
