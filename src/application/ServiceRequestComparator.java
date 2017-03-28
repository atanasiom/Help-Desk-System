package application;

import java.util.Comparator;

public class ServiceRequestComparator implements Comparator<ServiceRequest> {

	private SortType type;

	public ServiceRequestComparator(SortType type) {
		this.type = type;
	}

	@Override
	public int compare(ServiceRequest req1, ServiceRequest req2) {
		switch (type) {
			case CREATED_ASCENDING:
				return req1.getDateRequested().compareTo(req2.getDateRequested());
			case CREATED_DESCENDING:
				return req2.getDateRequested().compareTo(req1.getDateRequested());
			case STATUS_ASCENDING:
				return req1.getServiceStatus().compareTo(req2.getServiceStatus());
			case STATUS_DESCENDING:
				return req2.getServiceStatus().compareTo(req1.getServiceStatus());
			case COMPLETED_ASCENDING:
				return req1.getDateCompleted().compareTo(req2.getDateCompleted());
			case COMPLETED_DESCENDING:
				return req2.getDateCompleted().compareTo(req1.getDateCompleted());
			case TECHNICIAN_ASCENDING:
				return req1.getTechnician().compareTo(req2.getTechnician());
			case TECHNICIAN_DESCENDING:
				return req2.getTechnician().compareTo(req1.getTechnician());
			default:
				return 0;
		}
	}
}
