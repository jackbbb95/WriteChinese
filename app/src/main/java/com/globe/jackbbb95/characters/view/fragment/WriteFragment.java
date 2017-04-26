package com.globe.jackbbb95.characters.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.globe.jackbbb95.characters.R;
import com.globe.jackbbb95.characters.model.CategoryObject;
import com.globe.jackbbb95.characters.model.CharacterObject;

import java.util.ArrayList;

import me.panavtec.drawableview.DrawableView;
import me.panavtec.drawableview.DrawableViewConfig;

public class WriteFragment extends BaseFragment {

    //custom canvas view
    private static DrawableView drawableView;
    private static DrawableViewConfig config;
    private static int PEN_SIZE = 15;
    private static boolean SHOW_CHAR = true;
    private static boolean SHOW_PINYIN = true;
    private static boolean SHOW_DEF = true;

    private CharacterObject currChar;
    private int charIndex;
    private int swipeCount;

    public DrawableView getDrawableView(){return drawableView;}
    public static DrawableViewConfig getConfig(){return config;}

    public static int getPenSize(){return PEN_SIZE;}
    public static void setPenSize(int newSize){PEN_SIZE = newSize;}
    public static boolean getShowChar(){return SHOW_CHAR;}
    public static boolean getShowPinyin(){return SHOW_PINYIN;}
    public static boolean getShowDef(){return SHOW_DEF;}
    public int getCharIndex(){return charIndex;}

    public WriteFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView;
        SharedPreferences prefs = getActivity().getSharedPreferences("pen_prefs", Activity.MODE_PRIVATE);
        PEN_SIZE = prefs.getInt("PEN_SIZE",10);
        SHOW_CHAR = prefs.getBoolean("SHOW_CHAR",true);
        SHOW_PINYIN = prefs.getBoolean("SHOW_PINYIN",true);
        SHOW_DEF = prefs.getBoolean("SHOW_DEF",true);

        //if this activity is entered through the practice mode button
        if(getArguments().getBoolean("PracticeBoolean",false))
            rootView = inflater.inflate(R.layout.fragment_practice,container,false);
        else {
            rootView = inflater.inflate(R.layout.fragment_write, container, false);

            charIndex = getArguments().getInt("CharacterIndex",-1);
            ArrayList<CategoryObject> list = CategoryListFragment.getCategoryList();
            final CategoryObject category = list.get(getArguments().getInt("CategoryIndex", -1));

            currChar = category.getCharacters().get(charIndex);

            //character
            final TextView characterTV = (TextView) rootView.findViewById(R.id.write_act_char_tv);
            if(SHOW_CHAR)
                characterTV.setText(currChar.getHanyuCharacters());
            else
                characterTV.setVisibility(View.INVISIBLE);

            //pinyin
            final TextView pinyinTV = (TextView) rootView.findViewById(R.id.write_act_pinyin_tv);
            if(SHOW_PINYIN)
                pinyinTV.setText(currChar.getPinyin());
            else
                pinyinTV.setVisibility(View.INVISIBLE);

            //definition
            final TextView definition = (TextView) rootView.findViewById(R.id.definition);
            if(SHOW_DEF)
                definition.setText(currChar.getDefinition());
            else
                definition.setVisibility(View.INVISIBLE);

            //Swipe Text
            final TextView swipeText = (TextView) rootView.findViewById(R.id.swipe_text);

            //Character Count
            final TextView characterCount = (TextView) rootView.findViewById(R.id.char_count);
            String charCountText = "(" + String.valueOf(charIndex + 1) + "/" + String.valueOf(category.getCharacters().size()) + ")";
            characterCount.setText(charCountText);

            //handle swiping
            LinearLayout card = (LinearLayout) rootView.findViewById(R.id.write_top_layout);
            swipeCount = 0;
            card.setOnTouchListener(new OnSwipeTouchListener(getContext()) {
                @Override
                public void onSwipeLeft() {
                    swipeCount++;
                    if(swipeCount > 2)
                        swipeText.setVisibility(View.GONE);
                    if(charIndex < category.getCharacters().size()-1) {
                        charIndex++;
                        currChar = category.getCharacters().get(charIndex);
                        characterTV.setText(currChar.getHanyuCharacters());
                        pinyinTV.setText(currChar.getPinyin());
                        definition.setText(currChar.getDefinition());
                        String charCountText = "(" + String.valueOf(charIndex+1) + "/" + String.valueOf(category.getCharacters().size()) + ")";
                        characterCount.setText(charCountText);
                    }
                }

                @Override
                public void onSwipeRight() {
                    swipeCount++;
                    if(swipeCount > 2)
                        swipeText.setVisibility(View.GONE);
                    if(charIndex > 0) {
                        charIndex--;
                        currChar = category.getCharacters().get(charIndex);
                        characterTV.setText(currChar.getHanyuCharacters());
                        pinyinTV.setText(currChar.getPinyin());
                        definition.setText(currChar.getDefinition());
                        String charCountText = "(" + String.valueOf(charIndex+1) + "/" + String.valueOf(category.getCharacters().size()) + ")";
                        characterCount.setText(charCountText);
                    }
                }
            });



        }



        //Setup Drawing Surface
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        drawableView = (DrawableView) rootView.findViewById(R.id.drawable_view);
        config = new DrawableViewConfig();
        config.setStrokeColor(Color.BLACK);
        config.setShowCanvasBounds(false);
        config.setStrokeWidth(PEN_SIZE);
        config.setMinZoom(1.0f);
        config.setMaxZoom(1.0f);
        config.setCanvasHeight(size.y - 312);
        config.setCanvasWidth(size.x);
        drawableView.setConfig(config);

        return rootView;
    }

    //class that handles the swipes of the top card that switches between characters
    private class OnSwipeTouchListener implements View.OnTouchListener {

        private final GestureDetector gestureDetector;

        private OnSwipeTouchListener(Context context) {
            gestureDetector = new GestureDetector(context, new GestureListener());
        }

        public void onSwipeLeft() {
        }

        public void onSwipeRight() {
        }

        public boolean onTouch(View v, MotionEvent event) {
            return gestureDetector.onTouchEvent(event);
        }

        private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

            private static final int SWIPE_DISTANCE_THRESHOLD = 100;
            private static final int SWIPE_VELOCITY_THRESHOLD = 100;

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                float distanceX = e2.getX() - e1.getX();
                float distanceY = e2.getY() - e1.getY();
                if (Math.abs(distanceX) > Math.abs(distanceY) && Math.abs(distanceX) > SWIPE_DISTANCE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (distanceX > 0)
                        onSwipeRight();
                    else
                        onSwipeLeft();
                    return true;
                }
                return false;
            }
        }
    }


}
