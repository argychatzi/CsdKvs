package main.java;

public class KvsExecutor extends Thread {

	public interface KvsExecutable {
		public int execute();
	}

	private static final long SLEEP_DURATION = 1000;

	private static final String TAG = "KvsExecutor";
	private Object mutex = new Object();

	private KvsOperationMessageQueue mBox;
	private int mId;

	public KvsExecutor(KvsOperationMessageQueue box, int id) {
		mBox = box;
		mId = id;
		Logger.d(TAG, "thread with id "+ id +" instantiated");
	}

	@Override
	public void run() {
		super.run();
		Logger.d(TAG, "thread # " + mId +  "run");
		
		while (true) {
			
			if (!mBox.isEmpty()){
				synchronized (mutex) {
					KvsExecutableOperation operation = mBox.dequeueRequestOperation();
					operation.execute();
				}
			} else {
				try {
					Thread.sleep(SLEEP_DURATION);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
