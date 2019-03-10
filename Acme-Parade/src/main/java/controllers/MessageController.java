package controllers;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import security.LoginService;
import security.UserAccount;
import services.ActorService;
import services.BoxService;
import services.BrotherhoodService;
import services.ConfigurationService;
import services.MemberService;
import services.MessageService;
import domain.Actor;
import domain.Box;
import domain.Brotherhood;
import domain.Member;
import domain.Message;
import forms.MessageForm;

@Controller
@RequestMapping("/message")
public class MessageController extends AbstractController {

	@Autowired
	private MessageService messageService;

	@Autowired
	private BoxService boxService;

	@Autowired
	private ActorService actorService;

	@Autowired
	private MemberService memberService;

	@Autowired
	private BrotherhoodService brotherhoodService;

	@Autowired
	private ConfigurationService configurationService;

	@RequestMapping(value = "/actor/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int boxId,
			final RedirectAttributes redirectAttrs) {
		ModelAndView result;

		final Box box1 = this.boxService.findOne(boxId);

		try {
			Assert.notNull(box1);
			this.boxService.checkPrincipal(box1);
			final Collection<Message> messages = this.messageService
					.findByBox(box1);

			result = new ModelAndView("message/actor/list");
			result.addObject("messages", messages);
			result.addObject("requestURI", "message/actor/list.do");
			result.addObject("banner", this.configurationService.findAll()
					.iterator().next().getBanner());
			result.addObject("systemName", this.configurationService.findAll()
					.iterator().next().getSystemName());
		} catch (final Exception e) {

			result = new ModelAndView("redirect:/box/actor/list.do");

			final Box box = this.boxService.findOne(boxId);
			final Actor actor = this.actorService
					.findByUserAccount(LoginService.getPrincipal());

			if (box == null)
				redirectAttrs.addFlashAttribute("message", "box.error.unexist");
			else if (!(box.getActor().equals(actor)))
				redirectAttrs.addFlashAttribute("message",
						"box.error.notFromThisActor");
		}
		return result;
	}

	// Create
	@RequestMapping(value = "/administrator/broadcastMessage", method = RequestMethod.GET)
	public ModelAndView broadcastMessage() {
		final ModelAndView modelAndView;

		final Message message = this.messageService.create();
		final MessageForm messageForm = new MessageForm();

		message.setRecipient(this.actorService.findByUserAccount(LoginService
				.getPrincipal()));

		messageForm.setMessage(message);

		modelAndView = this.createEditModelAndView(messageForm);

		modelAndView.setViewName("message/administrator/broadcastMessage");
		return modelAndView;
	}

	// Save
	@RequestMapping(value = "/administrator/broadcastMessage", method = RequestMethod.POST, params = "save")
	public ModelAndView saveBroadcast(@Valid final MessageForm messageForm,
			final BindingResult binding) {

		ModelAndView result;

		if (binding.hasErrors()) {
			result = this.createEditModelAndView(messageForm);
			result.setViewName("message/administrator/broadcastMessage");
		} else
			try {
				final Message message = messageForm.getMessage();

				this.messageService.broadcastMessage(message);
				result = new ModelAndView("redirect:/box/actor/list.do");
			} catch (final Throwable oops) {

				result = this.createEditModelAndView(messageForm,
						"message.commit.error");
				result.setViewName("message/administrator/broadcastMessage");
			}
		return result;
	}

	// Create
	@RequestMapping(value = "/administrator/broadcastMessageMembers", method = RequestMethod.GET)
	public ModelAndView broadcastMessageMembers() {
		final ModelAndView modelAndView;

		final Message message = this.messageService.create();
		final MessageForm messageForm = new MessageForm();

		message.setRecipient(this.actorService.findByUserAccount(LoginService
				.getPrincipal()));
		message.setBody("SECURITY BREATCH DETECTED / BRECHA DE SEGURIDAD DETECTADA");
		message.setSubject("SECURITY BREACH / BRECHA DE SEGURIDAD");
		message.setTags("breach,security");

		messageForm.setMessage(message);

		modelAndView = this.createEditModelAndViewMembers(messageForm);

		modelAndView
				.setViewName("message/administrator/broadcastMessageMembers");
		return modelAndView;
	}

	// Save
	@RequestMapping(value = "/administrator/broadcastMessageMembers", method = RequestMethod.POST, params = "save")
	public ModelAndView saveBroadcastMembers(
			@Valid final MessageForm messageForm, final BindingResult binding) {

		ModelAndView result;

		if (binding.hasErrors()) {
			result = this.createEditModelAndViewMembers(messageForm);
			result.setViewName("message/administrator/broadcastMessageMembers");
		} else
			try {
				final Message message = messageForm.getMessage();

				this.messageService.broadcastMessageMembers(message);
				result = new ModelAndView("redirect:/box/actor/list.do");
			} catch (final Throwable oops) {

				result = this.createEditModelAndViewMembers(messageForm,
						"message.commit.error");
				result.setViewName("message/administrator/broadcastMessageMembers");
			}
		return result;
	}

	// Create
	@RequestMapping(value = "/administrator/broadcastMessageBrotherhoods", method = RequestMethod.GET)
	public ModelAndView broadcastMessageBrotherhoods() {
		final ModelAndView modelAndView;

		final Message message = this.messageService.create();
		final MessageForm messageForm = new MessageForm();

		message.setRecipient(this.actorService.findByUserAccount(LoginService
				.getPrincipal()));
		message.setBody("SECURITY BREATCH DETECTED / BRECHA DE SEGURIDAD DETECTADA");
		message.setSubject("SECURITY BREACH / BRECHA DE SEGURIDAD");
		message.setTags("breach,security");

		messageForm.setMessage(message);

		modelAndView = this.createEditModelAndViewBrotherhoods(messageForm);

		modelAndView
				.setViewName("message/administrator/broadcastMessageBrotherhoods");
		return modelAndView;
	}

	// Save
	@RequestMapping(value = "/administrator/broadcastMessageBrotherhoods", method = RequestMethod.POST, params = "save")
	public ModelAndView saveBroadcastBrotherhoods(
			@Valid final MessageForm messageForm, final BindingResult binding) {

		ModelAndView result;

		if (binding.hasErrors()) {
			result = this.createEditModelAndViewBrotherhoods(messageForm);
			result.setViewName("message/administrator/broadcastMessageBrotherhoods");
		} else
			try {
				final Message message = messageForm.getMessage();

				this.messageService.broadcastMessageBrotherhoods(message);
				result = new ModelAndView("redirect:/box/actor/list.do");
			} catch (final Throwable oops) {

				result = this.createEditModelAndViewBrotherhoods(messageForm,
						"message.commit.error");
				result.setViewName("message/administrator/broadcastMessageBrotherhoods");
			}
		return result;
	}

	// Create
	@RequestMapping(value = "/actor/exchangeMessage", method = RequestMethod.GET)
	public ModelAndView exchangeMessage() {
		final ModelAndView modelAndView;

		final Message message = this.messageService.create();

		final MessageForm messageForm = new MessageForm();
		messageForm.setMessage(message);

		modelAndView = this.createEditModelAndView(messageForm);
		return modelAndView;
	}

	@RequestMapping(value = "/actor/exchangeMessageMembers", method = RequestMethod.GET)
	public ModelAndView exchangeMessageMembers() {
		final ModelAndView modelAndView;

		final Message message = this.messageService.create();

		final MessageForm messageForm = new MessageForm();
		messageForm.setMessage(message);

		modelAndView = this.createEditModelAndViewMembers(messageForm);
		return modelAndView;
	}

	@RequestMapping(value = "/actor/exchangeMessageBrotherhoods", method = RequestMethod.GET)
	public ModelAndView exchangeMessageBrotherhoods() {
		final ModelAndView modelAndView;

		final Message message = this.messageService.create();

		final MessageForm messageForm = new MessageForm();
		messageForm.setMessage(message);

		modelAndView = this.createEditModelAndViewBrotherhoods(messageForm);
		return modelAndView;
	}

	// Show
	@RequestMapping(value = "/actor/show", method = RequestMethod.GET)
	public ModelAndView show(@RequestParam final int messageId,
			final RedirectAttributes redirectAttrs) {
		ModelAndView modelAndView;
		final Message message = this.messageService.findOne(messageId);
		try {
			Assert.notNull(message);
			this.messageService.checkPrincipal(message);

			final MessageForm messageForm = new MessageForm();
			messageForm.setMessage(message);

			final UserAccount userAccount = LoginService.getPrincipal();
			final Actor actor = this.actorService
					.findByUserAccount(userAccount);
			final Collection<Box> boxes = this.boxService
					.findBoxesByActorId(actor.getId());
			Assert.isTrue(boxes.contains(message.getBox()),
					"message.error.notFromThisActor");

			modelAndView = this.createEditModelAndView(messageForm);
			modelAndView.setViewName("message/actor/show");
			modelAndView.addObject("isRead", true);

		} catch (final Exception e) {
			modelAndView = new ModelAndView("redirect:/box/actor/list.do");
			final UserAccount userAccount = LoginService.getPrincipal();
			final Actor actor = this.actorService
					.findByUserAccount(userAccount);

			if (message == null)
				redirectAttrs.addFlashAttribute("message",
						"message.error.unexist");
			else if (!(message.getSender().equals(actor) || message
					.getRecipient().equals(actor)))
				redirectAttrs.addFlashAttribute("message",
						"message.error.notFromThisActor");
			else if (e.getMessage() == "message.error.notFromThisActor")
				redirectAttrs.addFlashAttribute("message",
						"message.error.notFromThisActor");
			else
				modelAndView.addObject("message", "message.commit.error");
		}
		return modelAndView;
	}

	// Move message
	@RequestMapping(value = "/actor/move", method = RequestMethod.GET)
	public ModelAndView move(@RequestParam final int messageId,
			final RedirectAttributes redirectAttrs) {
		ModelAndView modelAndView;
		final Actor actor = this.actorService.findByUserAccount(LoginService
				.getPrincipal());
		final Message message = this.messageService.findOne(messageId);

		try {
			Assert.notNull(message);
			this.messageService.checkPrincipal(message);

			final Collection<Box> boxes = this.boxService
					.findBoxesByActorId(actor.getId());
			Assert.isTrue(boxes.contains(message.getBox()),
					"message.error.notFromThisActor");

			final MessageForm messageForm = new MessageForm();
			messageForm.setMessage(message);

			modelAndView = this.createEditModelAndView(messageForm);
			modelAndView.setViewName("message/actor/move");
			modelAndView.addObject("isRead", true);
			modelAndView.addObject("isMove", true);
			modelAndView.addObject("boxes", boxes);
		} catch (final Exception e) {
			modelAndView = new ModelAndView("redirect:/box/actor/list.do");
			if (message == null)
				redirectAttrs.addFlashAttribute("message",
						"message.error.unexist");
			else if (!(message.getSender().equals(actor) || message
					.getRecipient().equals(actor)))
				redirectAttrs.addFlashAttribute("message",
						"message.error.notFromThisActor");
			else if (e.getMessage() == "message.error.notFromThisActor")
				redirectAttrs.addFlashAttribute("message",
						"message.error.notFromThisActor");
			else
				modelAndView.addObject("message", "message.commit.error");
		}
		return modelAndView;
	}

	// Save
	@RequestMapping(value = "/actor/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final MessageForm messageForm,
			final BindingResult binding) {
		ModelAndView result;
		final Message message = messageForm.getMessage();
		if (binding.hasErrors()) {
			result = this.createEditModelAndView(messageForm,
					"message.error.field");
			if (message.getId() != 0) {
				result.setViewName("message/actor/move");
				result.addObject("isRead", true);
				result.addObject("isMove", true);
			}
		} else
			try {
				if (message.getId() != 0)
					this.messageService.moveMessage(message, message.getBox());
				else
					this.messageService.save(message);
				result = new ModelAndView("redirect:/box/actor/list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(messageForm,
						"message.commit.error");
				// result.addObject("oops", oops.getStackTrace());
				if (message.getId() != 0) {
					result.setViewName("message/actor/move");
					result.addObject("isRead", true);
					result.addObject("isMove", true);

				}
			}
		return result;
	}

	@RequestMapping(value = "/actor/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView edit(final MessageForm messageForm) {

		ModelAndView result;
		try {
			final Message message = messageForm.getMessage();
			this.messageService.delete(message);
			result = new ModelAndView("redirect:/box/actor/list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(messageForm,
					"message.commit.error");
			System.out.println("========== " + oops.getMessage()
					+ " ==========");
			result.setViewName("message/actor/move");
			result.addObject("isRead", true);
			result.addObject("isMove", true);
		}
		return result;
	}

	// CreateModelAndView

	protected ModelAndView createEditModelAndView(final MessageForm messageForm) {
		ModelAndView result;

		result = this.createEditModelAndView(messageForm, null);

		return result;

	}

	protected ModelAndView createEditModelAndView(
			final MessageForm entityMessage, final String message) {
		ModelAndView result;
		final Actor actor = this.actorService.findByUserAccount(LoginService
				.getPrincipal());
		final Collection<Actor> actors = this.actorService.findAll();
		actors.remove(actor);
		final Collection<String> priorities = this.configurationService
				.findAll().iterator().next().getPriorities();
		final String action = "message/administrator/broadcastMessage.do";

		result = new ModelAndView("message/actor/exchangeMessage");
		result.addObject("messageForm", entityMessage);
		result.addObject("message", message);
		result.addObject("receivers", actors);
		result.addObject("priorities", priorities);
		result.addObject("isRead", false);
		result.addObject("action", action);
		result.addObject("banner", this.configurationService.findAll()
				.iterator().next().getBanner());
		result.addObject("systemName", this.configurationService.findAll()
				.iterator().next().getSystemName());
		return result;
	}

	protected ModelAndView createEditModelAndViewMembers(
			final MessageForm messageForm) {
		ModelAndView result;

		result = this.createEditModelAndViewMembers(messageForm, null);

		return result;

	}

	protected ModelAndView createEditModelAndViewMembers(
			final MessageForm entityMessage, final String message) {
		ModelAndView result;
		final Collection<Member> actors = this.memberService.findAll();
		final Collection<String> priorities = this.configurationService
				.findAll().iterator().next().getPriorities();
		final String action = "message/administrator/broadcastMessageMembers.do";

		result = new ModelAndView("message/actor/exchangeMessageMembers");
		result.addObject("messageForm", entityMessage);
		result.addObject("message", message);
		result.addObject("receivers", actors);
		result.addObject("priorities", priorities);
		result.addObject("isRead", false);
		result.addObject("action", action);
		result.addObject("banner", this.configurationService.findAll()
				.iterator().next().getBanner());
		result.addObject("systemName", this.configurationService.findAll()
				.iterator().next().getSystemName());
		return result;
	}

	protected ModelAndView createEditModelAndViewBrotherhoods(
			final MessageForm messageForm) {
		ModelAndView result;

		result = this.createEditModelAndViewBrotherhoods(messageForm, null);

		return result;

	}

	protected ModelAndView createEditModelAndViewBrotherhoods(
			final MessageForm entityMessage, final String message) {
		ModelAndView result;
		final Collection<Brotherhood> actors = this.brotherhoodService
				.findAll();
		final Collection<String> priorities = this.configurationService
				.findAll().iterator().next().getPriorities();
		final String action = "message/administrator/broadcastMessageBrotherhoods.do";

		result = new ModelAndView("message/actor/exchangeMessageBrotherhoods");
		result.addObject("messageForm", entityMessage);
		result.addObject("message", message);
		result.addObject("receivers", actors);
		result.addObject("priorities", priorities);
		result.addObject("isRead", false);
		result.addObject("action", action);
		result.addObject("banner", this.configurationService.findAll()
				.iterator().next().getBanner());
		result.addObject("systemName", this.configurationService.findAll()
				.iterator().next().getSystemName());
		return result;
	}

}
