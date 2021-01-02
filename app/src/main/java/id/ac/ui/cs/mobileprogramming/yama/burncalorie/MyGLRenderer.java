package id.ac.ui.cs.mobileprogramming.yama.burncalorie;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MyGLRenderer implements Renderer {

    private TextureCube textureCube;
    private final float[] vPMatrix = new float[16];

    private final float[] projectionMatrix = new float[16];
    private final float[] viewMatrix = new float[16];

    private float[] rotationMatrix = new float[16];

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig eglConfig) {
        textureCube = new TextureCube();
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        float[] scratch = new float[16];
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        Log.d("test", "testestsetse");
        long time = SystemClock.uptimeMillis() % 4000L;
        float angle = 0.090f * ((int) time);
//        float speed = 0.01f * ((int) time);
        Matrix.setLookAtM(viewMatrix, 0, 0, 2, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        Matrix.setRotateM(rotationMatrix, 0, angle, 0, 90, -1.0f);

        Matrix.multiplyMM(vPMatrix, 0, projectionMatrix, 0, viewMatrix, 0);
        Matrix.multiplyMM(scratch, 0, vPMatrix, 0, rotationMatrix, 0);

        textureCube.draw(scratch);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        float ratio = (float) width / height;
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
    }

    public static int loadShader(int type, String shaderCode){
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    };
}