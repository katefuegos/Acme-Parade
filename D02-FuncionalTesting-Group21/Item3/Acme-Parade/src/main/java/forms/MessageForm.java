
package forms;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.validation.Valid;

import domain.Message;

@Access(AccessType.PROPERTY)
public class MessageForm {

	// Attributes------------------------------------------------------------------
	private Message	message;


	// Constructor------------------------------------------------------------------

	public MessageForm() {
		super();
	}

	// Getter and Setters------------------------------------------------------------
	@Valid
	public Message getMessage() {
		return this.message;
	}

	public void setMessage(final Message message) {
		this.message = message;
	}

}
