#include <jni.h>        // JNI header provided by JDK
#include <stdio.h>      // C Standard IO Header
#include "HelloJNI.h"   // Generated
 
// Implementation of the native method sayHello()
JNIEXPORT void JNICALL JNICALL Java_dynamicAnalysis_ExecuteCode_executeInstruction
  (JNIEnv *, jobject, jbyte) {
   printf("Hello World!\n");
   return;
}