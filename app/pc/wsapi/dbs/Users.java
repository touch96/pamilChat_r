package pc.wsapi.dbs;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.avaje.ebean.validation.NotNull;

import play.db.ebean.Model;

@Entity
public class Users extends Model {
	
	@NotNull
	public String id;
	@NotNull
	public String pw;
	public String createdt;
	
	@Id
	@NotNull
	public String m_id;
	
	
	public String getId() {
		return id;
	}
	
	public String getPw() {
		return pw;
	}
	
	public String getCreatedt() {
		return (new Date()).toString();
	}
	
	
	public String getM_id() {
		return m_id;
	}



	public static Finder<Long,Users> find = new Finder<Long,Users>(
		    Long.class, Users.class
		  ); 
}
