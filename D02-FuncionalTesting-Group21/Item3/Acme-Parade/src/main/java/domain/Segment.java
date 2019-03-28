package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Entity
@Access(AccessType.PROPERTY)
public class Segment extends DomainEntity {

	// ATRIBUTOS
	private Double originLatitude;
	private Double originLongitude;
	private Double destinationLatitude;
	private Double destinationLongitude;
	private Double approximateTimeOri;
	private Double approximateTimeDes;

	@NotNull
	public Double getOriginLatitude() {
		return originLatitude;
	}

	public void setOriginLatitude(Double originLatitude) {
		this.originLatitude = originLatitude;
	}

	@NotNull
	public Double getOriginLongitude() {
		return originLongitude;
	}

	public void setOriginLongitude(Double originLongitude) {
		this.originLongitude = originLongitude;
	}

	@NotNull
	public Double getDestinationLatitude() {
		return destinationLatitude;
	}

	public void setDestinationLatitude(Double destinationLatitude) {
		this.destinationLatitude = destinationLatitude;
	}

	@NotNull
	public Double getDestinationLongitude() {
		return destinationLongitude;
	}

	public void setDestinationLongitude(Double destinationLongitude) {
		this.destinationLongitude = destinationLongitude;
	}

	@NotNull
	public Double getApproximateTimeOri() {
		return approximateTimeOri;
	}

	public void setApproximateTimeOri(Double approximateTimeOri) {
		this.approximateTimeOri = approximateTimeOri;
	}

	@NotNull
	public Double getApproximateTimeDes() {
		return approximateTimeDes;
	}

	public void setApproximateTimeDes(Double approximateTimeDes) {
		this.approximateTimeDes = approximateTimeDes;
	}

	// Relationships ---------------------------------------------------------

	private Path path;

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public Path getPath() {
		return path;
	}

	public void setPath(Path path) {
		this.path = path;
	}

}
