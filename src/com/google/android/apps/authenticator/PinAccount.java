package com.google.android.apps.authenticator;

import com.google.android.apps.authenticator.testability.DependencyInjector;

//Custom Pin account class
class PinAccount {
	private String id;
	private String account;
	private String secret;
	private String otp;
	private String scheme;
	private OtpSource mOtpProvider;

	public PinAccount(String scheme, String id, String account, String secret) {
		this.setScheme(scheme);
		this.setId(id);
		this.setAccount(account);
		this.setSecret(secret);
		mOtpProvider = DependencyInjector.getOtpProvider();
		this.setOtp(computePin(this.getAccount(), this.getSecret()));
		// mTotpCounter = mOtpProvider.getTotpCounter();
		// mTotpClock = mOtpProvider.getTotpClock();
	}

	public String computePin(String user, String secret) {

		try {

			return mOtpProvider.getNextCode(user, secret);
		} catch (OtpSourceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "abc";
		}
	}

	public void setScheme(String scheme) {
		this.scheme = scheme;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

}