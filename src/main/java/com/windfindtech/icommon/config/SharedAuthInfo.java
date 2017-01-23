package com.windfindtech.icommon.config;

/**
 * Created by cplu on 2016/11/4.
 */

public class SharedAuthInfo {
	private String userName;
	private String password;    // decrypted
	private String passwordEnc; // encrypted
	private String tokenProd;  // decrypted
	private String tokenProdEnc; // encrypted
	private String tokenTest;    // decrypted
	private String tokenTestEnc;   // encryped

	public SharedAuthInfo() {

	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPasswordEnc() {
		return passwordEnc;
	}

	public void setPasswordEnc(String passwordEnc) {
		this.passwordEnc = passwordEnc;
	}

	public String getTokenProd() {
		return tokenProd;
	}

	public void setTokenProd(String authTokenRelease) {
		this.tokenProd = authTokenRelease;
	}

	public String getTokenProdEnc() {
		return tokenProdEnc;
	}

	public void setTokenProdEnc(String authTokenReleaseEnc) {
		this.tokenProdEnc = authTokenReleaseEnc;
	}

	public String getTokenTest() {
		return tokenTest;
	}

	public void setTokenTest(String authTokenDebug) {
		this.tokenTest = authTokenDebug;
	}

	public String getTokenTestEnc() {
		return tokenTestEnc;
	}

	public void setTokenTestEnc(String authTokenDebugEnc) {
		this.tokenTestEnc = authTokenDebugEnc;
	}
}
