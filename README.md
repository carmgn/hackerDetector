# hackerDetector
Technical exersice

## Hacker detection system
A system provided by a company allows customers to sign in using their username and
password. There is a requirement for an automated system to be developed to help identify
attempts to hack the system and compromise accounts. Activity log files are recorded and
the new system will need to process these logs to identify suspicious activity.
Write a Java program implementing the HackerDetector interface (outlined below) which
defines a single method 'parseLine'. The method should take one line of the log file at a time
and return the IP address if any suspicious activity is identified or null if the activity appears to
be normal.

The parseline method will be called each time a new log line is produced.
The log lines will be in the following format:
ip,date,action,username
IP look like 80.238.9.179 Date is in the epoch format like 1336129471 Action is one of the following:
SIGNIN_SUCCESS or SIGNIN_FAILURE Username is a String like Will.Smith
A log line will therefore look like this: 80.238.9.179,133612947,SIGNIN_SUCCESS,Will.Smith
The first detection method will be to identify a single IP address that has attempted a failed
login 5 or more times within a 5 minute period. On detection you should return the suspicious IP.


## Time Calculation
Write a function that returns the number of minutes (rounded down) between two
timestamps time1 and time2 in RFC 2822 format (ie: Thu, 21 Dec 2000 16:01:07 +0200).
Don’t forget about the time zones.

## Usage
compile
```bash
gradlew clean build
```
Execute
```bash
gradlew bootRun
```

## API USE

POST method
http://localhost:8080/hackerDetector/parseLog
Body
80.238.9.176,1615026083,SIGNIN_FAILURE,Will.Smith