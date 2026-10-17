#include <jni.h>

#include <cstddef>

namespace {

jdouble weightedAverage(JNIEnv* env, jdoubleArray grades, jintArray coefficients) {
    const jsize count = env->GetArrayLength(grades);
    if (count == 0 || count > 5 || env->GetArrayLength(coefficients) != count) {
        return 0.0;
    }

    jdouble gradeValues[5]{};
    jint coefficientValues[5]{};
    env->GetDoubleArrayRegion(grades, 0, count, gradeValues);
    env->GetIntArrayRegion(coefficients, 0, count, coefficientValues);
    if (env->ExceptionCheck()) {
        return 0.0;
    }

    double weightedTotal = 0.0;
    int coefficientTotal = 0;
    for (jsize index = 0; index < count; ++index) {
        weightedTotal += gradeValues[index] * coefficientValues[index];
        coefficientTotal += coefficientValues[index];
    }
    return coefficientTotal > 0 ? weightedTotal / coefficientTotal : 0.0;
}

}  // namespace

extern "C" JNIEXPORT jdouble JNICALL
Java_com_bacnotecalc_MainActivity_calculateNational(JNIEnv* env, jobject, jdoubleArray grades,
                                                     jintArray coefficients) {
    return weightedAverage(env, grades, coefficients);
}

extern "C" JNIEXPORT jdouble JNICALL
Java_com_bacnotecalc_MainActivity_calculateRegional(JNIEnv* env, jobject, jdoubleArray grades,
                                                     jintArray coefficients) {
    return weightedAverage(env, grades, coefficients);
}

extern "C" JNIEXPORT jdouble JNICALL
Java_com_bacnotecalc_MainActivity_calculateGeneral(JNIEnv*, jobject, jdouble semesterOne,
                                                    jdouble semesterTwo, jdouble regional,
                                                    jdouble national) {
    const double continuous = (semesterOne + semesterTwo) / 2.0;
    return continuous * 0.25 + regional * 0.25 + national * 0.50;
}
