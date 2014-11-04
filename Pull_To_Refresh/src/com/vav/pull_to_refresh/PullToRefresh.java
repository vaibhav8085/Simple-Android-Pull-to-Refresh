/****************************************************************************************************************************************
 *Project : Pull To refresh for Android - Reusable Component
 *Programmer : Vaibhav Pathak
 *Version : 1.0
 *Description : Android pull to refresh implementation
 *How To Use : 1. create a RelativeLayout
 *			  2. create a sliding layout(currently support LinearLayout,RelativeLayout or FrameLayout)
 *			  3. call methods setSlidingLayout, setOriginalPosition, setReleasePosition, setPrompt, setReleasePromptString
 *				setInitialPromptString, setPromptIcon and finally goSlide.
 *Preliminary Settings : creation of Relative Layout and a Sliding Layout on top of it.
 *****************************************************************************************************************************************/
package com.vav.pull_to_refresh;

import android.animation.ObjectAnimator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


class PullToRefresh {

	private static PullToRefresh pullToRefreshInstance = new PullToRefresh();
	private static ViewGroup slidinglayout;
	private int originalPosition;
	private int releasePosition;
	private String releasePositionString;
	private String initialPositionString;
	private TextView promptTextView;
	private ProgressBar progressRefresh;
	private TextView pageHeading;
	private String initialPageHeadingString;
	private String finalPageHeadingString;
	private boolean disbalePullToRefresh;
	
	public ProgressBar getProgressRefresh() {
		return progressRefresh;
	}



	public void setProgressRefresh(ProgressBar progressRefresh) {
		this.progressRefresh = progressRefresh;
	}



	private PullToRefresh(){}
	


	/*************************** Setter and getters for final page heading string ***********************************/

	public String getFinalPageHeadingString() {
		return finalPageHeadingString;
	}

	public void setFinalPageHeadingString(String finalPageHeadingString) {
		this.finalPageHeadingString = finalPageHeadingString;
	}
	/*************************** Setter and getters for initial page heading string ***********************************/

	public String getInitialPageHeadingString() {
		return initialPageHeadingString;
	}

	public void setInitialPageHeadingString(String initialPageHeadingString) {
		this.initialPageHeadingString = initialPageHeadingString;
	}

	/*************************** Setter and getters for page heading ***********************************/

	public TextView getPageHeading() {
		return pageHeading;
	}

	public void setPageHeading(TextView pageHeading) {
		this.pageHeading = pageHeading;
	}

	/*************************** Setter and getters for slidinglayout ***********************************/
	public void setSlidingLayout(LinearLayout sl) {
		slidinglayout = sl;
	}

	public void setSlidingLayout(RelativeLayout rl) {
		slidinglayout = rl;
	}

	public void setSlidingLayout(FrameLayout fl) {
		slidinglayout = fl;
	}

	public ViewGroup getSlidingLayout() {
		return slidinglayout;
	}
	
	public boolean isDisbalePullToRefresh() {
		return disbalePullToRefresh;
	}

	public void setDisbalePullToRefresh(boolean disbalePullToRefresh) {
		this.disbalePullToRefresh = disbalePullToRefresh;
	}


	/*************** Method to set original position of the slidinglayout *******************************/
	public void setOriginalPosition(int op) {
		originalPosition = op;
	}
	public int getOriginalPosition(){
		return originalPosition;
	}

	/******* Method to get and set position after which app demands a release to refresh ************************/

	public void setReleasePosition(int pp) {
		releasePosition = pp;
	}
	public int getReleasePosition(){	
		return releasePosition;
	}

	/*******
	 * TextView that will change its text on slidinglayout release and initial
	 * positions
	 **********/
	public void setPrompt(TextView tp) {
		promptTextView = tp;
	}
	public TextView getPromptTextView(){
		return promptTextView;
	}
	/******* Method to set text that will change once release position reached **************************/
	public void setReleasePromptString(String rs) {
		releasePositionString = rs;
	}

	/**
	 * Method to set text that will change once slidinglayout is in initial
	 * position
	 *******************/
	public void setInitialPromptString(String is) {
		initialPositionString = is;
	}
	public String getInitialPositionString(){
		return initialPositionString;
	}



	public static PullToRefresh pullToRefreshSingleton() {
		return pullToRefreshInstance;
	}

	
	/*
	 * @Method:goSlide 
	 * @Description : This Method to start sliding for during pull to refresh.
	 * 
	 * @Parameters : none 
	 * 
	 * @Return parameter:none
	 */
	
	public void goSlide() {
		slidinglayout.setOnTouchListener(new OnTouchListener() {
			private int changeInY;
			RelativeLayout.LayoutParams layoutParams;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				
				if(disbalePullToRefresh)
					return false;
				
				
				int Y = (int) event.getRawY();
				Log.d("Y Value", "i" + Y);
				
				switch (event.getActionMasked()) {
				case MotionEvent.ACTION_DOWN:
					Log.d("Down called", "i" + Y);
					changeInY = (int) (Y - v.getY());

					return true;
				case MotionEvent.ACTION_MOVE:
					Log.d("move called", "i" + Y);
					layoutParams = (RelativeLayout.LayoutParams) v
							.getLayoutParams();
					Log.d("top margine initial", "i" + layoutParams.topMargin);

					if (v.getY() >= originalPosition ) {
							if((Y - changeInY)>originalPosition){
							v.setY(Y - changeInY);	
							}					
						if (v.getY() > releasePosition) {
							promptTextView.setText(releasePositionString);
						}

						v.invalidate();
						return true;
					} 

				case MotionEvent.ACTION_UP:

					if (v.getY() > releasePosition) {
						//  new PullToRefreshAsyncTask().onPreExecute();

						return true;
					} else {
						ObjectAnimator animGo = ObjectAnimator.ofFloat(
								slidinglayout, "Y", v.getY(), 90);
						animGo.setDuration(100);
						animGo.start();
						promptTextView.setText(initialPositionString);
						return true;
					}

				}

				return false;
			}
		});
	}

}
