package in.deloitte.screening.app.document.repositories;

public class DocumentQueries {

	public static String createJdResultsQuery(String jdFileName) {
		return "SELECT jd_file_name,jd_file FROM public.job_description WHERE jd_file_name LIKE '" + jdFileName + "%'";
	}

}