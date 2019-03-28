package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.AreaRepository;
import security.LoginService;
import domain.Area;
import domain.Brotherhood;

@Service
@Transactional
public class AreaService {

	// Repository-----------------------------------------------

	@Autowired
	private AreaRepository areaRepository;

	@Autowired
	private BrotherhoodService brotherhoodService;

	// Services-------------------------------------------------

	// Constructor----------------------------------------------

	public AreaService() {
		super();
	}

	// Simple CRUD----------------------------------------------

	public Area create() {
		Assert.isTrue(LoginService.getPrincipal().getAuthorities().toString()
				.contains("ADMIN"));
		final Area res = new Area();
		return res;
	}

	public Collection<Area> findAll() {
		Collection<Area> areas;

		areas = this.areaRepository.findAll();
		Assert.notNull(areas);

		return areas;
	}

	public Area findOne(final Integer areaId) {
		Area area;
		area = this.areaRepository.findOne(areaId);
		// Assert.notNull(area);

		return area;
	}

	public Area save(final Area area) {
		Assert.notNull(area);
		Assert.isTrue(LoginService.getPrincipal().getAuthorities().toString()
				.contains("ADMIN")
				|| LoginService.getPrincipal().getAuthorities().toString()
						.contains("CHAPTER"));

		final Area saved = this.areaRepository.save(area);
		return saved;
	}

	public void delete(final Area area) {
		Assert.notNull(area);
		Assert.isTrue(LoginService.getPrincipal().getAuthorities().toString()
				.contains("ADMIN"));
		final Collection<Brotherhood> brotherhoods = this.brotherhoodService
				.findByBrotherhood(area.getId());

		Assert.notNull(brotherhoods, "area.error.used");
		Assert.isTrue(brotherhoods.isEmpty(), "area.error.used");

		this.areaRepository.delete(area);
	}

	// Other Methods--------------------------------------------
	public void flush() {
		this.areaRepository.flush();

	}

	public Collection<domain.Area> findAreasNotAssigned() {
		final Collection<domain.Area> areas = this.areaRepository
				.findAreasNotAssigned();

		return areas;
	}

}
