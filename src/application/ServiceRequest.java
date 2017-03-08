package application;

import java.util.Date;

/**
 * @author Michael Atanasio
 */
public class ServiceRequest {

    private Date dateRequested;
    private Date dateCompleted;
    private String description;
    private String technician;

    /**
     * Creates a new ServiceRequest with a user-defined create date, complete
     * date, description, and technician. Set date completed to null if the
     * request is not yet completed.
     *
     * @param requested The Date requested
     * @param completed The Date completed. {@code Null} if not complete
     * @param desc      The description
     * @param tech      The technician
     */
    public ServiceRequest(Date requested, Date completed, String desc, String tech) {
        this.dateRequested = requested;
        this.dateCompleted = completed;
        this.description = desc;
        this.technician = tech;
    }

    public Date getDateRequested() {
        return dateRequested;
    }

    public void setDateRequested(Date dateRequested) {
        this.dateRequested = dateRequested;
    }

    public Date getDateCompleted() {
        return dateCompleted;
    }

    public void setDateCompleted(Date dateCompleted) {
        this.dateCompleted = dateCompleted;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTechnician() {
        return technician;
    }

    public void setTechnician(String technician) {
        this.technician = technician;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(RequestIO.DATE_FORMATTER.format(this.dateRequested));
        sb.append(",");
        sb.append(this.dateCompleted == null ? " " : RequestIO.DATE_FORMATTER.format(this.dateCompleted));
        sb.append(",");
        sb.append(this.getDescription());
        sb.append(",");
        sb.append(this.technician);
        return sb.toString();
    }

}