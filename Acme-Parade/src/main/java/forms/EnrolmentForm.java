package forms;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import domain.Enrolment;
import domain.Member;

@Access(AccessType.PROPERTY)
public class EnrolmentForm {

	// Attributes------------------------------------------------------------------

	private Enrolment enrolment;
	private Member member;

	// Constructor------------------------------------------------------------------

	public EnrolmentForm(Enrolment enrolment, Member member) {
		super();
		this.enrolment = enrolment;
		this.member = member;
	}

	// Getter and
	// Setters------------------------------------------------------------

	@Valid
	@NotNull
	public Enrolment getEnrolment() {
		return enrolment;
	}

	public void setEnrolment(Enrolment enrolment) {
		this.enrolment = enrolment;
	}

	@Valid
	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

}
