package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Floaat;

@Repository
public interface FloaatRepository extends JpaRepository<Floaat, Integer> {

	@Query("select f from Floaat f where f.brotherhood.id=?1")
	Collection<Floaat> findByBrotherhoodId(int brotherhoodId);
}
