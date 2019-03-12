
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;

@Entity
@Access(AccessType.PROPERTY)
public class PeriodRecord extends EveryRecord {

	private Integer	startYear;
	private Integer	endYear;


	public Integer getStartYear() {
		return this.startYear;
	}
	public void setStartYear(final Integer startYear) {
		this.startYear = startYear;
	}

	public Integer getEndYear() {
		return this.endYear;
	}
	public void setEndYear(final Integer endYear) {
		this.endYear = endYear;
	}

}
