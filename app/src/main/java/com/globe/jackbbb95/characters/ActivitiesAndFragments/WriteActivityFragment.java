package com.globe.jackbbb95.characters.ActivitiesAndFragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Display;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.globe.jackbbb95.characters.Objects.CategoryObject;
import com.globe.jackbbb95.characters.Objects.CharacterObject;
import com.globe.jackbbb95.characters.R;

import java.util.ArrayList;

import me.panavtec.drawableview.DrawableView;
import me.panavtec.drawableview.DrawableViewConfig;

public class WriteActivityFragment extends Fragment {

    //custom canvas view
    private static DrawableView drawableView;
    private static DrawableViewConfig config;
    private static int PEN_SIZE = 10;

    private CharacterObject currChar;
    private int charIndex;
    private int swipeCount;

    public static DrawableView getDrawableView(){return drawableView;}
    public static DrawableViewConfig getConfig(){return config;}
    public static int getPenSize(){return PEN_SIZE;}
    public static void setPenSize(int newSize){PEN_SIZE = newSize;}

    public WriteActivityFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView;
        //if this activity is entered through the practice mode button
        if(getActivity().getIntent().getBooleanExtra("PracticeBoolean",false))
            rootView = inflater.inflate(R.layout.fragment_practice,container,false);
        else {
            rootView = inflater.inflate(R.layout.fragment_write, container, false);

            charIndex = getActivity().getIntent().getIntExtra("CharacterIndex",-1);
            ArrayList<CategoryObject> list = CategoryListActivityFragment.getCategoryList();
            final CategoryObject category = list.get(getActivity().getIntent().getIntExtra("CategoryIndex", -1));
            currChar = category.getCharacters().get(charIndex);
            //character
            final TextView characterTV = (TextView) rootView.findViewById(R.id.write_act_char_tv);
            characterTV.setText(currChar.getHanyuCharacters());
            //pinyin
            final TextView pinyinTV = (TextView) rootView.findViewById(R.id.write_act_pinyin_tv);
            pinyinTV.setText(currChar.getPinyin());
            //definition
            final TextView definition = (TextView) rootView.findViewById(R.id.definition);
            definition.setText(currChar.getDefinition());
            //Swipe Text
            final TextView swipeText = (TextView) rootView.findViewById(R.id.swipe_text);
            //Character Count
            final TextView characterCount = (TextView) rootView.findViewById(R.id.char_count);
            String charCountText = "(" + String.valueOf(charIndex + 1) + "/" + String.valueOf(category.getCharacters().size()) + ")";
            characterCount.setText(charCountText);

            //handle swiping
            CardView card = (CardView) rootView.findViewById(R.id.write_top_card);
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
        SharedPreferences prefs = getActivity().getSharedPreferences("pen_prefs", Activity.MODE_PRIVATE);
        PEN_SIZE = prefs.getInt("PEN_SIZE",10);

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
    class OnSwipeTouchListener implements View.OnTouchListener {

        private final GestureDetector gestureDetector;

        public OnSwipeTouchListener(Context context) {
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
