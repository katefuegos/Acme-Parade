
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.MessageRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Actor;
import domain.Box;
import domain.Brotherhood;
import domain.Configuration;
import domain.Member;
import domain.Message;

@Service
@Transactional
public class MessageService {

	//Repository-------------------------------------------------------------------------

	@Autowired
	private MessageRepository		messageRepository;

	//Services---------------------------------------------------------------------------
	@Autowired
	private BoxService				boxService;

	@Autowired
	private ActorService			actorService;

	@Autowired
	private MemberService			memberService;
	
	@Autowired
	private BrotherhoodService			brotherhoodService;
	
	@Autowired
	private ConfigurationService	configurationService;


	//Constructor------------------------------------------------------------------------

	public MessageService() {
		super();
	}

	//Simple CRUD------------------------------------------------------------------------

	public Message create() {
		final Message message = new Message();
		final UserAccount userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount, "Debe estar logeado en el sistema para crear una carpeta");
		final Actor actor = this.actorService.findByUserAccount(userAccount);

		final String subject = "";
		final String body = "";
		final String priority = "LOW";
		final String tags = "";

		final Box box = this.boxService.findBoxByActorIdAndName(actor.getId(), "out box");
		final Actor sender = actor;
		//final Actor recipient = this.actorService.create();

		message.setSubject(subject);
		message.setBody(body);
		message.setMoment(new Date(System.currentTimeMillis() - 1000));
		message.setPriority(priority);
		message.setTags(tags);
		message.setBox(box);
		message.setSender(sender);

		return message;

	}

	public Collection<Message> findAll() {
		final Collection<Message> messages = this.messageRepository.findAll();
		Assert.notNull(messages);
		return messages;
	}

	public Message findOne(final Integer messageId) {
		return this.messageRepository.findOne(messageId);
	}

	public Message save(final Message message) {
		this.checkPriorities(message);

		final boolean isSpam = this.isSpam(message);

		final Actor sender = message.getSender();
		final Actor recipient = message.getRecipient();

		final Message messageSend = this.copyMessage(message);
		final Message messageRecipient = this.copyMessage(message);

		messageSend.setSender(sender);
		messageSend.setRecipient(recipient);

		messageRecipient.setSender(sender);
		messageRecipient.setRecipient(recipient);

		final Box outBoxSender = this.boxService.findBoxByActorIdAndName(sender.getId(), "out box");
		Assert.notNull(outBoxSender, "NULL OUT BOX\ncada actor debe tener debe tener un out Box");
		messageSend.setBox(outBoxSender);

		Box spamBoxRecipient;
		Box inBoxRecipient;
		if (isSpam) {
			spamBoxRecipient = this.boxService.findBoxByActorIdAndName(recipient.getId(), "spam box");
			Assert.notNull(spamBoxRecipient, "NULL SPAM BOX\nCada actor debe tener un spam box");
			messageRecipient.setBox(spamBoxRecipient);
		} else {
			inBoxRecipient = this.boxService.findBoxByActorIdAndName(recipient.getId(), "in box");
			Assert.notNull(inBoxRecipient, "NULL IN BOX\nCada actor debe tener un in box");
			messageRecipient.setBox(inBoxRecipient);
		}

		final Collection<Message> messages = new ArrayList<>();
		messages.add(messageRecipient);
		messages.add(messageSend);

		final List<Message> saved = this.messageRepository.save(messages);

		return saved.get(0);
	}
	public void delete(final Message entity) {
		final Box box = entity.getBox();
		final Actor actor = this.checkPrincipal(entity);

		if (box.getName().equals("trash box"))
			this.messageRepository.delete(entity);
		else {
			final Box trash = this.boxService.findBoxByActorIdAndName(actor.getId(), "trash box");
			Assert.notNull(trash, "Todo actor debe tener un trash box");
			entity.setBox(trash);
			this.messageRepository.save(entity);
		}

	}

	public void deleteByBox(final Box box) {
		if (this.messageRepository.findByBoxId(box.getId()) != null || !this.messageRepository.findByBoxId(box.getId()).isEmpty()) {
			final Collection<Message> messages = new LinkedList<>();
			final Actor actor = this.actorService.findByUserAccount(LoginService.getPrincipal());
			final Box trash = this.boxService.findBoxByActorIdAndName(actor.getId(), "trash box");
			for (final Message entity : this.messageRepository.findByBoxId(box.getId())) {
				Assert.notNull(trash, "Todo actor debe tener un trash box");
				entity.setBox(trash);
				messages.add(entity);
			}
			this.messageRepository.save(messages);
		}
	}
	//Other Methods---------------------------------------------------------------------------

	public Collection<Message> findByBox(final Box box) {
		Assert.notNull(box, "findByBox - Box must not be null");

		final Collection<Message> result = this.messageRepository.findByBoxId(box.getId());

		return result;

	}
	public Message moveMessage(final Message message, final Box newBox) {
		final Message oldMessage = this.findOne(message.getId());

		final int actorId = this.actorService.findByUserAccountId(LoginService.getPrincipal().getId()).getId();
		final Collection<Box> boxes = this.boxService.findBoxesByActorId(actorId);
		Assert.isTrue(boxes.contains(oldMessage.getBox()));

		this.checkPrincipal(message);
		message.setBox(newBox);
		final Message saved = this.messageRepository.save(message);
		return saved;

	}

	/*
	 * Requisito Funcional 12.4
	 */
	public void broadcastMessage(final Message message) {
		this.checkPriorities(message);
		final UserAccount userAccount = LoginService.getPrincipal();

		Assert.notNull(userAccount, "Debe estar logeado en el sistema para crear una carpeta");

		final Authority authority = new Authority();
		authority.setAuthority("ADMIN");

		Assert.isTrue(userAccount.getAuthorities().contains(authority), "Solo los administradores pueden realizar mensajes de difusión");

		//final Collection<Actor> allActor = this.actorService.findAll();
		final Collection<Actor> allActor = this.actorService.findAll();

		final Collection<Message> messages = new ArrayList<>();

		for (final Actor recipient : allActor) {
			final Message message2 = this.copyMessage(message);
			message2.setRecipient(recipient);
			Box box;
			if (this.isSpam(message2))
				box = this.boxService.findBoxByActorIdAndName(recipient.getId(), "spam box");
			else
				box = this.boxService.findBoxByActorIdAndName(recipient.getId(), "notification box");
			message2.setBox(box);
			messages.add(message2);

		}

		this.messageRepository.save(messages);

	}
	
	public void broadcastMessageMembers(final Message message) {
		this.checkPriorities(message);
		final UserAccount userAccount = LoginService.getPrincipal();

		Assert.notNull(userAccount, "Debe estar logeado en el sistema para crear una carpeta");

		final Authority authority = new Authority();
		authority.setAuthority("ADMIN");

		Assert.isTrue(userAccount.getAuthorities().contains(authority), "Solo los administradores pueden realizar mensajes de difusión");

		final Collection<Member> allActor = this.memberService.findAll();

		final Collection<Message> messages = new ArrayList<>();

		for (final Actor recipient : allActor) {
			final Message message2 = this.copyMessage(message);
			message2.setRecipient(recipient);
			Box box;
			if (this.isSpam(message2))
				box = this.boxService.findBoxByActorIdAndName(recipient.getId(), "spam box");
			else
				box = this.boxService.findBoxByActorIdAndName(recipient.getId(), "notification box");
			message2.setBox(box);
			messages.add(message2);

		}

		this.messageRepository.save(messages);

	}
	
	public void broadcastMessageBrotherhoods(final Message message) {
		this.checkPriorities(message);
		final UserAccount userAccount = LoginService.getPrincipal();

		Assert.notNull(userAccount, "Debe estar logeado en el sistema para crear una carpeta");

		final Authority authority = new Authority();
		authority.setAuthority("ADMIN");

		Assert.isTrue(userAccount.getAuthorities().contains(authority), "Solo los administradores pueden realizar mensajes de difusión");

		final Collection<Brotherhood> allActor = this.brotherhoodService.findAll();

		final Collection<Message> messages = new ArrayList<>();

		for (final Actor recipient : allActor) {
			final Message message2 = this.copyMessage(message);
			message2.setRecipient(recipient);
			Box box;
			if (this.isSpam(message2))
				box = this.boxService.findBoxByActorIdAndName(recipient.getId(), "spam box");
			else
				box = this.boxService.findBoxByActorIdAndName(recipient.getId(), "notification box");
			message2.setBox(box);
			messages.add(message2);

		}

		this.messageRepository.save(messages);

	}
	
	private Message copyMessage(final Message message) {
		final Message result = this.create();
		result.setSubject(message.getSubject());
		result.setBody(message.getBody());
		result.setSender(message.getSender());
		result.setRecipient(message.getRecipient());
		result.setMoment(message.getMoment());
		result.setTags(message.getTags());
		result.setPriority(message.getPriority());

		return result;
	}

	public Actor checkPrincipal(final Message message) {
		final UserAccount userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount, "Debe estar logeado para modificar o borrar un mensaje");
		final Actor actor = this.actorService.findByUserAccount(userAccount);
		Assert.isTrue(message.getSender().equals(actor) || message.getRecipient().equals(actor), "Un actor solo puede ver sus mensajes");
		return actor;
	}
	private boolean isSpam(final Message message) {
		boolean result = false;
		final Configuration configuration = this.configurationService.findAll().iterator().next();
		final Map<String, Collection<String>> spamWords = configuration.getSpamWords();

		final Collection<String> keySet = spamWords.keySet();

		for (final String key : keySet) {
			final Collection<String> spamList = spamWords.get(key);
			for (final String spamWord : spamList)
				if (message.getBody().contains(spamWord) || message.getSubject().contains(spamWord) || message.getTags().contains(spamWord)) {
					result = true;
					break;
				}
			if (result == true)
				break;
		}

		return result;
	}

	private boolean checkPriorities(final Message message) {
		boolean result = false;

		final Configuration configuration = this.configurationService.findAll().iterator().next();

		result = configuration.getPriorities().contains(message.getPriority());
		Assert.isTrue(result, "MessageService Error -- The priority of message isn't valid");
		return result;
	}

	public Collection<Message> findSentMessage(final Actor a) {
		final Collection<Message> result = this.messageRepository.findSentMessage(a.getId());
		return result;
	}

}
