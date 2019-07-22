package nachos.threads;

import nachos.machine.*;

import java.util.LinkedList;

public class PriorityScheduler extends Scheduler {

    public PriorityScheduler() {
    }
    
    public ThreadQueue newThreadQueue(boolean transferPriority) {
   return new PriorityQueue(transferPriority);
    }

    public int getPriority(KThread thread) {
   Lib.assertTrue(Machine.interrupt().disabled());
             
   return getThreadState(thread).getPriority();
    }

    public int getEffectivePriority(KThread thread) {
   Lib.assertTrue(Machine.interrupt().disabled());
             
   return getThreadState(thread).getEffectivePriority(null);
    }
 
    public void setPriority(KThread thread, int priority) {
   Lib.assertTrue(Machine.interrupt().disabled());
             
   Lib.assertTrue(priority >= priorityMinimum && priority <= priorityMaximum);
   
   getThreadState(thread).setPriority(priority);
    }
    


    public boolean increasePriority() {
   boolean intStatus = Machine.interrupt().disable();
             
   KThread thread = KThread.currentThread();

   int priority = getPriority(thread);
   if (priority == priorityMaximum)
       return false;

   setPriority(thread, priority+1);

   Machine.interrupt().restore(intStatus);
   return true;
    }

    public boolean decreasePriority() {
   boolean intStatus = Machine.interrupt().disable();
             
   KThread thread = KThread.currentThread();

   int priority = getPriority(thread);
   if (priority == priorityMinimum)
       return false;

   setPriority(thread, priority-1);

   Machine.interrupt().restore(intStatus);
   return true;
    }

    /**
     * The default priority for a new thread. Do not change this value.
     */
    public static final int priorityDefault = 1;
    /**
     * The minimum priority that a thread can have. Do not change this value.
     */
    public static final int priorityMinimum = 0;
    /**
     * The maximum priority that a thread can have. Do not change this value.
     */
    public static final int priorityMaximum = 7;    

    /**
     * Return the scheduling state of the specified thread.
     *
     * @param   thread   the thread whose scheduling state to return.
     * @return   the scheduling state of the specified thread.
     */
    protected ThreadState getThreadState(KThread thread) {
   if (thread.schedulingState == null)
       thread.schedulingState = new ThreadState(thread);

   return (ThreadState) thread.schedulingState;
    }

    /**
     * A <tt>ThreadQueue</tt> that sorts threads by priority.
     */
    protected class PriorityQueue extends ThreadQueue {
   PriorityQueue(boolean transferPriority) {
       this.transferPriority = transferPriority;
   }

   public void waitForAccess(KThread thread) {
       Lib.assertTrue(Machine.interrupt().disabled());

       
       getThreadState(thread).waitForAccess(this);
       time_add();   //waitQueue�� ��ThreadState���� time������ 1�� ���� ��Ŵ
   }

   public void acquire(KThread thread) {
       Lib.assertTrue(Machine.interrupt().disabled());
       getThreadState(thread).acquire(this);
   }

   public KThread nextThread() {
       Lib.assertTrue(Machine.interrupt().disabled());
       // implement me
       if(getThreadState(KThread.currentThread()).lock!=null)						//currentThread�� ���� �ְ�
    	   if(getThreadState(KThread.currentThread()).lock.isHeldByCurrentThread())	//�׶��� lockholder�� currentThread�̶��
    		   getThreadState(KThread.currentThread()).lockholder =true;			//lockholder��°� �����صд�.
       																				
       if (waitQueue.isEmpty())            //waitQueue�� ��������� null�� ��ȯ.
          return null;            
       time_add();         //waitQueue�� ��ThreadState���� time������ 1�� ���� ��Ŵ
         
      ThreadState next = pickNextThread();   //���� ���ƾߵ� threadState�� ��
      
       if (next != null) {                  //next�� null�� �ƴϸ�
          next.acquire(this);               //���羲���带 waitQueue���� �����ϰ�
          return next.thread;               //next�� thread�� ��ȯ
       }else                           //next�� null�̸� null��ȯ
          return null;
   }

   /**
    * Return the next thread that <tt>nextThread()</tt> would return,
    * without modifying the state of this queue.
    *
    * @return   the next thread that <tt>nextThread()</tt> would
    *      return.
    */
   protected ThreadState pickNextThread() {
       // implement me
	      ThreadState nextthread = waitQueue.getFirst();            //nextThread�� waitQueue�� ù��° ThreadState�� �ȴ�.
	      //waitQueue�� �켱���� ������ ThreadState�� ������ �ִ� ť�̴�.
          if(nextthread == null)                  //nextKthread�� null�̸� null��ȯ
              return null;

      return waitQueue.get(nextthread.getEffectivePriority(this));      //getEffectivePriority �Լ��� ���� ���� �����尡 ���� �ִ��� Ȯ��, ���� �ִٸ� ��Ȧ������ Ȯ��, ��Ȧ�尡 ������ ��Ȧ���� thread�� ã�´�.
   }
   
   public void print() {
       Lib.assertTrue(Machine.interrupt().disabled());
       // implement me (if you want)
   }
   
   public void time_add() {   //waitQueue�� ��ThreadState���� time������ 1�� ���� ��Ŵ   
      for(int i=0; i < waitQueue.size(); i++) {      //waitQueue���� ��� ThreadState�� �ð��� �ϳ��� ���δ�.
            waitQueue.get(i).time += 1;
      }
   }
   
   
   public void aging() {      //���� ������ ThreadState�� �켱������ 1����
       int max_time =-1,tmp=-1; //���� ���� �ð��� �ε����� �����ϴ� ����
       KThread tmp_KThread;   //kthread�� ������ ����
      for(int i=0; i < waitQueue.size(); i++) {      //waitQueue���� ��� ThreadState �� ���� �����־��� ThreadState�� ã�´�.
         if(waitQueue.get(i).time > max_time) {
            max_time = waitQueue.get(i).time;
            tmp = i;
         }
      }
      tmp_KThread = waitQueue.get(tmp).thread;         //���� �����־��� ThreadState�� �����带tmp_KThread�� ����
      if(waitQueue.get(tmp).priority<7) 
         setPriority(tmp_KThread,waitQueue.get(tmp).priority+1); //���� �����־��� ThreadState�� �켱������ ����
      waitQueue.get(tmp).acquire(this);//���� �����־��� ThreadState�� waitQueue���� ����
      getThreadState(tmp_KThread).waitForAccess(this);//���� �����־��� ThreadState�� waitQueue�� �ٽ� �߰� ��Ų�� (���� ����)
      
   }
   

   /**
    * <tt>true</tt> if this queue should transfer priority from waiting
    * threads to the owning thread.
    */
   public boolean transferPriority;
   
   LinkedList<ThreadState>  waitQueue = new LinkedList<ThreadState>();
   

   
    }

    /**
     * The scheduling state of a thread. This should include the thread's
     * priority, its effective priority, any objects it owns, and the queue
     * it's waiting for, if any.
     *
     * @see   nachos.threads.KThread#schedulingState
     */
    protected class ThreadState {
   /**
    * Allocate a new <tt>ThreadState</tt> object and associate it with the
    * specified thread.
    *
    * @param   thread   the thread this state belongs to.
    */
   public ThreadState(KThread thread) {
       this.thread = thread;
       
       setPriority(priorityDefault);
   }
   
   public ThreadState(KThread thread, Lock lock) {
       this.lock = lock;
       
       this.thread = thread;
       
       setPriority(priorityDefault);
   }

   /**
    * Return the priority of the associated thread.
    *
    * @return   the priority of the associated thread.
    */
   public int getPriority() {
       return priority;
   }

   /**
    * Return the effective priority of the associated thread.
    *
    * @return   the effective priority of the associated thread.
    */
   public int getEffectivePriority(PriorityQueue waitQueue) {
	    	  if(this.lock ==null)						//this�� ���� ������ this�� waitQueue�� index��ȯ
	    		  return waitQueue.waitQueue.indexOf(this) ;
	    	  else {									//���� ������
	    		  if(this.lockholder)					//this�� lockholder��� this�� waitQueue�� index��ȯ
	    			  return waitQueue.waitQueue.indexOf(this);
	    		  else {								//this�� lockholder�� �ƴ϶��
	    			  	if(this.lock.isHeldByCurrentThread())	//currentThread�� this�� lockholer���� Ȯ��
	    			  		return waitQueue.waitQueue.indexOf(getThreadState(KThread.currentThread()));
		    			  for(ThreadState temp:waitQueue.waitQueue) {	//currentThread�� this�� lockholder�� �ƴ϶��
		    				  if(this.lock==temp.lock&&temp.lockholder)	//waitQueue�ȿ��� this�� lock�� ����, lockholder�� �����彺����Ʈ�� ã�Ƽ� waiQueue�� index ��ȯ
		    					  return waitQueue.waitQueue.indexOf(getThreadState(temp.thread));
	    			  	}   
	    			  } 		  		
	    	  }
			return waitQueue.waitQueue.indexOf(this);
     }
   

     public void returnPriority() { //�ٲ� �켱������ ���� �켱������ �ٲ��ִ� �޼ҵ�
        for(int i=priority-previousPriority;i>0;i--)
           decreasePriority();
     }

   /**
    * Set the priority of the associated thread to the specified value.
    *
    * @param   priority   the new priority.
    */
   public void setPriority(int priority) {
       if (this.priority == priority)
      return;
       
       this.priority = priority;
       
       // implement me
   }

   public void setLock(Lock lock) {
	   this.lock = lock;
   }

   public void waitForAccess(PriorityQueue waitQueue) {	   
	   
      for(int i=0; i < waitQueue.waitQueue.size(); i++) {//�켱����,�ð� ū�ֵ��� �켱������ �տ� ���Խ��� ���ĵ� waitQueue�� ��´�
         if(this.priority == waitQueue.waitQueue.get(i).priority) {//�켱������ ���ٸ� �ð��� ūThreadState�� �տ� ����
            if(this.time > waitQueue.waitQueue.get(i).time) {
               waitQueue.waitQueue.add(i,this);
               return;
            }
         }
         else if(this.priority > waitQueue.waitQueue.get(i).priority) {//���Խ�ų ThreadState �켱������ ũ�� �տ�����
            waitQueue.waitQueue.add(i,this);
            return;
         }
      }
      waitQueue.waitQueue.add(this);   //�� ��쿡 ���Ե��� ������ ���� �ڿ� ����
   }

   /**
    * Called when the associated thread has acquired access to whatever is
    * guarded by <tt>waitQueue</tt>. This can occur either as a result of
    * <tt>acquire(thread)</tt> being invoked on <tt>waitQueue</tt> (where
    * <tt>thread</tt> is the associated thread), or as a result of
    * <tt>nextThread()</tt> being invoked on <tt>waitQueue</tt>.
    *
    * @see   nachos.threads.ThreadQueue#acquire
    * @see   nachos.threads.ThreadQueue#nextThread
    */
   public void acquire(PriorityQueue waitQueue) {
       // implement me
      
      waitQueue.waitQueue.remove(this);//�����尡 ���࿡ ��� �����Ƿ� waitQueue���� ����

   }   

   /** The thread with which this object is associated. */      
   protected KThread thread;
   /** The priority of the associated thread. */
   protected int maxpriority=2;
   protected int priority;   
   protected int previousPriority;
   public int time =0;
   public Lock lock;
   public boolean lockholder =false;
   }

}