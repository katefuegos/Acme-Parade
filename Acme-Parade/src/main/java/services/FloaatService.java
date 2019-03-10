
package services;

import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.FloaatRepository;
import security.LoginService;
import domain.Floaat;

@Service
@Transactional
public class FloaatService {

	// Repository-----------------------------------------------

	@Autowired
	private FloaatRepository	repository;

	// Services-------------------------------------------------
	@Autowired
	private BrotherhoodService	brotherhoodService;


	// Constructor----------------------------------------------

	public FloaatService() {
		super();
	}

	// Simple CRUD----------------------------------------------

	public Floaat create() {
		final Floaat res = new Floaat();
		res.setBrotherhood(this.brotherhoodService.findByUserAccountId(LoginService.getPrincipal().getId()));
		return res;
	}

	public List<Floaat> findAll() {
		return this.repository.findAll();
	}

	public Floaat findOne(final Integer floaatId) {
		return this.repository.findOne(floaatId);
	}

	public Floaat save(final Floaat floaat) {
		Assert.notNull(floaat);
		Assert.isTrue(LoginService.getPrincipal().getAuthorities().toString().contains("BROTHERHOOD"), "SOLO UN BROTHERHOOD PUEDE CREAR/EDITAR PROCESSION");
		final Floaat saved = this.repository.save(floaat);
		return saved;
	}

	public void delete(final Floaat floaat) {
		this.repository.delete(floaat);
	}

	// Other Methods--------------------------------------------

	public Collection<Floaat> findByBrotherhoodId(final int brotherhoodId) {
		Assert.notNull(brotherhoodId);
		return this.repository.findByBrotherhoodId(brotherhoodId);
	}

}
