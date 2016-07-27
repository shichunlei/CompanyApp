package com.cells.companyapp.been;

import com.cells.companyapp.utils.ApiUtils;

public class User {

	/** ID */
	private int id;
	/** 用户名 */
	private String name;
	/** 邮箱 */
	private String email;
	/** 性别 */
	private String gender;
	/** 个人(企业)用户头像 */
	private String avatar;
	/** TOKEN */
	private String authentication_token;

	/** 企业名称 */
	private String c_name;
	/** 企业注册地址 */
	private String addr;
	/** 企业类型 */
	private String c_type;
	/** 经营范围 */
	private String scopes;
	/** 企业成立日期 */
	private String established;
	/** 企业营业期限 */
	private String expire;
	/** 工商执照注册号 */
	private String license;
	/** 资料认证审核 */
	private String verify;
	/** 公司LOGO */
	private String logo;

	/** 状态 */
	private boolean status;
	private String error;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getAuthentication_token() {
		return authentication_token;
	}

	public void setAuthentication_token(String authentication_token) {
		this.authentication_token = authentication_token;
	}

	public String getAvatar() {
		return (ApiUtils.ROOT_URL + avatar);
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getAuth_token() {
		return authentication_token;
	}

	public void setAuth_token(String authentication_token) {
		this.authentication_token = authentication_token;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getC_type() {
		return c_type;
	}

	public void setC_type(String c_type) {
		this.c_type = c_type;
	}

	public String getScopes() {
		return scopes;
	}

	public void setScopes(String scopes) {
		this.scopes = scopes;
	}

	public String getExpire() {
		return expire;
	}

	public void setExpire(String expire) {
		this.expire = expire;
	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}

	public String getVerify() {
		return verify;
	}

	public void setVerify(String verify) {
		this.verify = verify;
	}

	public String getLogo() {
		return (ApiUtils.ROOT_URL + logo);
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getC_name() {
		return c_name;
	}

	public void setC_name(String c_name) {
		this.c_name = c_name;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getEstablished() {
		return established;
	}

	public void setEstablished(String established) {
		this.established = established;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", email=" + email
				+ ", gender=" + gender + ", avatar=" + avatar
				+ ", authentication_token=" + authentication_token
				+ ", c_name=" + c_name + ", addr=" + addr + ", c_type="
				+ c_type + ", scopes=" + scopes + ", established="
				+ established + ", expire=" + expire + ", license=" + license
				+ ", verify=" + verify + ", logo=" + logo + ", status="
				+ status + ", error=" + error + "]";
	}

}
