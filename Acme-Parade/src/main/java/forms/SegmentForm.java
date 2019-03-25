package forms;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import domain.Path;

@Access(AccessType.PROPERTY)
public class SegmentForm {

	// ATRIBUTOS
	private Double originLatitude;
	private Double originLongitude;
	private Double destinationLatitude;
	private Double destinationLongitude;
	private Double approximateTimeOri;
	private Double approximateTimeDes;
	private Path path;
	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@NotNull
	@Valid
	public Path getPath() {
		return path;
	}

	public void setPath(Path path) {
		this.path = path;
	}

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

}
