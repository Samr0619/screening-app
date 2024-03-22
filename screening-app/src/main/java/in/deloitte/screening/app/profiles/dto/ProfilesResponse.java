package in.deloitte.screening.app.profiles.dto;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import in.deloitte.screening.app.document.entities.ResumeDownloadedUserInfo;


@JsonPropertyOrder (
	{ 
		"email",
		"cosineSimilarity",
		"experience",
		"skills",
		"resumeFileName",
		"jdFileName",
		"uploadedBy",
		"uploadTime",
		"downloads",
		"resumeFile"
	}
)
public class ProfilesResponse {

		private String email;
		
		private Double experience;
		
		private Set<String> skills;
		
		private String resumeFileName;
		
		private String uploadedBy;
		
		private LocalDateTime uploadTime;
		
		private byte [] resumeFile;
		
		List<ResumeDownloadedUserInfo> downloads;
		
		private String jdFileName;
		
		private Double cosineSimilarity;
		

		/**
		 * @return the experience
		 */
		public Double getExperience() {
			return experience;
		}

		/**
		 * @param experience the experience to set
		 */
		public void setExperience(Double experience) {
			this.experience = experience;
		}

		/**
		 * @return the resumeFile
		 */
		public byte[] getResumeFile() {
			return resumeFile;
		}

		/**
		 * @param resumeFile the resumeFile to set
		 */
		public void setResumeFile(byte[] resumeFile) {
			this.resumeFile = resumeFile;
		}

		/**
		 * @return the resumeFileName
		 */
		public String getResumeFileName() {
			return resumeFileName;
		}

		/**
		 * @param resumeFileName the resumeFileName to set
		 */
		public void setResumeFileName(String resumeFileName) {
			this.resumeFileName = resumeFileName;
		}
		
		/**
		 * @return the uploadedBy
		 */
		public String getUploadedBy() {
			return uploadedBy;
		}

		/**
		 * @param uploadedBy the uploadedBy to set
		 */
		public void setUploadedBy(String uploadedBy) {
			this.uploadedBy = uploadedBy;
		}

		/**
		 * @return the uploadTime
		 */
		public LocalDateTime getUploadTime() {
			return uploadTime;
		}

		/**
		 * @param uploadTime the uploadTime to set
		 */
		public void setUploadTime(LocalDateTime uploadTime) {
			this.uploadTime = uploadTime;
		}

		/**
		 * @return the downloads
		 */
		public List<ResumeDownloadedUserInfo> getDownloads() {
			return downloads;
		}

		/**
		 * @param downloads the downloads to set
		 */
		public void setDownloads(List<ResumeDownloadedUserInfo> downloads) {
			this.downloads = downloads;
		}

		/**
		 * @return the cosineSimilarity
		 */
		public Double getCosineSimilarity() {
			return cosineSimilarity;
		}

		/**
		 * @param cosineSimilarity the cosineSimilarity to set
		 */
		public void setCosineSimilarity(Double cosineSimilarity) {
			this.cosineSimilarity = cosineSimilarity;
		}

		/**
		 * @return the jdFileName
		 */
		public String getJdFileName() {
			return jdFileName;
		}

		/**
		 * @param jdFileName the jdFileName to set
		 */
		public void setJdFileName(String jdFileName) {
			this.jdFileName = jdFileName;
		}

		/**
		 * @return the email
		 */
		public String getEmail() {
			return email;
		}

		/**
		 * @param email the email to set
		 */
		public void setEmail(String email) {
			this.email = email;
		}
		
		/**
		 * @return the skills
		 */
		public Set<String> getSkills() {
			return skills;
		}

		/**
		 * @param skills the skills to set
		 */
		public void setSkills(Set<String> skills) {
			this.skills = skills;
		}
		
		public ProfilesResponse(String email, Double experience, Set<String> skills, String resumeFileName,
				String uploadedBy, LocalDateTime uploadTime, byte[] resumeFile,
				List<ResumeDownloadedUserInfo> downloads, String jdFileName, Double cosineSimilarity) {
			super();
			this.email = email;
			this.experience = experience;
			this.skills = skills;
			this.resumeFileName = resumeFileName;
			this.uploadedBy = uploadedBy;
			this.uploadTime = uploadTime;
			this.resumeFile = resumeFile;
			this.downloads = downloads;
			this.jdFileName = jdFileName;
			this.cosineSimilarity = cosineSimilarity;
		}
		

		public ProfilesResponse() {
			
		}

		@Override
		public String toString() {
			return "ProfileResponse [email=" + email + ", experience=" + experience + ", skills=" + skills
					+ ", resumeFileName=" + resumeFileName + ", uploadedBy=" + uploadedBy + ", uploadTime=" + uploadTime
					+ ", resumeFile=" + Arrays.toString(resumeFile) + ", downloads=" + downloads + ", jdFileName="
					+ jdFileName + ", cosineSimilarity=" + cosineSimilarity + "]";
		}
		
}

