
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.ActorRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Actor;
import domain.Administrator;
import domain.Brotherhood;
import domain.Configuration;
import domain.Member;
import domain.Message;
import forms.ActorForm;

@Service
@Transactional
public class ActorService {

	// Repository-----------------------------------------------

	@Autowired
	private ActorRepository			actorRepository;

	// Services-------------------------------------------------
	@Autowired
	private MessageService			messageService;

	@Autowired
	private ConfigurationService	configurationService;

	@Autowired
	private MemberService			memberService;

	@Autowired
	private BoxService				boxService;

	@Autowired
	private BrotherhoodService		brotherhoodService;

	@Autowired
	private AdministratorService	administratorService;


	// Constructor----------------------------------------------

	public ActorService() {
		super();
	}

	// Simple CRUD----------------------------------------------

	public Actor create(final String authority) {
		final Actor actor = new Actor();
		final UserAccount userAccount = new UserAccount();
		final Collection<Authority> authorities = new ArrayList<Authority>();

		final Authority a = new Authority();
		a.setAuthority(authority);
		authorities.add(a);
		userAccount.setAuthorities(authorities);
		userAccount.setEnabled(true);
		actor.setUserAccount(userAccount);
		actor.setIsBanned(false);
		actor.setIsSpammer(false);
		actor.setPolarityScore(0.);
		return actor;
	}

	public List<Actor> findAll() {
		return this.actorRepository.findAll();
	}

	public Actor findOne(final Integer actorId) {
		return this.actorRepository.findOne(actorId);
	}

	public Actor save(final Actor actor) {
		Assert.notNull(actor);
		final Actor saved = this.actorRepository.save(actor);
		return saved;
	}

	public void delete(final Actor actor) {
		this.actorRepository.delete(actor);
	}

	// Other Methods--------------------------------------------

	public Actor findActorByUsername(final String username) {
		final Actor actor = this.actorRepository.findActorByUsername(username);
		return actor;
	}

	public Actor findByUserAccount(final UserAccount userAccount) {
		return this.actorRepository.findByUserAccountId(userAccount.getId());
	}
	public Actor findPrincipal() {
		final UserAccount userAccount = LoginService.getPrincipal();
		return this.actorRepository.findByUserAccountId(userAccount.getId());
	}
	public Collection<Actor> findSpammersActors() {
		return this.actorRepository.findSpammersActors();
	}

	public void ban(final Actor actor) {
		actor.setIsBanned(true);
		actor.getUserAccount().setEnabled(false);
		this.save(actor);
	}

	public void unban(final Actor actor) {
		actor.setIsBanned(false);
		actor.setIsSpammer(false);
		actor.getUserAccount().setEnabled(true);
		this.save(actor);

	}

	public Collection<Actor> findPossibleBanned() {

		return this.actorRepository.findPossibleBanned();

	}

	public Collection<Actor> findActorsNegativePolarity() {

		return this.actorRepository.findActorsNegativePolarity();

	}

	public void asignSpammers() {

		final Collection<Actor> result = this.actorRepository.asignSpammersActors();
		final Collection<Actor> allActor = this.actorRepository.findAll();

		for (final Actor actor : allActor)
			if (result.contains(actor))
				actor.setIsSpammer(true);
			else
				actor.setIsSpammer(false);

		this.actorRepository.save(allActor);

	}
	public void updatePolarity() {
		final Collection<Actor> allActors = this.actorRepository.findAll();

		//final Collection<Actor> actors = new ArrayList<>();
		for (final Actor actor : allActors)
			actor.setPolarityScore(this.updateIndividualPoparity(actor));

		this.actorRepository.save(allActors);

	}

	protected Double updateIndividualPoparity(final Actor actor) {
		Double result = 0.0;

		final Collection<Message> sentMessages = this.messageService.findSentMessage(actor);
		if (sentMessages != null && !sentMessages.isEmpty()) {
			// Se obtiene todas las palabras de todos los mensajes de un actor
			final StringBuilder strBuilder = new StringBuilder();

			for (final Message message : sentMessages) {
				strBuilder.append(" ");
				strBuilder.append(message.getSubject());
				strBuilder.append(" ");
				strBuilder.append(message.getBody());
				strBuilder.append(" ");
				strBuilder.append(message.getTags());
				strBuilder.append(" ");
			}

			String comments = strBuilder.toString();

			final Configuration configuration = this.configurationService.findOne();

			final Collection<String> positiveWords = new LinkedList<>();
			for (final String string : configuration.getPositiveWords().keySet()) {
				positiveWords.removeAll(configuration.getPositiveWords().get(string));
				positiveWords.addAll(configuration.getPositiveWords().get(string));
			}
			final Collection<String> negativeWords = new LinkedList<>();
			for (final String string : configuration.getNegativeWords().keySet()) {
				negativeWords.removeAll(configuration.getNegativeWords().get(string));
				negativeWords.addAll(configuration.getNegativeWords().get(string));
			}
			comments = comments.toUpperCase();
			String cadena = "";
			final String auxComments = comments;
			double p = 0.0;
			for (final String positive : positiveWords) {
				cadena = " " + positive.toUpperCase() + " ";
				while (comments.indexOf(cadena) > -1) {
					comments = comments.substring(comments.indexOf(cadena) + (cadena).length(), comments.length());
					p++;
				}
				comments = auxComments;
			}
			double n = 0.0;
			for (final String negative : negativeWords) {
				cadena = " " + negative.toUpperCase() + " ";
				while (comments.indexOf(cadena) > -1) {
					comments = comments.substring(comments.indexOf(cadena) + (cadena).length(), comments.length());
					n++;
				}
				comments = auxComments;
			}
			final double total = p + n;
			if (total != 0.0)
				result = (p - n) / total;
		}
		return result;
	}

	public Actor findByUserAccountId(final int id) {
		return this.actorRepository.findByUserAccountId(id);
	}

	public void update(final ActorForm actorform) {

		Assert.notNull(actorform);

		final Collection<Authority> authorities = actorform.getUserAccount().getAuthorities();
		final Authority member = new Authority();
		member.setAuthority(Authority.MEMBER);
		final Authority brotherhood = new Authority();
		brotherhood.setAuthority(Authority.BROTHERHOOD);

		final Authority admin = new Authority();
		admin.setAuthority(Authority.ADMIN);

		if (authorities.contains(member)) {
			Member memb = null;
			if (actorform.getId() != 0)
				memb = this.memberService.findOne(actorform.getId());
			else {
				memb = this.memberService.create();
				memb.setUserAccount(actorform.getUserAccount());
				// TODO adaptar a requisitos
			}
			memb.setId(actorform.getId());
			memb.setVersion(actorform.getVersion());
			memb.setName(actorform.getName());
			memb.setSurname(actorform.getSurname());
			memb.setMiddleName(actorform.getMiddleName());
			memb.setAddress(actorform.getAddress());
			memb.setEmail(actorform.getEmail());
			memb.setPhone(actorform.getPhone());
			memb.setPhoto(actorform.getPhoto());

			final Actor actor1 = this.memberService.save(memb);
			this.boxService.addSystemBox(actor1);

		} else if (authorities.contains(brotherhood)) {
			Brotherhood brother = null;
			if (actorform.getId() != 0)
				brother = this.brotherhoodService.findOne(actorform.getId());
			else {
				brother = this.brotherhoodService.create();
				brother.setUserAccount(actorform.getUserAccount());

			}

			brother.setId(actorform.getId());
			brother.setVersion(actorform.getVersion());
			brother.setName(actorform.getName());
			brother.setSurname(actorform.getSurname());
			brother.setMiddleName(actorform.getMiddleName());
			brother.setAddress(actorform.getAddress());
			brother.setEmail(actorform.getEmail());
			brother.setPhone(actorform.getPhone());
			brother.setPhoto(actorform.getPhoto());

			brother.setTitle(actorform.getTitle());
			brother.setPictures(actorform.getPictures());
			brother.setArea(actorform.getArea());

			final Actor actor1 = this.brotherhoodService.save(brother);
			this.boxService.addSystemBox(actor1);

		} else if (authorities.contains(admin)) {
			Administrator administrator = null;
			if (actorform.getId() != 0)
				administrator = this.administratorService.findOne(actorform.getId());
			else {
				administrator = this.administratorService.create();
				administrator.setUserAccount(actorform.getUserAccount());
			}

			administrator.setId(actorform.getId());
			administrator.setVersion(actorform.getVersion());
			administrator.setName(actorform.getName());
			administrator.setSurname(actorform.getSurname());
			administrator.setMiddleName(actorform.getMiddleName());
			administrator.setAddress(actorform.getAddress());
			administrator.setEmail(actorform.getEmail());
			administrator.setPhone(actorform.getPhone());
			administrator.setPhoto(actorform.getPhoto());

			final Actor actor1 = this.administratorService.save(administrator);
			this.boxService.addSystemBox(actor1);
		}

	}

}
