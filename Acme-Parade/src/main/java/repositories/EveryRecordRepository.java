
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.EveryRecord;

@Repository
public interface EveryRecordRepository extends JpaRepository<EveryRecord, Integer> {
	
	@Query("select e from EveryRecord e where e.history.id =?1")
	Collection<EveryRecord> findByHistoryId(int historyId);

}
