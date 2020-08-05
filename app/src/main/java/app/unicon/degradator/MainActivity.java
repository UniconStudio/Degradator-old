package app.unicon.degradator;

import android.app.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.content.*;
import android.graphics.*;
import android.media.*;
import android.net.*;
import android.text.*;
import android.util.*;
import android.webkit.*;
import android.animation.*;
import android.view.animation.*;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.text.*;
import android.app.Activity;
import java.util.HashMap;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ProgressBar;
import java.util.Timer;
import java.util.TimerTask;
import android.content.Intent;
import android.net.Uri;
import android.content.SharedPreferences;
import android.Manifest;
import android.content.pm.PackageManager;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.enums.SnackbarType;
import com.nispok.snackbar.listeners.ActionClickListener;

public class MainActivity extends Activity {
	LinearLayout linear1;
	LinearLayout linear2;
	LinearLayout linear3;
	TextView textview1;
	TextView textview2;
	ProgressBar progressbar1;
	RequestNetwork net;
	RequestNetwork.RequestListener _net_request_listener;
	Intent i = new Intent();
	SharedPreferences file;
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.main);

		linear1 = findViewById(R.id.linear1);
		linear2 = findViewById(R.id.linear2);
		linear3 = findViewById(R.id.linear3);
		textview1 = findViewById(R.id.textview1);
		textview2 = findViewById(R.id.textview2);
		progressbar1 = findViewById(R.id.progressbar1);
		net = new RequestNetwork(this);
		file = getSharedPreferences("file", Activity.MODE_PRIVATE);

		if(!FileUtil.isExistFile(VersionInfo.appdir))
			FileUtil.makeDir(VersionInfo.appdir);
		
		final MainActivity main = this;
		_net_request_listener = new RequestNetwork.RequestListener() {
			@Override
			public void onResponse(String _param1, String _param2) {
				final String _tag = _param1;
				final String _response = _param2;

				FileUtil.writeFile(VersionInfo.appdir + "default.json", _response);
				i.setClass(getApplicationContext(), AppActivity.class);
				startActivity(i);
				finish();
			}
			
			@Override
			public void onErrorResponse(String _param1, String _param2) {
				final String _tag = _param1;
				final String _message = _param2;

				if (FileUtil.isExistFile(VersionInfo.appdir + "default.json")) {
					i.setClass(getApplicationContext(), AppActivity.class);
					startActivity(i);
				}
				else {
					    Snackbar.with(getApplicationContext()) // context
						.text("Ошибка! Проверьте подключение к интернету.")
						.type(SnackbarType.MULTI_LINE)
						.duration(Snackbar.SnackbarDuration.LENGTH_INDEFINITE)
						.actionLabel("Повторить")
						.actionListener(new ActionClickListener() {
							@Override
							public void onActionClicked(Snackbar snackbar) {
								
								i.setClass(getApplicationContext(), MainActivity.class);
								startActivity(i);
								finishAffinity();
							}
						}) 
						.show(MainActivity.this);
					}
			}
		};

		if (file.getString("autoupdate", "true") == "true") {
			String vulgar = "0";
			if(file.getString("vulgar","false") == "true")
				vulgar = "1";
			net.startRequestNetwork(RequestNetworkController.GET, VersionInfo.geturl + "?vulgar=" + vulgar, "get", _net_request_listener);
		}
		else {
			i.setClass(getApplicationContext(), AppActivity.class);
			startActivity(i);
			finish();
		}
	}

}
