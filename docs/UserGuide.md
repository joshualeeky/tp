# User Guide

## Introduction

PayMeBack is a CLI-based expense tracker for a group of friends travelling overseas together.

In such a group, it is easier for an individual to pay for expenses on behalf of the group rather than having each
individual pay for their share for every activity. At the end of the day, it is quite troublesome to
manually calculate how much money each individual owes to another. PayMeBack is designed to make this process
easy and fuss-free, by helping you calculate how much each person owes every other person.

<br />

## Table of Contents
* [Using this guide](#using-this-guide)
* [Quick Start](#quick-start)
* [Features](#features)
  * [Saving your data](#saving-your-data)
  * [Loading your saved data](#loading-your-saved-data)
  * [Trips](#trips)
    * [Create Trip](#--create-trip)
    * [Open Trip](#--open-trip)
    * [Close Trip](#--close-trip)
    * [List Trips](#--list-trips)
    * [View People in Trip](#--view-people-in-trip)
    * [Delete Trip](#--delete-trip)
    * [Edit Trip](#--edit-trip)
  * [Expenses](#expenses)
    * [Create Expense](#--create-expense)
    * [List Expenses](#--list-expenses)
    * [View an Expense](#--view-an-expense)
    * [Filter Expenses by Attribute](#--filter-expenses-by-attribute)
    * [Delete Expense](#--delete-expense)
  * [Settling Expenses](#settling-expenses)
    * [Amount](#amount)
    * [Optimize Transactions](#optimize-transactions)
  * [Summary of Expenses](#summary-of-expenses)
* [FAQ](#faq)
* [Command Summary](#command-summary)

<br>

## Using this guide

- Text bounded by `this formatting` refers to elements displayed in your window as the program is running.
It can either be an input entered by the user, or an output displayed by the program.
- All entries enclosed in square brackets, "[" and "]" refer to user-defined inputs.
For example, input values displayed as `[something]` can be determined by the user, where `something` is the data
entered by the user.

<br />


## Quick Start
1. Ensure that you have Java 11 or above installed.
2. Download the latest version of `PayMeBack` from [here](https://github.com/AY2122S1-CS2113T-T12-2/tp/releases), 
and move the downloaded file to your preferred folder.
3. Open any command-line application (such as Terminal, Command Prompt, or Powershell) and navigate to the folder
   containing your downloaded copy of `PayMeBack`.
4. In the command-line interface, type `java -jar PayMeBack.jar`.
5. If the program starts successfully, you should see the following on your screen:


```
Welcome to
    ____              __  ___     ____             __  
   / __ \____ ___  __/  |/  ___  / __ )____ ______/ /__
  / /_/ / __ `/ / / / /|_/ / _ \/ __  / __ `/ ___/ //_/
 / ____/ /_/ / /_/ / /  / /  __/ /_/ / /_/ / /__/ ,<   
/_/    \__,_/\__, /_/  /_/\___/_____/\__,_/\___/_/|_|  
            /____/                                     

```
<br>

<div style="page-break-after: always;"></div>

## Features

### Saving your data

By default, the program attempts to save your data to a file named `trips.json` located in the same directory
as the app each time after you run a command. This includes the `quit` command.

If the save attempt is unsuccessful, you will see an error message. The program will then attempt to save again the next time you run a command.

<br />

### Loading your saved data

The first time you open the program, or if the save file was deleted or moved, the program will create a save file 
named `trips.json` for you.

The next time you open the program, it will attempt to load your data from the save file. If the data is successfully 
loaded, you will see the following message:
```
Your saved data was successfully loaded!
```

If there is an error loading your save file, please go to the [FAQ](#faq)  for more information.

<br />

### Trips

#### - Create Trip

Creates a new Trip in the program. This command will only work when there is no trip open.

Input syntax:
```
create /[location] /[date] /[foreign-currency] /[exchange-rate] /[persons-in-trip]
```

All fields are compulsory. Note the following:
- `[location]` is the location of the trip. Any string can be entered.
- `[date]` must follow the format of dd-mm-yyyy.
- `[foriegn-currency]` is the 3-digit ISO code of the foreign currency (e.g USD, GBP). Currently there are 30 currencies supported, the currencies' names and ISO codes are listed below.
  - Some currencies will not have symbols as some terminals may not be able to support displaying of certain symbols.
  - Supported currencies will be rounded to either 2 decimal places or to the nearest whole number, 
  depending on the currency's smallest denomination.
  - The program is still runnable with unknown currencies, however there will be no symbol and the decimal place may not be accurate.
- `[exchange-rate]` should be how much 1 of your home currency costs in foreign currency.
  - Example: SGD $1 is equivalent to USD $0.74, hence the `exchange-rate` will be 0.74.
  - Note that the default home currency is SGD. To change the home currency, please refer to [Edit Trip](#--edit-trip).
- `persons-in-trip` should be separated with commas.

#### Compatible Currencies

Currency Name | ISO Code | Is symbol <br/> available?
--- | :---: | :---: |
United States Dollar | USD | Yes
Singapore Dollar | SGD | Yes
Australian Dollar | AUD | Yes
Canadian Dollar | CAD | Yes
New Zealand Dollar | NZD | Yes
Euro | EUR | No
Pound Sterling| GBP | No
Malaysian Ringgit | MYR | Yes
Hong Kong Dollar | HKD | Yes
Thai Baht | THB | No
Russian Ruble | RUB | No
South African Rand | ZAR | Yes
Turkish Lira | TRY |  No
Brazilian Real | BRL | Yes
Danish Krone | DKK | Yes
Polish Zloty | PLN | No
Israeli New Shekel | ILS | No
Saudi Riyal | SAR | Yes
Chinese Yuan | CNY | No
Japanese Yen | JPY | No
South Korean Won | KRW | No
Indonesian Rupiah | IDR | Yes
Indian Rupee | INR | Yes
Swiss Franc | CHF | Yes
Swedish Krona | SEK | Yes
Norwegian Krone | NOK | Yes
Mexican Peso | MXN | Yes
New Taiwan Dollar | TWD | Yes
Hungarian Forint | HUF | Yes
Czech Koruna | CZK | Yes
Chilean Peso | CLP | Yes
Philippine Peso | PHP | No
United Arab Emirates Dirham | AED | No
Colombian Peso | COP | Yes
Romanian Leu | RON | Yes

For example,

Input:

```
create /United States of America /02-02-2021 /USD /0.74 /Ben, Jerry, Tom
```

If successful, the output will be as follows:

```
Your trip to United States of America on 02 Feb 2021 has been successfully added!
```

<br />

#### - Open Trip

Opens a Trip, which allows the user to interact with other functionalities of our app, such as adding and viewing expenses.

Input syntax:
```
open [trip-number]
```

For example,

Input:

````
open 1
````
If successful, the output will be as follows:
```
You have opened the following trip:
United States of America | 02 Feb 2021
```

You can also run the `open` command while a Trip is already open. This will close the currently-opened
Trip, and open the specified Trip in the most recent command.

*Note: Only one Trip can be open at any time.*

<br />

#### - Close Trip

Closes the current Trip you are in. This allows you to interact with trips (e.g. add a new trip, delete a trip, or list all trips)

This command can only be used if a Trip is already open.

Input syntax:
```
close
```
For example,

Input:
````
close
````
If successful, the output will be as follows:
```
You have closed the following trip:
America | 02 Feb 2021
```

<br />

#### - List Trips

Lists all the Trips that you have created along with their index numbers. 

This command can only be used if no Trip is open. If a trip is open, this command will list down your expenses instead. See [List Expenses](#--list-expenses)
for more information on listing expenses.

Input syntax:
```
list
```
For example,

Input:
````
list
````
If successful, the output will be as follows:
```
List of Trips:
    1. London | 27 Oct 2021
```

<br />

#### - View People in Trip

Lists the persons involved in a particular Trip. 

This command can only be used if you have a Trip opened.

Input syntax:
```
people
```

<div style="page-break-after: always;"></div>

For example,

Input:
````
people
````


If successful, the output will be as follows:
```
These are the people involved in this trip:
	Ben
	Jerry
	Tom
```

<br />

#### - Delete Trip

Deletes a Trip from the program. 

This command can only be used when no trip is open. If a trip is open, this command will delete an expense instead. See [Delete Expense](#--delete-expense)
for more information on deleting expenses.

Input syntax:
```
delete [trip-number]
```
- `[trip-number]` is the index of the Trip you wish to delete, which can be found by using `list` command while no Trip is open.
- `delete last` to delete the trip you last interacted with.


For example,

Input:

````
delete 1
````
If successful, the output will be as follows:
```
Your trip to America on 02 Feb 2021 has been successfully removed.
```
<br />

<div style="page-break-after: always;"></div>

#### - Edit Trip

Edit the attributes of a Trip. This command can only be used when no trip is open.

Input syntax:
```
edit [trip-num] -[attribute] [new-value]
```
All fields are compulsory. Note the following:
- The following are the attributes that can be edited along with their corresponding syntax: 
  - Location: `-location`
  - Date: `-date`
  - Exchange Rate: `-exchangerate`
  - Foreign Currency ISO Code: `-forcur`
  - Home Currency ISO Code: `-homecur`
- The hyphen preceding `[attribute]` is part of the syntax.
- `[trip-number]` is the index of the Trip you wish to edit, which can be found by using `list` command while no Trip is open.
- `last` can be used for `[trip num]`. This will modify the last trip you interacted with.


For example,

Input:

````
edit 2 -location japan
````
If successful, the output will be as follows:
```
The location of your trip has been changed from tokyo to japan.
```
<br />

### Expenses
#### - Create Expense
Creates a new expense entry for the currently opened Trip. 

This command can only be used if you have a Trip opened.

<div style="page-break-after: always;"></div>

Input syntax:
````  
expense [amount] [category] [people] /[description]
````
All fields are compulsory. Note the following:
- `[amount]` is the total amount spent on the expense.

- `[category]` is a category tag for the expense. Only 1 category per Expense is allowed.

- `[people]` denotes the people involved in the expense. Multiple people involved are to be separated by commas.
  - Entering `-all` will add every person in the trip to the expense

- `[description]` is the description of the expense.

For example,

Input:
````
expense 30 food Ben, Jerry /In-and-Out Burgers
````

PayMeBack will then ask you to enter the date of the expense:
````
Enter date of expense:
	Press enter to use today's date
````
- The date must follow the format of dd-mm-yyyy.
- Pressing the enter key without keying in anything will use the current date.
- If the expense only has one person involved, the steps below will be skipped and the expense will be automatically added.

PayMeBack will then ask for the name of the person who paid for the expense:
````
Who paid for the expense?: 
````
- The name entered has to be part of the expense or PayMeBack will request for the name again.

<div style="page-break-after: always;"></div>

PayMeBack will then ask for the amounts spent by each person involved along
with how much of the amount has yet to be assigned:
````
Who paid for the expense?: Ben
Enter "equal" if expense is to be evenly split, enter individual spending otherwise
There is USD $30.00 left to be assigned. How much did Tom spend?: 
````
- The program will automatically cycle through every person involved in the expense.
- Entering `equal` when the program asks for the amount spent for the first person will cause the program to evenly split the expense among all the people involved in it.
  - Note: If the amount is not perfectly divisible by the number of people, the payer will bear the surplus or deficit.

If there is no amount remaining but there are still people left to be assigned, PayMeBack will prompt the user if they would like to assign 0 to the rest of the people involved in the expense:
````
There is USD $30.00 left to be assigned. How much did Ben spend?: 30
There will be people involved that don't need to pay, are you sure? (y/n): 
````
- Entering `y` will allow PayMeBack to assign 0 to the remaining people involved in the expense.
- Entering `n` will result in PayMeBack requesting for the name of the person who paid for the expense again, followed by the same sequence of steps.

PayMeBack will automatically prompt the user to assign the remaining amount when it requests for the amount spent of the last person in the expense:
````
There is USD $30.00 left to be assigned. How much did Ben spend?: 20
Assign the remaining USD $10.00 to Jerry? (y/n): 
````
- Entering `y` will allow PayMeBack to assign the amount to the persons stated.
- Entering `n` will result in PayMeBack requesting for the name of the person who paid for the expense again, followed by the same sequence of steps.

If successful, the output will be as follows:
````
Your expense has been added successfully
````

<br />

<div style="page-break-after: always;"></div>

#### - List Expenses

List all expenses in the current opened trip.

This command can only be used if a trip is open. If no trip is currently open, this command will list trips instead. See [List Trips](#--list-trips)
for more information on listing trips.

Input syntax:
````  
list
````

For example,

Input:
````
list
````

If successful, the output will be as follows:
````
List of Expenses: 
	1. In-and-Out Burgers | 03 Feb 2021
	2. Gift shopping at mall | 03 Feb 2021
	3. Dinner at Taco Bell | 03 Feb 2021
````

<br />

#### - View an Expense
Shows the details of a particular expense of a trip.

This command can only be used if a trip is open, and there is at least 1 expense.

Input syntax:
````
view [expense-number]
````
- Note that entering `view` without an index will print all expenses in the currently opened trip.
- Enter `view last` to view the last added expense.
  - Note: If the last added expense is deleted, you will not be able to use this command.

<div style="page-break-after: always;"></div>

For example,

Input:
````
view 1
````

If successful, the output will be as follows:
````
	In-and-Out Burgers
	Date: 03 Feb 2021
	Amount Spent: USD $30.00
	People involved: 
		1) Ben, USD $20.00
		2) Jerry, USD $10.00
	Payer: Ben
	Category: food
````


<br />

#### - Filter Expenses By Attribute
Allows the user to view specific expenses and index numbers based on an attribute of their choice.

This command can only be used if a trip is open, and there is at least 1 expense.

Input syntax:
````
view filter [expense-attribute] [search-keyword]
````
- `[expense-attribute]` can be either `[category]`, `[description]`, `[payer]`, `[person]` or `[date]`.

For example, if the user would like to search for all expenses in the category "food",

Input:
````
Enter your command: view filter category food
````

<div style="page-break-after: always;"></div>

If successful, the output will be as follows:
````
1.  In-and-Out Burgers
    Date: 03 Feb 2021
    Amount Spent: USD $30.00
    People involved: 
        1) Ben, USD $20.00
        2) Jerry, USD $10.00
    Payer: Ben
    Category: food
	
3.  Dinner at Taco Bell
    Date: 03 Feb 2021
    Amount Spent: USD $20.00
    People involved:
        1) Ben, USD $8.00 
        2) Jerry, USD $6.00
        3) Tom, USD $6.00
    Payer: Tom
    Category: food
````


<br />

#### - Delete Expense

Deletes an expense from a trip.

This command can only be used if a trip is open, and there is at least 1 expense. If no trip is open, this command will delete trips instead. See [Delete Trip](#--delete-trip)
for more information on deleting trips.

Input syntax:
```
delete [expense-number]
```
- `[expense-number]` is the index of the expense you wish to delete, 
and can be found by using `list` while a trip is open.
- `delete last` to delete the last added expense.
  - Note: After deletion, you will not be able to use this command again until you add another expense.

<div style="page-break-after: always;"></div>

For example,

Input:
````
delete 1
````
If successful, the output will be as follows:
```
Your expense of SGD $50.00 has been successfully removed.
```
<br />

### Settling Expenses
There are 2 commands that you can run to get a list of who pays who (WPW) to 
settle expenses. `amount` displays the WPW for 1 person, while `optimize` displays the WPW for everyone in 
the trip.

<br />

#### Amount
Shows the transactions that a person will have to engage in so that the person will not owe or be owed any money.
User needs to have a trip opened and have expenses added to use this command. Note that this list is not optimized, 
if you would like to settle all payments, please use `optimize` instead.

Input syntax:
```
amount [person-in-trip]
```

For example,

Input:

```
amount Ben
```

If successful, the output will be as follows:

```
Ben spent USD $350.50 (SGD $473.65) on the trip so far
Ben owes USD $30.00 (SGD $40.54) to Jerry
Ben does not owe anything to Dick
Ben owes USD $7.00 (SGD $9.46) to Eve
```
<div style="page-break-after: always;"></div>

#### Optimize Transactions

Shows the most optimized number of transactions to ensure that everyone is being paid back. User needs to have opened a trip and have expenses to use the command. 

Input syntax:

```
optimize
```

For example,

Input:

```
optimize
```

If successful, the output will be as follows:

```
Here is the optimized payment transactions:
yuzhao needs to pay USD $8.00 (SGD $8.00) to yikai
yuzhao needs to pay USD $13.00 (SGD $13.00) to qian
```

If no transactions are required, the user will see this message:

Output:
```
All are paid! :)
```
<br />

### Summary of Expenses
Shows an overall summary of individual expenses in a trip.

This command can only be used if you have opened a trip.

The input syntax is as follows:
````
summary [name]
````
- `[name]` is an optional argument.
- Note that entering `summary` without a name will print the summary of everybody in the opened trip.

<div style="page-break-after: always;"></div>

For example,

Input:

```
summary
```

If successful, the output will be as follows:
```
Ben has spent USD $50.00 (SGD $67.57) on 3 expenses on the following categories:
food: USD $30.00 (SGD $40.54)
travel: USD $20.00 (SGD $27.03)

Jerry has spent USD $150.00 (SGD $202.71) on 6 expenses on the following categories:
food: USD $30.00 (SGD $40.54)
travel: USD $20.00 (SGD $27.03)
leisure: USD $100.00 (SGD $135.14)

Tom has spent USD $75.00 (SGD $101.35) on 3 expenses on the following categories:
food: USD $25.00 (SGD $33.78)
travel: USD $20.00 (SGD $27.03)
shopping: USD $30.00 (SGD $40.54)
```

### Help

Shows a quick help message, depending on which stage the user is at.

Input syntax:

```
help
```

For example,

Input:

```
help
```

Output for if no trip is open:
````
Type "open [trip number]" to open your trip
While a trip is open, type "expense" to create an expense for that trip
Type "quit" to exit
````

<div style="page-break-after: always;"></div>

Output for if trip is open:
````
You are inside a trip. Trip specific commands:
	<more help information will be displayed here>
````

## FAQ

**Q**: How do I transfer my data to another computer?

**A**: To transfer your data to another device, simply copy over the "trips.json" file in the same directory as this
app to the device you wish to use. Ensure that the save file is stored in the same directory as the PayMeBack
app on your destination device before starting the program.

**IMPORTANT!** Before transferring data to the destination device, please ensure that the app is not running on the destination device.
If the app is running when the save file is pasted, you may accidentally overwrite the data in the save file when you run a command.

**Q**: There was an error loading my save file. What should I do?

**A**: If you see an error message when starting up the app, it is likely that your save file is corrupted. If you have modified the save file directly using another
application, you should try to undo those changes. Also ensure that your save file is in the same directory as 
the jar file.

If the file cannot be recovered, we recommend that you run the app again. When prompted to overwrite the
corrupted save file, enter `y`. You may then proceed to re-enter your data.

**Q**: I made a mistake while entering a command. How can I exit the process and start again?

**A**: If the app is asking you to correct an erroneous input, but you wish to cancel the process, 
type `-cancel`. This will return you to the preceding state of the program, allowing you to enter 
any command.

<div style="page-break-after: always;"></div>

## Command Summary
Hyphen before square brackets (eg `summary -[name]`) denotes optional arguments

### General commands

Action | Command syntax
---|---
Display help|`help`
Quit|`quit`
Cancel operation ([see FAQ](#faq)) | `-cancel`

<br />

### Trip commands

Action | Command syntax | Example
---|---|---
Create trip | `create /[location] /[date] /[foreign-currency-ISO-code] /[exchange-rate] /[persons-in-trip]`|`create /America /02-02-2021 /USD /0.74 /Ben, Jerry, Tom`
Open trip | `open [trip-number]` | `open 1`
Close trip | `close` | `close`
List trips | `list` when no trip is opened| `list`
List persons involved in a trip | `people` | `people`
Delete trip | `delete [trip-number]`|`delete 1`
Edit trip | `edit [trip num] [attribute] [new value]` <br /><br /> attributes: -location, -date, -exchangerate, -forcur, -homecur  | `edit 1 -location Afghanistan`

<br />

<div style="page-break-after: always;"></div>

### Expense commands
Open a trip to use

Action | Command syntax | Example
---|---|---
Create expense|`expense [amount] [category] [people] /[description]`|`expense 30 food Ben, Jerry /In-and-Out Burgers`
List expenses |`list` when inside a trip| `list`
View an expense in detail |`view [expense-number]`| `view 1`
View filtered expenses in detail | `view filter [expense-attribute] [search-keyword]` <br /><br /> `expense-attribute: category, payer, person, description`|`view filter category food`
Delete expense|`delete [expense-number]`|`delete 1`
View list of expense| `summary -[name]` <br /> If name is not provided, displays all expenses like `list`| `summary` or `summary Ben`
View expense settlement actions of a person| `amount [person-in-trip]` | `amount Ben`
View optimized settlement actions for everyone in the trip|`optimize`|`optimize`






