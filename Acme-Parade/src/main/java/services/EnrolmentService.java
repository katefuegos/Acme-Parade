package services;

import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.EnrolmentRepository;
import security.LoginService;
import domain.Enrolment;

@Service
@Transactional
public class EnrolmentService {

	// Repository-----------------------------------------------

	@Autowired
	private EnrolmentRepository enrolmentRepository;

	// Services-------------------------------------------------
	@Autowired
	private BrotherhoodService brotherhoodService;

	// Constructor----------------------------------------------

	public EnrolmentService() {
		super();
	}

	// Simple CRUD----------------------------------------------

	public Enrolment create() {
		final Enrolment res = new Enrolment();
		res.setBrotherhood(this.brotherhoodService
				.findByUserAccountId(LoginService.getPrincipal().getId()));
		res.setAccepted(false);

		return res;
	}

	public Enrolment createFromMember() {
		final Enrolment res = new Enrolment();
		res.setAccepted(false);
		return res;
	}

	public List<Enrolment> findAll() {
		return this.enrolmentRepository.findAll();
	}

	public Enrolment findOne(final Integer enrolmentId) {
		Assert.notNull(enrolmentId);
		return this.enrolmentRepository.findOne(enrolmentId);
	}

	public Enrolment save(final Enrolment enrolment) {
		Assert.notNull(enrolment);
		final Enrolment saved = this.enrolmentRepository.save(enrolment);
		return saved;
	}

	public void delete(final Enrolment enrolment) {
		this.enrolmentRepository.delete(enrolment);
	}

	// Other Methods--------------------------------------------

	public Collection<Enrolment> findByBrotherhoodAndAccepted(
			final int brotherhoodId) {
		Assert.notNull(brotherhoodId);
		return this.enrolmentRepository
				.findByBrotherhoodAndAccepted(brotherhoodId);
	}

	public Collection<Enrolment> findByBrotherhood(final int brotherhoodId) {
		Assert.notNull(brotherhoodId);
		return this.enrolmentRepository.findByBrotherhood(brotherhoodId);
	}

	public Collection<Enrolment> findByPosition(final int positionId) {
		Assert.notNull(positionId);
		return this.enrolmentRepository.findByPosition(positionId);
	}

	public boolean deleteRelationshipPosition(final int positionId) {
		Assert.notNull(positionId);

		final Collection<Enrolment> collection = this
				.findByPosition(positionId);

		if (collection.isEmpty())
			return false;
		else
			return true;

	}

	public Collection<Enrolment> findByMemberIdAccepted(int memberId) {
		Assert.notNull(memberId);
		return this.enrolmentRepository.findByMemberIdAccepted(memberId);
	}

	public Collection<Enrolment> findByMemberIdPending(int memberId) {
		Assert.notNull(memberId);
		return this.enrolmentRepository.findByMemberIdPending(memberId);
	}

	public Collection<Enrolment> findByMemberIdDropOut(int memberId) {
		Assert.notNull(memberId);
		return this.enrolmentRepository.findByMemberIdDropOut(memberId);
	}

	public Enrolment findByMemberIdAndBrotherhoodId(int memberId,
			int brotherhoodId) {
		Assert.notNull(memberId);
		Assert.notNull(brotherhoodId);
		return this.enrolmentRepository.findByMemberIdAndBrotherhoodId(
				memberId, brotherhoodId);
	}
}
