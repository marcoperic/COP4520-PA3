import java.util.concurrent.atomic.*;
import java.lang.Exception;

public class Problem1
{
    // constants. threads and number of presents.
    final static int N = 4;
    final static int PRESENTS = 500000;

    public static void main(String[] args) throws Exception
    {
        ParallelList list = new ParallelList();
        AtomicInteger cards = new AtomicInteger(0);

        long start = System.currentTimeMillis();

        for (int i = 0; i < N; i++)
        {
            MinotaurServant s = new MinotaurServant(list, cards);
            Thread temp = new Thread(s);

            temp.start();
            temp.join();
        }
        
        System.out.println(cards.get() + " cards were written. [" + (System.currentTimeMillis() - start) + "ms]");
    }

    static class MinotaurServant implements Runnable
    {
        public ParallelList list;
        public AtomicInteger cards;
    
        MinotaurServant(ParallelList list, AtomicInteger cards)
        {
            this.list = list;
            this.cards = cards;
        }
    
        public boolean giftPresent()
        {
            return list.contains(new Present().getKey());
        }

        @Override
        public void run()
        {
            while (true)
            {   
                // System.out.println(cards.get());
                if (cards.get() >= PRESENTS)
                    return;
                    
                // per the minotaur's reqeust, check whether a gift was present or not.
                giftPresent();

                // operations
                Present p = new Present();
                list.add(p.getKey());
                list.remove(p.getKey());
                cards.getAndIncrement();
            }
        }
    }

    static class Present implements Comparable<Present>
    {
        private int key;

        Present()
        {
            key = this.hashCode();
        }
    
        public int getKey()
        {
            return key;
        }

        @Override
        public String toString(){return "";}

        @Override
        public int compareTo(Present p){return -1;} // not important. marginal impact on runtime.
    }
}