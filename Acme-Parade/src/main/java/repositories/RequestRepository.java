package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Request;

@Repository
public interface RequestRepository extends JpaRepository<Request, Integer> {
	
	@Query("select r from Request r where r.roow=?1 and r.coluumn=?2 and r.procession.id=?3")
	Request findRequestByPosition(int roow, int coluumn, int processionId);
	
	@Query("select r from Request r where r.procession.id=?1")
	Collection<Request> findRequestByProcessionId(int processionId);
	
	@Query("select r from Request r where r.member.id=?1")
	Collection<Request> findRequestByMemberId(int memberId);

}
