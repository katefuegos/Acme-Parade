package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.PeriodRecordRepository;
import security.LoginService;
import domain.Brotherhood;
import domain.History;
import domain.PeriodRecord;

@Service
@Transactional
public class PeriodRecordService {

	// Repository-----------------------------------------------

	@Autowired
	private PeriodRecordRepository periodRecordRepository;

	// Services-------------------------------------------------

	@Autowired
	private BrotherhoodService brotherhoodService;

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
		Brotherhood b = brotherhoodService.findByUserAccountId(LoginService
				.getPrincipal().getId());
		Assert.isTrue(periodRecord.getHistory().getBrotherhood().equals(b));
		final PeriodRecord saved = this.periodRecordRepository
				.save(periodRecord);
		return saved;
	}

	public void delete(final PeriodRecord periodRecord) {
		Assert.notNull(periodRecord);
		Assert.notNull(periodRecord);
		Brotherhood b = brotherhoodService.findByUserAccountId(LoginService
				.getPrincipal().getId());
		Assert.isTrue(periodRecord.getHistory().getBrotherhood().equals(b));
		this.periodRecordRepository.delete(periodRecord);
	}

	public Collection<PeriodRecord> findPeriodRecordByHistoryId(
			final int historyId) {
		return this.periodRecordRepository
				.findPeriodRecordByHistoryId(historyId);
	}

	public History findHistoryByBrotherhoodId(final int brotherhoodId) {
		return this.periodRecordRepository
				.findHistoryByBrotherhoodId(brotherhoodId);
	}

	public void flush() {
		this.periodRecordRepository.flush();
	}

}
