## **Design Description Document**

1)When the app is started, the user is presented with the main menu, which allows the user to (1) enter or edit current job details, (2) enter job offers, (3) adjust the comparison settings, or (4) compare job offers (disabled if no job offers were entered yet).

- I add a 'user' class which can do these four action: 'enter job offer', 'enter or edit current job', adjust the 'setting', do the 'comparison' of jobs offers. 

2)When choosing to enter current job details, a user will:
a. Be shown a user interface to enter (if it is the first time) or edit all of the details of their current job, which consist of:
Title
Company
Location (entered as city and state)
Cost of living in the location (expressed as an index)
Yearly salary
Yearly bonus
Allowed weekly telework days (expressed as the number of days per week allowed for remote work, inclusively between 0 and 5)
Leave time (vacation days and holiday and/or sick leave, as a single overall number of days)
Number of company shares offered at hiring (valued at $1 per share and expressed as a number >= 0)
b. Be able to either save the job details or cancel and exit without saving, returning in both cases to the main menu

- 'enter current job' action includes many requirement which we added every details of job as an item. The 'has entered current job' in 'User' class tell if it is the first time. The method 'savejob()' can save the current job with details and 'cancel and exit()' will cancel and exit without saving. the 'current job' is a class and it's sub class of 'job'.'job' class has all the details of jobs as items.

3)When choosing to enter job offers, a user will:
Be shown a user interface to enter all of the details of the offer, which are the same ones listed above for the current job.
Be able to either save the job offer details or cancel.
Be able to (1) enter another offer, (2) return to the main menu, or (3) compare the offer (if they saved it) with the current job details (if present).

- 'enter job offer' action requires all the details of the job offers which are same items in 'job' class. save() method save the job. cancel() method cancel the action. enter another() method can let user enter another offer. exit to menu() method return to the main menu. compare offer() method compare the offer with 'current job'. the 'saved' will tell if offer is saved or not. 'isempty' in 'current job' tell if we have current job entered. 

4)When adjusting the comparison settings, the user can assign integer weights to:
Yearly salary
Yearly bonus
Allowed weekly telework days
Leave time
Shares offered
If no weights are assigned, all factors are considered equal.

- 'user' can adjust 'setting' which has items (weights for Yearly salary, Yearly bonus, Allowed weekly telework days, Leave time, Shares offered). and their default value are all 1 which means all factors are considered equal if no weights are assigned.

5)When choosing to compare job offers, a user will:
Be shown a list of job offers, displayed as Title and Company, ranked from best to worst (see below for details), and including the current job (if present), clearly indicated.
Select two jobs to compare and trigger the comparison.
Be shown a table comparing the two jobs, displaying, for each job:
Title
Company
Location
Yearly salary adjusted for cost of living
Yearly bonus adjusted for cost of living
Allowed weekly telework days
Leave time
Number of shares offered
Be offered to perform another comparison or go back to the main menu.

- 'user' can performe 'comparison'. I create a 'rank' class which use weight from 'setting' and jobs' detail in database to create a ordered job list. job is odered by job's score which is calculate by compute job's score() method. and then ranking() method will return the ordered job list which will be shown to user by GUI. user will choose two jobs so I add two job item. compare() method will perform the comparison. compare() method will show a table contains the details of two jobs. so 'title' 'company' 'location' ... these details of job added as item to the 'comparison' class. another compare() method can perform another comparison and exit to main menu() method will take user back to main menu()

6)When ranking jobs, a job’s score is computed as the weighted sum of:
AYS + AYB + CSO/4 + (LT * AYS / 260) - ((260 - 52 * RWT) * (AYS / 260) / 8)
where:
AYS = yearly salary adjusted for cost of living
AYB = yearly bonus adjusted for cost of living
CSO = Company shares offered (assuming a 4-year vesting schedule and a price-per-share of $1)
LT = leave time
RWT = telework days per week
The rationale for the RWT subformula is:
value of an employee hour = (AYS / 260) / 8
commute hours per year (assuming a 1-hour/day commute) =
1 * (260 - 52 * RWT)
therefore travel-time cost = (260 - 52 * RWT) * (AYS / 260) / 8
For example, if the weights are 2 for the yearly salary, 2 for the shares offered, and 1 for all other factors, the score would be computed as:
2/7 * AYS + 1/7 * AYB + 2/7 * (CSO/4) + 1/7 * (LT * AYS / 260) - 1/7 * ((260 - 52 * RWT) * (AYS / 260) / 8)

- 'rank' class will create an ordered job list by their job's score. job's score is calculated using compute job's score method().'AYS','AYB','CSO','LT','RWT' along with the weights from 'setting' are added to 'rank' class because they will be used by job's score calculation.

7)The user interface must be intuitive and responsive.

- This is not represented in my design, as it will be handled entirely within the GUI implementation.



