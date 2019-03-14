
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.LegalRecordRepository;
import domain.LegalRecord;

@Service
@Transactional
public class LegalRecordService {

	// Repository-----------------------------------------------

	@Autowired
	private LegalRecordRepository	legalRecordRepository;


	// Services-------------------------------------------------

	// Constructor----------------------------------------------

	public LegalRecordService() {
		super();
	}

	// Simple CRUD----------------------------------------------

	public LegalRecord create() {
		final LegalRecord res = new LegalRecord();

		final String title = "";
		final String description = "";

		final String applicableLaws = "";
		final String legalName = "";
		final int VATnumber = 0;

		res.setTitle(title);
		res.setDescription(description);
		res.setApplicableLaws(applicableLaws);
		res.setLegalName(legalName);
		res.setVATnumber(VATnumber);

		return res;
	}

	public Collection<LegalRecord> findAll() {
		Collection<LegalRecord> legalRecords;

		legalRecords = this.legalRecordRepository.findAll();

		return legalRecords;
	}

	public LegalRecord findOne(final Integer legalRecordId) {
		LegalRecord legalRecord;
		legalRecord = this.legalRecordRepository.findOne(legalRecordId);
		return legalRecord;
	}

	public LegalRecord save(final LegalRecord legalRecord) {
		Assert.notNull(legalRecord);
		final LegalRecord saved = this.legalRecordRepository.save(legalRecord);
		return saved;
	}

	public void delete(final LegalRecord legalRecord) {
		Assert.notNull(legalRecord);
		this.legalRecordRepository.delete(legalRecord);
	}

}
