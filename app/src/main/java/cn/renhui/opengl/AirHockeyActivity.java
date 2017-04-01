package cn.renhui.opengl;

import android.app.ActivityManager;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * 空气曲棍球 Activity
 */
public class AirHockeyActivity extends AppCompatActivity {


    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    private GLSurfaceView glSurfaceView;
    private boolean rendererSet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setContentView(R.layout.activity_main);
        // Example of a call to a native method
//        TextView tv = (TextView) findViewById(R.id.sample_text);
//        tv.setText(stringFromJNI());

        // 初始化容器
        glSurfaceView = new GLSurfaceView(this);


        // 检测OS是否支持OpenGL ES 2.0
        final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        final boolean supportEs2 = activityManager.getDeviceConfigurationInfo().reqGlEsVersion >= 0x20000;

        // 为OpenGL渲染表面

        if (supportEs2) {
            //  请求 OpenGL ES 2.0 兼容的 context
            glSurfaceView.setEGLContextClientVersion(2);

            glSurfaceView.setRenderer(new FirstOpenGLProjectRenderer());
            rendererSet = true;
        } else {
            Toast.makeText(this, "this device does not support OpenGL ES 2.0.", Toast.LENGTH_LONG).show();
            return;
        }

        setContentView(glSurfaceView);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (rendererSet) {
            glSurfaceView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (rendererSet) {
            glSurfaceView.onPause();
        }
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}
