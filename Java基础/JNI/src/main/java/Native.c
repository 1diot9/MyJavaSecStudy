#include <jni.h> // JNI 头文件
#include <stdlib.h> // 提供 popen 和 pclose 函数
#include <stdio.h> // 提供 fgets 和 FILE 操作
#include <string.h> // 提供字符串操作函数

#include "Native.h" // 假设这是 JNI 的头文件

//命名规则：Java_类的全限定名_方法名
JNIEXPORT jstring JNICALL Java_Native_exec(JNIEnv *env, jclass clazz, jstring str) {
    if (str != NULL) {
        jboolean isCopy;
        // 将 jstring 参数转成 char 指针
        const char *cmd = (*env)->GetStringUTFChars(env, str, &isCopy);

        if (cmd != NULL) {
            // 使用 popen 函数执行系统命令
            FILE *fd = popen(cmd, "r");

            if (fd != NULL) {
                // 定义缓冲区和结果字符串
                char buf[128];
                char result[4096] = {0}; // 用于存储命令执行结果

                // 读取 popen 的执行结果
                while (fgets(buf, sizeof(buf), fd) != NULL) {
                    // 将读取到的内容拼接到结果字符串中
                    strcat(result, buf);
                }

                // 关闭 popen
                pclose(fd);

                // 返回命令执行结果给 Java
                jstring ret = (*env)->NewStringUTF(env, result);
                (*env)->ReleaseStringUTFChars(env, str, cmd); // 释放字符串资源
                return ret;
            }

            (*env)->ReleaseStringUTFChars(env, str, cmd); // 如果 popen 失败，也要释放字符串资源
        }
    }

    return NULL; // 如果输入为空或执行失败，返回 NULL
}