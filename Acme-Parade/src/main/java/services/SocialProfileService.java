package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.SocialProfileRepository;
import security.LoginService;
import security.UserAccount;
import domain.SocialProfile;

@Service
@Transactional
public class SocialProfileService {

	// Repository

	@Autowired
	private SocialProfileRepository socialProfileRepository;

	@Autowired
	private ActorService actorService;

	@Autowired
	private Validator validator;

	// Services

	// Constructor----------------------------------------------------------------------------

	public SocialProfileService() {
		super();
	}

	// Simple CRUD methods
	// -------------------------------------------------------------------
	public SocialProfile create() {
		final SocialProfile profile = new SocialProfile();
		profile.setActor(this.actorService.findByUserAccountId(LoginService
				.getPrincipal().getId()));
		return profile;
	}

	public Collection<SocialProfile> findAll() {
		Collection<SocialProfile> profiles;

		profiles = this.socialProfileRepository.findAll();
		Assert.notNull(profiles);

		return profiles;
	}

	public SocialProfile findOne(final int profileId) {
		SocialProfile profile;
		profile = this.socialProfileRepository.findOne(profileId);
		Assert.notNull(profileId);

		return profile;
	}

	public SocialProfile save(final SocialProfile profile) {
		Assert.notNull(profile);
		this.checkPrincipal(profile);
		SocialProfile result;

		result = this.socialProfileRepository.save(profile);

		return result;
	}

	public void delete(final SocialProfile profile) {

		Assert.notNull(profile);
		// this.checkPrincipal(profile);
		this.socialProfileRepository.delete(profile);
	}

	// Other
	// Methods-----------------------------------------------------------------
	public Boolean checkPrincipal(final SocialProfile profile) {
		final UserAccount u = profile.getActor().getUserAccount();
		Assert.isTrue(u.equals(LoginService.getPrincipal()),
				"este perfil no corresponde con este actor");
		return true;
	}

	// como el actor se pasaría como hidden hay que hacer el reconstruct
	// set --> los que no has modificado
	// Los test no van a tirar por el autowired
	public SocialProfile reconstruct(final SocialProfile socialProfile,
			final BindingResult binding) {
		final SocialProfile result = socialProfile;
		result.setActor(this.actorService.findPrincipal());
		this.validator.validate(result, binding);
		return result;
	}

	public Collection<SocialProfile> findByActor(Integer actorId) {
		Assert.notNull(actorId);
		return socialProfileRepository.findByActor(actorId);
	}
}
