package in.deloitte.screening.app.document.services;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import in.deloitte.screening.app.document.dto.SearchJdResponse;
import in.deloitte.screening.app.document.dto.UploadJdResponse;


@Service
public interface JobDescriptionService {

	public UploadJdResponse saveJobDescription(MultipartFile jd, String userEmail) throws IOException;
	
	public List<SearchJdResponse>  getAllJdInfo(String jdFileName);
}
