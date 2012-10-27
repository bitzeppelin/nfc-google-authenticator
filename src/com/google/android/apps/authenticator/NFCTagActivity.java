package com.google.android.apps.authenticator;

import java.util.HashMap;

import android.app.Activity;
import android.app.PendingIntent;

import android.content.Intent;
import android.content.IntentFilter;

import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcF;
import android.os.Bundle;
import android.widget.ListView;

import com.google.android.apps.authenticator2.R;

public class NFCTagActivity extends Activity {
	private NfcAdapter nfcAdapter;
	private PendingIntent nfcPendingIntent;
	private IntentFilter[] nfcFilters;
	private String[][] nfcTechLists;
	private ListView mUserList;
	private HashMap<String, PinAccount> mUsers;
	private UserListAdapter mUserAdapter;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nfc_scan_activity);
		// ListView
		mUserList = (ListView) findViewById(R.id.user_list);
		mUsers = new HashMap<String, PinAccount>();
		mUserAdapter = new UserListAdapter(this, R.layout.user_row, mUsers);
		mUserList.setAdapter(mUserAdapter);
		// NFC handler
		nfcAdapter = NfcAdapter.getDefaultAdapter(this);

		nfcPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
				getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

		IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
		ndef.addDataScheme("totp");
		nfcFilters = new IntentFilter[] { ndef, };

		// Setup a tech list for all NfcF tags
		nfcTechLists = new String[][] { new String[] { NfcF.class.getName() } };

		if (getIntent().getAction()
				.equals("android.nfc.action.NDEF_DISCOVERED")) {
			// Manually Reading the tag
			this.loadTag(getIntent());
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		nfcAdapter.enableForegroundDispatch(this, nfcPendingIntent, nfcFilters,
				nfcTechLists);

	}

	@Override
	protected void onPause() {
		super.onPause();
		nfcAdapter.disableForegroundDispatch(this);
	}

	@Override
	public void onNewIntent(Intent intent) {
		this.loadTag(intent);

	}

	public void loadTag(Intent intent) {
		Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
		Ndef ndefTag = Ndef.get(tag);

		NdefMessage ndefMesg = ndefTag.getCachedNdefMessage();
		try {

			PinAccount account = Utilities.getAccountFromString(ndefMesg);
			mUsers = mUserAdapter.users;
			mUsers.put(account.getId(), account);
			mUserAdapter.updateItems(mUsers);
			mUserAdapter.notifyDataSetChanged();

		} catch (RuntimeException e) {
			e.printStackTrace();
		}
	}

}