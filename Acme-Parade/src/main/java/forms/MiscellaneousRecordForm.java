
package forms;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.validation.constraints.NotNull;

@Access(AccessType.PROPERTY)
public class MiscellaneousRecordForm extends EveryRecordForm {

	// Attributes------------------------------------------------------------------

	private int	id;


	@Override
	@NotNull
	public int getId() {
		return this.id;
	}

	@Override
	public void setId(final int id) {
		this.id = id;
	}
}
