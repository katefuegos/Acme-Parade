
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Enrolment;
import domain.Member;
import domain.Request;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {

	@Query("select m from Member m where m.userAccount.id=?1")
	Member findByUserAccountId(int userAccountId);

	@Query("select m from Member m where ?1 member of m.enrolments")
	Member findByEnrolment(Enrolment enrolment);

	@Query("select r from Request r where r.member.id=?1")
	Collection<Request> findRequestsByMemberId(int memberId);

}
