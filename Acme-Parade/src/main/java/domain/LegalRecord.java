
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;

@Entity
@Access(AccessType.PROPERTY)
public class LegalRecord extends EveryRecord {

	private String				legalName;
	private Integer				VATnumber;
	private Collection<String>	applicableLaws;


	public Integer getVATnumber() {
		return this.VATnumber;
	}
	public void setVATnumber(final Integer VATnumber) {
		this.VATnumber = VATnumber;
	}

	public String getLegalName() {
		return this.legalName;
	}
	public void setLegalName(final String legalName) {
		this.legalName = legalName;
	}

	public Collection<String> getApplicableLaws() {
		return this.applicableLaws;
	}
	public void setApplicableLaws(final Collection<String> applicableLaws) {
		this.applicableLaws = applicableLaws;
	}

}
