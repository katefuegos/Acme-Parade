package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.PathRepository;
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
		Assert.notNull(path);

		return path;
	}

	public Path save(final Path path) {
		Assert.notNull(path);
		final Path saved = this.pathRepository.save(path);
		return saved;
	}

	public void delete(final Path path) {
		Assert.notNull(path);
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
}
