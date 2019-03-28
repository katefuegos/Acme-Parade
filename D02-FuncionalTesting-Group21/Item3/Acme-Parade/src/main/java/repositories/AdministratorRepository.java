
package repositories;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import domain.Administrator;
import domain.Brotherhood;
import domain.Member;
import domain.Parade;

@Repository
public interface AdministratorRepository extends JpaRepository<Administrator, Integer> {

	@Query("select h from Administrator h where h.userAccount.id = ?1")
	Administrator findAdministratorByUserAccount(int userAccountId);

	@Query("select a from Actor a where a.userAccount.username=?1")
	Administrator findAdminByUsername(String username);

	///////////
	//DASHBOARD QUERIES
	//////////

	//DASHBOARD C
	//C1 - avg,min,max,stddev of the number of members per brotherhood.
	@Query("select avg(1.0 * (select count(e) from Enrolment e where e.brotherhood.id = b.id)),min(1.0 * (select count(e) from Enrolment e where e.brotherhood.id = b.id)),max(1.0 * (select count(e) from Enrolment e where e.brotherhood.id = b.id)),stddev(1.0 * (select count(e) from Enrolment e where e.brotherhood.id = b.id)) from Brotherhood b")
	Object[] queryC1();

	//C2 - The largest brotherhood, minimum 1
	@Query("select e.brotherhood.id,e.brotherhood.title, count(e) from Enrolment e group by e.brotherhood order by 1 desc")
	Collection<Object[]> queryC2();
	//C3 - The smallest brotherhood, minimum 1
	@Query("select e.brotherhood.id,e.brotherhood.title, count(e) from Enrolment e group by e.brotherhood order by 1")
	Collection<Object[]> queryC3();
	//C4 - The ratio of requests to march in a parade, grouped by their status.
	@Query("select a.status,(count(a)*1.0)/(select count(aa) from Request aa) from Request a group by a.status")
	Collection<Object[]> queryC4();
	//C5 - The parades that are going to be organised in 30 days or less.
	@Query("select p from Parade p where p.moment BETWEEN current_timestamp and :currentDayPlus30Days")
	Collection<Parade> queryC5(@Param("currentDayPlus30Days") Date date);
	//C7 - The listing of members who have got at least 10% the maximum number request to march accepted. 
	@Query("select m from Member m where 0.1<=(select count(q)*1/(select count(qq) from Request qq)	from Request q where q.status = 'APPROVED' and q.member.id=m.id)")
	Collection<Member> queryC7();
	//C8 - A histogram of positions.
	@Query("select e.position, count(e) from Enrolment e group by e.position.id")
	Collection<Object[]> queryC8();
	//DASHBOARD B
	//B1-A
	@Query("select b.area.name,count(b)*1.0/(select count(a) from Area a),count(b) from Brotherhood b group by b.area order by 2 desc")
	Collection<Object[]> queryB1A();
	//	//B1-B
	@Query("select avg(1.0 * (select count(e) from Brotherhood e where e.area.id = b.id)),min(1.0 * (select count(e) from Brotherhood e where e.area.id = b.id)),max(1.0 * (select count(e) from Brotherhood e where e.area.id = b.id)),stddev(1.0 * (select count(e) from Brotherhood e where e.area.id = b.id)) from Area b")
	Object[] queryB1B();
	//B2
	@Query("select avg(1.0*f.parades.size),min(1.0*f.parades.size),max(1.0*f.parades.size),stddev(1.0*f.parades.size) from Finder f")
	Object[] queryB2();
	//	//B3
	@Query("select (select count(f1) from Finder f1 where f1.parades.size = (select count(p) from Parade p))*1.0/count(f) from Finder f")
	Double queryB3Empty();

	@Query("select (select count(f1) from Finder f1 where f1.parades.size != (select count(p) from Parade p))*1.0/count(f) from Finder f")
	Double queryB3NotEmpty();

	// New DASHBOARD C

	// New C1
	@Query("select avg(1.0 * (select count(e) from EveryRecord e where e.history.id = b.id)),min(1.0 * (select count(e) from EveryRecord e where e.history.id = b.id)), max(1.0 * (select count(e) from EveryRecord e where e.history.id = b.id)),stddev(1.0 * (select count(e) from EveryRecord e where e.history.id = b.id)) from History b")
	Object[] queryNewC1();

	// New C2
	@Query("select f.history.brotherhood from EveryRecord f group by f.history having count(f)= (select max(1.0 * (select count(e) from EveryRecord e where e.history.id = b.id)) from History b)")
	Collection<Brotherhood> queryNewC2();

	// New C3
	@Query("select f.history.brotherhood from EveryRecord f group by f.history having count(f)> (select avg(1.0 * (select count(e) from EveryRecord e where e.history.id = b.id)) from History b)")
	Collection<Brotherhood> queryNewC3();

	//NEW DASHBOARD B

	// New B1
	@Query("select (select count(b) from Area b where b.chapter.id=null)/(1.0*count(a)) from Area a")
	Double queryNewB1();
	// New B2
	@Query("select avg(1.0 * (select count(e) from Parade e where e.brotherhood.area.chapter.id = b.id)), min(1.0 * (select count(e) from Parade e where e.brotherhood.area.chapter.id = b.id)), max(1.0 * (select count(e) from Parade e where e.brotherhood.area.chapter.id = b.id)), stddev(1.0 * (select count(e) from Parade e where e.brotherhood.area.chapter.id = b.id)) from Chapter b")
	Object[] queryNewB2();

	// New B3
	@Query("select c from Parade p join p.brotherhood.area.chapter c group by c having count(p)> (select avg(1.0 * (select count(e) from Parade e where e.brotherhood.area.chapter.id = b.id)) from Chapter b)")
	Collection<domain.Chapter> queryNewB3();

	// New B4 - 2 valores [0,1]
	@Query("select (select count(b) from Parade b where b.draftMode=true)/(1.0*count(a)),(select count(c) from Parade c where c.draftMode=false)/(1.0*count(a)) from Parade a")
	Object[] queryNewB4();

	// New B5
	@Query("select p.status, (count(p)*1.0)/(1.0*(select count(r) from Parade r)) from Parade p group by p.status")
	Collection<Object[]> queryNewB5();

}
