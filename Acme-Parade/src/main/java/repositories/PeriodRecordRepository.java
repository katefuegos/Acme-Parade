
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.History;
import domain.PeriodRecord;

@Repository
public interface PeriodRecordRepository extends JpaRepository<PeriodRecord, Integer> {

	@Query("select b from PeriodRecord b where b.history.id = ?1")
	Collection<PeriodRecord> findPeriodRecordByHistoryId(int historyId);

	@Query("select h from History h where h.brotherhood.id = ?1")
	History findHistoryByBrotherhoodId(int brotherhoodId);

}
