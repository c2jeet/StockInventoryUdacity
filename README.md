# StockInventoryUdacity

This project is done for the Udacity Inventory App. For the Core Functionality of the Database, it used Content Providers and Uri Matcher concepts.

Rubrics Requirements:


1.The App Contains an ListView which is populated by querying all Items Stored in the Database. There is a FAB Button which redirects to Editor Activity to Add New Items.

2. List Item contains Item Name, Item Price, Quantity of Items left in stock and a Sell! Button, used to indicate a Sale scenario, which updated a decremented quantity of the Item into the database, Qty is always > 0.

3. Upon clicking on the Item, we are redirected to the Editor Activity which shows rest of the details of the Item.

4. Editor Activity contains Quantity Increment and Decrement Buttons which Increase or Decrease the Quantity based on the Quantity Value taken input in the Shared Preferences Activity.

5. Users can select on the Order More functionality to order more Items of the said product. Phone and Mail Intents are used to help with the same.

6. Users can select Delete This Item functionality to delete the given item from the Database.

7. When the List View is empty i.e. when there are no Items in the Database to be queried upon, an Empty View is displayed on the MainActivity.

8. Editor Activity checks if the inputs into the Text Views in the Editor Activity are not null. Also, selected Uri of the image and Mail, Phone are also validated.


I have used Picasso Library to load Image Uris into the corresponding Image Views.

![1](https://user-images.githubusercontent.com/29694099/27535817-e53f66c0-5a89-11e7-8ed2-510d531ed41d.jpg)
![2](https://user-images.githubusercontent.com/29694099/27535838-f8cf3594-5a89-11e7-8f86-4a24eb34878a.jpg)
![3](https://user-images.githubusercontent.com/29694099/27535854-05a8d392-5a8a-11e7-9d7e-605775528565.jpg)
![5](https://user-images.githubusercontent.com/29694099/27535861-0cba8b80-5a8a-11e7-8cdf-5c4207c424a2.jpg)
