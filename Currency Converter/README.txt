I worked on my own for this homework. 
Name: Bella Xia
JHED: zxia15


Variations:

1. Added two more toasts:

A toast when the user did not select a from currency. This is implemented since the initial screen for the currencies show a placeholder "Select Currency", which does not specify which currency to convert to and thus cannot proceed the convert command.

A toast when the user did not select a home currency. This is not mentioned in the design because the screen before and after selecting Franc currency as home currency are made into one screen, so I am not sure what is the default screen without home currency. But I presume that no assumption is made on home currency so that a "No Default Home Currency" is displayed. And when the user hit on the convert button, the toast is implemented which tells the user that a home currency is not selected.

2. Implemented the EditText for From Currency Amount input so that the user cannot input negative numbers. In this way, a potential exception is resolved and that the only other condition requiring the toast for positive values would be when the user leaves the from amount value at the default 0.

3. Since custom toast was deprecated for API 30, I switched the design to a snack bar. I switched the cross sign to a text button saying "dismiss" at the end of the snack bar, and otherwise retained its background and text color.

The separation of activities is a little non-intuitional since it separates an integral and logically cohesive part of the currency conversion - namely the home currency - as an independent activity. It does make sense that the designer seems to want to differentiate the home currency - the one that is mostly set in stone and unchangeable - and the from currency, which can be flexibly changed each time of use. However, by separating the home currency out and moving it to another activity, the designer increases the difficulty of navigating to the home currency. Therefore a design change I would suggest would be that instead of making the selection of home currency a part of the main activity, such as adding a bookmark notation in the front of the currency suggesting a saved currency. As to the transaction fee, it is relatively more distant from the other elements of the currency conversion that it is more reasonable that it is in a separate activity. However, since an activity with only the functionality of setting a transaction fee would not be the most efficient desire, I would suggest that the transaction fee may also be incorporated as an edit text in the main activity so that all value-settings can remain in one activity without the need to navigate back and forth.

I tested all the combinations of currencies to make sure that they either produce a toast (if I have two identical currencies) or are able to convert the correct values. I used values such as 0 and 1 for the from amount and 10% for transaction for ease of identifying the conversion rate. I also tested on all devices to ensure that there will be no problem particular to one device and that the layout would fit on all devices.

The most challenging part of the assignment to me maybe the layout design part of the application. I had a very hard time trying to make the screen fit for all devices and at least aesthetically tolerable. The most frustrating part of it is letting the most shrunk view (the Nexus One phone) and the most expanding view (the landscape tablet) both to have their correspondingly sized text and buttons. In the end, I have managed to ensure that the layout fits all devices relatively. However, even now I would concede that there are several devices that have thinner than ideal text-boxes that I cannot figure out a way to fit better.
