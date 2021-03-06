
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
public class PeriodRecord extends EveryRecord {

	private Integer	startYear;
	private Integer	endYear;
	private String	photos;


	@NotNull
	public Integer getStartYear() {
		return this.startYear;
	}
	public void setStartYear(final Integer startYear) {
		this.startYear = startYear;
	}
	
	@NotNull
	public Integer getEndYear() {
		return this.endYear;
	}
	public void setEndYear(final Integer endYear) {
		this.endYear = endYear;
	}

	@URL
	public String getPhotos() {
		return this.photos;
	}
	public void setPhotos(final String photos) {
		this.photos = photos;
	}

}
