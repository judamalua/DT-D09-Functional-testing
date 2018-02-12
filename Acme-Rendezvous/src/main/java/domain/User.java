
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Entity
@Access(AccessType.PROPERTY)
public class User extends Actor {

	// Constructors -----------------------------------------------------------

	// Attributes -------------------------------------------------------------

	// Relationships ----------------------------------------------------------
	private Collection<Rendezvous>	rendezvouss;
	private Rendezvous				rendezvous;
	private Collection<Rendezvous>	rendezvouss;
	private Collection<Answer>		answers;
	private Collection<Comment>		comments;


	@Valid
	@OneToMany
	public Collection<Rendezvous> getrendezvouss() {
		return this.rendezvouss;
	}

	public void setrendezvouss(final Collection<Rendezvous> rendezvouss) {
		this.rendezvouss = rendezvouss;

	}
	@Valid
	@NotNull
	@OneToOne(optional = false, mappedBy = "Rendezvous")
	public Rendezvous getrendezvous() {
		return this.rendezvous;
	}

	public void setrendezvous(final Rendezvous rendezvous) {
		this.rendezvous = rendezvous;

	}
	@Valid
	@ManyToMany
	public Collection<Rendezvous> getrendezvouss() {
		return this.rendezvouss;
	}

	public void setrendezvouss(final Collection<Rendezvous> rendezvouss) {
		this.rendezvouss = rendezvouss;

	}
	@Valid
	@OneToMany
	public Collection<Answer> getanswers() {
		return this.answers;
	}

	public void setanswers(final Collection<Answer> answers) {
		this.answers = answers;

	}
	@Valid
	@OneToMany
	public Collection<Comment> getcomments() {
		return this.comments;
	}

	public void setcomments(final Collection<Comment> comments) {
		this.comments = comments;

	}
}
