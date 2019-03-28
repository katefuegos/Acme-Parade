package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Enrolment;

@Repository
public interface EnrolmentRepository extends JpaRepository<Enrolment, Integer> {

	@Query("select e from Enrolment e where e.accepted = true and e.brotherhood.id=?1")
	Collection<Enrolment> findByBrotherhoodAndAccepted(int brotherhoodId);

	@Query("select e from Enrolment e where e.brotherhood.id=?1")
	Collection<Enrolment> findByBrotherhood(int brotherhoodId);

	@Query("select e from Enrolment e where e.position.id = ?1")
	Collection<Enrolment> findByPosition(int positionId);

	@Query("select e from Member m join m.enrolments e where m.id=?1 and e.accepted is true")
	Collection<Enrolment> findByMemberIdAccepted(int memberId);

	@Query("select e from Member m join m.enrolments e where m.id=?1 and e.accepted is false and e.momentDropOut is null")
	Collection<Enrolment> findByMemberIdPending(int memberId);

	@Query("select e from Member m join m.enrolments e where m.id=?1 and e.accepted is false and e.momentDropOut is not null")
	Collection<Enrolment> findByMemberIdDropOut(int memberId);

	@Query("select e from Member m join m.enrolments e where m.id=?1 and e.brotherhood.id=?2")
	Enrolment findByMemberIdAndBrotherhoodId(int memberId, int brotherhoodId);

}
