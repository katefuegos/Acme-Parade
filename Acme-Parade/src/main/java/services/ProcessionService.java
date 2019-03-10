package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.ProcessionRepository;
import security.LoginService;
import domain.Floaat;
import domain.Procession;

@Service
@Transactional
public class ProcessionService {

	// Repository-----------------------------------------------

	@Autowired
	private ProcessionRepository processionRepository;

	// Services-------------------------------------------------
	@Autowired
	private BrotherhoodService brotherhoodService;

	// Constructor----------------------------------------------

	public ProcessionService() {
		super();
	}

	// Simple CRUD----------------------------------------------

	public Procession create() {
		final Procession res = new Procession();
		String ticker = generateTicker();
		final Collection<Floaat> floaats = new ArrayList<>();
		res.setTicker(ticker);
		res.setDraftMode(true);
		res.setBrotherhood(brotherhoodService.findByUserAccountId(LoginService
				.getPrincipal().getId()));
		res.setFloats(floaats);
		return res;
	}

	public List<Procession> findAll() {
		return this.processionRepository.findAll();
	}

	public Procession findOne(final Integer processionId) {
		return this.processionRepository.findOne(processionId);
	}

	public Procession save(final Procession procession) {
		Assert.notNull(procession);
		Assert.isTrue(LoginService.getPrincipal().getAuthorities().toString()
				.contains("BROTHERHOOD"),
				"SOLO UN BROTHERHOOD PUEDE CREAR/EDITAR PROCESSION");
		final Procession saved = this.processionRepository.save(procession);
		return saved;
	}

	public void delete(final Procession procession) {
		this.processionRepository.delete(procession);
	}

	// Other Methods--------------------------------------------

	@SuppressWarnings("deprecation")
	public String generateTicker() {
		final Date date = new Date();
		final Integer s1 = date.getDate();
		String day = s1.toString();
		if (day.length() == 1)
			day = "0" + day;
		final Integer s2 = date.getMonth() + 1;
		String month = s2.toString();
		if (month.length() == 1)
			month = "0" + month;
		final Integer s3 = date.getYear();
		final String year = s3.toString().substring(1);

		return year + month + day + "-" + this.generateStringAux();
	}

	private String generateStringAux() {
		final int length = 6;
		final String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		final Random rng = new Random();
		final char[] text = new char[length];
		for (int i = 0; i < 6; i++)
			text[i] = characters.charAt(rng.nextInt(characters.length()));
		return new String(text);
	}

	public Collection<Procession> findByBrotherhoodIdAndNotDraft(
			int brotherhoodId) {
		Assert.notNull(brotherhoodId);
		return processionRepository
				.findByBrotherhoodIdAndNotDraft(brotherhoodId);
	}

	public Collection<Procession> findByBrotherhoodId(int brotherhoodId) {
		Assert.notNull(brotherhoodId);
		return processionRepository.findByBrotherhoodId(brotherhoodId);
	}

	public Collection<Procession> findByFloaat(Floaat floaat) {
		Assert.notNull(floaat);
		return processionRepository.findByFloaat(floaat);
	}
}
