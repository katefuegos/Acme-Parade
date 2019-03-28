package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.SegmentRepository;
import security.LoginService;
import domain.Brotherhood;
import domain.Segment;

@Service
@Transactional
public class SegmentService {

	// Repository-----------------------------------------------

	@Autowired
	private SegmentRepository segmentRepository;

	// Services-------------------------------------------------

	@Autowired
	BrotherhoodService brotherhoodService;

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
		return segment;
	}

	public Segment save(final Segment segment) {
		Assert.notNull(segment);
		Brotherhood b = brotherhoodService.findByUserAccountId(LoginService
				.getPrincipal().getId());
		Assert.notNull(b);
		Assert.isTrue(segment.getPath().getParade().getBrotherhood().equals(b));

		final Segment saved = this.segmentRepository.save(segment);
		return saved;
	}

	public void delete(final Segment segment) {
		Assert.notNull(segment);
		Brotherhood b = brotherhoodService.findByUserAccountId(LoginService
				.getPrincipal().getId());
		Assert.notNull(b);
		Assert.isTrue(segment.getPath().getParade().getBrotherhood().equals(b));

		this.segmentRepository.delete(segment);
	}
	
	public void flush() {
		this.segmentRepository.flush();
	}

	// Other Methods--------------------------------------------

	public Collection<Segment> findByPathId(int pathId) {
		Assert.notNull(pathId);
		return segmentRepository.findByPathId(pathId);
	}
}
