package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.PathRepository;
import security.LoginService;
import domain.Brotherhood;
import domain.Path;
import domain.Segment;

@Service
@Transactional
public class PathService {

	// Repository-----------------------------------------------

	@Autowired
	private PathRepository pathRepository;

	// Services-------------------------------------------------
	@Autowired
	private SegmentService segmentService;
	
	@Autowired
	private BrotherhoodService brotherhoodService;

	// Constructor----------------------------------------------

	public PathService() {
		super();
	}

	// Simple CRUD----------------------------------------------

	public Path create() {
		final Path res = new Path();
		return res;
	}

	public Collection<Path> findAll() {
		Collection<Path> paths;

		paths = this.pathRepository.findAll();
		Assert.notNull(paths);

		return paths;
	}

	public Path findOne(final Integer pathId) {
		Path path;
		path = this.pathRepository.findOne(pathId);

		return path;
	}

	public Path save(final Path path) {
		Assert.notNull(path);
		Brotherhood b = brotherhoodService.findByUserAccountId(LoginService.getPrincipal().getId());
		Assert.notNull(b);
		Assert.isTrue(path.getParade().getBrotherhood().equals(b));
		final Path saved = this.pathRepository.save(path);
		return saved;
	}

	public void delete(final Path path) {
		Assert.notNull(path);
		Brotherhood b = brotherhoodService.findByUserAccountId(LoginService.getPrincipal().getId());
		Assert.notNull(b);
		Assert.isTrue(path.getParade().getBrotherhood().equals(b));
		
		Collection<Segment> segments = segmentService
				.findByPathId(path.getId());
		if (!segments.isEmpty()) {
			for (Segment s : segments) {
				segmentService.delete(s);
			}
		}
		this.pathRepository.delete(path);
	}

	// Other Methods--------------------------------------------
	
	public Path findByParadeId(int paradeId){
		Assert.notNull(paradeId);
		return pathRepository.findByParadeId(paradeId);
	}
	
	public Collection<Path> findByBrotherhoodId(int brotherhoodId){
		Assert.notNull(brotherhoodId);
		return pathRepository.findByBrotherhoodId(brotherhoodId);
	}
}
