Data Processing System – Concurrency in Java and Go

This project implements a multi-threaded Data Processing System in Java and Go, demonstrating two different concurrency models:

Java: Thread-based, shared-memory concurrency

Go: Goroutine + channel–based concurrency

The system simulates multiple workers retrieving tasks from a shared queue, processing them in parallel, and writing results to a shared output resource with proper synchronization and safe shutdown.

📁 Project Structure
data-processing-system/
│
├── java/
│   └── src/main/java/com/example/dataproc/
│        ├── Task.java
│        ├── TaskQueue.java
│        ├── Worker.java
│        ├── ResultWriter.java
│        └── DataProcessingApp.java
│
└── go/
    ├── main.go
    └── go_results.txt   (generated after running)

🚀 Java Version
✔ Technologies Used

Java 11+

ExecutorService (thread pool)

BlockingQueue (LinkedBlockingQueue)

Synchronized file writing

Try/catch exception handling

Logging with java.util.logging

📌 Features

Shared thread-safe task queue

Worker threads process tasks in parallel

Synchronized output writing

“Poison pill” tasks for clean termination

Logging for lifecycle + error handling

▶️ How to Run the Java Program
1. Navigate to the Java source directory:
cd java/src/main/java

2. Compile:
javac com/example/dataproc/*.java

3. Move one level up (into the java/ folder):
cd ../..

4. Run the main class:
java com.example.dataproc.DataProcessingApp

📄 Output

A file java_results.txt is created containing lines like:

Worker 1 processed payload-3
Worker 3 processed payload-1
Worker 2 processed payload-2
Worker 4 processed payload-4
...


Order varies because of parallel execution.

🟦 Go Version
✔ Technologies Used

Go 1.20+

Goroutines

Channels for task distribution + output collection

sync.WaitGroup for concurrency coordination

Buffered file I/O

Explicit error handling through returned error

📌 Features

Worker goroutines process tasks concurrently

Channel-based task and result flow

Dedicated writer goroutine

Graceful shutdown once all workers finish

▶️ How to Run the Go Program
1. Navigate to the Go folder:
cd go

2. Run the program:
go run main.go

📄 Output

A file go_results.txt is generated, containing lines like:

worker 3 processed task 1 with payload 'payload-1'. RESULT = PAYLOAD-1
worker 1 processed task 2 with payload 'payload-2'. RESULT = PAYLOAD-2
...

📘 Comparison: Java vs Go Concurrency
Java

Uses threads (heavy OS-level)

Shared-memory model

Requires explicit synchronization (synchronized, locks)

Exception handling via try/catch

Go

Lightweight goroutines (multiplexed on OS threads)

Communicate via channels, not shared state

Synchronization comes from channel semantics

Error handling via explicit error return values

🧪 Sample Terminal Output
Java:
Worker 1 started.
Worker 4 started.
Worker 3 started.
Worker 2 started.
...
Worker 3 finished.
Worker 4 finished.

Go:
2025/11/30 23:32:06 worker 3 started
2025/11/30 23:32:06 worker 2 started
2025/11/30 23:32:06 worker 1 started
2025/11/30 23:32:06 worker 4 started
...
