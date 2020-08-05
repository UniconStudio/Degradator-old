package app.unicon.degradator;

import android.os.*;
import android.view.*;
import android.widget.*;
import android.graphics.*;
import android.app.Activity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.app.AlertDialog;
import android.content.Intent;
import android.animation.ObjectAnimator;
import android.content.SharedPreferences;
import android.view.View;
import android.animation.Animator;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomSheetDialog;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.enums.SnackbarType;
import com.nispok.snackbar.listeners.ActionClickListener;

import java.util.Random;

public class AppActivity extends Activity {
	String str = "";
	String[] quests;
	double r = 0;
	double b = 0;
	LinearLayout linear2;
	LinearLayout linear6;
	TextView textview1;
	BottomNavigationView bottom;
	
	AlertDialog.Builder dialog;
	// Intent zz = new Intent();
	AlertDialog.Builder dz;
	ObjectAnimator q = new ObjectAnimator();
	ObjectAnimator fly = new ObjectAnimator();
	RequestNetwork rq;
	RequestNetwork.RequestListener _rq_request_listener;
	
	ObjectAnimator dxi = new ObjectAnimator();
	ObjectAnimator flyx = new ObjectAnimator();
	SharedPreferences file;

	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.app);
		linear2 = findViewById(R.id.linear2);

		linear6 = findViewById(R.id.linear6);
		textview1 = findViewById(R.id.textview1);
		bottom = findViewById(R.id.bottom);

		dialog = new AlertDialog.Builder(this);
		dz = new AlertDialog.Builder(this);
		rq = new RequestNetwork(this);
		file = getSharedPreferences("file", Activity.MODE_PRIVATE);

		linear2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				if(file.getString("animation","true") == "true"){
					fly.setTarget(linear6);
					fly.setDuration(180);
					fly.setPropertyName("translationX");
					fly.setFloatValues(1000);
					fly.start();
				}else{
					// no animation
					_ault();
				}
			}
		});

		fly.addListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationStart(Animator _param1) {}
			
			@Override
			public void onAnimationEnd(Animator _param1) {
				_ault();

				b = getRandom((-360), (360));
				q.setTarget(linear6);
				q.setDuration(80);
				q.setPropertyName("rotation");
				q.setFloatValues((float)(b));
				q.start();

				dxi.setTarget(textview1);
				dxi.setDuration(80);
				dxi.setPropertyName("rotation");
				dxi.setFloatValues((float)(360 - b));
				dxi.start();

				flyx.setTarget(linear6);
				flyx.setDuration(180);
				flyx.setPropertyName("translationX");
				flyx.setFloatValues(0);
				flyx.start();
			}
			
			@Override
			public void onAnimationCancel(Animator _param1) {}
			
			@Override
			public void onAnimationRepeat(Animator _param1) {}
		});
		
		_rq_request_listener = new RequestNetwork.RequestListener() {
			@Override
			public void onResponse(String _param1, String _param2) {
				final String _tag = _param1;
				final String _response = _param2;
			}
			
			@Override
			public void onErrorResponse(String _param1, String _param2) {
				final String _tag = _param1;
				final String _message = _param2;
			}
		};

		str = FileUtil.readFile(VersionInfo.appdir + "default.json");
		quests = str.split(";");

		_ault();
		bottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() { 
				@Override 
				public boolean onNavigationItemSelected(MenuItem item){
					bottom.clearAnimation();
					
					if(item.getItemId() == R.id.action_settings){
						final BottomSheetDialog bsd = new BottomSheetDialog(AppActivity.this); 
						View infla = getLayoutInflater().inflate(R.layout.settings, null);  
						bsd.setContentView(infla);  
						bsd.show(); 
						final Switch switch1 = infla.findViewById(R.id.switch1);
						final Switch switch3 = infla.findViewById(R.id.switch3);
						final Switch switch4 = infla.findViewById(R.id.switch4);
						
						if (file.getString("autoupdate", "true").contains("false")) {
							switch1.setChecked(false);
						}
						if (file.getString("vulgar", "true").contains("true")) {
							switch3.setChecked(true);
						}
						
						if (file.getString("animation", "true").contains("true")) {
							switch4.setChecked(true);
						}
						
						switch1.getTrackDrawable().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_IN);
						switch3.getTrackDrawable().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_IN);
						switch4.getTrackDrawable().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_IN);
						
						switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
								@Override
								public void onCheckedChanged(CompoundButton _param1, boolean _param2)  {
									final boolean _isChecked = _param2;
									if (_isChecked) {
										file.edit().putString("autoupdate", "true").commit();
									}
									else {
										file.edit().putString("autoupdate", "false").commit();
									}
								}
							});

							
							
						switch3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
								@Override
								public void onCheckedChanged(CompoundButton _param1, boolean _param2)  {
									final boolean _isChecked = _param2;
										if(switch1.isChecked()){
											file.edit().putString("vulgar", "true").commit();
											
										Snackbar.with(getApplicationContext()) // context
											.text("Требуется перезапуск, для применения параметров.")
											.type(SnackbarType.MULTI_LINE)
											.duration(Snackbar.SnackbarDuration.LENGTH_INDEFINITE)
											.actionLabel("OK")
											.actionListener(new ActionClickListener() {
												@Override
												public void onActionClicked(Snackbar snackbar) {
    												Intent i = new Intent();
													i.setClass(getApplicationContext(), MainActivity.class);
													startActivity(i);
													finishAffinity();
												}
											}) 
											.show(AppActivity.this);
											bsd.hide();
											}else{ // if no auto update
												Snackbar.with(getApplicationContext()) // context
													.text("Сначала включите автообновление")
													.type(SnackbarType.MULTI_LINE)
													.duration(Snackbar.SnackbarDuration.LENGTH_INDEFINITE)
													.actionLabel("OK")
													.actionListener(new ActionClickListener() {
														@Override
														public void onActionClicked(Snackbar snackbar) {
															bsd.show();
														}
													}) 
													.show(AppActivity.this);
												bsd.hide();
												switch3.setChecked(false);
											}

								}
							});
						switch4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
								@Override
								public void onCheckedChanged(CompoundButton _param1, boolean _param2)  {
									final boolean _isChecked = _param2;
									if (_isChecked) {
										file.edit().putString("animation", "true").commit();
									}
									else {
										file.edit().putString("animation", "false").commit();
										linear6.setRotation(0);
										textview1.setRotation(0);
									}
								}
							});
					}else{
						
						final BottomSheetDialog bsd = new BottomSheetDialog(AppActivity.this); 
						View infla = getLayoutInflater().inflate(R.layout.about, null);  
						bsd.setContentView(infla);
						

						bsd.show(); 
					}
					
					
					
					
				return false;
				
					}});
					
					
		}

	void _ault () {
		if(quests != null) {
			r = getRandom(0, (quests.length - 1));
			textview1.setText(quests[(int)r]);
		}
	}

	public static int getRandom(int _min, int _max) {
		Random random = new Random();
		return random.nextInt(_max - _min + 1) + _min;
	}
}
