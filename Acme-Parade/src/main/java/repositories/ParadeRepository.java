package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Floaat;
import domain.Parade;

@Repository
public interface ParadeRepository extends JpaRepository<Parade, Integer> {
	
	@Query("select p from Parade p where p.draftMode = false and p.brotherhood.id =?1")
	Collection<Parade> findByBrotherhoodIdAndNotDraft(int brotherhoodId);
	
	@Query("select p from Parade p where p.brotherhood.id =?1")
	Collection<Parade> findByBrotherhoodId(int brotherhoodId);
	
	@Query("select p from Parade p where ?1 member of p.floats")
	Collection<Parade> findByFloaat(Floaat floaat);


}
