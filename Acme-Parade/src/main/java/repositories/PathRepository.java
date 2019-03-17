
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Path;

@Repository
public interface PathRepository extends JpaRepository<Path, Integer> {
	
	@Query("select p from Path p where p.parade.id=?1")
	Path findByParadeId(int paradeId);
}
