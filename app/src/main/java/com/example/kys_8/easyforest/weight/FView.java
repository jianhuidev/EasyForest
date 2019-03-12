package com.example.kys_8.easyforest.weight;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.example.kys_8.easyforest.R;

/**
 * @author zjh
 * Created by kys_8 on 2018/7/18.
 */
public class FView extends FrameLayout {

	public FView(Context context) {
		super(context);
		init();
	}

	public FView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public FView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init() {
		inflate(getContext(), R.layout.layout_fview, this);
	}
}
