package ZMetalHeartY.demo.Entity;


import org.springframework.stereotype.Component;

@Component
public class FlatformAdminGroup implements Comparable<FlatformAdminGroup>{

	private Integer ag_id;

	private String ag_name;

	private String role;

	private String ag_info;

	private String ag_auth;

	private Integer ag_status;

	private Integer u_id;

	private String create_time;

	private Integer delete_status;

	private String delete_time;


	public Integer getAg_id() {
		return ag_id;
	}

	public void setAg_id(Integer ag_id) {
		this.ag_id = ag_id;
	}

	public String getAg_name() {
		return ag_name;
	}

	public void setAg_name(String ag_name) {
		this.ag_name = ag_name;
	}

	public String getAg_info() {
		return ag_info;
	}

	public void setAg_info(String ag_info) {
		this.ag_info = ag_info;
	}

	public String getAg_auth() {
		return ag_auth;
	}

	public void setAg_auth(String ag_auth) {
		this.ag_auth = ag_auth;
	}

	public Integer getAg_status() {
		return ag_status;
	}

	public void setAg_status(Integer ag_status) {
		this.ag_status = ag_status;
	}

	public Integer getU_id() {
		return u_id;
	}

	public void setU_id(Integer u_id) {
		this.u_id = u_id;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public Integer getDelete_status() {
		return delete_status;
	}

	public void setDelete_status(Integer delete_status) {
		this.delete_status = delete_status;
	}

	public String getDelete_time() {
		return delete_time;
	}

	public void setDelete_time(String delete_time) {
		this.delete_time = delete_time;
	}

	@Override
	public int compareTo(FlatformAdminGroup o) {
		return 0;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
}