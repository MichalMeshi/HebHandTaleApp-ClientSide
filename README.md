# HebHandTaleApp-ClientSide

**Description:**
An application for recognize a photo of a handwriting in the Hebrew language, using deep learning, and translation to any other language.

**Installation:**
In order to run this project, you need to install Android Studio and JDK.
In all place that there are IP:PORT, you need to change it with the IP and PORT in your pc. here you can see how to get your IP and PORT: https://support.microsoft.com/he-il/windows/%D7%9E%D7%A6%D7%90-%D7%90%D7%AA-%D7%9B%D7%AA%D7%95%D7%91%D7%AA-%D7%94-ip-%D7%A9%D7%9C%D7%9A-windows-f21a9bbc-c582-55cd-35e0-73431160a1b9

These are the files to change there the IP:PORT:
* HistoryTableConnection
* ServerConnectBase64 
* SendWordToSaveInDB 
* TranslateAPI 
* network_security_config.xml
  
There is an option to sign in to the application through google.
The google sign-in require to configure the project in Google API Console and configure the project in Android Studio. You can look here: https://developers.google.com/identity/sign-in/android/start-integrating?hl=he and take the steps to be able to register through Google.

**Run the application**
In order to run the application you need to download the server side from here: https://github.com/PeninaSchuss/HebHandTaleApp-ServerSide, and look in the README there to know how to run the server.
There is option to run the app with emulator. ypu can see how to do it here: https://developer.android.com/studio/run/managing-avds.

and another option to run the app with physical device. you can see how to do it here: https://developer.android.com/studio/run/device.

after that, you just need to run the file MainActivity.java in order to run this appliction.



