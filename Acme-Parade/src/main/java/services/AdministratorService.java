
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.AdministratorRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Actor;
import domain.Administrator;
import domain.Brotherhood;
import domain.Member;
import domain.Position;
import domain.Procession;
import forms.AreaQueryB1Form;
import forms.PositionCountForm;

@Service
@Transactional
public class AdministratorService {

	//Repository-----------------------------------------------

	@Autowired
	private AdministratorRepository	administratorRepository;

	//Services-------------------------------------------------
	@Autowired
	private BrotherhoodService		brotherhoodService;

	@Autowired
	private MemberService			memberService;

	@Autowired
	private PositionService			positionService;


	//Constructor----------------------------------------------

	public AdministratorService() {

		super();
	}

	//Simple CRUD----------------------------------------------

	public Administrator create() {
		final Administrator administrator = new Administrator();
		final UserAccount userAccount = new UserAccount();
		final Collection<Authority> authorities = new ArrayList<Authority>();

		final Authority a = new Authority();
		a.setAuthority("ADMIN");
		authorities.add(a);
		userAccount.setAuthorities(authorities);
		userAccount.setEnabled(true);
		administrator.setUserAccount(userAccount);

		administrator.setIsBanned(false);
		administrator.setIsSpammer(null);
		return administrator;
	}
	public List<Administrator> findAll() {
		return this.administratorRepository.findAll();
	}

	public Administrator findOne(final Integer administratorId) {
		return this.administratorRepository.findOne(administratorId);
	}

	public Administrator save(final Administrator administrator) {
		Assert.notNull(administrator);

		final Authority admin = new Authority();
		admin.setAuthority(Authority.ADMIN);
		Assert.isTrue(LoginService.getPrincipal().getAuthorities().contains(admin), "actor.register.error.authority");

		final Administrator saved = this.administratorRepository.save(administrator);
		return saved;
	}
	public void delete(final Administrator entity) {
		this.administratorRepository.delete(entity);
	}

	//Other Methods--------------------------------------------

	public Actor isSpamer(final Actor actor) {
		Actor result = null;
		final UserAccount userAccount = actor.getUserAccount();
		final Authority authority = userAccount.getAuthorities().iterator().next();

		switch (authority.getAuthority()) {
		case "ADMIN":
			final Administrator administrator = this.findByUseraccount(userAccount);
			administrator.setIsSpammer(true);
			result = this.save(administrator);
			break;
		case "MEMBER":
			final Member member = this.memberService.findByUserAccountId(userAccount.getId());
			member.setIsSpammer(true);
			result = this.memberService.save(member);
			break;
		case "BROTHERHOOD":
			final Brotherhood brotherhood = this.brotherhoodService.findByUserAccountId(userAccount.getId());
			brotherhood.setIsSpammer(true);
			result = this.brotherhoodService.save(brotherhood);
			break;

		}

		return result;
	}

	public Administrator findByUseraccount(final UserAccount userAccount) {
		return this.administratorRepository.findAdministratorByUserAccount(userAccount.getId());

	}

	public Administrator findAdminByUsername(final String username) {
		return this.administratorRepository.findAdminByUsername(username);
	}

	public Object[] queryC1() {
		Object[] result = null;

		result = this.administratorRepository.queryC1();

		return result;
	}

	public Collection<Object[]> queryC2() {
		Collection<Object[]> result = null;

		result = this.administratorRepository.queryC2();

		return result;
	}

	public Collection<Object[]> queryC3() {
		Collection<Object[]> result = null;

		result = this.administratorRepository.queryC3();

		return result;
	}

	public Collection<Object[]> queryC4() {
		Collection<Object[]> result = null;

		result = this.administratorRepository.queryC4();

		return result;
	}

	public Collection<Procession> queryC5() {
		Collection<Procession> result = null;

		result = this.administratorRepository.queryC5(new Date(new Date().getTime() + 2592000000L));

		return result;
	}

	public Collection<Member> queryC7() {
		Collection<Member> result = null;

		result = this.administratorRepository.queryC7();
		return result;
	}

	public Collection<PositionCountForm> queryC8() {
		Collection<Object[]> queryC8 = null;
		queryC8 = this.administratorRepository.queryC8();
		Collection<PositionCountForm> result = new ArrayList<>();

		final Collection<Position> positions = this.positionService.findAll();
		if (queryC8 != null)
			for (final Object[] objects : queryC8) {
				final Position p = (domain.Position) objects[0];
				positions.remove(p);
				final PositionCountForm countForm = new PositionCountForm();
				countForm.setPosition(p);
				countForm.setCount((Long) objects[1]);

				result.add(countForm);
			}
		else
			result = null;

		for (final Position p : positions) {
			final PositionCountForm countForm = new PositionCountForm();
			countForm.setPosition(p);
			countForm.setCount(0L);

			result.add(countForm);
		}
		return result;
	}

	public Collection<AreaQueryB1Form> queryB1A() {
		final Collection<AreaQueryB1Form> result = new LinkedList<>();

		final Collection<Object[]> queryB1A = this.administratorRepository.queryB1A();

		for (final Object[] objects : queryB1A) {
			final AreaQueryB1Form areaQueryB1Form = new AreaQueryB1Form();

			areaQueryB1Form.setName((String) objects[0]);
			areaQueryB1Form.setRatio((Double) objects[1]);
			areaQueryB1Form.setCount((Long) objects[2]);

			result.add(areaQueryB1Form);

		}

		return result;
	}

	public Object[] queryB1B() {
		Object[] result = null;
		result = this.administratorRepository.queryB1B();

		return result;
	}

	public Object[] queryB2() {
		Object[] result = null;

		result = this.administratorRepository.queryB2();

		return result;

	}

	public Double queryB3Empty() {

		final Double result = this.administratorRepository.queryB3Empty();

		return result;
	}

	public Double queryB3NotEmpty() {
		final Double result = this.administratorRepository.queryB3NotEmpty();

		return result;
	}

}
