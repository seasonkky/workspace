/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


#define LOG_TAG "DVD_CTRL"
#define LOG_NDEBUG 0
#include <jni.h>
#include <utils/Log.h>
#include "dvdCtrl.h"
#include <fcntl.h>
#include <linux/ioctl.h>
  
#define DVD_FILE "/sys/kernel/debug/dvd_ops/dvd_io_ctl"
#define   VOLUME_0 	300
#define   VOLUME_1  	301
#define   VOLUME_2	302
#define   VOLUME_3	303
#define   VOLUME_4	304
#define   VOLUME_5	305
#define   VOLUME_6	306
#define   VOLUME_7	307

#define   PLAY_PAUSE	119
#define   PREV		103
#define   NEXT		108
#define   REPEAT	182
#define   STOP	        128
#define   MENU          139

#ifdef __cplusplus   
extern "C"  
{   
#endif 
/**
 * @param env the JNI environment
 * @param yBuf the buffer containing the Y component of the image
 * @param yPStride the stride between adjacent pixels in the same row in yBuf
 * @param yRStride the stride between adjacent rows in yBuf
 * @param cbBuf the buffer containing the Cb component of the image
 * @param cbPStride the stride between adjacent pixels in the same row in cbBuf
 * @param cbRStride the stride between adjacent rows in cbBuf
 * @param crBuf the buffer containing the Cr component of the image
 * @param crPStride the stride between adjacent pixels in the same row in crBuf
 * @param crRStride the stride between adjacent rows in crBuf
 */

  JNIEXPORT jint JNICALL Java_com_android_camera_util_DvdCtrl_StartPause
  (JNIEnv *, jclass){
jint fd;

	fd = open(DVD_FILE, O_RDWR);
	if(fd<0)
		{
			close(fd);
			return 0;
		}

          ALOGD("-------------startpause-----------------");	
	ioctl(fd, PLAY_PAUSE, 0,0);

  return 0;
}

extern "C" JNIEXPORT jint JNICALL Java_com_android_camera_util_DvdCtrl_Pre
  (JNIEnv *, jclass){
	jint fd;

	fd = open(DVD_FILE, O_RDWR);
	if(fd<0)
		{
			close(fd);
			return 0;
		}

           ALOGD("-------------pre-----------------");
	ioctl(fd, PREV, 0,0);

  return 0;
}

extern "C" JNIEXPORT jint JNICALL Java_com_android_camera_util_DvdCtrl_Next
  (JNIEnv *, jclass){
	jint fd;

	fd = open(DVD_FILE, O_RDWR);
	if(fd<0)
		{
			close(fd);
			return 0;
		}

           ALOGD("-------------next-----------------");
	ioctl(fd, NEXT, 0,0);

  return 0;
}

extern "C" JNIEXPORT jint JNICALL Java_com_android_camera_util_DvdCtrl_Repeat
  (JNIEnv *, jclass){
	jint fd;

	fd = open(DVD_FILE, O_RDWR);
	if(fd<0)
		{
			close(fd);
			return 0;
		}

 ALOGD("-------------repeat-----------------");
	ioctl(fd, REPEAT, 0,0);

  return 0;
}

extern "C" JNIEXPORT jint JNICALL Java_com_android_camera_util_DvdCtrl_Menu
  (JNIEnv *, jclass){
	jint fd;

	fd = open(DVD_FILE, O_RDWR);
	if(fd<0)
		{
			close(fd);
			return 0;
		}

	printf("dvd_ops menu fd=  %d",fd);
	ioctl(fd, MENU, 0,0);

  return 0;
}

extern "C" JNIEXPORT jint JNICALL Java_com_android_camera_util_DvdCtrl_SetVol
  (JNIEnv *, jclass, jint in){
        jint fd;
	jint vol = 300 + in;

        fd = open(DVD_FILE, O_RDWR);
        if(fd<0)
                {
                        close(fd);
                        return 0;
                }
	
        printf("dvd_ops vol fd=  %d  %d",fd, vol);
        ioctl(fd, vol, 0,0);

  return 0;
}

#ifdef __cplusplus   



}   
#endif
