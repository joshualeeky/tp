# Liang Yuzhao - Project Portfolio Page

## Overview of Project

_PayMeBack_ is an expense tracker targeted at groups travelling overseas. It aims to help groups simplify the process of
repayment for overseas expenses, by consolidating all expenses and issuing a summary report at the end, so users can easily
identify who they need to pay.

_PayMeBack_ is a greenfield project. My main responsibilities in this project included:
- As the team member most proficient in Java, to provide guidance and mentorship to other team members on Java.
- To manage the team organisation and repository.
- To implement and test new features in the program.
- To maintain high quality of code and workflows in collaboration with other members of the team.

### Summary of Contributions

#### Code Contributions

I have contributed over >1800 lines of code and documentation in total.
Detailed code contribution information can be viewed via RepoSense [here](https://nus-cs2113-ay2122s1.github.io/tp-dashboard/?search=&sort=groupTitle&sortWithin=title&timeframe=commit&mergegroup=&groupSelect=groupByRepos&breakdown=true&checkedFileTypes=docs~functional-code~test-code~other&since=2021-09-25&tabOpen=true&tabType=authorship&tabAuthor=yeezao&tabRepo=AY2122S1-CS2113T-T12-2%2Ftp%5Bmaster%5D&authorshipIsMergeGroup=false&authorshipFileTypes=docs~functional-code~test-code~other&authorshipIsBinaryFileTypeChecked=false).

- Provided basic skeleton for `Ui`, `Parser`, `Trip`, `Expense`, `Person` classes.
- Implemented and tested `FileStorage` class, and some of `Ui`, `Trip`, `Storage`, `Parser`, and `ForceCancelException` classes.
- Wrote a large part, or the entirety, of `FileTest`, `TripTest`, and `ValidityCheckerTest`.

#### Enhancements and Features Implemented:

- **New**: Using `last` to get the trip which the user last interacted with.
- **New**: Reading from and writing to a save file using the JSON format (through serialisation/deserialisation with Google Gson).
- **Enhancement**: Adding custom serialiser/deserialiser to Gson for LocalDate objects.
- **Enhancement**: Ability to assist user in detecting and overwriting a corrupted save file.
- **New**: Allowing users to abort a process when asked to correct an erroneous input (completed in collaboration with @joshualeeky)

#### Contributions to User Guide:

The following sections in the [User Guide](https://ay2122s1-cs2113t-t12-2.github.io/tp/UserGuide.html) were largely or wholly written by me:

- Saving your data, Loading your saved data
- Create Trip, Open Trip, Close Trip, Delete Trip, Edit Trip
- FAQ #2 and #3

In addition, I made contributions to the following sections:

- Introduction, Using this guide, Quick Start
- FAQ #1

#### Contributions to Developer Guide:

The following sections in the [Developer Guide](https://ay2122s1-cs2113t-t12-2.github.io/tp/DeveloperGuide.html) were largely or wholly written by me:

- `Storage` component, and its related diagrams
- Non-functional Requirements

In addition, I made contributions to the following sections:

- Some text in `Trip` class
- Manual testing instructions for trip-related features

#### Contributions to Team Tasks:

- Setup and administration of organisation and team repository, and reviewing pull requests
- Adding templates for User Stories and Bug Reporting in Issues
- Gradle modifications (for enabling assertions and adding Gson dependency)
- Managed `v2.0` and `v2.1` release
- Cleanup of PE-D issues (marking duplicates, rejecting non-issues)
- General maintenance of issue tracker (milestone and label assignment)

#### Review and Mentoring contributions

- Comments on PRs - [#31](https://github.com/AY2122S1-CS2113T-T12-2/tp/pull/31#discussion_r723066635),
  [#42](https://github.com/AY2122S1-CS2113T-T12-2/tp/pull/42#discussion_r725532182), [#55](https://github.com/AY2122S1-CS2113T-T12-2/tp/pull/55#discussion_r726785554)
- Bug reports - [#135](https://github.com/AY2122S1-CS2113T-T12-2/tp/issues/135), [#49](https://github.com/AY2122S1-CS2113T-T12-2/tp/issues/49), [#45](https://github.com/AY2122S1-CS2113T-T12-2/tp/issues/45), 
- Mentoring on JUnit tests to simulate and test user inputs and outputs

#### Contributions beyond the team

- Replying to module forum posts ([#74](https://github.com/nus-cs2113-AY2122S1/forum/issues/74#issuecomment-922768286))
- Reporting of bugs on module website ([#93](https://github.com/nus-cs2113-AY2122S1/forum/issues/93))