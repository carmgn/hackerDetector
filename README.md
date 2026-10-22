# hackerDetector

[![CI](https://github.com/carmgn/hackerDetector/actions/workflows/ci.yml/badge.svg)](https://github.com/carmgn/hackerDetector/actions/workflows/ci.yml)

REST service that reads sign-in log lines one at a time and flags IPs that look like a brute-force attack: **5 or more failed logins from the same IP within 5 minutes**.

It started as a technical exercise for a hiring process. Version 2 rewrites it on a current stack and fixes the detection window.

**Stack:** Java 21 · Spring Boot 3.3 · Gradle 8 · JUnit 5 · AssertJ

## The exercise

### Hacker detection

Implement `HackerDetector.parseLine(String line)`. It receives one log line each time the system writes one, and returns the IP if the activity is suspicious or `null` if it looks normal.

Log line format: `ip,date,action,username`, where the date is epoch seconds and the action is `SIGNIN_SUCCESS` or `SIGNIN_FAILURE`.

```
80.238.9.179,1336129471,SIGNIN_FAILURE,Will.Smith
```

### Time calculation

Return the number of minutes, rounded down, between two RFC 2822 timestamps such as `Thu, 21 Dec 2000 16:01:07 +0200`, taking time zones into account.

## How it works

- `LogLine.parse` validates the line (4 fields, numeric date, known action, non-blank IP). Malformed lines are ignored instead of throwing.
- `SlidingWindowHackerDetector` keeps, per IP, the timestamps of its recent failures in a `ConcurrentHashMap<String, Deque<Long>>`.
  - Each failure is handled atomically inside `compute`: the timestamp is appended, entries older than the window are evicted, and the IP is flagged when the count reaches the threshold.
  - The window is measured on **log timestamps**, not on the server clock, so replayed or delayed logs give the same result.
  - IPs with no failure inside the window are purged periodically, so memory stays bounded.
- `TimeCalculation.minutesBetween` parses both values with `DateTimeFormatter.RFC_1123_DATE_TIME` and returns the absolute difference in whole minutes.

The threshold and window are configurable in `application.properties`:

```properties
hackerdetector.attempts=5
hackerdetector.window=5m
```

## Run

```bash
./gradlew build      # compile and run the tests
./gradlew bootRun    # start on http://localhost:8080
```

## API

`POST /hackerDetector/parseLog` with one log line as a plain-text body.

| Response | Meaning |
|---|---|
| `200 OK` + IP in the body | The IP is suspicious |
| `204 No Content` | Normal activity, or a malformed line |

```bash
curl -i -X POST http://localhost:8080/hackerDetector/parseLog \
     -H 'Content-Type: text/plain' \
     -d '80.238.9.176,1615026083,SIGNIN_FAILURE,Will.Smith'
```

Health check: `GET /actuator/health`.

## What changed in v2

| v1 | v2 |
|---|---|
| Spring Boot 2.0, Gradle 6, Lombok, Java EE annotations | Java 21, Spring Boot 3.3, Gradle 8, records |
| Custom cache on `DelayQueue` that expired entries after 60 s of wall-clock time, shorter than the 5-minute rule | Sliding window measured on the log timestamps, inclusive of the full 5 minutes |
| Hard-coded rule | Threshold and window in configuration, validated at startup |
| Always `200`, empty body when normal | `200` with the IP, or `204` |
| JUnit 4 + junit-dataprovider | JUnit 5 parameterized tests, a concurrency test and a MockMvc test |
