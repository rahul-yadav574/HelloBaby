package in.evolve.hellobaby;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import com.gelitenight.waveview.library.WaveView;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class LandingActivity extends AppCompatActivity implements ContactsRetrieved{

    private final String TAG = LandingActivity.this.getClass().getName();
    private AnimatorSet mAnimatorSet;
    private WaveView mWaveView;
    private int mBorderColor = Color.parseColor("#44FFFFFF");
    private int mBorderWidth = 10;
    private static  final String SERVER_URL = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        mWaveView = (WaveView) findViewById(R.id.wave);
        mWaveView.setBorder(mBorderWidth, mBorderColor);
        initAnimation();
        start();
        new FetchContacts(LandingActivity.this,this).execute();
    }


    public void start() {
        mWaveView.setShowWave(true);
        if (mAnimatorSet != null) {
            mAnimatorSet.start();
        }
    }

    private void initAnimation() {
        List<Animator> animators = new ArrayList<>();
        ObjectAnimator waveShiftAnim = ObjectAnimator.ofFloat(
                mWaveView, "waveShiftRatio", 0f, 1f);
        waveShiftAnim.setRepeatCount(ValueAnimator.INFINITE);
        waveShiftAnim.setDuration(1000);
        waveShiftAnim.setInterpolator(new LinearInterpolator());
        animators.add(waveShiftAnim);

        ObjectAnimator waterLevelAnim = ObjectAnimator.ofFloat(
                mWaveView, "waterLevelRatio", 0f, 0.5f);
        waterLevelAnim.setDuration(10000);
        waterLevelAnim.setInterpolator(new DecelerateInterpolator());
        animators.add(waterLevelAnim);
        ObjectAnimator amplitudeAnim = ObjectAnimator.ofFloat(
                mWaveView, "amplitudeRatio", 0.0001f, 0.05f);
        amplitudeAnim.setRepeatCount(ValueAnimator.INFINITE);
        amplitudeAnim.setRepeatMode(ValueAnimator.REVERSE);
        amplitudeAnim.setDuration(5000);
        amplitudeAnim.setInterpolator(new LinearInterpolator());
        animators.add(amplitudeAnim);

        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.playTogether(animators);
    }



    @Override
    public void workDone(JSONArray contacts) {

        // this list contains all the contacts ....do what you want to do....:)

        Toast.makeText(LandingActivity.this, "size is : " +contacts.length(), Toast.LENGTH_LONG).show();

        OkHttpClient httpClient = new OkHttpClient();

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),contacts.toString());

        Request request = new Request.Builder()
                .post(body)
                .url(SERVER_URL)
                .build();

       /* httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Toast.makeText(LandingActivity.this, "Your Phone is Kachre Ka Dabba", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                Toast.makeText(LandingActivity.this, "Work Done Successfully", Toast.LENGTH_SHORT).show();
                LandingActivity.this.finish();
            }
        });*/
    }
}

