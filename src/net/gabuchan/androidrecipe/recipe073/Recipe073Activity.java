
package net.gabuchan.androidrecipe.recipe073;

import net.gabuchan.androidrecipe.R;
import android.annotation.TargetApi;
import android.app.Activity;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.os.Bundle;

@TargetApi(9)
public class Recipe073Activity extends Activity {
    // メディアプレイヤー
    MediaPlayer mMediaPlayer;

    // ビジュアライザー
    Visualizer mVisualizer;

    // Visualizerの値（波形データ）を描画するView
    VisualizerView mVisualizerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_073);

        mVisualizerView = (VisualizerView) findViewById(R.id.visualizer_view);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // MediaPlayerを生成
        mMediaPlayer = MediaPlayer.create(this, R.raw.sound);
        // 無限ループ再生
        mMediaPlayer.setLooping(true);

        // Visualizerを生成
        mVisualizer = new Visualizer(mMediaPlayer.getAudioSessionId());
        // for Android 2.3
        mVisualizer.setEnabled(false);
        // キャプチャーサイズをセット
        mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
        // データがキャプチャーできた時のリスナーをセット
        mVisualizer.setDataCaptureListener(new Visualizer.OnDataCaptureListener() {
            public void onWaveFormDataCapture(Visualizer visualizer, byte[] bytes,
                    int samplingRate) {
                // 波形データを渡してVisualizerViewを更新する
                mVisualizerView.updateVisualizer(bytes);
            }

            public void onFftDataCapture(Visualizer visualizer, byte[] bytes, int samplingRate) {
                // 今回は使いません
            }
        },
                Visualizer.getMaxCaptureRate() / 2, // キャプチャーする間隔(mHz)
                true, // 波形データを取得する場合は、true
                false); // FFT変換後のデータを取得する場合は、true
        // Visualizerを有効にする
        mVisualizer.setEnabled(true);
        // 音声再生をスタート
        mMediaPlayer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 再生中なら停止
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
        // 解放
        mMediaPlayer.release();
        mVisualizer.release();
    }
}
