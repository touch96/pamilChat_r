package pc.wsapi.dbs.sqlbean;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import pc.wsapi.dbs.Friends;

import com.avaje.ebean.annotation.Sql;

@Entity
@Sql
public class UsersSQL {
	public String code;
//	public String password;
//	public String token;
//	
//	public String f_code;
	
//	@OneToMany(cascade=CascadeType.PERSIST)
//	@JoinColumn(name="f_code", referencedColumnName="f_code")
//	public List<Friends> friends;
	
	
	
//	public String getF_code() {
//		return f_code;
//	}
//	public void setF_code(String f_code) {
//		this.f_code = f_code;
//	}
//	public List<Friends> getFriends() {
//		return friends;
//	}
//	public void setFriends(List<Friends> friends) {
//		this.friends = friends;
//	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
//	public String getPassword() {
//		return password;
//	}
//	public void setPassword(String password) {
//		this.password = password;
//	}
//	public String getToken() {
//		return token;
//	}
//	public void setToken(String token) {
//		this.token = token;
//	}
//	
	
}
