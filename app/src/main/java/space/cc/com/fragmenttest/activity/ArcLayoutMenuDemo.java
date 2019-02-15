package space.cc.com.fragmenttest.activity;

import androidx.appcompat.app.AppCompatActivity;
import space.cc.com.fragmenttest.R;
import space.cc.com.fragmenttest.domain.util.ToastUtils;
import space.cc.com.fragmenttest.util.AnimatorUtils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.Toast;

import com.ogaclejapan.arclayout.Arc;
import com.ogaclejapan.arclayout.ArcLayout;

import java.util.ArrayList;
import java.util.List;

public class ArcLayoutMenuDemo extends MyBaseNew {

    private static final String KEY_DEMO = "demo";
    Toast toast = null;
    View fab;
    View menuLayout;
    ArcLayout arcLayout;
    private static final String TAG = "ArcLayoutMenuDemo";

    @Override
    protected int setContentLayout() {
        return R.layout.arc_small;
    }

    @Override
    protected void initView() {
        fab = findViewById(R.id.arc_fab);
        menuLayout = findViewById(R.id.menu_layout);
        arcLayout =  findViewById(R.id.arc_layout);
//        设置弧形菜单的属性右下角
        arcLayout.setArc( Arc.BOTTOM_RIGHT);
        for (int i = 0, size = arcLayout.getChildCount(); i < size; i++) {
            arcLayout.getChildAt(i).setOnClickListener(this);
        }

        fab.setOnClickListener(this);
    }

    @Override
    protected void initViewClick(int id) {
        if (id == R.id.arc_fab) {
            onFabClick(fab);
            return;
        }



    }

    @Override
    protected void initViewClick(View view) {
        try {
            if (view instanceof Button) {
                ToastUtils.showDisplay("Clicked: " + ((Button)view).getText());
            }
        } catch (Exception e) {
            Log.e(TAG, "defaultData transfer error", e);
        }
    }

    private void showToast(CharSequence btn) {
        ToastUtils.showDisplay("Clicked: " + btn);

    }
    private void onFabClick(View v) {
        if (v.isSelected()) {
            hideMenu();
        } else {
            showMenu();
        }
        v.setSelected(!v.isSelected());
    }


    @SuppressWarnings("NewApi")
    private void showMenu() {
        menuLayout.setVisibility(View.VISIBLE);

        List<Animator> animList = new ArrayList<>();

        for (int i = 0, len = arcLayout.getChildCount(); i < len; i++) {
            animList.add(createShowItemAnimator(arcLayout.getChildAt(i)));
        }

        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(400);
        animSet.setInterpolator(new OvershootInterpolator());
        animSet.playTogether(animList);
        animSet.start();
    }

    @SuppressWarnings("NewApi")
    private void hideMenu() {

        List<Animator> animList = new ArrayList<>();

        for (int i = arcLayout.getChildCount() - 1; i >= 0; i--) {
            animList.add(createHideItemAnimator(arcLayout.getChildAt(i)));
        }

        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(400);
        animSet.setInterpolator(new AnticipateInterpolator());
        animSet.playTogether(animList);
        animSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                menuLayout.setVisibility(View.INVISIBLE);
            }
        });
        animSet.start();

    }

    private Animator createShowItemAnimator(View item) {

        float dx = fab.getX() - item.getX();
        float dy = fab.getY() - item.getY();

        item.setRotation(0f);
        item.setTranslationX(dx);
        item.setTranslationY(dy);

        Animator anim = ObjectAnimator.ofPropertyValuesHolder(
                item,
                AnimatorUtils.rotation(0f, 720f),
                AnimatorUtils.translationX(dx, 0f),
                AnimatorUtils.translationY(dy, 0f)
        );

        return anim;
    }

    private Animator createHideItemAnimator(final View item) {
        float dx = fab.getX() - item.getX();
        float dy = fab.getY() - item.getY();

        Animator anim = ObjectAnimator.ofPropertyValuesHolder(
                item,
                AnimatorUtils.rotation(720f, 0f),
                AnimatorUtils.translationX(0f, dx),
                AnimatorUtils.translationY(0f, dy)
        );

        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                item.setTranslationX(0f);
                item.setTranslationY(0f);
            }
        });

        return anim;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
