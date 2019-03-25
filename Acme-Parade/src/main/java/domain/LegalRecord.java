
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

@Entity
@Access(AccessType.PROPERTY)
public class LegalRecord extends EveryRecord {

	private String	legalName;
	private Integer	VATnumber;
	private String	applicableLaws;


	@Range(min = 0, max = 100)
	public Integer getVATnumber() {
		return this.VATnumber;

	}
	public void setVATnumber(final Integer VATnumber) {
		this.VATnumber = VATnumber;

	}

	@NotBlank
	public String getLegalName() {
		return this.legalName;
	}
	public void setLegalName(final String legalName) {
		this.legalName = legalName;
	}

	@NotBlank
	public String getApplicableLaws() {
		return this.applicableLaws;
	}
	public void setApplicableLaws(final String applicableLaws) {
		this.applicableLaws = applicableLaws;
	}

}
