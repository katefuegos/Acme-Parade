
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.Position;

@Repository
public interface PositionRepository extends JpaRepository<Position, Integer> {

	//	@Query("select c from Position c where c.name = ?1")
	//	Position findCategoryByName(String Position);

}
