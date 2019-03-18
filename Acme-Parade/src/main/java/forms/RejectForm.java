
package forms;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

@Access(AccessType.PROPERTY)
public class RejectForm {

	// Attributes------------------------------------------------------------------

	private int		id;
	private int		version;

	private String	reasonReject;


	// Constructor------------------------------------------------------------------

	public RejectForm() {
		super();
	}

	// Getter and
	// Setters------------------------------------------------------------
	@NotBlank
	public String getReasonReject() {
		return this.reasonReject;
	}

	public void setReasonReject(final String reasonReject) {
		this.reasonReject = reasonReject;
	}

	@NotNull
	public int getId() {
		return this.id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	@NotNull
	public int getVersion() {
		return this.version;
	}

	public void setVersion(final int version) {
		this.version = version;
	}
}
