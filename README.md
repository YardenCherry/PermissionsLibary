# Permissions Libary

PermissionsLibrary is an Android library for managing app permissions with ease. This library simplifies the process of requesting and handling app permissions.

# Features
- Simple API to request permissions
- Handles permission results
- Supports multiple permissions


# Installation
To use PermissionsLibrary in your Android project, follow these steps:

### Step 1: Add the Library to Your Project

1. Clone or download the PermissionsLibrary repository.
2. Copy the PermissionsLibrary module to your Android project's root directory.
   
### Step 2: Include the Library in Your Project

1. Open your project in Android Studio.
2. Edit your project's settings.gradle file to include the PermissionsLibrary module:
   ```groovy
   include ':app', ':PermissionsLibrary'
3. Open your app module's build.gradle file and add the library as a dependency:
   dependencies {
    implementation project(':PermissionsLibrary')
   }

### Step 3: Sync Your Project with Gradle Files

After editing the build.gradle files, sync your project with the Gradle files to ensure that the library is correctly added to your project.

## Usage
Here's how to use PermissionsLibrary in your Android project:

### Requesting Permissions
1. Import the library in your activity or fragment:
   import com.example.permissionslibrary.PermissionManager;
2. Request permissions using the 'PermissionManager':
   PermissionManager.requestPermissions(this, new String[]{
    Manifest.permission.CAMERA,
    Manifest.permission.READ_CONTACTS
   }, PERMISSION_REQUEST_CODE);
   
## Running the Application
To run the application, follow these steps:

1. Open the project in Android Studio.
2. Connect an Android device or start an emulator.
3. Click on the "Run" button or use the Shift + F10 shortcut to build and run the application.



# video 




