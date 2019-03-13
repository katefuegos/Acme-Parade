
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;

import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
public class InceptionRecord extends EveryRecord {

	private String	photos;


	@URL
	public String getPhotos() {
		return this.photos;
	}
	public void setPhotos(final String photos) {
		this.photos = photos;
	}
}
