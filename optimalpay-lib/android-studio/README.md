-------------------------------------------------------------------------------
OPTIMAL PAYMENT LIBRARY FOR ANDROID
-------------------------------------------------------------------------------


Introduction - Android - Version 1.0 - July 2015
---------------------------------------------------

Optimal Payments Android SDK provides development for Single Use Token API.


Table of Contents
-----------------
1. Installation Guide
2. Pre-requisites
3. Setting up Optimal Payment Library
4. Content
5. Folder Structure
6. Known Issues
7. Contact Information


1. Installation Guide
---------------------

Please refer to below link to understand and follow steps for Android Studio 
Installation:

http://developer.android.com/sdk/index.html


2. Pre-requisites
-----------------

These are minimum configurations required before getting started with Library.
Following tools should be available before getting started with Optimal Payment 
Library:
a. Android Studio
b. JDK version 1.7.0
c. Gradle version 1.2.3
d. Android SDK with API Level 15(version 4.0.3) and above.


3. Setting up Optimal Payment Library
-------------------------------------

Getting Started with Optimal Payment Library on Android Studio.

Following are the steps to guide you to import Optimal Payment Library on 
Android Studio:
a. Click Android Studio icon to open the framework.
b. Choose "Open an Existing Android Studio Project" and provide the path to 
   Optimal Payment Library: "\Android SDK\optimalpay-lib\android-studio"
c. Steps to run your library.
   Following are steps to setup configuration to build your library:
   a. Go to Run -> Edit Configurations
   b. Choose "Add New Configuration" and select "Gradle"
   c. Name the configuration (for e.g. Name: app)
   d. Select Gradle Project as: android-studio
   e. Under "Before launch" add "Run Gradle Task".
		a. Select Gradle Project as: android-studio:app
		b. Select Tasks and add "build"
		c. Click OK
	f. Click Apply and OK.
	g. Goto Run and select "Run app"
	h. After successful gradle build this library project will generate archive
	   file at the following path:
	   app\build\outputs\aar\
	   

4. Content
----------

Main Components:

a. Customer Vault Service
   - This will contain API service call to Single Use Token.
   
b. Optimal Api Client
   - Main functionality of this component is to process request to the server
     which contains:
		- This contains connection to server
		- Get Authentication Credentials to process request
		- Function to serialize data
		- Function to de-serialize data
		
c. Environment
   - This will set Environment for the server connection.
   - There are two Environments provided to connect to server:
		a. LIVE (Production URL)
		b. TEST (Beta URL)
	
d. Other Components:
   - Other components include builder classes to build objects and send data to 
     the optimal server.
	 
	 
5. Folder Structure
-------------------

a. 'app'     - SDK classes and Application Test classes
b. java-doc  - SDK related documentation


6. Known Issues
---------------

None.


7. Contact Information
----------------------

http://www.optimalpayments.com/contact-us