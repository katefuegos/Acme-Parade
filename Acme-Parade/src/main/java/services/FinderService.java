
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.FinderRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Actor;
import domain.Configuration;
import domain.Finder;
import domain.Member;
import domain.Procession;

@Service
@Transactional
public class FinderService {

	// Repository-----------------------------------------------

	@Autowired
	private FinderRepository		finderRepository;

	// Services-------------------------------------------------
	@Autowired
	private ConfigurationService	configurationService;
	@Autowired
	private ActorService			actorService;
	@Autowired
	private MemberService			memberService;

	@Autowired
	private ProcessionService		processionService;


	// Constructor----------------------------------------------

	public FinderService() {
		super();
	}

	// Simple CRUD----------------------------------------------

	public Finder create() {
		final UserAccount userAccount = LoginService.getPrincipal();
		final Actor actor = this.actorService.findByUserAccount(userAccount);

		final Authority handyAuthority = new Authority();
		handyAuthority.setAuthority("MEMBER");
		Assert.isTrue(actor.getUserAccount().getAuthorities().contains(handyAuthority), "Solo los member tiene finder");

		final Finder res = new Finder();
		// final Date lastUpdate = new Date();

		final Collection<Procession> processions = new ArrayList<Procession>();

		res.setKeyword("");
		res.setNameArea("");
		res.setLastUpdate(null);
		res.setProcessions(processions);

		final Date current = new Date();
		res.setDateMin(current);
		res.setDateMax(new Date(current.getTime() + 315360000000L * 2));

		return res;
	}

	public List<Finder> findAll() {
		return this.finderRepository.findAll();
	}

	public Finder findOne(final Integer finderId) {
		Assert.notNull(finderId);
		final Finder finder = this.finderRepository.findOne(finderId);
		this.check(finder);
		return finder;
	}

	public Finder save(Finder finder) {
		Assert.notNull(finder);
		this.check(finder);

		finder.setLastUpdate(this.updateTime());
		finder = this.updateFinder(finder);

		final Finder saved = this.finderRepository.save(finder);
		if (finder.getId() == 0) {
			final UserAccount userAccount = LoginService.getPrincipal();
			final Member member = this.memberService.findByUserAccountId(userAccount.getId());
			member.setFinder(saved);
			this.memberService.save(member);
		}
		return saved;
	}

	public Finder clear(final Finder f) {
		final Date currentDate = new Date();
		f.setKeyword("");
		f.setNameArea("");
		f.setDateMin(currentDate);
		f.setDateMax(new Date(currentDate.getTime() + 315360000000L * 2));
		f.setLastUpdate(new Date(currentDate.getTime() - 1000));
		final Collection<Procession> processions = new ArrayList<>(this.processionService.findAll());
		f.setProcessions(processions);

		this.save(f);

		return f;

	}

	// Other Methods--------------------------------------------

	private Date updateTime() {
		final Date currentDate = new Date();
		final Configuration configuration = this.configurationService.findDefault();
		final Date updateFinder = new Date(currentDate.getTime() - configuration.getFinderCacheTime() * 1000 * 60 * 60);
		final Date lastUpdate = new Date(updateFinder.getTime() - 1000);

		return lastUpdate;
	}

	public Finder updateFinder(final Finder finder) {
		this.check(finder);
		final Finder result = this.checkPrincipal(finder);

		final Configuration configuration = this.configurationService.findAll().iterator().next();

		final Date currentDate = new Date();
		final Date updateFinder = new Date(currentDate.getTime() - configuration.getFinderCacheTime() * 1000 * 60 * 60);
		final Date lastUpdate = new Date(currentDate.getTime() - 1000);

		if (!finder.getLastUpdate().after(updateFinder) || finder.getId() == 0) {
			result.setProcessions(this.searchProcession(finder, configuration.getFinderMaxResults()));
			result.setLastUpdate(lastUpdate);
			// result = this.finderRepository.save(result);
		}

		return result;
	}

	private void check(final Finder f) {
		final UserAccount userAccount = LoginService.getPrincipal();
		final Actor actor = this.actorService.findByUserAccount(userAccount);

		final Authority memberAuthority = new Authority();
		memberAuthority.setAuthority("MEMBER");

		final Finder finder = this.findFinderByMemberId(actor.getId());

		Assert.isTrue(											//No es necesaria esta comprobacion
			actor.getUserAccount().getAuthorities().contains(memberAuthority), "Solo los member tiene finder");

		Assert.isTrue(f.equals(finder) || (f.getId() == 0 && finder == null), "Un finder solo puede ser modificado por su dueño");

	}

	private Finder checkPrincipal(final Finder f) {
		Finder result;

		final Date currentDate = new Date();

		if (f.getKeyword() == null)
			f.setKeyword("");

		if (f.getNameArea() == null)
			f.setNameArea("");

		if (f.getDateMin() == null)
			f.setDateMin(currentDate);

		if (f.getDateMax() == null)
			f.setDateMax(new Date(currentDate.getTime() + 315360000000L * 2));// 315360000000L son 10 años en milisegundos

		result = f;

		return result;
	}

	public Collection<Procession> searchProcession(final Finder f, final int maxResult) {
		List<Procession> result = new ArrayList<>();
		final Finder finder = this.checkPrincipal(f);

		final Page<Procession> p;
		p = this.finderRepository.searchProcessions(finder.getKeyword(), finder.getDateMin(), finder.getDateMax(), finder.getNameArea(), new PageRequest(0, maxResult));

		if (p.getContent() != null)
			result = new ArrayList<>(p.getContent());

		return result;
	}

	public Finder findFinderByMemberId(final int memberId) {
		return this.finderRepository.findByMemberId(memberId);
	}

	public Finder findFinder() {
		final UserAccount userAccount = LoginService.getPrincipal();
		final Member member = this.memberService.findByUserAccountId(userAccount.getId());

		Finder finder = this.findFinderByMemberId(member.getId());

		if (finder == null)
			finder = this.save(this.create());

		return finder;

	}
}
