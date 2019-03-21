
package forms;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.URL;

@Access(AccessType.PROPERTY)
public class PeriodRecordForm extends EveryRecordForm {

	// Attributes------------------------------------------------------------------

	private int		id;
	private Integer	startYear;
	private Integer	endYear;
	private String	photos;


	@Override
	@NotNull
	public int getId() {
		return this.id;
	}

	@Override
	public void setId(final int id) {
		this.id = id;
	}

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
