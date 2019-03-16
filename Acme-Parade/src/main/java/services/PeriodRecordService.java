
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.PeriodRecordRepository;
import domain.PeriodRecord;

@Service
@Transactional
public class PeriodRecordService {

	// Repository-----------------------------------------------

	@Autowired
	private PeriodRecordRepository	periodRecordRepository;


	// Services-------------------------------------------------

	// Constructor----------------------------------------------

	public PeriodRecordService() {
		super();
	}

	// Simple CRUD----------------------------------------------

	public PeriodRecord create() {
		final PeriodRecord res = new PeriodRecord();

		final String title = "";
		final String description = "";

		final String photos = "";
		final int startYear = 0;
		final int endYear = 0;

		res.setTitle(title);
		res.setDescription(description);

		res.setPhotos(photos);
		res.setStartYear(startYear);
		res.setEndYear(endYear);

		return res;
	}

	public Collection<PeriodRecord> findAll() {
		Collection<PeriodRecord> periodRecords;

		periodRecords = this.periodRecordRepository.findAll();

		return periodRecords;
	}

	public PeriodRecord findOne(final Integer periodRecordId) {
		PeriodRecord periodRecord;
		periodRecord = this.periodRecordRepository.findOne(periodRecordId);
		return periodRecord;
	}

	public PeriodRecord save(final PeriodRecord periodRecord) {
		Assert.notNull(periodRecord);
		final PeriodRecord saved = this.periodRecordRepository.save(periodRecord);
		return saved;
	}

	public void delete(final PeriodRecord periodRecord) {
		Assert.notNull(periodRecord);
		this.periodRecordRepository.delete(periodRecord);
	}

}
