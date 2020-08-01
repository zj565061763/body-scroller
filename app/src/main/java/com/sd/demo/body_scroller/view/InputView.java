package com.sd.demo.body_scroller.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sd.demo.body_scroller.databinding.ViewInputBinding;
import com.sd.lib.bodyscroller.FBodyScroller;
import com.sd.lib.bodyscroller.panel.IFootPanel;
import com.sd.lib.bodyscroller.panel.KeyboardFootPanel;
import com.sd.lib.bodyscroller.panel.ViewFootPanel;
import com.sd.lib.utils.FKeyboardUtil;

public class InputView extends FrameLayout implements View.OnClickListener
{
    public static final String TAG = InputView.class.getSimpleName();
    private final ViewInputBinding mBinding;

    private final IFootPanel mExtFootPanel;
    private InputMoreView mMoreView;

    public InputView(@NonNull Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        mBinding = ViewInputBinding.inflate(LayoutInflater.from(context), this, true);
        mExtFootPanel = new ViewFootPanel(mBinding.flExt);

        mBodyScroller.addFootPanel(new KeyboardFootPanel((Activity) context));
        mBodyScroller.addFootPanel(mExtFootPanel);

        mBinding.etContent.setOnClickListener(this);
        mBinding.btnMore.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        if (v == mBinding.btnMore)
        {
            clickMore();
        } else if (v == mBinding.etContent)
        {
            mBodyScroller.setCurrentFootPanel(null);

            mBinding.flExt.removeAllViews();
            FKeyboardUtil.show(mBinding.etContent);
        }
    }

    private InputMoreView getMoreView()
    {
        if (mMoreView == null)
            mMoreView = new InputMoreView(getContext(), null);
        return mMoreView;
    }

    private void clickMore()
    {
        if (getMoreView().getParent() != mBinding.flExt)
        {
            mBodyScroller.setCurrentFootPanel(mExtFootPanel);

            mBinding.flExt.removeAllViews();
            mBinding.flExt.addView(getMoreView());
            FKeyboardUtil.hide(mBinding.etContent);
        }
    }

    private final FBodyScroller mBodyScroller = new FBodyScroller()
    {
        @Override
        protected void onFootHeightChanged(int oldHeight, final int newHeight, IFootPanel currentFootPanel)
        {
            final int delta = newHeight - oldHeight;
            Log.i(TAG, "onFootHeightChanged oldHeight:" + oldHeight + " newHeight:" + newHeight + " delta:" + delta);

            if (currentFootPanel instanceof KeyboardFootPanel)
            {
                mBinding.llRoot.scrollTo(0, newHeight);
            } else
            {
                mBinding.llRoot.scrollTo(0, 0);
            }
        }
    };
}