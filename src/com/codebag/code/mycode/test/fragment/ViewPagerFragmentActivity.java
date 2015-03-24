package com.codebag.code.mycode.test.fragment;

import com.codebag.R;
import com.codebag.code.mycode.utils.Log;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ViewPagerFragmentActivity extends FragmentActivity {

	int rootId = 123456;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.addLog("peter", this, "ViewPagerFragmentActivity====" + "onCreate");
		
		setContentView(R.layout.activity_root);
		FrameLayout fl = (FrameLayout) findViewById(R.id.container);
		
		View.inflate(this, R.layout.viewpager_fragment, fl);
		
		ViewPager vp = (ViewPager) findViewById(R.id.page_container);
		
		Fragment[] fragments = new Fragment[]{
				new MyFragment() {
					@Override
					public View onCreateView(LayoutInflater inflater, ViewGroup container,
							Bundle savedInstanceState) {
						Log.addLog("peter", this, "MyFragment====" + "onCreateView");
						LinearLayout ll = new LinearLayout(getActivity());
						ll.setOrientation(LinearLayout.VERTICAL);
						
						TextView v = new TextView(ViewPagerFragmentActivity.this);
						v.setText("fragment1");
						v.setTextSize(50);
						
						Button bt = new Button(getActivity());
						bt.setText("startActivityForResult()");
						bt.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								Intent intent = new Intent(getActivity(), ResultActivity.class);
								startActivityForResult(intent, 1);
							}
						});
						
						ll.addView(v);
						ll.addView(bt);
						return ll;
						
					}

					/**
					 * 	注意坑：被启动的activity是singleTask, singleInstance时会出错。onActivityResult会提前被调用		 
					 */
					@Override
					public void onActivityResult(int requestCode,
							int resultCode, Intent data) {
						
						if(requestCode == 1) {
							Log.addLog("peter", this, "onActivityResult");
						}
						super.onActivityResult(requestCode, resultCode, data);
					}
					
					
				},
				
				new MyFragment() {
					@Override
					public View onCreateView(LayoutInflater inflater, ViewGroup container,
							Bundle savedInstanceState) {
						Log.addLog("peter", this, "MyFragment====" + "onCreateView");
						TextView v = new TextView(ViewPagerFragmentActivity.this);
						v.setText("fragment2");
						v.setTextSize(50);
						return v;
						
					}
				}
		};
		
		PageAdapter adapter = new PageAdapter(getSupportFragmentManager(), fragments);
		
		vp.setAdapter(adapter);
		
		final View line = new View(ViewPagerFragmentActivity.this);
		DisplayMetrics dm = getResources().getDisplayMetrics();
		final int width = dm.widthPixels;
		line.setBackgroundColor(Color.BLUE);
		FrameLayout.LayoutParams p = new FrameLayout.LayoutParams(width/2, 15);
		fl.addView(line, p);
		
		vp.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				
			}
			
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				FrameLayout.LayoutParams p = (FrameLayout.LayoutParams) line.getLayoutParams();
				p.width = width/2;
				p.leftMargin = (int)(position * p.width + positionOffset * p.width);
				Log.addLog("peter", this, "positionOffset=" + positionOffset);
				line.setLayoutParams(p);
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
	}

	
    public class PageAdapter extends FragmentPagerAdapter {

    	Fragment[] mFragments;

		public PageAdapter(FragmentManager fm, Fragment[] fragments) {
			super(fm);
			mFragments = fragments;
		}

		@Override
		public Fragment getItem(int arg0) {
			return mFragments[arg0];
		}

		@Override
		public int getCount() {
			return mFragments.length;
		}

    }
	
	@Override
	protected void onStart() {
		super.onStart();
		Log.addLog("peter", this, "ViewPagerFragmentActivity====" + "onStart");
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.addLog("peter", this, "ViewPagerFragmentActivity====" + "onResume");
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.addLog("peter", this, "ViewPagerFragmentActivity====" + "onPause");
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.addLog("peter", this, "ViewPagerFragmentActivity====" + "onStop");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.addLog("peter", this, "ViewPagerFragmentActivity====" + "onDestroy");
	}
	
	public static class MyFragment extends Fragment{

		@Override
		public void onActivityResult(int requestCode, int resultCode,
				Intent data) {
			// TODO Auto-generated method stub
			super.onActivityResult(requestCode, resultCode, data);
			Log.addLog("peter", this, "MyFragment====" + "onActivityResult");
		}

		@Override
		public void onAttach(Activity activity) {
			// TODO Auto-generated method stub
			super.onAttach(activity);
			Log.addLog("peter", this, "MyFragment====" + "onAttach");
		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			Log.addLog("peter", this, "MyFragment====" + "onCreate");
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			Log.addLog("peter", this, "MyFragment====" + "onCreateView");
			return super.onCreateView(inflater, container, savedInstanceState);
			
		}

		@Override
		public void onViewCreated(View view, Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onViewCreated(view, savedInstanceState);
			Log.addLog("peter", this, "MyFragment====" + "onViewCreated");
		}

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onActivityCreated(savedInstanceState);
			Log.addLog("peter", this, "MyFragment====" + "onActivityCreated");
		}

		@Override
		public void onStart() {
			// TODO Auto-generated method stub
			Log.addLog("peter", this, "MyFragment====" + "onStart");
			super.onStart();
		}

		@Override
		public void onResume() {
			// TODO Auto-generated method stub
			Log.addLog("peter", this, "MyFragment====" + "onResume");
			super.onResume();
		}

		@Override
		public void onPause() {
			// TODO Auto-generated method stub
			Log.addLog("peter", this, "MyFragment====" + "onPause");
			super.onPause();
		}

		@Override
		public void onStop() {
			// TODO Auto-generated method stub
			Log.addLog("peter", this, "MyFragment====" + "onStop");
			super.onStop();
		}

		@Override
		public void onDestroyView() {
			// TODO Auto-generated method stub
			Log.addLog("peter", this, "MyFragment====" + "onDestroyView");
			super.onDestroyView();
		}

		@Override
		public void onDestroy() {
			// TODO Auto-generated method stub
			Log.addLog("peter", this , "MyFragment====" + "onDestroy");
			super.onDestroy();
		}

		@Override
		public void onDetach() {
			// TODO Auto-generated method stub
			Log.addLog("peter", this, "MyFragment====" + "onDetach");
			super.onDetach();
		}
		
	}
	
}
