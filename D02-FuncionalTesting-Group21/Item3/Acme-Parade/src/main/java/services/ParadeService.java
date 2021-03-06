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

import repositories.ParadeRepository;
import security.LoginService;
import domain.Brotherhood;
import domain.Floaat;
import domain.Parade;
import domain.Path;
import domain.Segment;

@Service
@Transactional
public class ParadeService {

	// Repository-----------------------------------------------

	@Autowired
	private ParadeRepository paradeRepository;

	// Services-------------------------------------------------
	@Autowired
	private BrotherhoodService brotherhoodService;

	@Autowired
	private ChapterService chapterService;

	@Autowired
	private SegmentService segmentService;

	@Autowired
	private PathService pathService;

	// Constructor----------------------------------------------

	public ParadeService() {
		super();
	}

	// Simple CRUD----------------------------------------------

	public Parade create() {
		final Parade res = new Parade();
		final String ticker = this.generateTicker();
		final Collection<Floaat> floaats = new ArrayList<>();
		res.setTicker(ticker);
		res.setDraftMode(true);
		res.setBrotherhood(this.brotherhoodService
				.findByUserAccountId(LoginService.getPrincipal().getId()));
		res.setFloats(floaats);
		res.setStatus("PENDING");
		// System.out.println(ticker);
		return res;
	}

	public List<Parade> findAll() {
		return this.paradeRepository.findAll();
	}

	public Parade findOne(final Integer paradeId) {
		return this.paradeRepository.findOne(paradeId);
	}

	public Parade save(final Parade parade) {
		Assert.notNull(parade);
		Assert.isTrue(LoginService.getPrincipal().getAuthorities().toString()
				.contains("BROTHERHOOD"),
				"SOLO UN BROTHERHOOD PUEDE CREAR/EDITAR PARADE");
		Assert.isTrue(parade.getBrotherhood().equals(
				this.brotherhoodService.findByUserAccountId(LoginService
						.getPrincipal().getId())));

		if (parade.isDraftMode() == false)
			parade.setStatus("SUBMITTED");
		final Parade saved = this.paradeRepository.save(parade);

		return saved;
	}

	public void delete(final Parade parade) {
		Assert.isTrue(LoginService.getPrincipal().getAuthorities().toString()
				.contains("BROTHERHOOD"),
				"SOLO UN BROTHERHOOD PUEDE CREAR/EDITAR PARADE");

		this.paradeRepository.delete(parade);
	}

	// Other Methods--------------------------------------------
	public void copy(final Parade parade) {
		Assert.notNull(parade);
		final Brotherhood b = this.brotherhoodService
				.findByUserAccountId(LoginService.getPrincipal().getId());
		Assert.notNull(b);
		Assert.isTrue(parade.getBrotherhood()
				.equals(b));
		
		Parade copy = this.create();
		copy.setDescription(parade.getDescription());
		copy.getFloats().addAll(parade.getFloats());
		copy.setMoment(parade.getMoment());
		copy.setTitle(parade.getTitle());

		copy = this.save(copy);

		final Path pathOriginal = this.pathService.findByParadeId(parade
				.getId());
		if (pathOriginal != null) {
			Path copyPath = this.pathService.create();
			copyPath.setParade(copy);
			copyPath = pathService.save(copyPath);

			Collection<Segment> segmentsOriginal = segmentService
					.findByPathId(pathOriginal.getId());
			if (!segmentsOriginal.isEmpty()) {
				for (Segment s : segmentsOriginal) {
					Segment copySegment = segmentService.create();
					copySegment
							.setApproximateTimeDes(s.getApproximateTimeDes());
					copySegment
							.setApproximateTimeOri(s.getApproximateTimeOri());
					copySegment.setDestinationLatitude(s
							.getDestinationLatitude());
					copySegment.setDestinationLongitude(s
							.getDestinationLongitude());
					copySegment.setOriginLatitude(s.getOriginLatitude());
					copySegment.setOriginLongitude(s.getOriginLongitude());
					copySegment.setPath(copyPath);
					copySegment = this.segmentService.save(copySegment);
				}
			}
		}
	}

	public Parade changeStatus(final Parade parade) {
		Assert.notNull(parade, "chapter.parade.error.null");
		Assert.isTrue(LoginService.getPrincipal().getAuthorities().toString()
				.contains("CHAPTER"), "chapter.parade.error.noChapter");
		Assert.isTrue(parade.getBrotherhood().getArea().getChapter().getId() == this.chapterService
				.findByUserAccountId(LoginService.getPrincipal().getId())
				.getId());
		Assert.isTrue(parade.isDraftMode() == false,
				"chapter.parade.error.DraftMode");

		final Parade saved = this.paradeRepository.save(parade);
		return saved;
	}

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
		final int length = 5;
		final String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		final Random rng = new Random();
		final char[] text = new char[length];
		for (int i = 0; i < 5; i++)
			text[i] = characters.charAt(rng.nextInt(characters.length()));
		return new String(text);
	}

	public Collection<Parade> findByBrotherhoodIdAndNotDraft(
			final int brotherhoodId) {
		Assert.notNull(brotherhoodId);
		return this.paradeRepository
				.findByBrotherhoodIdAndNotDraft(brotherhoodId);
	}

	public Collection<Parade> findByBrotherhoodId(final int brotherhoodId) {
		Assert.notNull(brotherhoodId);
		return this.paradeRepository.findByBrotherhoodId(brotherhoodId);
	}

	public Collection<Parade> findByFloaat(final Floaat floaat) {
		Assert.notNull(floaat);
		return this.paradeRepository.findByFloaat(floaat);
	}

	public Collection<Parade> findByBrotherhoodIdAndNotDraftAndAccepted(
			final int brotherhoodId) {
		Assert.notNull(brotherhoodId);
		return this.paradeRepository
				.findByBrotherhoodIdAndNotDraftAndAccepted(brotherhoodId);
	}

	public Collection<Parade> findAccepted() {
		return this.paradeRepository.findAccepted();
	}

	public Collection<Parade> findByBrotherhoodIdAndAccepted(
			final int brotherhoodId) {
		Assert.notNull(brotherhoodId);
		return this.paradeRepository
				.findByBrotherhoodIdAndAccepted(brotherhoodId);
	}

	public Collection<Parade> findByBrotherhoodIdAndRejected(
			final int brotherhoodId) {
		Assert.notNull(brotherhoodId);
		return this.paradeRepository
				.findByBrotherhoodIdAndRejected(brotherhoodId);
	}

	public Collection<Parade> findByBrotherhoodIdAndSubmitted(
			final int brotherhoodId) {
		Assert.notNull(brotherhoodId);
		return this.paradeRepository
				.findByBrotherhoodIdAndSubmitted(brotherhoodId);
	}

	public Collection<Parade> findByBrotherhoodIdAndPending(
			final int brotherhoodId) {
		Assert.notNull(brotherhoodId);
		return this.paradeRepository
				.findByBrotherhoodIdAndPending(brotherhoodId);
	}

	public Collection<Parade> findByChapterId(final int chapterId) {
		return this.paradeRepository.findByChapterId(chapterId);

	}

	public void flush() {
		this.paradeRepository.flush();
	}

}
