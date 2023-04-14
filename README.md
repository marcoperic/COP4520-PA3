# COP4520-PA3

## Execution
```Problem 1 execution: javac Problem1.java && java Problem1```

```Problem 2 execution: javac Problem2.java && java Problem2```

## Description

### Problem 1
The initial linked list used by the servants is not thread-safe. If they had known previously that there existed more presents than thank you cards, then that must mean that some presents were lost during the add/remove operations for the list. As such, the problem is solvable with a concurrent linked list.

A LockFreeList was utilized, and a class called Present was defined and made Comparable for the sake of ordering the list. Furthermore, the defined Servant class serves as the class that performs all thread operations and serves as a simulatory "Servant" for the minotaur. Servants work to create Presants and add/remove them to the chain until the number of cards is equal to presents. 

### Problem 2
A PriorityBlockingQueue was utilized for this problem. PriorityBlockingQueue is a heap-like datastructure in Java, which allows for the easy storage of min/max values and easy pruning as well. It is also easily reversible, which I took advantage of to easily accomodate the tracking of both min and max temperatures. The ParallelList class also finds the interval with the greatest difference in temperatures, as needed.

### Correctness

Problem1 is solved properly and it can be seen by both the runtime and code of the file. I also experimented with different values for N and NUM_PRESENTS to see how the output would change, and it is certainly leveraging the concurrency.

Problem2 is solved properly as shown by the output. It does everything tasked to do!
