
package repositories;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import domain.Finder;
import domain.Procession;

@Repository
public interface FinderRepository extends JpaRepository<Finder, Integer> {

	@Query("select m.finder from Member m where m.id=?1")
	Finder findByMemberId(int memberId);
	//
	//	@Query("select p from Procession p where (p.ticker like %:keyword% or p.title like %:keyword% or p.description %:keyword%) and (p.brotherhood.area.name like %:nameArea%) and (p.moment BETWEEN :dateMin and :dateMax)")
	//	Page<Procession> searchProcessions(@Param("keyword") String keyword, @Param("nameArea") String nameArea, @Param("dateMin") Date dateMin, @Param("dateMax") Date dateMax, Pageable pageable);

	@Query("select f from Procession f where ((f.ticker like %:keyword% or f.title like %:keyword% or f.description like %:keyword%) and (f.moment BETWEEN :dateMin and :dateMax) and (f.brotherhood.area.name like %:nameArea%)) and f.draftMode=false")
	Page<Procession> searchProcessions(@Param("keyword") String keyword, @Param("dateMin") Date dateMin, @Param("dateMax") Date dateMax, @Param("nameArea") String areaName, Pageable pageable);

}
