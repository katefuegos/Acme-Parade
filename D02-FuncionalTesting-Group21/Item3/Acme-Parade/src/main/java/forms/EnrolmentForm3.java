package forms;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.validation.constraints.NotNull;

@Access(AccessType.PROPERTY)
public class EnrolmentForm3 {

	// Attributes------------------------------------------------------------------

	private int id;

	// Constructor------------------------------------------------------------------

	public EnrolmentForm3() {
		super();
	}

	// Getter and
	// Setters------------------------------------------------------------
	@NotNull
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
