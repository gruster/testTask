Screens:
1) Splash Screen
It shall appear on app start for 3 seconds. After that, "Users Screen" shall appear.

UI
White background with black centered text "Logo".


2) Users Screen
Displays list of users. Row selection should cause navigating to "User Details Screen" and display detailed info of selected user. If there are some network/server problems while fetching data from the server, notify user about it (use Toast/Alert/SnackBar).

UI
Bar on the top of the screen. Title: "Users"
Vertical list of users.
https://lh4.googleusercontent.com/7ATp_byJkExWFTJrP-DwrKsWuRnRbDvNT-2hzC2VkT1T4eosfP8rl208cotmhXgIqzcueeZfpFMbQTNxY6q11A0Tcryi48twxjCONv73Ym6aesPP4L6WLgdDa2_cGFj4QElhbfOq
name = {user.name}. Bold.
description = {user.email}
info = {user.company.catchPhrase}. Truncate text with … if there are more than 2 lines.

API
users list: http://jsonplaceholder.typicode.com/users
user avatars: https://quizee.app/storage/avatars/{user.id}.jpeg (i. e. use https://quizee.app/storage/avatars/9.jpeg for user with id = 9)

3) User Details Screen
Displays detail user's info.

UI
Data should be displayed vertically (top-->bottom):
- name
- email. Tapping on it should provide possibility to send an email to this address.
- phone. Tapping on it should provide possibility to make call.
Rest of the screen shall be splitted into 2 equal parts horizontally:
- web view with opened website from user's data
- map view with user's address pin. Animate and zoom map (street level). Tapping on pin should show popup with address's street

Optional ussues:
1) Handle portrait + landscape orientations on non-tablet devices.
2) Support tablet devices. Landscape mode only. Use different design patterns for both mobile and tablet: http://developer.android.com/images/fundamentals/fragments.png("Users Screen" to the left and "User Details Screen" to the right).
3) Provide data cache. User should be able to browse the app even if data fetching from server has failed. If there is no cached data yet (1st app launch), notify user (show error message).
