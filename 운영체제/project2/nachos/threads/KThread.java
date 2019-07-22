package nachos.threads;

import nachos.machine.*;
import nachos.threads.PriorityScheduler.ThreadState;

public class KThread {
   /**
    * Get the current thread.
    *
    * @return the current thread.
    */

   public static KThread currentThread() {
      Lib.assertTrue(currentThread != null);
      return currentThread;
   }
   

   /**
    * Allocate a new <tt>KThread</tt>. If this is the first <tt>KThread</tt>,
    * create an idle thread as well.
    */
   public KThread() {// ������
      if (currentThread != null) {
         tcb = new TCB();// ó�� ������ �ƴҶ� ���ο� tcb�� �������
      } else {// ó�� �����϶�
         readyQueue = ThreadedKernel.scheduler.newThreadQueue(false);
         readyQueue.acquire(this);

         currentThread = this;
         tcb = TCB.currentTCB();// ó������ null���� ��
         name = "main";
         restoreState();

         createIdleThread();
      }
   }

   /**
    * Allocate a new KThread.
    *
    * @param target the object whose <tt>run</tt> method is called.
    */
   public KThread(Runnable target) {// �Ű������� ���� �����ڸ� �����
      this();// �����ڸ� ȣ���ؼ�
      this.target = target;
   }

   /**
    * Set the target of this thread.
    *
    * @param target the object whose <tt>run</tt> method is called.
    * @return this thread.
    */

   public KThread setTarget(Runnable target) {
      Lib.assertTrue(status == statusNew);

      this.target = target;
      return this;
   }

   /**
    * Set the name of this thread. This name is used for debugging purposes only.
    *
    * @param name the name to give to this thread.
    * @return this thread.
    */
   public KThread setName(String name) {
      this.name = name;

      return this;
   }

   /**
    * Get the name of this thread. This name is used for debugging purposes only.
    *
    * @return the name given to this thread.
    */
   public String getName() {
      return name;
   }

   /**
    * Get the full name of this thread. This includes its name along with its
    * numerical ID. This name is used for debugging purposes only.
    *
    * @return the full name given to this thread.
    */
   public String toString() {
      return (name + " (#" + id + ")");
   }

   /**
    * Deterministically and consistently compare this thread to another thread.
    */
//   public int getStatue() {
//      return status;
//   }
   public int compareTo(Object o) {
      KThread thread = (KThread) o;

      if (id < thread.id)
         return -1;
      else if (id > thread.id)
         return 1;
      else
         return 0;
   }

   /**
    * Causes this thread to begin execution. The result is that two threads are
    * running concurrently: the current thread (which returns from the call to the
    * <tt>fork</tt> method) and the other thread (which executes its target's
    * <tt>run</tt> method).
    */
   public void fork() {
      Lib.assertTrue(status == statusNew);// ���ۻ���
      Lib.assertTrue(target != null);// ó�� �������� �ƴ����� Ȯ��??

      Lib.debug(dbgThread, "Forking thread: " + toString() + " Runnable: " + target);

      boolean intStatus = Machine.interrupt().disable();// false���� ��

      tcb.start(new Runnable() {
         public void run() {
            runThread();
         }
      });

      ready();

      Machine.interrupt().restore(intStatus);
   }

   private void runThread() {
      begin();
      target.run();
      finish();// �ٽ� Ÿ��.run�ؼ� �ݺ�����
   }

   private void begin() {
      Lib.debug(dbgThread, "Beginning thread: " + toString());

      Lib.assertTrue(this == currentThread);

      restoreState();

      Machine.interrupt().enable();
   }

   /**
    * Finish the current thread and schedule it to be destroyed when it is safe to
    * do so. This method is automatically called when a thread's <tt>run</tt>
    * method returns, but it may also be called directly.
    *
    * The current thread cannot be immediately destroyed because its stack and
    * other execution state are still in use. Instead, this thread will be
    * destroyed automatically by the next thread to run, when it is safe to delete
    * this thread.
    */

   public static void finish() {
      Lib.debug(dbgThread, "Finishing thread: " + currentThread.toString());

      Machine.interrupt().disable();

      Machine.autoGrader().finishingCurrentThread();
//restorestate�Լ��� �ҷ����� tobedestroyed�� tcb�� �����ش�.
      Lib.assertTrue(toBeDestroyed == null);
      toBeDestroyed = currentThread;

      currentThread.status = statusFinished;

      sleep();
   }

   /**
    * Relinquish the CPU if any other thread is ready to run. If so, put the
    * current thread on the ready queue, so that it will eventually be rescheuled.
    *
    * <p>
    * Returns immediately if no other thread is ready to run. Otherwise returns
    * when the current thread is chosen to run again by
    * <tt>readyQueue.nextThread()</tt>.
    *
    * <p>
    * Interrupts are disabled, so that the current thread can atomically add itself
    * to the ready queue and switch to the next thread. On return, restores
    * interrupts to the previous state, in case <tt>yield()</tt> was called with
    * interrupts disabled.
    */
   public static void yield() {
      Lib.debug(dbgThread, "Yielding thread: " + currentThread.toString());

      Lib.assertTrue(currentThread.status == statusRunning);

      boolean intStatus = Machine.interrupt().disable();
      // ���׻����̸� ���ͷ�Ʈ �߻� ���ϰ� ��
      currentThread.ready();
//����ť�� ��
      runNextThread();

      Machine.interrupt().restore(intStatus);
   }

   /**
    * Relinquish the CPU, because the current thread has either finished or it is
    * blocked. This thread must be the current thread.
    *
    * <p>
    * If the current thread is blocked (on a synchronization primitive, i.e. a
    * <tt>Semaphore</tt>, <tt>Lock</tt>, or <tt>Condition</tt>), eventually some
    * thread will wake this thread up, putting it back on the ready queue so that
    * it can be rescheduled. Otherwise, <tt>finish()</tt> should have scheduled
    * this thread to be destroyed by the next thread to run.
    */

   public static void sleep() {
      Lib.debug(dbgThread, "Sleeping thread: " + currentThread.toString());

      Lib.assertTrue(Machine.interrupt().disabled());

      if (currentThread.status != statusFinished) { 
         currentThread.status = statusBlocked;
      }
      runNextThread();
   }

   /**
    * Moves this thread to the ready state and adds this to the scheduler's ready
    * queue.
    */
   public void ready() {
      Lib.debug(dbgThread, "Ready thread: " + toString());

      Lib.assertTrue(Machine.interrupt().disabled());
      Lib.assertTrue(status != statusReady);

      status = statusReady;
      if (this != idleThread)// t
         readyQueue.waitForAccess(this);
      //�� �״ϱ� �̰� ����ť�� �̰� access�����ش�.
      Machine.autoGrader().readyThread(this);
      //�״ϱ� �� sleep���¿� �ִ��� ������ ����ť�� �ٽ� �ִ°ǰ�.
   }

   /**
    * Waits for this thread to finish. If this thread is already finished, return
    * immediately. This method must only be called once; the second call is not
    * guaranteed to return. This thread must not be the current thread.
    */
   public void join() {
      Lib.debug(dbgThread, "Joining to thread: " + toString());

      Lib.assertTrue(this != currentThread);

   }

   /**
    * Create the idle thread. Whenever there are no threads ready to be run, and
    * <tt>runNextThread()</tt> is called, it will run the idle thread. The idle
    * thread must never block, and it will only be allowed to run when all other
    * threads are blocked.
    *
    * <p>
    * Note that <tt>ready()</tt> never adds the idle thread to the ready set.
    */
   private static void createIdleThread() {
      Lib.assertTrue(idleThread == null);

      idleThread = new KThread(new Runnable() {
         public void run() {
            while (true)
               yield();
         }
      });
      idleThread.setName("idle");

      Machine.autoGrader().setIdleThread(idleThread);

      idleThread.fork();
   }

   /**
    * Determine the next thread to run, then dispatch the CPU to the thread using
    * <tt>run()</tt>.
    */
   private static void runNextThread() {// �����ִ� ���� & ���� ���� ���� �ƴϳ� ����
      KThread nextThread = readyQueue.nextThread();// ����ť���� �ؽ�Ʈ �����带 ������ �ؽ�Ʈ�������� �̸��� ���̽����带 ����
      // ����ť���� ���������带 ����������
      if (nextThread == null)// �ؽ�Ʈ�����尡 ����ֳ�
         nextThread = idleThread;// �����尡 �������� ������
      // �׷��� ���̵� �����带 �Ѿ���
//// ������丮������ ���̵� ����
//      System.out.println("runNextThread ������ �̸� "+nextThread.name);
      nextThread.run();// ���� ����ť���� �����°� �ִ�. ���� �ִ�.
   }

   /**
    * Dispatch the CPU to this thread. Save the state of the current thread, switch
    * to the new thread by calling <tt>TCB.contextSwitch()</tt>, and load the state
    * of the new thread. The new thread becomes the current thread.
    *
    * <p>
    * If the new thread and the old thread are the same, this method must still
    * call <tt>saveState()</tt>, <tt>contextSwitch()</tt>, and
    * <tt>restoreState()</tt>.
    *
    * <p>
    * The state of the previously running thread must already have been changed
    * from running to blocked or ready (depending on whether the thread is sleeping
    * or yielding).
    *
    * @param finishing <tt>true</tt> if the current thread is finished, and should
    *                  be destroyed by the new thread.
    */
   private void run() {
      Lib.assertTrue(Machine.interrupt().disabled());

      Machine.yield();// �ؽ�Ʈ ������� �ٲ�??

      currentThread.saveState();

      Lib.debug(dbgThread, "Switching from: " + currentThread.toString() + " to: " + toString());

      currentThread = this;
//�ٲ� ������(�ؽ�Ʈ ������)
      tcb.contextSwitch();
//������� tcb�� ���ٴϴ� �ֵ��ϻ� tcb���� runnable�� ��
//Ŀ��Ʈ ������ �ٲ�� ���� �˾Ƽ� ���ȿ����� ���� ������
      // �ٸ� �Լ��� �� �� ����

      currentThread.restoreState();
   }

   /**
    * Prepare this thread to be run. Set <tt>status</tt> to <tt>statusRunning</tt>
    * and check <tt>toBeDestroyed</tt>.
    */
   protected void restoreState() {
      Lib.debug(dbgThread, "Running thread: " + currentThread.toString());

      Lib.assertTrue(Machine.interrupt().disabled());
      Lib.assertTrue(this == currentThread);
      Lib.assertTrue(tcb == TCB.currentTCB());// ������ tcb�� �´��� Ȯ��??

      Machine.autoGrader().runningThread(this);

      status = statusRunning;// ���׻��·� �ٲ���

      if (toBeDestroyed != null) {
         toBeDestroyed.tcb.destroy();
         toBeDestroyed.tcb = null;
         toBeDestroyed = null;
      } // �������� ����ΰ�? �� ���� ������ �������忡�� target.run�� ���� ����
   }

   /**
    * Prepare this thread to give up the processor. Kernel threads do not need to
    * do anything here.
    */
   protected void saveState() {
      Lib.assertTrue(Machine.interrupt().disabled());//
      Lib.assertTrue(this == currentThread);//
   }

//�߰��� �κ�
   private static class PriorityTest implements Runnable {
      String name;

      PriorityTest(String name) {
         this.name = name;
      }

      public void run() {
         for (int i = 0; i < 3; i++) {
            System.out.println(name + "  looped  " + i + " times");
            KThread.yield();
         }
      }
   }

 //�߰��� �κ�
   private static class LockTest implements Runnable{
      String name;
      Lock lock;
      
      LockTest(String name,Lock lock){
         this.name = name;
         this.lock = lock;
      }
      LockTest(String name){
         this.name =name;
      }
      public void run() {
         lock.acquire();
         
         for(int i =0;i<3;i++) {
//           System.out.println("�̰� ���⼭ ���� �Ǵ°ǰ� : LockTest");
            System.out.println(name + "  looped  " + i + " times");
            KThread.yield();
         }
         lock.release();
      }
      
   }
   //�߰��� �κ�
   public static class donationTest implements Runnable {
      String name;
      Lock lock;
      
      donationTest(String name,Lock lock){
         this.name = name;
         this.lock = lock;
      }
 
      
      public void run() {
         lock.acquire();        

         
         Runnable test2 = new LockTest("test2 priority 5",lock); 
         KThread t2 = new KThread(test2).setName("test2");// lock�� ��û�� t1 ������
         
         Runnable test3 = new PriorityTest("test3 priority 6");
         KThread t3 = new KThread(test3).setName("test3"); //lock�� ��û ���� t2 ������
         
         Runnable test4 = new PriorityTest("test4 priority 4");
         KThread t4 = new KThread(test4).setName("test4"); //lock�� ��û ���� t2 ������
         
 
         Machine.interrupt().disable();
         
         ThreadedKernel.scheduler.setPriority(t2,5); //lock�� ��û�� t1 �������� �켱���� 4

         ThreadedKernel.scheduler.setPriority(t3,6); //lock�� ��û ���� t2 �������� �켱������ 4
         ThreadedKernel.scheduler.setPriority(t4,4);
        
         Machine.interrupt().enable();
         
         t2.fork();

         ((ThreadState)t2.schedulingState).setLock(lock);	//���� �ִ°� �����ٷ����� �˸�
         //t1 �� fork() ���� ����Queue�� ����
         t3.fork();
         //t2 �� fork() ��Ŵ
         t4.fork();
 
         for(int i =0;i<3;i++) {
           System.out.println(name + "  looped  " + i + " times");
           KThread.yield();
         }
         lock.release();
      }
      
   }
   //�߰��� �κ�
   public static void selfTest2() {
      
     Machine.interrupt().disable();
      System.out.println("task2_selftest\n");
      Lock lock = new Lock();
      donationTest test1 = new donationTest("Dtest1 priority 2",lock);
      KThread t1 = new KThread(test1).setName("Dtest1");
      ThreadedKernel.scheduler.setPriority(t1,2);

      
      Machine.interrupt().enable();
      t1.fork();
      ((ThreadState)t1.schedulingState).setLock(lock);
      
      for (int j = 0; j < 20; j++) {
         KThread.yield();

      }

      System.out.println("\n\ntask2_selftest finish\n");
      
   }
//�߰��� �κ�
   public static void selfTest1() {
      Machine.interrupt().disable();
      System.out.println("task1_selftest\n");

      PriorityTest test1 = new PriorityTest("test1 priority default");
      KThread t1 = new KThread(test1);
      PriorityTest test2 = new PriorityTest("test2 priority 2     ");
      KThread t2 = new KThread(test2);
      PriorityTest test3 = new PriorityTest("test3 priority 4     ");
      KThread t3 = new KThread(test3);
      PriorityTest test4 = new PriorityTest("test4 priority 4     ");
      KThread t4 = new KThread(test4);
      PriorityTest test5 = new PriorityTest("test5 priority 3     ");
      KThread t5 = new KThread(test5);

      ThreadedKernel.scheduler.setPriority(t2, 2);
      ThreadedKernel.scheduler.setPriority(t3, 4);
      ThreadedKernel.scheduler.setPriority(t4, 4);
      ThreadedKernel.scheduler.setPriority(t5, 3);

      Machine.interrupt().enable();

      t1.fork();
      t2.fork();
      t3.fork();
      t4.fork();
      t5.fork();

      
      for (int j = 0; j < 5; j++) {
         KThread.yield();

      }
      System.out.println("\n\n");
      System.out.println("task1_selftest finish\n");

   }

   private static final char dbgThread = 't';

   /**
    * Additional state used by schedulers.
    *
    * @see nachos.threads.PriorityScheduler.ThreadState
    */
   public Object schedulingState = null;

   private static final int statusNew = 0;
   private static final int statusReady = 1;
   private static final int statusRunning = 2;
   private static final int statusBlocked = 3;
   private static final int statusFinished = 4;

   /**
    * The status of this thread. A thread can either be new (not yet forked), ready
    * (on the ready queue but not running), running, or blocked (not on the ready
    * queue and not running).
    */
   private int status = statusNew;
   private String name = "(unnamed thread)";
   private Runnable target;
   private TCB tcb;// �ؽ�Ʈ �����带 �ϴ� �ֵ� tcb�� ����

   /**
    * Unique identifer for this thread. Used to deterministically compare threads.
    */
   private int id = numCreated++;
   /** Number of times the KThread constructor was called. */
   private static int numCreated = 0;

   private static ThreadQueue readyQueue = null;
   private static KThread currentThread = null;
   private static KThread toBeDestroyed = null;
   private static KThread idleThread = null;



}