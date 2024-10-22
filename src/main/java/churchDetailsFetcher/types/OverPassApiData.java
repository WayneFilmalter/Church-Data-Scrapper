package churchDetailsFetcher.types;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import churchDetailsFetcher.types.OverPassApiData.Osm3s;

//@JsonIgnoreProperties(ignoreUnknown = true)
public class OverPassApiData {
	private double version;
	private String generator;
	private Osm3s osm3s;
	private List<Element> elements;

	// Getters and Setters
	public double getVersion() {
		return version;
	}

	public void setVersion(double version) {
		this.version = version;
	}

	public String getGenerator() {
		return generator;
	}

	public void setGenerator(String generator) {
		this.generator = generator;
	}

	public Osm3s getOsm3s() {
		return osm3s;
	}

	public void setOsm3s(Osm3s osm3s) {
		this.osm3s = osm3s;
	}

	public List<Element> getElements() {
		return elements;
	}

	public void setElements(List<Element> elements) {
		this.elements = elements;
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Osm3s {
		@JsonProperty("timestamp_osm_base")
		private String timestampOsmBase;

		@JsonProperty("copyright")
		private String copyright;

		// Getters and Setters
		public String getTimestampOsmBase() {
			return timestampOsmBase;
		}

		public void setTimestampOsmBase(String timestampOsmBase) {
			this.timestampOsmBase = timestampOsmBase;
		}

		public String getCopyright() {
			return copyright;
		}

		public void setCopyright(String copyright) {
			this.copyright = copyright;
		}
	}

	public static class Element {
		private String type;
		private long id;
		private double lat;
		private double lon;
		private Tags tags;

		// Getters and Setters
		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

		public double getLat() {
			return lat;
		}

		public void setLat(double lat) {
			this.lat = lat;
		}

		public double getLon() {
			return lon;
		}

		public void setLon(double lon) {
			this.lon = lon;
		}

		public Tags getTags() {
			return tags;
		}

		public void setTags(Tags tags) {
			this.tags = tags;
		}

		@JsonIgnoreProperties(ignoreUnknown = true)
		@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
		public static class Tags {
			@JsonProperty("amenity")
			private String amenity;

			@JsonProperty("denomination")
			private String denomination;

			@JsonProperty("gnis:feature_id")
			private String gnisFeatureId;

			@JsonProperty("name")
			private String name;

			@JsonProperty("religion")
			private String religion;

			@JsonProperty("place")
			private String place;

			@JsonProperty("wikidata")
			private String wikidata;

			@JsonProperty("ele") // Optional field for elevation
			private String ele;

			@JsonProperty("check_date") // Add this line to capture check_date
			private String checkDate;

			@JsonProperty("addr:city")
			private String addrCity; // This line specifies the mapping for addr:city

			@JsonProperty("addr:housenumber") // New field added
			private String addrHousenumber; // For addr:housenumber

			@JsonProperty("addr:postcode") // New field for addr:postcode
			private String addrPostcode; // For addr:postcode

			@JsonProperty("addr:state") // New field for addr:postcode
			private String addrState; // For addr:postcode

			@JsonProperty("addr:street") // New field for addr:postcode
			private String addrStreet; // For addr:postcode

			@JsonProperty("phone") // New field for addr:postcode
			private String phone; // For addr:postcode

			@JsonProperty("website") // New field for addr:postcode
			private String website; // For addr:postcode

			@JsonProperty("created_by") // New field for addr:postcode
			private String createdBy; // For addr:postcode

			@JsonProperty("fixme") // New field for addr:postcode
			private String fixme; // For addr:postcode

			@JsonProperty("wheelchair") // New field for addr:postcode
			private String wheelchair; // For addr:postcode

			@JsonProperty("source") // New field for addr:postcode
			private String source; // For addr:postcode

			@JsonProperty("source_ref") // New field for addr:postcode
			private String sourceRef; // For addr:postcode

			@JsonProperty("alt_name") // New field for addr:postcode
			private String altName; // For addr:postcode

			@JsonProperty("name:en")
			private String nameEn;

			// Getters and Setters
			public String getAmenity() {
				return amenity;
			}

			public void setAmenity(String amenity) {
				this.amenity = amenity;
			}

			public String getDenomination() {
				return denomination;
			}

			public void setDenomination(String denomination) {
				this.denomination = denomination;
			}

			public String getGnisFeatureId() {
				return gnisFeatureId;
			}

			public void setGnisFeatureId(String gnisFeatureId) {
				this.gnisFeatureId = gnisFeatureId;
			}

			public String getName() {
				return name;
			}

			public void setName(String name) {
				this.name = name;
			}

			public String getReligion() {
				return religion;
			}

			public void setReligion(String religion) {
				this.religion = religion;
			}

			public String getWikidata() {
				return wikidata;
			}

			public void setWikidata(String wikidata) {
				this.wikidata = wikidata;
			}

			public String getEle() {
				return ele;
			}

			public void setEle(String ele) {
				this.ele = ele;
			}

			public String getCheckDate() {
				return checkDate;
			}

			public void setCheckDate(String checkDate) {
				this.checkDate = checkDate;
			}

			public String getAddrCity() {
				return addrCity;
			}

			public void setAddrCity(String addrCity) {
				this.addrCity = addrCity;
			}

			public String getAddrHousenumber() {
				return addrHousenumber;
			}

			public void setAddrHousenumber(String addrHousenumber) {
				this.addrHousenumber = addrHousenumber;
			}

			public String getAddrPostcode() {
				return addrPostcode; // Getter for addr:postcode
			}

			public void setAddrPostcode(String addrPostcode) {
				this.addrPostcode = addrPostcode; // Setter for addr:postcode
			}

			public String getAddrState() {
				return addrState; // Getter for addr:postcode
			}

			public void setAddrState(String addrState) {
				this.addrState = addrState; // Setter for addr:postcode
			}

			public String getAddrStreet() {
				return addrState; // Getter for addr:postcode
			}

			public void setAddrStreet(String addrStreet) {
				this.addrStreet = addrStreet; // Setter for addr:postcode
			}

			public String getPhone() {
				return phone; // Getter for addr:postcode
			}

			public void setPhone(String phone) {
				this.phone = phone; // Setter for addr:postcode
			}

			public String getWebsite() {
				return website; // Getter for addr:postcode
			}

			public void setWebsite(String website) {
				this.website = website; // Setter for addr:postcode
			}

			public String getCreatedBy() {
				return createdBy; // Getter for addr:postcode
			}

			public void setCreatedBy(String createdBy) {
				this.createdBy = createdBy; // Setter for addr:postcode
			}

			public String getFixMe() {
				return fixme; // Correct getter for fixme
			}

			public void setFixMe(String fixme) {
				this.fixme = fixme; // Correct setter for fixme
			}

			public String getWheelChair() {
				return wheelchair; // Correct getter for fixme
			}

			public void setWheelChair(String wheelchair) {
				this.wheelchair = wheelchair; // Correct setter for fixme
			}

			public String getsource() {
				return source; // Correct getter for fixme
			}

			public void setSource(String source) {
				this.source = source; // Correct setter for fixme
			}

			public String getsourceRef() {
				return sourceRef; // Correct getter for fixme
			}

			public void setSourceRef(String sourceRef) {
				this.sourceRef = sourceRef; // Correct setter for fixme
			}

			public String getAltName() {
				return altName; // Correct getter for fixme
			}

			public void setAltName(String altName) {
				this.altName = altName; // Correct setter for fixme
			}

			public String getNameEN() {
				return nameEn; // Correct getter for fixme
			}

			public void setNameEN(String nameEn) {
				this.nameEn = nameEn; // Correct setter for fixme
			}

			public String getPlace() {
				return place;
			}

			public void setPlace(String place) {
				this.place = place;
			}

		}
	}
}