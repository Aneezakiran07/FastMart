# FastMart - Android E-Commerce App

A simple e-commerce app UI. FastMart demonstrates core Android development concepts including layouts, intents, SMS, and alert dialogs.

## Screens

### Splash Screen
- Animated courier truck sliding across the screen
- Transitions automatically to the Home screen after 3 seconds

### Home Activity
- Welcome header message
- Deal of the Day section with a featured product
- Recommended Products section with 4 product cards

### Product Activity
- Displays full product details including image, name, model, price and description
- Buy Now button triggers a confirmation alert dialog
- On confirmation, sends an SMS with the purchase message


## Tech Stack

- Language: Java
- UI: XML Layouts (LinearLayout, RelativeLayout, CardView, ScrollView)
- Android Concepts Used:
  - Translate animations and animation XML files
  - Explicit Intents with data passing via putExtra and getStringExtra
  - Reusable layouts with the include tag
  - SmsManager for SMS
  - AlertDialog.Builder for confirmation dialogs


## Project Structure

```
app/
├── java/
│   └── com.example.fastmart/
│       ├── SplashActivity.java
│       ├── HomePage.java
│       └── ProductActivity.java
└── res/
    ├── anim/
    │   └── left_to_right.xml
    └── layout/
        ├── splash_activity.xml
        ├── homepage_activity.xml
        ├── activity_product.xml
        └── product_card.xml
```

---

## How to Use

A pre-built APK is available for direct installation on Android devices. No need to build from source.

Download it here:

Steps:
1. Download the APK from the link above
2. Open the APK file on your Android device
3. If prompted, allow installation from unknown sources
4. Install and open FastMart


## Build From Source

1. Clone the repository
2. Open in Android Studio
3. Run on an emulator or physical Android device (API 21 or higher)
4. Grant SMS permissions when prompted
