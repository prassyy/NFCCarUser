package com.tonybeltramelli.mobile.nfcp2pcommunication.activity;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.EditText;
import android.widget.Toast;

import com.tonybeltramelli.mobile.nfcp2pcommunication.R;
import com.tonybeltramelli.mobile.nfcp2pcommunication.utils.NFCUtils;

public class MainActivity extends Activity
{
	private NfcAdapter _nfcAdapter;
	private PendingIntent _pendingIntent;
	private IntentFilter[] _intentFilters;
	private String carAccess = "abc1";
	private final String _MIME_TYPE = "text/plain";
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		_init();
	}
	
	private void _init()
	{
		_nfcAdapter = NfcAdapter.getDefaultAdapter(this);

		
		if (_nfcAdapter == null)
		{
			Toast.makeText(this, "This device does not support NFC.", Toast.LENGTH_LONG).show();
			return;
		}
		
		if (_nfcAdapter.isEnabled())
		{

			_pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
			
			IntentFilter ndefDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
			try
			{
                Toast.makeText(this, "Inside try", Toast.LENGTH_LONG).show();
                ndefDetected.addDataType(_MIME_TYPE);
			} catch (MalformedMimeTypeException e)
			{
				Log.e(this.toString(), e.getMessage());
			}
			
			_intentFilters = new IntentFilter[] { ndefDetected };
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		
	}
	

	@Override
	protected void onNewIntent(Intent intent)
	{
		super.onNewIntent(intent);
        Toast.makeText(this, "New Intent", Toast.LENGTH_LONG).show();

		if(NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction()))
		{
            Toast.makeText(this, "NDEF discovered", Toast.LENGTH_LONG).show();
			List<String> msgs = NFCUtils.getStringsFromNfcIntent(intent);
			if(msgs.get(0).equals(carAccess)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.accessGranted);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
            else {

                 AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.accessDenied);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
		}
	}
//    protected void onNewIntent(Intent intent)
//    {
//        super.onNewIntent(intent);
//        if(NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction()))
//        {
//            List<String> msgs = NFCUtils.getStringsFromNfcIntent(intent);
//           // accessKey = msgs.get(0);
//            if(msgs.get(0).equals(carAccess)) {
//                Toast.makeText(this, "Car Granted", Toast.LENGTH_LONG).show(); }
//            else
//            { Toast.makeText(this, "Access not received", Toast.LENGTH_LONG).show();}
//            // Toast.makeText(this, "Access received", Toast.LENGTH_LONG).show();
//        }
//    }
}
