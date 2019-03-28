package forms;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

@Access(AccessType.PROPERTY)
public class RequestForm {

	// Attributes------------------------------------------------------------------

	private int id;
	private Integer roow;
	private Integer coluumn;
	private String reasonReject;

	// Constructor------------------------------------------------------------------

	public RequestForm() {
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

	@Range(min = 1)
	@NotNull
	public Integer getRoow() {
		return this.roow;
	}

	public void setRoow(final Integer roow) {
		this.roow = roow;
	}

	@Range(min = 1)
	@NotNull
	public Integer getColuumn() {
		return this.coluumn;
	}

	public void setColuumn(final Integer coluumn) {
		this.coluumn = coluumn;
	}

	@NotNull
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
