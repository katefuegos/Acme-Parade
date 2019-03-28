package services;

import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.EveryRecordRepository;
import security.LoginService;
import domain.EveryRecord;
import domain.History;
import domain.LegalRecord;
import domain.LinkRecord;
import domain.MiscellaneousRecord;
import domain.PeriodRecord;
import forms.EveryRecordForm;

@Service
@Transactional
public class EveryRecordService {

	// Repository-----------------------------------------------

	@Autowired
	private EveryRecordRepository everyRecordRepository;

	// Services-------------------------------------------------

	@Autowired
	private MiscellaneousRecordService miscellaneousRecordService;

	@Autowired
	private LegalRecordService legalRecordService;

	@Autowired
	private LinkRecordService linkRecordService;

	@Autowired
	private PeriodRecordService periodRecordService;

	// Constructor----------------------------------------------

	public EveryRecordService() {
		super();
	}

	// Simple CRUD----------------------------------------------

	public EveryRecord create(final String authority) {
		final EveryRecord everyRecord = new EveryRecord();
		final History history = new History();
		everyRecord.setHistory(history);
		return everyRecord;
	}

	public List<EveryRecord> findAll() {
		return this.everyRecordRepository.findAll();
	}

	public EveryRecord findOne(final Integer everyRecordId) {
		return this.everyRecordRepository.findOne(everyRecordId);
	}

	public EveryRecord save(final EveryRecord everyRecord) {
		Assert.notNull(everyRecord);
		final EveryRecord saved = this.everyRecordRepository.save(everyRecord);
		return saved;
	}

	public void delete(final EveryRecord everyRecord) {
		this.everyRecordRepository.delete(everyRecord);
	}

	// Other Methods--------------------------------------------
	public void update(final EveryRecordForm everyRecordform) {
		if (LoginService.getPrincipal().getAuthorities()
				.contains("brotherhood")) {
			LegalRecord legalRecord = null;
			LinkRecord linkRecord = null;
			MiscellaneousRecord miscellaneousRecord = null;
			PeriodRecord periodRecord = null;

			if (everyRecordform.getId() != 0) {
				legalRecord = this.legalRecordService.findOne(everyRecordform
						.getId());
				linkRecord = this.linkRecordService.findOne(everyRecordform
						.getId());
				miscellaneousRecord = this.miscellaneousRecordService
						.findOne(everyRecordform.getId());
				legalRecord = this.legalRecordService.findOne(everyRecordform
						.getId());

			} else {
				legalRecord = this.legalRecordService.create();
				linkRecord = this.linkRecordService.create();
				miscellaneousRecord = this.miscellaneousRecordService.create();
				periodRecord = this.periodRecordService.create();
			}
			legalRecord.setId(everyRecordform.getId());
			legalRecord.setTitle(everyRecordform.getTitle());
			legalRecord.setDescription(everyRecordform.getDescription());

			linkRecord.setId(everyRecordform.getId());
			linkRecord.setTitle(everyRecordform.getTitle());
			linkRecord.setDescription(everyRecordform.getDescription());

			miscellaneousRecord.setId(everyRecordform.getId());
			miscellaneousRecord.setTitle(everyRecordform.getTitle());
			miscellaneousRecord
					.setDescription(everyRecordform.getDescription());

			periodRecord.setId(everyRecordform.getId());
			periodRecord.setTitle(everyRecordform.getTitle());
			periodRecord.setDescription(everyRecordform.getDescription());

		}
	}

	public Collection<EveryRecord> findByHistoryId(int historyId) {
		Assert.notNull(historyId);
		return everyRecordRepository.findByHistoryId(historyId);
	}
}
