package com.dhiraj.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Entity
public class UserRegistration extends BaseEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "First Name should not be blank")
	private String firstName;

	private String middleName;

	@NotBlank(message = "Last Name should not be blank")
	private String lastName;

	@Email(message = "Enter Correct Email Address")
	@Column(unique = true)
	private String email;
	
	@Pattern(regexp = "^[0-9]{10}$", message = "Contact Number should be 10 digit only.")
	@Column(nullable = false, unique = true)
	private String contact;
	
//	@Pattern(
//	        regexp = "(?=^.{8,}$)(?=.*\\d)(?=.*\\W+)(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$",
//	        message = "Password must be between 8 and 16 characters long, and include at least one uppercase letter, one lowercase letter, one number, and one special character."
//	    )
	
	@Pattern(regexp = "(?=^.{8,}$).*$", message = "Password must be grater than 8 characters.")
    @Pattern(regexp = "(?=.*[a-z]).*$", message = "Password must contain one lowercase letter.")
    @Pattern(regexp = "(?=.*[A-Z]).*$", message = "Password must contain one uppercase letter.")
    @Pattern(regexp = "(?=.*\\d).*$", message = "Password must contain one digit.")
    @Pattern(regexp = "(?=.*\\W+).*$", message = "Password must contain one special character.")
    @Pattern(regexp = "(?=\\S+$).*$", message = "Password must contain no whitespace.")
	private String password;
	
	private String otp;
	
	@Column(nullable = false)
	private int status;

	@ManyToOne
	@JoinColumn(name = "role")
	private Role role;
    
	public UserRegistration() {
		super();
	}

	public UserRegistration(Long id, String firstName, String middleName, String lastName, String email, String contact,
			String password, String otp, int status, Role role) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.email = email;
		this.contact = contact;
		this.password = password;
		this.otp = otp;
		this.status = status;
		this.role = role;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() { return firstName; }

	public void setFirstName(String firstName) { this.firstName = firstName; }

	public String getMiddleName() {	return middleName; }

	public void setMiddleName(String middleName) { this.middleName = middleName; }

	public String getLastName() { return lastName; }

	public void setLastName(String lastName) { this.lastName = lastName; }

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

}
