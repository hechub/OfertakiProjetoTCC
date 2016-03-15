package br.com.hector.ofertaki;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.Scroller;

public class MenuDeslizante extends LinearLayout {

	private View menu;
	private View conteudo;
	
	protected static final int menuMargin = 150;
	
	public enum MenuState {CLOSED, OPEN, CLOSING, OPENING};
	
	protected int currentContentOffset = 0;
	protected MenuState menuCurrentState = MenuState.CLOSED;
	
	//Animation objects
	protected Scroller menuAnimationScroller = new Scroller(this.getContext(), new SmoothInterpolator());
	protected Runnable menuAnimationRunnable = new AnimationRunnable();
	protected Handler menuAnimationHandler = new Handler();
	
	//Animation constants
	private static final int menuAnimationDuration = 1000;
	private static final int menuAnimationPollingInterval = 16;
	
	public MenuDeslizante(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
	}
	
	public MenuDeslizante(Context context, AttributeSet attrs){
		super(context, attrs);
	}
	
	public MenuDeslizante(Context context){
		super(context);
	}
	
	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		
		this.menu = this.getChildAt(0);
		this.conteudo = this.getChildAt(1);
		
		this.menu.setVisibility(View.GONE);
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if(changed)
			this.calculateChildDimensions();
		
		this.menu.layout(l, t, r - menuMargin, b);
		this.conteudo.layout(l+this.currentContentOffset, t, r+this.currentContentOffset, b);
	
	}
	
	public void toggleMenu(){
		switch(this.menuCurrentState){
		case CLOSED:
			this.menuCurrentState = MenuState.OPENING;
			this.menu.setVisibility(View.VISIBLE);
			this.menuAnimationScroller.startScroll(0, 0, this.getMenuWidth(), 0, menuAnimationDuration);
			break;
		case OPEN:
			this.menuCurrentState = MenuState.CLOSING;
			this.menuAnimationScroller.startScroll(this.currentContentOffset, 0, -this.currentContentOffset, 0, menuAnimationDuration);
			break;
		default:
			return;
		}
		this.menuAnimationHandler.postDelayed(this.menuAnimationRunnable, menuAnimationPollingInterval);
	}
	
	private int getMenuWidth(){
		return this.menu.getLayoutParams().width;
	}
	
	private void calculateChildDimensions(){
		this.conteudo.getLayoutParams().height = this.getHeight();
		this.conteudo.getLayoutParams().width = this.getWidth();
		this.menu.getLayoutParams().width = this.getWidth()-menuMargin;
		this.menu.getLayoutParams().height = this.getHeight();
	}
	
	
	
	protected class SmoothInterpolator implements Interpolator{
		@Override
		public float getInterpolation(float input) {
			return (float)Math.pow(input-1, 5)+1;
		}
	}
	
	private void adjustContentPosition(boolean isAnimationOngoing){
		int scrollerOffset = this.menuAnimationScroller.getCurrX();
		this.conteudo.offsetLeftAndRight(scrollerOffset - this.currentContentOffset);
		
		this.currentContentOffset = scrollerOffset;
		
		this.invalidate();
		if(isAnimationOngoing)
			this.menuAnimationHandler.postDelayed(this.menuAnimationRunnable, menuAnimationPollingInterval);
		else
			this.onMenuTransitionComplete();
	}
	
	private void onMenuTransitionComplete(){
		switch(this.menuCurrentState){
		case OPENING:
			this.menuCurrentState = MenuState.OPEN;
			break;
		case CLOSING:
			this.menuCurrentState = MenuState.CLOSED;
			this.menu.setVisibility(View.GONE);
			break;
		default:
			return;
		}
	}
	
	
	protected class AnimationRunnable implements Runnable{
		@Override
		public void run() {
			boolean isAnimationOngoing = MenuDeslizante.this.menuAnimationScroller.computeScrollOffset();
			MenuDeslizante.this.adjustContentPosition(isAnimationOngoing);
			
		}
	}
	
	
	
	
}
