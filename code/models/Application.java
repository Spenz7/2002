package models;

import models.enums.ApplicationStatus;
import models.enums.FlatType;

public class Application {
    // Attributes
    private int applicationId;        // Unique identifier for the application
    private String applicantNric;     // NRIC of the applicant who submitted the application
    private String applicantName;     // Name of the applicant
    private String projectName;       // Name of the BTO project
    private FlatType flatType;        // FlatType (e.g., "2-Room", "3-Room")
    private ApplicationStatus status; // Current status of the application (e.g., "Pending", "Approved")

    // Constructor
    public Application(
        int applicationId,
        String applicantNric,
        String applicantName,
        String projectName,
        FlatType flatType,
        ApplicationStatus status,
        boolean isSingle,
        int age
    ) {
        // Eligibility validation
        if (isSingle && age >= 35 && !flatType.equals(FlatType.TWO_ROOM)) {
            throw new IllegalArgumentException("Singles can only apply for 2-room flats.");
        }
        if (!isSingle && age >= 21 && !flatType.equals(FlatType.TWO_ROOM) && !flatType.equals(FlatType.THREE_ROOM)) {
            throw new IllegalArgumentException("Married applicants can only apply for 2-room or 3-room flats.");
        }

        this.applicationId = applicationId;
        this.applicantNric = applicantNric;
        this.applicantName = applicantName;
        this.projectName = projectName;
        this.flatType = flatType; // Use FlatType enum
        this.status = status;
    }

    // Getters and Setters
    public int getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(int applicationId) {
        this.applicationId = applicationId;
    }

    public String getApplicantNric() {
        return applicantNric;
    }

    public void setApplicantNric(String applicantNric) {
        this.applicantNric = applicantNric;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public FlatType getFlatType() {
        return flatType;
    }

    public void setFlatType(FlatType flatType) {
        this.flatType = flatType;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    // Method: Update Application Status
    public void updateStatus(ApplicationStatus newStatus) {
        this.status = newStatus;
    }

    // Override toString for Displaying Application Details
    @Override
    public String toString() {
        return "Application{" +
                "applicationId=" + applicationId +
                ", applicantNric='" + applicantNric + '\'' +
                ", applicantName='" + applicantName + '\'' +
                ", projectName='" + projectName + '\'' +
                ", flatType=" + flatType +
                ", status=" + status +
                '}';
    }

    // Method for Getting the String Representation of FlatType
    public String getFlatTypeString() {
        return flatType != null ? flatType.getDisplayName() : "Unknown";
    }
}
