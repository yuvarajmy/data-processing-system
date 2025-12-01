package main

import (
	"bufio"
	"fmt"
	"log"
	"os"
	"strings"
	"sync"
	"time"
)

type Task struct {
	ID      int
	Payload string
}

func worker(id int, tasks <-chan Task, results chan<- string, wg *sync.WaitGroup) {
	defer wg.Done()
	log.Printf("worker %d started", id)

	for task := range tasks {
		// Simulate processing delay
		sleepDuration := 200 * time.Millisecond
		time.Sleep(sleepDuration)

		// Process task (uppercase payload)
		result := fmt.Sprintf(
			"worker %d processed task %d with payload '%s'. RESULT = %s",
			id, task.ID, task.Payload, strings.ToUpper(task.Payload),
		)
		results <- result
	}

	log.Printf("worker %d finished", id)
}

func resultWriter(results <-chan string, filePath string, wg *sync.WaitGroup) {
	defer wg.Done()

	file, err := os.Create(filePath)
	if err != nil {
		log.Printf("error creating result file: %v", err)
		return
	}
	defer func() {
		if cerr := file.Close(); cerr != nil {
			log.Printf("error closing result file: %v", cerr)
		}
	}()

	writer := bufio.NewWriter(file)
	defer func() {
		if err := writer.Flush(); err != nil {
			log.Printf("error flushing writer: %v", err)
		}
	}()

	for line := range results {
		_, err := writer.WriteString(line + "\n")
		if err != nil {
			log.Printf("error writing line '%s': %v", line, err)
		}
	}
}

func main() {
	log.Println("Data Processing System (Go) starting...")

	const numWorkers = 4
	const numTasks = 20
	const resultFile = "go_results.txt"

	tasks := make(chan Task)
	results := make(chan string)

	var wgWorkers sync.WaitGroup
	var wgWriter sync.WaitGroup

	// Start result writer goroutine
	wgWriter.Add(1)
	go resultWriter(results, resultFile, &wgWriter)

	// Start worker goroutines
	for i := 1; i <= numWorkers; i++ {
		wgWorkers.Add(1)
		go worker(i, tasks, results, &wgWorkers)
	}

	// Feed tasks in a separate goroutine
	go func() {
		for i := 1; i <= numTasks; i++ {
			tasks <- Task{
				ID:      i,
				Payload: fmt.Sprintf("payload-%d", i),
			}
		}
		close(tasks)
	}()

	// When all workers are done, close results so writer can finish
	go func() {
		wgWorkers.Wait()
		close(results)
	}()

	// Wait for writer to finish writing to file
	wgWriter.Wait()

	log.Println("Data Processing System (Go) finished. Check go_results.txt for output.")
}
