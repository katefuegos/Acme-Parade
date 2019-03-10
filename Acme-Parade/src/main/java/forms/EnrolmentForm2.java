package forms;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import domain.Position;

@Access(AccessType.PROPERTY)
public class EnrolmentForm2 {

	// Attributes------------------------------------------------------------------

	private int id;
	private Position position;

	// Constructor------------------------------------------------------------------

	public EnrolmentForm2() {
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
	
	@Valid
	@NotNull
	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}
}
