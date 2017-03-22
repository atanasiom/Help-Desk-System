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
			case COMPLETED_ASCENDING:
				return req1.getDateRequested().compareTo(req2.getDateRequested());
			case CREATED_DESCENDING:
				return req2.getDateRequested().compareTo(req1.getDateRequested());
			default:
				return 0;
		}
	}
}
