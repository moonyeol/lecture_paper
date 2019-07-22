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
       time_add();   //waitQueue에 들어간ThreadState들의 time변수를 1식 증가 시킴
   }

   public void acquire(KThread thread) {
       Lib.assertTrue(Machine.interrupt().disabled());
       getThreadState(thread).acquire(this);
   }

   public KThread nextThread() {
       Lib.assertTrue(Machine.interrupt().disabled());
       // implement me
       if(getThreadState(KThread.currentThread()).lock!=null)						//currentThread의 락이 있고
    	   if(getThreadState(KThread.currentThread()).lock.isHeldByCurrentThread())	//그락의 lockholder가 currentThread이라면
    		   getThreadState(KThread.currentThread()).lockholder =true;			//lockholder라는걸 저장해둔다.
       																				
       if (waitQueue.isEmpty())            //waitQueue가 비어있을시 null을 반환.
          return null;            
       time_add();         //waitQueue에 들어간ThreadState들의 time변수를 1식 증가 시킴
         
      ThreadState next = pickNextThread();   //다음 돌아야될 threadState를 고름
      
       if (next != null) {                  //next가 null이 아니면
          next.acquire(this);               //현재쓰레드를 waitQueue에서 제거하고
          return next.thread;               //next의 thread를 반환
       }else                           //next가 null이면 null반환
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
	      ThreadState nextthread = waitQueue.getFirst();            //nextThread는 waitQueue의 첫번째 ThreadState가 된다.
	      //waitQueue는 우선순위 순으로 ThreadState을 가지고 있는 큐이다.
          if(nextthread == null)                  //nextKthread가 null이면 null반환
              return null;

      return waitQueue.get(nextthread.getEffectivePriority(this));      //getEffectivePriority 함수를 통해 다음 쓰레드가 락이 있는지 확인, 락이 있다면 락홀드인지 확인, 락홀드가 없으면 락홀드인 thread를 찾는다.
   }
   
   public void print() {
       Lib.assertTrue(Machine.interrupt().disabled());
       // implement me (if you want)
   }
   
   public void time_add() {   //waitQueue에 들어간ThreadState들의 time변수를 1식 증가 시킴   
      for(int i=0; i < waitQueue.size(); i++) {      //waitQueue안의 모든 ThreadState의 시간을 하나식 높인다.
            waitQueue.get(i).time += 1;
      }
   }
   
   
   public void aging() {      //가장 오래된 ThreadState의 우선순위를 1높임
       int max_time =-1,tmp=-1; //가장 높은 시간과 인덱스를 저장하는 변수
       KThread tmp_KThread;   //kthread를 저장할 변수
      for(int i=0; i < waitQueue.size(); i++) {      //waitQueue안의 모든 ThreadState 중 가장 오래있었던 ThreadState를 찾는다.
         if(waitQueue.get(i).time > max_time) {
            max_time = waitQueue.get(i).time;
            tmp = i;
         }
      }
      tmp_KThread = waitQueue.get(tmp).thread;         //가장 오래있었던 ThreadState의 쓰레드를tmp_KThread에 저장
      if(waitQueue.get(tmp).priority<7) 
         setPriority(tmp_KThread,waitQueue.get(tmp).priority+1); //가장 오래있었던 ThreadState의 우선순위를 높임
      waitQueue.get(tmp).acquire(this);//가장 오래있었던 ThreadState를 waitQueue에서 제거
      getThreadState(tmp_KThread).waitForAccess(this);//가장 오래있었던 ThreadState를 waitQueue에 다시 추가 시킨다 (정렬 목적)
      
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
	    	  if(this.lock ==null)						//this가 락이 없으면 this의 waitQueue의 index반환
	    		  return waitQueue.waitQueue.indexOf(this) ;
	    	  else {									//락이 있으면
	    		  if(this.lockholder)					//this가 lockholder라면 this의 waitQueue의 index반환
	    			  return waitQueue.waitQueue.indexOf(this);
	    		  else {								//this가 lockholder가 아니라면
	    			  	if(this.lock.isHeldByCurrentThread())	//currentThread가 this의 lockholer인지 확인
	    			  		return waitQueue.waitQueue.indexOf(getThreadState(KThread.currentThread()));
		    			  for(ThreadState temp:waitQueue.waitQueue) {	//currentThread의 this의 lockholder가 아니라면
		    				  if(this.lock==temp.lock&&temp.lockholder)	//waitQueue안에서 this와 lock이 같고, lockholder인 쓰레드스테이트를 찾아서 waiQueue의 index 반환
		    					  return waitQueue.waitQueue.indexOf(getThreadState(temp.thread));
	    			  	}   
	    			  } 		  		
	    	  }
			return waitQueue.waitQueue.indexOf(this);
     }
   

     public void returnPriority() { //바꾼 우선순위를 원래 우선순위로 바꿔주는 메소드
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
	   
      for(int i=0; i < waitQueue.waitQueue.size(); i++) {//우선순위,시간 큰애들을 우선적으로 앞에 삽입시켜 정렬된 waitQueue를 얻는다
         if(this.priority == waitQueue.waitQueue.get(i).priority) {//우선순위가 같다면 시간이 큰ThreadState를 앞에 삽입
            if(this.time > waitQueue.waitQueue.get(i).time) {
               waitQueue.waitQueue.add(i,this);
               return;
            }
         }
         else if(this.priority > waitQueue.waitQueue.get(i).priority) {//삽입시킬 ThreadState 우선순위가 크면 앞에삽입
            waitQueue.waitQueue.add(i,this);
            return;
         }
      }
      waitQueue.waitQueue.add(this);   //위 경우에 포함되지 않으면 제일 뒤에 삽입
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
      
      waitQueue.waitQueue.remove(this);//쓰레드가 수행에 들어 갔으므로 waitQueue에서 제거

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