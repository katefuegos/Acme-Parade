
package forms;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

@Access(AccessType.PROPERTY)
public class PositionForm {

	// Constructor ---------------------------------------------------
	public PositionForm() {
		super();
	}


	// Attributes -----------------------------------------------------
	private int		id;
	private String	nameEN;
	private String	nameES;


	@NotNull
	public int getId() {
		return this.id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	@NotBlank
	public String getNameEN() {
		return this.nameEN;
	}

	public void setNameEN(final String nameEN) {
		this.nameEN = nameEN;
	}

	@NotBlank
	public String getNameES() {
		return this.nameES;
	}

	public void setNameES(final String nameES) {
		this.nameES = nameES;
	}

}
