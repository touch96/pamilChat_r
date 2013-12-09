package pc.wsapi.dbs;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.validation.NotNull;

import play.db.ebean.Model;

@Entity
public class Users extends Model {
	
	@NotNull
	public String code;
	@NotNull
	public String password;
	
	
	public String createdt;
	
	@Id
	@NotNull
	public String token;
	
	
	public String getCode() {
		return code;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getCreatedt() {
		return (new Date()).toString();
	}
	
	
	public String getToken() {
		return token;
	}



	public static Finder<Long,Users> find = new Finder<Long,Users>(
		    Long.class, Users.class
		  ); 
}
