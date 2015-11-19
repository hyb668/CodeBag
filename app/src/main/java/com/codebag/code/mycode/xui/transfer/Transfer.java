package com.codebag.code.mycode.xui.transfer;

import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.codebag.R;
import com.codebag.bag.Entry;
import com.codebag.bag.MyCode;
import com.codebag.bag.main.InovkedViewActivity;

public class Transfer extends MyCode {

	View music;
	
	public Transfer(InovkedViewActivity act) {
		super(act);
	}

	@Entry
	public void trans() {
		showView(R.layout.curve_path);
		music = getActivity().findViewById(R.id.music);
		music.post(new Runnable() {
			
			@Override
			public void run() {
				anim();
			}
		});
		

	}

	private void anim() {
		AnimatorPath path = new AnimatorPath();
		View root = getActivity().findViewById(R.id.rootview);
		int x = root.getWidth();
		int y = root.getHeight();
		
		path.moveTo(0, y);
		path.curveTo(0, y, x/2, 0, x, y);

		ObjectAnimator anim = ObjectAnimator.ofObject(this, "MusicLoc",
				new PathEvaluator(), path.getPoints().toArray());
		anim.setRepeatCount(ObjectAnimator.INFINITE);
		anim.setDuration(2000);
		anim.start();

		music.setPivotX(music.getWidth() / 2f);
		music.setPivotY(music.getHeight() / 2f);
		ObjectAnimator xa = ObjectAnimator.ofFloat(music, "scaleX", 0.2f, 1,
				0.2f);
		ObjectAnimator ya = ObjectAnimator.ofFloat(music, "scaleY", 0.2f, 1,
				0.2f);

		xa.setDuration(2000);
		ya.setDuration(2000);
		xa.setInterpolator(new LinearInterpolator());
		ya.setInterpolator(new LinearInterpolator());
		xa.setRepeatCount(ObjectAnimator.INFINITE);
		ya.setRepeatCount(ObjectAnimator.INFINITE);

		xa.start();
		ya.start();
	}

	public void setMusicLoc(PathPoint newLoc) {
		music.setTranslationX(newLoc.mX);
		music.setTranslationY(newLoc.mY);
	}

}
