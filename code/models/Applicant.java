package models;

import models.enums.MaritalStatus;
import java.util.regex.Pattern;

public class Applicant {
    // Attributes
    private String name;
    private String nric;
    private int age;
    private String maritalStatus;
    private String password;
    private String applicationStatus; // New: Tracks application status (e.g., Pending, Approved, Rejected)
    private String flatType;          // New: Tracks the type of flat applied for
    private String projectName;       // New: Tracks the project name applied for

    // Constructor
    public Applicant(String name, String nric, int age, String maritalStatus, String password) {
        this.name = name;
        this.nric = nric;
        this.age = age;
        this.maritalStatus = maritalStatus;
        this.password = password;
        this.applicationStatus = "Pending"; // Default status
        this.flatType = "";                 // Default empty flat type
        this.projectName = "";              // Default empty project name
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNric() {
        return nric;
    }

    public void setNric(String nric) {
        this.nric = nric;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public boolean isSingle() {
        // Assumes that maritalStatus is stored as "Single" or "Married"
        return "single".equalsIgnoreCase(this.maritalStatus);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(String applicationStatus) {
        this.applicationStatus = applicationStatus;
    }

    public String getFlatType() {
        return flatType;
    }

    public void setFlatType(String flatType) {
        this.flatType = flatType;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    // Method: Validate NRIC Format
    public boolean isValidNric() {
        // Simple regex for NRIC validation
        String nricRegex = "^[STFG]\\d{7}[A-Z]$";
        return Pattern.matches(nricRegex, this.nric);
    }

    // Method: Check Password
    public boolean checkPassword(String inputPassword) {
        return this.password.equals(inputPassword);
    }

    // Method: Change Password
    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

    // Method: Validate Age for Application Eligibility
    public boolean isEligibleForApplication() {
        return maritalStatus.equalsIgnoreCase("Single") && age >= 35;
    }

    // New Method: Check if Application is Approved
    public boolean isApplicationApproved() {
        return "Approved".equalsIgnoreCase(this.applicationStatus);
    }

    // Override toString for Displaying Applicant Information
    @Override
    public String toString() {
        return "Applicant{" +
                "name='" + name + '\'' +
                ", nric='" + nric + '\'' +
                ", age=" + age +
                ", maritalStatus='" + maritalStatus + '\'' +
                ", applicationStatus='" + applicationStatus + '\'' +
                ", flatType='" + flatType + '\'' +
                ", projectName='" + projectName + '\'' +
                '}';
    }
}
