
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Chapter;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, Integer> {

	@Query("select a from Chapter a where a.userAccount.username=?1")
	Chapter findActorByUsername(String username);

	@Query("select a from Chapter a where a.userAccount.id=?1")
	Chapter findByUserAccountId(int userAccountId);

}
