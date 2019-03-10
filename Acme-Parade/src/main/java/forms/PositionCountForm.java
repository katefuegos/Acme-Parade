
package forms;

import javax.persistence.Access;
import javax.persistence.AccessType;

import domain.Position;

@Access(AccessType.PROPERTY)
public class PositionCountForm {

	// Constructor ---------------------------------------------------
	public PositionCountForm() {
		super();
	}


	// Attributes -----------------------------------------------------
	private Position	position;
	private Long		count;


	public Position getPosition() {
		return this.position;
	}

	public void setPosition(final Position position) {
		this.position = position;
	}

	public Long getCount() {
		return this.count;
	}

	public void setCount(final Long count) {
		this.count = count;
	}

}
