
package forms;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.validation.constraints.NotNull;

@Access(AccessType.PROPERTY)
public class LegalRecordForm extends EveryRecordForm {

	// Attributes------------------------------------------------------------------

	private int		id;
	private Integer	VATnumber;
	private String	legalName;
	private String	applicableLaws;


	@Override
	@NotNull
	public int getId() {
		return this.id;
	}

	@Override
	public void setId(final int id) {
		this.id = id;
	}

	@NotNull
	public Integer getVATnumber() {
		return this.VATnumber;
	}
	public void setVATnumber(final Integer VATnumber) {
		this.VATnumber = VATnumber;
	}

	@NotNull
	public String getLegalName() {
		return this.legalName;
	}
	public void setLegalName(final String legalName) {
		this.legalName = legalName;
	}

	@NotNull
	public String getApplicableLaws() {
		return this.applicableLaws;
	}
	public void setApplicableLaws(final String applicableLaws) {
		this.applicableLaws = applicableLaws;
	}
}
