package pc.wsapi.dbs;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.validation.NotNull;

import play.db.ebean.Model;

@Entity
public class Friendrequest extends Model {
	
	@NotNull
	@Id
	public String code;
	
	@NotNull
	public String f_code;
	
	public boolean isnew;
	
//	@OneToMany(targetEntity=Friends.class, mappedBy="friendrequest")
//	public List<Friends> friends;
	
	/**
	 * 00 : 申請中
	 * 01 : 承認済
	 * 02 : 拒否
	 */
	public String status;

	public String getCode() {
		return code;
	}

	public String getF_code() {
		return f_code;
	}

	public boolean isnew() {
		return isnew;
	}

	public String getStatus() {
		return status;
	}
	
	@CreatedTimestamp
	public Date createdt;
	
	
	
	public Date getCreatedt() {
		return createdt;
	}

	public static Finder<Long,Friendrequest> find = new Finder<Long,Friendrequest>(
		    Long.class, Friendrequest.class
		  ); 


}
