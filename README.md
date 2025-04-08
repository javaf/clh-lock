CLH Queue Lock maintains a linked-list for
threads waiting to enter critical section (CS).

Each thread that wants to enter CS raises its
hand, joins at the end of the queue, and waits
for the thread infront of it to lower its hand
(indicating that he has finishes his CS). Then
it enters its CS.

Once the thread is done with CS, it lowers its
hand and moves to take the place of the thread
standing infront of it. To ensure that there is
always a thread infront of another, the queue
is initialized with a dummy node. Atomic
instructions are used when updating the shared
queue.

As each thread waits (spins) on its predecessor's
hand status "locked" field only, cache invalidate
only occurs due to the predecessor. Also, this
does not require the knowledge of number of
threads before hand. This also provides first-
come-first-served fairness. The CLHLock is
due to [Travis Craig], [Erik Hagersten], and
[Anders Landin].

[Travis Craig]: https://www.semanticscholar.org/author/T.-O.-Craig/144386870
[Erik Hagersten]: https://scholar.google.se/citations?user=0D8vpBwAAAAJ&hl=en
[Anders Landin]: https://scholar.google.se/citations?hl=en&user=FO4ByfoAAAAJ

> **Course**: [Concurrent Data Structures], Monsoon 2020\
> **Taught by**: Prof. Govindarajulu Regeti

[Concurrent Data Structures]: https://github.com/iiithf/concurrent-data-structures

```java
lock():
1. When thread wants to access critical
   section, it raises its hand "locked" and
   stands at the end of the queue (FIFO).
2. If thread standing infront has his hand
   raised "locked", it waits for him to be
   done with CS.
```

```java
unlock():
1. When a thread is done with its critical
   section, it drops down its hand "locked".
2. It then takes the place of the thread
   standing infront of it.
```

```java
(To ensure that there is always a thread
 infront of another, the queue is
 initialized with a dummy node.)
```

See [CLHLock.java] for code, [Main.java] for test, and [repl.it] for output.

[CLHLock.java]: https://repl.it/@wolfram77/clh-lock#CLHLock.java
[Main.java]: https://repl.it/@wolfram77/clh-lock#Main.java
[repl.it]: https://clh-lock.wolfram77.repl.run


### references

- [The Art of Multiprocessor Programming :: Maurice Herlihy, Nir Shavit](https://dl.acm.org/doi/book/10.5555/2385452)
- [Building FIFO and priority-queueing spin locks from atomic swap :: Travis Craig](https://www.semanticscholar.org/paper/Building-FIFO-and-Priority-Queuing-Spin-Locks-from-Craig/f808a588b9a9b60877edc39b457ffd55db10dd7d)
- [Queue locks on cache coherent multiprocessors :: Peter S. Magnusson, Anders Landin, Erik Hagersten](https://ieeexplore.ieee.org/document/288305)

![](https://ga-beacon.deno.dev/G-G1E8HNDZYY:v51jklKGTLmC3LAZ4rJbIQ/github.com/javaf/clh-lock)
