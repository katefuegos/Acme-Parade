
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.validation.Valid;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
public class EveryRecord extends DomainEntity {

	private String	title;
	private String	description;


	@NotBlank
	public String getTitle() {
		return this.title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	@NotBlank
	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}


	// Relationships ---------------------------------------------------------
	private History				history;
	private PeriodRecord		periodRecord;
	private MiscellaneousRecord	miscellaneousRecord;
	private LegalRecord			legalRecord;
	private LinkRecord			linkRecord;


	@Valid
	@OneToOne()
	public History getHistory() {
		return this.history;
	}

	public void setHistory(final History history) {
		this.history = history;
	}

	@Valid
	@OneToOne()
	public PeriodRecord getPeriodRecord() {
		return this.periodRecord;
	}

	public void setPeriodRecord(final PeriodRecord periodRecord) {
		this.periodRecord = periodRecord;
	}

	@Valid
	@OneToOne()
	public MiscellaneousRecord getMiscellaneousRecord() {
		return this.miscellaneousRecord;
	}

	public void setMiscellaneousRecord(final MiscellaneousRecord miscellaneousRecord) {
		this.miscellaneousRecord = miscellaneousRecord;
	}

	@Valid
	@OneToOne()
	public LegalRecord getLegalRecord() {
		return this.legalRecord;
	}

	public void setLegalRecord(final LegalRecord legalRecord) {
		this.legalRecord = legalRecord;
	}

	@Valid
	@OneToOne()
	public LinkRecord getLinkRecord() {
		return this.linkRecord;
	}

	public void setLinkRecord(final LinkRecord linkRecord) {
		this.linkRecord = linkRecord;
	}
}
