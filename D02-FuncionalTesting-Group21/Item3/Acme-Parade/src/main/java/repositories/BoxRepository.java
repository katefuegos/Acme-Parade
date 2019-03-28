
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Box;

@Repository
public interface BoxRepository extends JpaRepository<Box, Integer> {

	@Query("select b from Box b where b.isSystem=true and b.actor.id=?1")
	Collection<Box> findSystemBoxByActorId(int actorId);

	@Query("select b from Box b where b.name=?2 and   b.actor.id=?1")
	Box findBoxByActorAndName(int actorId, String name);

	@Query("select b from Box b where b.actor.id=?1")
	Collection<Box> findBoxByActor(int actorId);

}
