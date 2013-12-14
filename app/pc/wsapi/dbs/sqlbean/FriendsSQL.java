package pc.wsapi.dbs.sqlbean;

import java.sql.Timestamp;

import javax.persistence.Entity;

import com.avaje.ebean.annotation.Sql;

@Entity
@Sql
public class FriendsSQL {
	public String code;
	public String f_code;
	public Timestamp createdt;
	
	
	public Timestamp getCreatedt() {
		return createdt;
	}
	public void setCreatedt(Timestamp createdt) {
		this.createdt = createdt;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getF_code() {
		return f_code;
	}
	public void setF_code(String f_code) {
		this.f_code = f_code;
	}
	
	
}
