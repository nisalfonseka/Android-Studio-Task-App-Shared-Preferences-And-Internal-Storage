

# Task Management App

This project is an Android application built using **Kotlin**. The app is designed to help users manage their daily tasks efficiently by saving data locally using **SharedPreferences** and **Internal Storage**.

## Features

- **Task List Management**: Users can create, view, edit, and delete tasks.
- **Persistent Storage**: 
  - **SharedPreferences**: Used for storing lightweight data, such as app settings or user preferences.
  - **Internal Storage**: Used for saving task details (titles, descriptions, deadlines) locally on the device, ensuring data is persistent across app restarts.
- **Simple User Interface**: The app uses a clean, intuitive interface for managing tasks.
- **Task Reminders**: Basic task reminders using notifications (optional, if implemented).

## Tech Stack

- **Language**: Kotlin
- **IDE**: Android Studio
- **Data Storage**:
  - **SharedPreferences**: For storing user preferences.
  - **Internal Storage**: For storing task data.
  
## How the App Works

1. **Adding Tasks**: Users can add new tasks by entering a task title, description, and deadline.
2. **Editing Tasks**: Tasks can be edited or deleted easily by tapping on the task from the list.
3. **Saving Data**: 
   - Task data is saved locally on the device using **Internal Storage**.
   - User preferences (like sorting tasks by deadline) are saved using **SharedPreferences**.
4. **Persistent Data**: Task information is available even after restarting the app, thanks to internal storage.

