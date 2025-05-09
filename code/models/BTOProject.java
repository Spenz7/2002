package models;

import models.enums.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BTOProject {
    private String name;
    private String neighborhood;
    private String type1;     // E.g., "2-Room"
    private int unitsType1;   // Number of units available for type1
    private double priceType1;// Selling price for type1
    private String type2;     // E.g., "3-Room"
    private int unitsType2;   // Number of units available for type2
    private double priceType2;// Selling price for type2
    private Date openingDate; // Application opening date
    private Date closingDate; // Application closing date
    private String manager;   // Manager for the project
    private int officerSlot;  // Number of officer slots available
    private List<String> officers; // List of officers assigned to the project
    private FlatType flatType;

    private boolean visibility = true; // Default to visible

    public BTOProject(String name, String neighborhood, String type1, int unitsType1, double priceType1,
                      String type2, int unitsType2, double priceType2, Date openingDate, Date closingDate,
                      String manager, int officerSlot, String[] officers) {
        this.name = name;
        this.neighborhood = neighborhood;
        this.type1 = type1;
        this.unitsType1 = unitsType1;
        this.priceType1 = priceType1;
        this.type2 = type2;
        this.unitsType2 = unitsType2;
        this.priceType2 = priceType2;
        this.openingDate = openingDate;
        this.closingDate = closingDate;
        this.manager = manager;
        this.officerSlot = officerSlot;
        this.officers = List.of(officers); // Converts an array to a List
    }

    public String getProjectName() {
        return name;
    }

    public String getName() {
        return name;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public String getType1() {
        return type1;
    }

    public void setType1(String type1) {
        this.type1 = type1;
    }

    public int getUnitsType1() {
        return unitsType1;
    }

    public void setUnitsType1(int unitsType1) {
        this.unitsType1 = unitsType1;
    }

    public double getPriceType1() {
        return priceType1;
    }

    public void setPriceType1(double priceType1) {
        this.priceType1 = priceType1;
    }

    public String getType2() {
        return type2;
    }

    public void setType2(String type2) {
        this.type2 = type2;
    }

    public int getUnitsType2() {
        return unitsType2;
    }

    public void setUnitsType2(int unitsType2) {
        this.unitsType2 = unitsType2;
    }

    public double getPriceType2() {
        return priceType2;
    }

    public void setPriceType2(double priceType2) {
        this.priceType2 = priceType2;
    }

    public Date getOpeningDate() {
        return openingDate;
    }

    public void setOpeningDate(Date openingDate) {
        this.openingDate = openingDate;
    }

    public Date getClosingDate() {
        return closingDate;
    }

    public void setClosingDate(Date closingDate) {
        this.closingDate = closingDate;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public int getOfficerSlot() {
        return officerSlot;
    }

    public void setOfficerSlot(int officerSlot) {
        this.officerSlot = officerSlot;
    }

    public List<String> getOfficers() {
        return officers;
    }

    public void setOfficers(List<String> officers) {
        this.officers = officers;
    }

    public boolean isVisible() {
        return visibility;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }

    public int getAvailableFlats(FlatType flatType) {
    // Check for null flatType to prevent runtime errors
        if (flatType == null) {
            System.err.println("Error: flatType is null.");
            return 0;
        }

        // Retrieve the display name for the flat type
        String flatTypeStr = flatType.getDisplayName();

        // Debug output for tracing the flat type being checked
        System.out.println("Checking availability for flat type: " + flatTypeStr);

        // Compare with project flat types and return availability
        if (type1 != null && type1.equalsIgnoreCase(flatTypeStr)) {
            System.out.println("Available units for " + flatTypeStr + ": " + unitsType1);
            return unitsType1;
        } else if (type2 != null && type2.equalsIgnoreCase(flatTypeStr)) {
            System.out.println("Available units for " + flatTypeStr + ": " + unitsType2);
            return unitsType2;
        }

        // Debug output if no match is found
        System.err.println("Flat type \"" + flatTypeStr + "\" does not match type1 or type2.");
        return 0;
    }



    public boolean overlaps(BTOProject otherProject) {
        return !(this.closingDate.before(otherProject.openingDate) || 
                 this.openingDate.after(otherProject.closingDate));
    }

    public Map<String, Integer> getFlatAvailability() {
        Map<String, Integer> availability = new HashMap<>();
        availability.put(type1, unitsType1);
        availability.put(type2, unitsType2);
        return availability;
    }

    // @Override
    // public String toString() {
    //     return "BTOProject{" +
    //             "name='" + name + '\'' +
    //             ", neighborhood='" + neighborhood + '\'' +
    //             ", type1='" + type1 + '\'' +
    //             ", unitsType1=" + unitsType1 +
    //             ", priceType1=" + priceType1 +
    //             ", type2='" + type2 + '\'' +
    //             ", unitsType2=" + unitsType2 +
    //             ", priceType2=" + priceType2 +
    //             ", openingDate=" + openingDate +
    //             ", closingDate=" + closingDate +
    //             ", manager='" + manager + '\'' +
    //             ", officerSlot=" + officerSlot +
    //             ", officers=" + officers +
    //             ", visibility=" + visibility +
    //             '}';
    // }

    @Override
    public String toString() {
        return String.format("%s (%s)", this.name, this.neighborhood); // Display project name and neighborhood only
    }


    public FlatType getFlatType() {
        // if (type1.toLowerCase().contains("2") || type2.toLowerCase().contains("2")) {
        //     return 2; // Represents "2-Room"
        // }
        // return 0; // Represents no "2-Room" type
        return this.flatType;

    }

    public void setFlatType(int flatType) {
        if (flatType == 2) {
            this.type1 = "2-Room";
            this.unitsType1 = 0; // Set to 0 for simplicity, adjust as needed
        } else {
            this.type1 = "Unknown"; // Default or error case
        }
    }

    public boolean approveApplication(FlatType flatType) {
        System.out.println("Available flats for " + flatType + ": " + getAvailableFlats(flatType)); // Debug line

        if (flatType.equals(FlatType.TWO_ROOM) && unitsType1 > 0) {
            unitsType1--; // Decrease available units for type 1
            System.out.println("Approved! Remaining units for 2-Room: " + unitsType1);
            return true;
        } else if (flatType.equals(FlatType.THREE_ROOM) && unitsType2 > 0) {
            unitsType2--; // Decrease available units for type 2
            System.out.println("Approved! Remaining units for 3-Room: " + unitsType2);
            return true;
        }

        System.out.println("No available flats for " + flatType);  // Debug line
        return false;
    }


    
    public boolean rejectApplication(FlatType flatType) {
        // Logic to handle rejection of application (if needed)
        return true; // Placeholder for rejection logic
    }

    // Inside BTOProject.java

    public boolean processApplication(Application application) {
        // Determine the flat type of the application
        FlatType flatType = application.getFlatType();
        System.out.println("Processing application for flat type: " + flatType);

        // Process the application for approval or rejection
        if (approveApplication(flatType)) {
            application.updateStatus(ApplicationStatus.APPROVED);  // Approve the application
            return true;
        } else {
            application.updateStatus(ApplicationStatus.REJECTED);  // Reject the application
            return false;
        }
    }


}
