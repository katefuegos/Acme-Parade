
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {
	@Index(name = "ID1Request", columnList = "roow,coluumn,procession")

})
public class Request extends DomainEntity {

	// Identification ---------------------------------------------------------
	// ATRIBUTOS
	private String	status;
	private Integer	roow;
	private Integer	coluumn;
	private String	reasonReject;


	@NotBlank
	@Pattern(regexp = "^(PENDING|APPROVED|REJECTED)$")
	public String getStatus() {
		return this.status;
	}

	public void setStatus(final String status) {
		this.status = status;
	}

	@Range(min = 1)
	public Integer getRoow() {
		return this.roow;
	}

	public void setRoow(final Integer roow) {
		this.roow = roow;
	}
	@Range(min = 1)
	public Integer getColuumn() {
		return this.coluumn;
	}

	public void setColuumn(final Integer coluumn) {
		this.coluumn = coluumn;
	}

	public String getReasonReject() {
		return this.reasonReject;
	}

	public void setReasonReject(final String reasonReject) {
		this.reasonReject = reasonReject;
	}


	// Relationships ---------------------------------------------------------
	private Member		member;
	private Procession	procession;


	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public Member getMember() {
		return this.member;
	}

	public void setMember(final Member member) {
		this.member = member;
	}

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public Procession getProcession() {
		return this.procession;
	}

	public void setProcession(final Procession procession) {
		this.procession = procession;
	}
}
