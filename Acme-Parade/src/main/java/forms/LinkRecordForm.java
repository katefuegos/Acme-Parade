package forms;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import domain.Brotherhood;

@Access(AccessType.PROPERTY)
public class LinkRecordForm extends EveryRecordForm {

	// Attributes------------------------------------------------------------------

	private Brotherhood brotherhood;

	@Valid
	@NotNull
	public Brotherhood getBrotherhood() {
		return brotherhood;
	}

	public void setBrotherhood(Brotherhood brotherhood) {
		this.brotherhood = brotherhood;
	}

}
