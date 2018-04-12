# FakeGApps by thermatk

Xposed Repo: http://repo.xposed.info/module/com.thermatk.android.xf.fakegapps

F-Droid Repo: https://f-droid.org/packages/com.thermatk.android.xf.fakegapps/

Used in combinaton with [µg GmsCore](https://github.com/microg/android_packages_apps_GmsCore) to enable Google Cloud Messaging (GCM) and much more.

This tutorial describes only the installation process to get GCM working. The also available Google Play Store replacement Blankstore is not discussed here.

## Installation for Android with root access and without Gapps

* Install Xposed 
    * For Lollipop and Marshmallow from http://forum.xda-developers.com/showthread.php?t=3034811

    * For version below Lollipop using Xposed installer from http://repo.xposed.info/module/de.robv.android.xposed.installer

* Install this FakeGApps module from Xposed Repo(or releases) and enable it in Xposed Installer under 'Modules'
* Install FakeStore from http://own.darkcloud.ca:90/Android-Builds/ instead you can use Blankstore (see below)
* Install latest GmsCore build 'play-services-core-debug.apk' from http://files.brnmod.rocks/apps/GmsCore/Latest/
* Reboot and dial in your phone app \*#\*#CHECKIN#\*#\* to register GCM

Note: to enable GCM e.g for messaging apps like Threema or Textsecure, you have to update / install the same apk once again to allow them to obtain the GCM related permissions.

GCM can be tested using e.g. [Google Play Store](https://play.google.com/store/apps/details?id=com.firstrowria.pushnotificationtester&hl=en) which is also downloadble from [evozi](http://apps.evozi.com/apk-downloader/?id=com.firstrowria.pushnotificationtester).


## Untested ways to remove Gapps

The procedure described [here](http://hex.ro/wp/blog/removing-gapps-from-cyanogenmod-11/) for removing Gapps from CM11 should also work for other ROMs where Gapps has been installed using a flashable zip. 

## How To Install Blankstore 
Its assumed that Gapps has already been removed completely including previous folders such as ```/data/app/com.android.vending/```.

### For Android 5.0+
* create the folder ```/system/priv-app/Phonesky``` and change permissions to 755
* Download latest ```Blankstore.apk``` from [BlankStore Repo](https://github.com/mar-v-in/BlankStore/releases/), copy it to folder ```/system/priv-app/Phonesky``` and change the APK's permissions to 644
* Reboot
* Download [this JAR file](http://forum.xda-developers.com/attachment.php?attachmentid=3392935&d=1436186022) and run ```java -jar android-checkin-1.0.jar <email> <password>``` from the <b>console/terminal of your PC running Linux/OSX</b>
* Start the new Market app and login using your credentials and the Android ID from output of previous command

### Untested ways for Android 4.0+ - 4.3.x
* Instead of the first two steps for Android 5.0, download the Blankstore.apk, rename it to ```com.android.vending.apk``` and copy it to ```/system/app```, apply same permissions as above and proceed the same way as for Android 5.0 

### Untested ways for Android 4.4.x
* Instead of the first two steps for Android 5.0, download the Blankstore.apk, rename it to ```com.android.vending.apk``` and copy it to ```/system/priv-app```, apply same permissions as above and proceed the same way as for Android 5.0 
