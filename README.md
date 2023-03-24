# RemindMe

Repository for Mobile Computing course project. Idea is to make a reminder app for Android using Kotlin. User can set reminder with time or location requirements and the reminder will notify user when the requirements are met.

![main_screen](https://user-images.githubusercontent.com/55877751/227501645-14ff4704-41d9-4df2-9337-a97566a63eb7.png)

The main screen consists of a list of all existing reminders. There is also buttons for choosing user's virtual location and adding new reminders. User can also choose if the app should display all reminders or nearby reminder. If neather of these is selected only reminders that have already been occurred will be displayed.

![new_reminder](https://user-images.githubusercontent.com/55877751/227502071-5719edd8-f242-49d3-ad18-a55530a969c8.png)

New reminders are created through this screen. All the attributes are optional but in normal use user would write some message there and then pick either time or location for the reminder. In some cases user may want to pick both.

![pick_location](https://user-images.githubusercontent.com/55877751/227502579-5be2b479-d788-425b-961e-8ee161bc65fc.png)

The app has Google Maps implementation in it. It is used to pick the location for the reminders and choosing the virtual location for the user.

![edit_screen](https://user-images.githubusercontent.com/55877751/227502785-412acfb1-812c-486a-b05b-2a9ce7849f76.png)

Reminders can be modified/deleted by clicking any existing reminder which opens this modify screen. It uses the same popup as adding new reminders. 
