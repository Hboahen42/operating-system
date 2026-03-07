#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <time.h>
#include <sys/wait.h>

#define NUM_EXCHANGES 100000

int main(void) {
    int p1[2], p2[2];

    printf("\n[main] Starting ping-pong program\n");
    printf("[main] Creating two pipes...\n");

    pipe(p1);
    pipe(p2);

    printf("[main] Pipes created. Forking process...\n\n");

    pid_t pid = fork();

    if (pid == 0) {
        printf("[child] Child process started (PID: %d)\n", getpid());
        printf("[child] Waiting to receive bytes from parent...\n");

        close(p1[1]);
        close(p2[0]);
        char buf;
        for (int i = 0; i < NUM_EXCHANGES; i++) {
            read(p1[0], &buf, 1);
            write(p2[1], &buf, 1);
            if (i % 10000 == 0) {
                printf("[child] Exchanged %d bytes so far\n", i);
            }
        }

        printf("[child] Done. Exiting.\n");
        close(p1[0]);
        close(p2[1]);
        exit(0);
    } else {
        printf("[parent] Parent process (PID: %d), child PID: %d\n", getpid(), pid);
        printf("[parent] Starting %d exchanges...\n", NUM_EXCHANGES);

        close(p1[0]);
        close(p2[1]);
        char buf = 'X';
        struct timespec start, end;
        clock_gettime(CLOCK_MONOTONIC, &start);

        for (int i = 0; i < NUM_EXCHANGES; i++) {
            write(p1[1], &buf, 1);
            read(p2[0], &buf, 1);
            if (i % 10000 == 0) {
                printf("[parent] Completed %d exchanges so far\n", i);
            }
        }

        clock_gettime(CLOCK_MONOTONIC, &end);
        double elapsed = (end.tv_sec - start.tv_sec) +
                         (end.tv_nsec - start.tv_nsec) / 1e9;

        printf("\n[parent] All exchanges complete.\n");
        printf("[parent] %d exchanges in %.4f seconds\n", NUM_EXCHANGES, elapsed);
        printf("[parent] %.0f exchanges/sec\n", (double)NUM_EXCHANGES / elapsed);

        close(p1[1]);
        close(p2[0]);
        wait(NULL);
        printf("[main] Child process finished. Program complete.\n");
    }
    return 0;
}