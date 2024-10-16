package churchDetailsFetcher.types;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class HunterData {
	private Data data;
	private Meta meta;

	// Getters and Setters
	@JsonProperty("data")
	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	@JsonProperty("meta")
	public Meta getMeta() {
		return meta;
	}

	public void setMeta(Meta meta) {
		this.meta = meta;
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Data {
		private String domain;
		private boolean disposable;
		private boolean webmail;
		private boolean acceptAll;
		private String pattern;
		private String organization;
		private String description;
		private String industry;
		private String twitter;
		private String facebook;
		private String linkedin;
		private String instagram;
		private String youtube;
		private List<String> technologies;
		private String country;
		private String state;
		private String city;
		private String postalCode;
		private String street;
		private String headcount;
		private String companyType;
		private List<Email> emails;
		private List<String> linkedDomains;

		// Getters and Setters
		@JsonProperty("domain")
		public String getDomain() {
			return domain;
		}

		public void setDomain(String domain) {
			this.domain = domain;
		}

		@JsonProperty("disposable")
		public boolean isDisposable() {
			return disposable;
		}

		public void setDisposable(boolean disposable) {
			this.disposable = disposable;
		}

		@JsonProperty("webmail")
		public boolean isWebmail() {
			return webmail;
		}

		public void setWebmail(boolean webmail) {
			this.webmail = webmail;
		}

		@JsonProperty("accept_all")
		public boolean isAcceptAll() {
			return acceptAll;
		}

		public void setAcceptAll(boolean acceptAll) {
			this.acceptAll = acceptAll;
		}

		@JsonProperty("pattern")
		public String getPattern() {
			return pattern;
		}

		public void setPattern(String pattern) {
			this.pattern = pattern;
		}

		@JsonProperty("organization")
		public String getOrganization() {
			return organization;
		}

		public void setOrganization(String organization) {
			this.organization = organization;
		}

		@JsonProperty("description")
		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		@JsonProperty("industry")
		public String getIndustry() {
			return industry;
		}

		public void setIndustry(String industry) {
			this.industry = industry;
		}

		@JsonProperty("twitter")
		public String getTwitter() {
			return twitter;
		}

		public void setTwitter(String twitter) {
			this.twitter = twitter;
		}

		@JsonProperty("facebook")
		public String getFacebook() {
			return facebook;
		}

		public void setFacebook(String facebook) {
			this.facebook = facebook;
		}

		@JsonProperty("linkedin")
		public String getLinkedin() {
			return linkedin;
		}

		public void setLinkedin(String linkedin) {
			this.linkedin = linkedin;
		}

		@JsonProperty("instagram")
		public String getInstagram() {
			return instagram;
		}

		public void setInstagram(String instagram) {
			this.instagram = instagram;
		}

		@JsonProperty("youtube")
		public String getYoutube() {
			return youtube;
		}

		public void setYoutube(String youtube) {
			this.youtube = youtube;
		}

		@JsonProperty("technologies")
		public List<String> getTechnologies() {
			return technologies;
		}

		public void setTechnologies(List<String> technologies) {
			this.technologies = technologies;
		}

		@JsonProperty("country")
		public String getCountry() {
			return country;
		}

		public void setCountry(String country) {
			this.country = country;
		}

		@JsonProperty("state")
		public String getState() {
			return state;
		}

		public void setState(String state) {
			this.state = state;
		}

		@JsonProperty("city")
		public String getCity() {
			return city;
		}

		public void setCity(String city) {
			this.city = city;
		}

		@JsonProperty("postal_code")
		public String getPostalCode() {
			return postalCode;
		}

		public void setPostalCode(String postalCode) {
			this.postalCode = postalCode;
		}

		@JsonProperty("street")
		public String getStreet() {
			return street;
		}

		public void setStreet(String street) {
			this.street = street;
		}

		@JsonProperty("headcount")
		public String getHeadcount() {
			return headcount;
		}

		public void setHeadcount(String headcount) {
			this.headcount = headcount;
		}

		@JsonProperty("company_type")
		public String getCompanyType() {
			return companyType;
		}

		public void setCompanyType(String companyType) {
			this.companyType = companyType;
		}

		@JsonProperty("emails")
		public List<Email> getEmails() {
			return emails;
		}

		public void setEmails(List<Email> emails) {
			this.emails = emails;
		}

		@JsonProperty("linked_domains")
		public List<String> getLinkedDomains() {
			return linkedDomains;
		}

		public void setLinkedDomains(List<String> linkedDomains) {
			this.linkedDomains = linkedDomains;
		}
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Email {
		private String value;
		private String type;
		private int confidence;
		private List<Source> sources;
		private String firstName;
		private String lastName;
		private String position;
		private String seniority;
		private String department;
		private String linkedin;
		private String twitter;
		private String phoneNumber;
		private Verification verification;

		// Getters and Setters
		@JsonProperty("value")
		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		@JsonProperty("type")
		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		@JsonProperty("confidence")
		public int getConfidence() {
			return confidence;
		}

		public void setConfidence(int confidence) {
			this.confidence = confidence;
		}

		@JsonProperty("sources")
		public List<Source> getSources() {
			return sources;
		}

		public void setSources(List<Source> sources) {
			this.sources = sources;
		}

		@JsonProperty("first_name")
		public String getFirstName() {
			return firstName;
		}

		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}

		@JsonProperty("last_name")
		public String getLastName() {
			return lastName;
		}

		public void setLastName(String lastName) {
			this.lastName = lastName;
		}

		@JsonProperty("position")
		public String getPosition() {
			return position;
		}

		public void setPosition(String position) {
			this.position = position;
		}

		@JsonProperty("seniority")
		public String getSeniority() {
			return seniority;
		}

		public void setSeniority(String seniority) {
			this.seniority = seniority;
		}

		@JsonProperty("department")
		public String getDepartment() {
			return department;
		}

		public void setDepartment(String department) {
			this.department = department;
		}

		@JsonProperty("linkedin")
		public String getLinkedin() {
			return linkedin;
		}

		public void setLinkedin(String linkedin) {
			this.linkedin = linkedin;
		}

		@JsonProperty("twitter")
		public String getTwitter() {
			return twitter;
		}

		public void setTwitter(String twitter) {
			this.twitter = twitter;
		}

		@JsonProperty("phone_number")
		public String getPhoneNumber() {
			return phoneNumber;
		}

		public void setPhoneNumber(String phoneNumber) {
			this.phoneNumber = phoneNumber;
		}

		@JsonProperty("verification")
		public Verification getVerification() {
			return verification;
		}

		public void setVerification(Verification verification) {
			this.verification = verification;
		}
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Source {
		private String domain;
		private String uri;
		private String extractedOn;
		private String lastSeenOn;
		private boolean stillOnPage;

		// Getters and Setters
		@JsonProperty("domain")
		public String getDomain() {
			return domain;
		}

		public void setDomain(String domain) {
			this.domain = domain;
		}

		@JsonProperty("uri")
		public String getUri() {
			return uri;
		}

		public void setUri(String uri) {
			this.uri = uri;
		}

		@JsonProperty("extracted_on")
		public String getExtractedOn() {
			return extractedOn;
		}

		public void setExtractedOn(String extractedOn) {
			this.extractedOn = extractedOn;
		}

		@JsonProperty("last_seen_on")
		public String getLastSeenOn() {
			return lastSeenOn;
		}

		public void setLastSeenOn(String lastSeenOn) {
			this.lastSeenOn = lastSeenOn;
		}

		@JsonProperty("still_on_page")
		public boolean isStillOnPage() {
			return stillOnPage;
		}

		public void setStillOnPage(boolean stillOnPage) {
			this.stillOnPage = stillOnPage;
		}
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Verification {
		private boolean valid;
		private String reason;
		private boolean disposable;
		private boolean role;
		private String date;

		// Getters and Setters
		@JsonProperty("valid")
		public boolean isValid() {
			return valid;
		}

		public void setValid(boolean valid) {
			this.valid = valid;
		}

		@JsonProperty("reason")
		public String getReason() {
			return reason;
		}

		public void setReason(String reason) {
			this.reason = reason;
		}

		@JsonProperty("disposable")
		public boolean isDisposable() {
			return disposable;
		}

		public void setDisposable(boolean disposable) {
			this.disposable = disposable;
		}

		@JsonProperty("role")
		public boolean isRole() {
			return role;
		}

		public void setRole(boolean role) {
			this.role = role;
		}

		public String getDate() {
			return date;
		}

		public void setDate(String date) {
			this.date = date;
		}
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Meta {
		private String requestId;
		private String provider;
		private int count;
		private boolean success;

		// Getters and Setters
		@JsonProperty("request_id")
		public String getRequestId() {
			return requestId;
		}

		public void setRequestId(String requestId) {
			this.requestId = requestId;
		}

		@JsonProperty("provider")
		public String getProvider() {
			return provider;
		}

		public void setProvider(String provider) {
			this.provider = provider;
		}

		@JsonProperty("count")
		public int getCount() {
			return count;
		}

		public void setCount(int count) {
			this.count = count;
		}

		@JsonProperty("success")
		public boolean isSuccess() {
			return success;
		}

		public void setSuccess(boolean success) {
			this.success = success;
		}
	}
}