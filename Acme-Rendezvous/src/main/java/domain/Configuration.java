
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Access(AccessType.PROPERTY)
public class Configuration extends DomainEntity {

	// Constructors -----------------------------------------------------------

	// Attributes -------------------------------------------------------------
	private String	terms_eng;
	private String	terms_es;
	private String	cookies_eng;
	private String	cookies_es;
	private Integer	pageSize;


	@NotBlank
	public String getTerms_eng() {
		return this.terms_eng;
	}

	public void setTerms_eng(final String terms_eng) {
		this.terms_eng = terms_eng;
	}

	@NotBlank
	public String getTerms_es() {
		return this.terms_es;
	}

	public void setTerms_es(final String terms_es) {
		this.terms_es = terms_es;
	}

	@NotBlank
	public String getCookies_eng() {
		return this.cookies_eng;
	}

	public void setCookies_eng(final String cookies_eng) {
		this.cookies_eng = cookies_eng;
	}

	@NotBlank
	public String getCookies_es() {
		return this.cookies_es;
	}

	public void setCookies_es(final String cookies_es) {
		this.cookies_es = cookies_es;
	}

	@NotNull
	public Integer getPageSize() {
		return this.pageSize;
	}

	public void setPageSize(final Integer pageSize) {
		this.pageSize = pageSize;
	}

	// Relationships ----------------------------------------------------------

}
