# Project Portfolio Page - Joshua Lee

## Overview
_PayMeBack_ is an expense tracker targeted at groups travelling overseas. It aims to help groups simplify the process of
repayment for overseas expenses, by consolidating all expenses and issuing a summary report at the end, so users can easily
identify who they need to pay.

## Summary of Contributions
The following section provides a summary of what I have contributed to the project.

### Code Contributed
I have contributed over 1800 lines of code, split among documentation, test and functional code. <br />
My code contributions can be found via this 
[link](https://nus-cs2113-ay2122s1.github.io/tp-dashboard/#breakdown=true&search=joshualeeky). 

I have contributed the following features to PayMeBack:
1. The Person class in its entirety.
2. Part of the Expense class constructor that helps determine if the names that have been entered are valid.
3. The ExpenseSplitter interface which helps keep track of how much the people involved in the expense owe the payer.
4. The `amount` command which gives a brief overview of the amount of money that is owed between the specified person 
and the other people involved in the trip.

### Enhancements implemented
I have contributed the following enhancements to PayMeBack:
1. The refactoring of the Parser class to help with overall neatness of the project.
2. The printing of currency in a user-friendly manner including the HashMap of the various currency ISO-codes and symbols.
3. Implementation of `last` for expenses to allow users to delete or view their most recently added expense.
4. Implemented a check to ensure that the people added into a trip or expense do not contain people with the same name.
5. Implementation of the command `-cancel` which allows users to abort a process when asked to correct an erroneous 
input (completed in collaboration with @yeezao).


### Contributions to the UG
For the user guide, I contributed mainly to the features that I had implemented, which includes the section on the command 
`amount` as well as the entirety of the section regarding the command `expense`. I also aided in the creation of 
the table of available currencies. The user guide can be found 
[here](https://ay2122s1-cs2113t-t12-2.github.io/tp/UserGuide.html).

### Contributions to the DG
For the developer guide, I contributed to the sequence as well as the class diagram of the `parser` class and the class' 
overall description. The developer guide can be found 
[here](https://ay2122s1-cs2113t-t12-2.github.io/tp/DeveloperGuide.html).

### Contributions to team-based tasks
In terms of contributions to team-based tasks, I feel like I have contributed most significantly in the following areas:
1. I helped to review members' pull requests into the GitHub repository while ensuring that whatever is being pulled is 
in line with the proposed intent.
2. I assisted in the refactoring of functions to help with the overall neatness of the project.
3. I openly reached out to help any teammate who may have any bugs in their code with whatever tips and advice
I could offer.
4. I actively sought out bugs in the program and placed them on the issue tracker on GitHub to be addressed by the team.

### Review/mentoring contributions
Throughout the project I ensured I was fully aware of the different functions and enhancements each member was adding 
into the project. This allowed me to understand the scope and capabilities of the program so that I would be able to
guide the team in areas of improvement if needed. This also allowed me to help with any misunderstandings a member may 
have with the overall structure of the program.

### Contributions beyond the project team
I took part in peer review exercises seriously to give sincere and meaningful feedback to other teams to help them 
improve their projects.