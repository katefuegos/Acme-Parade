package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.SegmentRepository;
import domain.Segment;

@Service
@Transactional
public class SegmentService {

	// Repository-----------------------------------------------

	@Autowired
	private SegmentRepository segmentRepository;

	// Services-------------------------------------------------

	// Constructor----------------------------------------------

	public SegmentService() {
		super();
	}

	// Simple CRUD----------------------------------------------

	public Segment create() {
		final Segment res = new Segment();
		return res;
	}

	public Collection<Segment> findAll() {
		Collection<Segment> segments;

		segments = this.segmentRepository.findAll();
		Assert.notNull(segments);

		return segments;
	}

	public Segment findOne(final Integer segmentId) {
		Segment segment;
		segment = this.segmentRepository.findOne(segmentId);
		Assert.notNull(segment);

		return segment;
	}

	public Segment save(final Segment segment) {
		Assert.notNull(segment);
		final Segment saved = this.segmentRepository.save(segment);
		return saved;
	}

	public void delete(final Segment segment) {
		Assert.notNull(segment);

		this.segmentRepository.delete(segment);
	}

	// Other Methods--------------------------------------------

	public Collection<Segment> findByPathId(int pathId) {
		Assert.notNull(pathId);
		return segmentRepository.findByPathId(pathId);
	}
}
