package onetwentyonegigawatt.funfacts;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.TextView;
/**
 * Created by William.Davis on 12/19/2014.+
 * Found here : http://stackoverflow.com/questions/6700374/android-character-by-character-display-text-animation
 */
public class TypeWriter extends TextView {

    private CharSequence mText;
    private int mIndex;
    private long mDelay = 90; //Default 500ms delay


    public TypeWriter(Context context) {
        super(context);
    }

    public TypeWriter(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private Handler mHandler = new Handler();
    private Runnable characterAdder = new Runnable() {
        @Override
        public void run() {
            setText(mText.subSequence(0, mIndex++));
            if(mIndex <= mText.length()) {
                mHandler.postDelayed(characterAdder, mDelay);
            }
        }
    };

    public void animateText(CharSequence text) {
        mText = text;
        mIndex = 0;

        setText("");
        mHandler.removeCallbacks(characterAdder);
        mHandler.postDelayed(characterAdder, mDelay);
    }

    public void setCharacterDelay(long millis) {
        mDelay = millis;
    }
}