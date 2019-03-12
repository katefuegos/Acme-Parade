
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.validation.Valid;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
public class History extends DomainEntity {

	// -------------------ATRIBUTOS---------------------

	private String				title;
	private String				description;
	private Collection<String>	photos;


	@NotBlank
	public String getTitle() {
		return this.title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	@NotBlank
	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	@URL
	public Collection<String> getPhotos() {
		return this.photos;
	}

	public void setPhotos(final Collection<String> photos) {
		this.photos = photos;
	}


	// Relationships ---------------------------------------------------------
	private Brotherhood				brotherhood;
	private Collection<EveryRecord>	everyRecords;


	@Valid
	@OneToOne()
	public Brotherhood getBrotherhood() {
		return this.brotherhood;
	}

	public void setBrotherhood(final Brotherhood brotherhood) {
		this.brotherhood = brotherhood;
	}

	@URL
	public Collection<EveryRecord> getEveryRecords() {
		return this.everyRecords;
	}

	public void setEveryRecords(final Collection<EveryRecord> everyRecords) {
		this.everyRecords = everyRecords;
	}

}
