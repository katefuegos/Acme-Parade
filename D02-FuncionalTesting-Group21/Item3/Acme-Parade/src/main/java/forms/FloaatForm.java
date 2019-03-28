
package forms;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

@Access(AccessType.PROPERTY)
public class FloaatForm {

	// Identification ---------------------------------------------------------
	// ATRIBUTOS
	private int		id;
	private String	title;
	private String	description;
	private String	pictures;
	private boolean	add;
	private int		paradeId;


	public boolean isAdd() {
		return this.add;
	}

	public void setAdd(final boolean add) {
		this.add = add;
	}

	public int getParadeId() {
		return this.paradeId;
	}

	public void setParadeId(final int paradeId) {
		this.paradeId = paradeId;
	}

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
	public String getPictures() {
		return this.pictures;
	}

	public void setPictures(final String pictures) {
		this.pictures = pictures;
	}

	@NotNull
	public int getId() {
		return this.id;
	}

	public void setId(final int id) {
		this.id = id;
	}
}
