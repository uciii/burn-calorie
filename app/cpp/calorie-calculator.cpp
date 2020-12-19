#include <jni.h>
#include <stdlib.h>

extern "C"
JNIEXPORT jlong JNICALL
Java_id_ac_ui_cs_mobileprogramming_yama_burncalorie_FragmentSummaryList_calcCalorie(JNIEnv *env,
                                                                                    jobject thiz,
                                                                                    jlong calorie,
                                                                                    jlong second) {
    return (calorie * second) / 3600;
}