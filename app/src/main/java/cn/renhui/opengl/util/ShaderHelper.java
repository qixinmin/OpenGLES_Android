package cn.renhui.opengl.util;

import android.util.Log;

import static android.opengl.GLES20.GL_COMPILE_STATUS;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_LINK_STATUS;
import static android.opengl.GLES20.GL_VALIDATE_STATUS;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glAttachShader;
import static android.opengl.GLES20.glCompileShader;
import static android.opengl.GLES20.glCreateProgram;
import static android.opengl.GLES20.glCreateShader;
import static android.opengl.GLES20.glDeleteProgram;
import static android.opengl.GLES20.glDeleteShader;
import static android.opengl.GLES20.glGetProgramInfoLog;
import static android.opengl.GLES20.glGetProgramiv;
import static android.opengl.GLES20.glGetShaderInfoLog;
import static android.opengl.GLES20.glGetShaderiv;
import static android.opengl.GLES20.glShaderSource;
import static android.opengl.GLES20.glValidateProgram;

/**
 * 着色器辅助类
 * Created by renhui on 2017/4/7.
 */
public class ShaderHelper {
    private static final String TAG = "ShaderHelper";

    public static int compileVertexShader(String shaderCode) {
        return compileShader(GL_VERTEX_SHADER, shaderCode);
    }

    public static int compileFragmentShader(String shaderCode) {
        return compileShader(GL_FRAGMENT_SHADER, shaderCode);
    }

    private static int compileShader(int type, String shaderCode) {
        // 创建一个新的着色器对象，并检查创建是否成功
        final int shaderObjectId = glCreateShader(type);
        if (shaderObjectId == 0) {
            Log.w(TAG, "Could not create new shader.");
            return 0;
        }

        // 上传和编译着色器源码
        glShaderSource(shaderObjectId, shaderCode);
        glCompileShader(shaderObjectId);

        // 取出编译状态
        final int[] compileStatus = new int[1];
        glGetShaderiv(shaderObjectId, GL_COMPILE_STATUS, compileStatus, 0);

        // 取出着色器信息日志
        Log.v(TAG, "Result of compiling source : " + "\n" + shaderCode + "\n:" + glGetShaderInfoLog(shaderObjectId));

        // 查看是否编译成功
        if (compileStatus[0] == 0) {
            // 如果编译失败，删除Shader Object
            glDeleteShader(shaderObjectId);
            Log.w(TAG, "Compilation of shader failed.");
            return 0;
        }

        return shaderObjectId;
    }

    // 着色器链接进OpenGl的程序(OpenGL的一个组件)
    public static int linkProgram(int vertexShaderId, int fragmentShaderId) {
        // 新建程序对象并附上着色器
        final int programObjectId = glCreateProgram();
        if (programObjectId == 0) {
            Log.w(TAG, "Could not create new program");
            return 0;
        }

        // 附上着色器
        glAttachShader(programObjectId, vertexShaderId);
        glAttachShader(programObjectId, fragmentShaderId);

        // 链接程序
        final int[] linkStatus = new int[1];
        glGetProgramiv(programObjectId, GL_LINK_STATUS, linkStatus, 0);

        // 检查链接结果
        Log.v(TAG, "Result of linking program:\n" + glGetProgramInfoLog(programObjectId));

        // 验证链接状态并返回程序对象id
        if (linkStatus[0] == 0) {
            glDeleteProgram(programObjectId);
            Log.w(TAG, "linking of program failed.");
            return 0;
        }

        return programObjectId;
    }

    // 验证OpenGL程序的对象
    public static boolean validateProgram(int programObjectId) {
        glValidateProgram(programObjectId);
        final int[] validateStatus = new int[1];
        glGetProgramiv(programObjectId, GL_VALIDATE_STATUS, validateStatus, 0);
        Log.v(TAG, "Result of validating program: " + validateStatus[0] + "\nLog: " + glGetProgramInfoLog(programObjectId));
        return validateStatus[0] != 0;
    }

}
