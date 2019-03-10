
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Actor;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Integer> {

	@Query("select a from Actor a where a.userAccount.username=?1")
	Actor findActorByUsername(String username);

	@Query("select a from Actor a where a.userAccount.id=?1")
	Actor findByUserAccountId(int userAccountId);

	@Query("select a from Actor a where a.isSpammer = true")
	Collection<Actor> findSpammersActors();

	@Query("select m.sender from Message m group by m.sender.id having ((select count(m1) from Message m1 where m1.sender.id = m.sender.id and m1.box.name='spam box')*100.0)/1.0*(select count(m2) from Message m2 where m2.sender.id = m.sender.id ) >=10")
	Collection<Actor> asignSpammersActors();

	@Query("select a from Actor a where a.polarityScore = -1.0")
	Collection<Actor> findActorsNegativePolarity();

	@Query("select a from Actor a where a.isSpammer = true OR a.polarityScore = -1.0")
	Collection<Actor> findPossibleBanned();

}
