package application;

import java.util.Comparator;

public class ServiceRequestComparator implements Comparator<ServiceRequest> {

    private SortType type;

    public ServiceRequestComparator(SortType type) {
        this.type = type;
    }

    @Override
    public int compare(ServiceRequest req1, ServiceRequest req2) {
        return 0;
    }
}
