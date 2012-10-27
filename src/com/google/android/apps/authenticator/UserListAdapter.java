package com.google.android.apps.authenticator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.android.apps.authenticator.testability.DependencyInjector;
import com.google.android.apps.authenticator2.R;

class UserListAdapter extends BaseAdapter {
	private Context context;
	private List<PinAccount> items;
	public HashMap<String, PinAccount> users;
	private double mTotpCountdownPhase;
	private TotpCountdownTask mTotpCountdownTask;
	private TotpCounter mTotpCounter;
	private TotpClock mTotpClock;
	private OtpSource mOtpProvider;

	public UserListAdapter(Context context, int userRowId,
			HashMap<String, PinAccount> items) {
		this.context = context;
		this.items = new ArrayList<PinAccount>(items.values());
		this.users = items;
		mOtpProvider = DependencyInjector.getOtpProvider();
		mTotpCounter = mOtpProvider.getTotpCounter();
		mTotpClock = mOtpProvider.getTotpClock();
		updateCodesAndStartTotpCountdownTask();
	}

	// Counter helpers
	private void stopTotpCountdownTask() {
		if (mTotpCountdownTask != null) {
			mTotpCountdownTask.stop();
			mTotpCountdownTask = null;
		}
	}

	private void updateCodesAndStartTotpCountdownTask() {
		stopTotpCountdownTask();

		mTotpCountdownTask = new TotpCountdownTask(mTotpCounter, mTotpClock,
				AuthenticatorActivity.TOTP_COUNTDOWN_REFRESH_PERIOD);
		mTotpCountdownTask.setListener(new TotpCountdownTask.Listener() {
			@Override
			public void onTotpCountdown(long millisRemaining) {

				setTotpCountdownPhaseFromTimeTillNextValue(millisRemaining);
			}

			@Override
			public void onTotpCounterValueChanged() {

				refreshVerificationCodes();
			}
		});

		mTotpCountdownTask.startAndNotifyListener();
	}

	private void setTotpCountdownPhaseFromTimeTillNextValue(long millisRemaining) {
		setTotpCountdownPhase(((double) millisRemaining)
				/ Utilities.secondsToMillis(mTotpCounter.getTimeStep()));
	}

	private void refreshVerificationCodes() {
		refreshUserList();
		setTotpCountdownPhase(1.0);
	}

	public void refreshUserList(boolean isAccountModified) {
		// Deleting all items
		this.items.clear();
		this.users.clear();
		this.notifyDataSetChanged();
	}

	protected void refreshUserList() {
		refreshUserList(false);
	}

	private void setTotpCountdownPhase(double phase) {
		mTotpCountdownPhase = phase;
		updateCountdownIndicators();
	}

	private void updateCountdownIndicators() {

		for (int i = 0, len = this.getCount(); i < len; i++) {
			View listEntry = this.getView(i, null, null);
			CountdownIndicator indicator = (CountdownIndicator) listEntry
					.findViewById(R.id.countdown_icon);
			if (indicator != null) {
				indicator.setPhase(mTotpCountdownPhase);
			}
		}
		this.notifyDataSetChanged();
	}

	// Counter Helpers
	@Override
	public int getCount() {
		return this.items.size();
	}

	public void updateItems(HashMap<String, PinAccount> items) {
		this.items = new ArrayList<PinAccount>(items.values());
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) this.context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		PinAccount currentPin = this.items.get(position);

		View row;
		if (convertView != null) {
			// Reuse an existing view
			row = convertView;
		} else {
			// Create a new view
			row = inflater.inflate(R.layout.user_row, null);
		}
		TextView pinView = (TextView) row.findViewById(R.id.pin_value);
		TextView userView = (TextView) row.findViewById(R.id.current_user);
		View buttonView = row.findViewById(R.id.next_otp);
		CountdownIndicator countdownIndicator = (CountdownIndicator) row
				.findViewById(R.id.countdown_icon);

		// TOTP, so no button needed
		buttonView.setVisibility(View.GONE);
		buttonView.setOnClickListener(null);
		row.setTag(null);

		countdownIndicator.setVisibility(View.VISIBLE);
		countdownIndicator.setPhase(mTotpCountdownPhase);

		// mTotpCountdownPhase
		if (this.context.getString(R.string.empty_pin).equals(
				currentPin.getOtp())) {
			pinView.setTextScaleX(AuthenticatorActivity.PIN_TEXT_SCALEX_UNDERSCORE);
		} else {
			pinView.setTextScaleX(AuthenticatorActivity.PIN_TEXT_SCALEX_NORMAL);
		}
		pinView.setText(currentPin.getOtp());
		userView.setText(currentPin.getAccount());

		return row;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
}