
package forms;

import javax.persistence.Access;
import javax.persistence.AccessType;

@Access(AccessType.PROPERTY)
public class AreaQueryB1Form {

	// Attributes------------------------------------------------------------------

	private String	name;
	private Double	ratio;
	private Long	count;


	// Constructor------------------------------------------------------------------

	public AreaQueryB1Form() {
		super();
	}

	// Getter and
	// Setters------------------------------------------------------------

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public Double getRatio() {
		return this.ratio;
	}

	public void setRatio(final Double ratio) {
		this.ratio = ratio;
	}

	public Long getCount() {
		return this.count;
	}

	public void setCount(final Long count) {
		this.count = count;
	}

}
