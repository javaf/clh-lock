import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import java.util.concurrent.atomic.*;

// CLH Queue Lock maintains a linked-list for
// threads waiting to enter critical section (CS).
// 
// Each thread that wants to enter CS raises its
// hand, joins at the end of the queue, and waits
// for the thread infront of it to lower its hand
// (indicating that he has finishes his CS). Then
// it enters its CS.
// 
// Once the thread is done with CS, it lowers its
// hand and moves to take the place of the thread
// standing infront of it. To ensure that there is
// always a thread infront of another, the queue
// is initialized with a dummy node. Atomic
// instructions are used when updating the shared
// queue.
// 
// As each thread waits (spins) on its predecessor's
// hand status "locked" field only, cache invalidate
// only occurs due to the predecessor. Also, this
// does not require the knowledge of number of
// threads before hand. This also provides first-
// come-first-served fairness.

class CLHLock implements Lock {
  AtomicReference<QNode> queue;
  ThreadLocal<QNode> node;
  ThreadLocal<QNode> pred;
  // queue: points to the tail
  // node:  is unique for each thread
  // pred:  predecessor thread's node (infront)

  public CLHLock() {
    queue = new AtomicReference<>(new QNode());
    node = new ThreadLocal<>() {
      protected QNode initialValue() {
        return new QNode();
      }
    };
    pred = new ThreadLocal<>() {
      protected QNode initialValue() {
        return null;
      }
    };
  }

  // 1. When thread wants to access critical
  //    section, it raises its hand "locked" and
  //    stands at the end of the queue (FIFO).
  // 2. If thread standing infront has his hand
  //    raised "locked", it waits for him to be
  //    done with CS.
  @Override
  public void lock() {
    QNode n = node.get();         // 1
    n.locked = true;              // 1
    QNode m = queue.getAndSet(n); // 1
    pred.set(m);                  // 1
    while(m.locked) Thread.yield(); // 2
  }

  // 1. When a thread is done with its critical
  //    section, it drops down its hand "locked".
  // 2. It then takes the place of the thread
  //    standing infront of it.
  @Override
  public void unlock() {
    QNode n = node.get(); // 1
    n.locked = false;     // 1
    node.set(pred.get()); // 2
  }

  @Override
  public void lockInterruptibly() throws InterruptedException {
    lock();
  }

  @Override
  public boolean tryLock() {
    lock();
    return true;
  }

  @Override
  public boolean tryLock(long arg0, TimeUnit arg1) throws InterruptedException {
    lock();
    return true;
  }

  @Override
  public Condition newCondition() {
    return null;
  }
}
