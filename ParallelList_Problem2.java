import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// slightly modified version of ParallelList. previous statement in ParallelList still holds
public class ParallelList_Problem2 {

    class Node {
        public Lock lock = new ReentrantLock();
        int key;
        int min;
        int max;
        Node next;

        public Node(int key, int temperature) {
            this.key = key;
            this.min = temperature;
            this.max = temperature;
        }

        public void lock() {
            lock.lock();
        }

        public void unlock() {
            lock.unlock();
        }
    }
    public Node head;
    public AtomicInteger size;

    public ParallelList_Problem2() {
        head = new Node(Integer.MIN_VALUE, Integer.MIN_VALUE);
        head.next = new Node(Integer.MAX_VALUE, Integer.MAX_VALUE);
        size = new AtomicInteger(2);
    }

    public boolean add(int m, int temperature) {
        int key = m;
        while (true) {

            Node pred = head;
            Node curr = pred.next;

            while (curr.key < key) {
                pred = curr;
                curr = curr.next;
            }

            pred.lock();
            curr.lock();

            try {
                if (validate(pred, curr)) {
                    if (curr.key == key) {
                        curr.max = temperature;
                        curr.min = temperature;
                        return false;
                    } else {
                        Node node = new Node(m, temperature);
                        node.next = curr;
                        pred.next = node;
                        size.getAndIncrement();
                        return true;
                    }
                }
            } finally {
                pred.unlock();
                curr.unlock();
            }
        }
    }

    public boolean remove(int item) {
        int key = item;
        while (true) {
            Node pred = head;
            Node curr = pred.next;
            while (curr.key < key) {
                pred = curr;
                curr = curr.next;
            }

            pred.lock();
            curr.lock();
            try {
                if (validate(pred, curr)) {
                    if (curr.key == key) {
                        pred.next = curr.next;
                        size.getAndDecrement();
                        return true;
                    } else {
                        return false;
                    }
                }
            } finally {
                pred.unlock();
                curr.unlock();
            }
        }
    }

    public boolean contains(int item) {
        int key = item;
        while (true) {
            Node pred = this.head;
            Node curr = pred.next;
            while (curr.key < key) {
                pred = curr;
                curr = curr.next;
            }

            // lock it up
            pred.lock();
            curr.lock();
            try {
                if (validate(pred, curr)) {
                    return (curr.key == key);
                }
            } finally {
                pred.unlock();
                curr.unlock();
            }
        }
    }

    public boolean validate(Node pred, Node curr) {
        Node node = head;
        while (node.key <= pred.key) {
            if (node == pred) {
                return pred.next == curr;
            }
            node = node.next;
        }
        return false;
    }

    // work in the ten minute intervals
    public void solve() {

        Node curr = head;
        int idx = -1;
        int delta = -1;

        for (int i = 0; i < 30; i++) {
            int min_temp = Integer.MAX_VALUE;
            int max_temp = Integer.MIN_VALUE;

            curr = curr.next;
            Node temp = curr;

            for (int j = 0; j < 10; j++){
                min_temp = Math.min(min_temp, temp.min);
                max_temp = Math.max((max_temp), temp.max);
                temp = temp.next;
            }

            if (max_temp - min_temp > delta) {
                delta = max_temp - min_temp;
                idx = i;
            }
        }

        System.out.println("interval " + idx + "-" + (idx + 10) + " has the largest difference of: " + delta);
    }
}
