
package forms;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.URL;

@Access(AccessType.PROPERTY)
public class InceptionRecordForm extends EveryRecordForm {

	// Attributes------------------------------------------------------------------

	private int		id;
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

	@URL
	public String getPhotos() {
		return this.photos;
	}
	public void setPhotos(final String photos) {
		this.photos = photos;
	}
}
