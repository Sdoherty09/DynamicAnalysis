JNIEXPORT jstring JNICALL
Java_com_dynamicAnalysis_ExecuteCode_test( JNIEnv* env, jobject thiz ) {
	return (*env)->NewStringUTF(env, "Hello from JNI ! Compiled with ABI " ABI ".");
}