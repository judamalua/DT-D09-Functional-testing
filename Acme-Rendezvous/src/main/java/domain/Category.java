
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Access(AccessType.PROPERTY)
public class Category extends DomainEntity {

	// Constructors -----------------------------------------------------------

	// Attributes -------------------------------------------------------------
	private String	name;
	private String	description;


	@SafeHtml
	@NotBlank
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@SafeHtml
	@NotBlank
	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}


	// Relationships ----------------------------------------------------------
	private Collection<DomainService>	services;
	private Category			fatherCategory;


	@NotNull
	@ManyToMany(mappedBy = "categories")
	public Collection<DomainService> getServices() {
		return this.services;
	}

	public void setServices(final Collection<DomainService> services) {
		this.services = services;
	}

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public Category getFatherCategory() {
		return this.fatherCategory;
	}

	public void setFatherCategory(final Category fatherCategory) {
		this.fatherCategory = fatherCategory;
	}

}
