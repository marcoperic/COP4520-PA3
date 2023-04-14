import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// Some code repurposed from the textbook. ISBN: 978-0123973375
public class ParallelList {

    class Node {
        Lock l = new ReentrantLock();
        Node next;
        int key;
        int data;
        public int min;
        public int max;

        public Node(int key) {
            this.key = key;
        }

        public Node(int key, int data) {
            this.key = key;
            this.data = data;
        }

        public void unlock() {
            l.unlock();
        }

        public void lock() {
            l.lock();
        }

    }

    Node head;
    AtomicInteger size;

    public ParallelList() {
        head = new Node(Integer.MIN_VALUE);
        head.next = new Node(Integer.MAX_VALUE);
        size = new AtomicInteger(2);
    }

    public boolean add(int item) {
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
                        return false;
                    } else {
                        Node node = new Node(item);
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

    public boolean add(int item, int data) {
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
                        curr.max = item;
                        curr.min = item;
                        return false;
                    } else {
                        Node node = new Node(item, data);
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
}