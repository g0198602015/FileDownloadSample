package jerome;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ListView;
import android.widget.Toast;

import entities.FileInfo;
import services.DownloadService;


public class MainActivity extends Activity {

	public static MainActivity mMainActivity = null;
	private ListView mListView = null;
	private List<FileInfo> mFileInfoList = null;
	private FileListAdapter mAdapter = null;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mListView = (ListView) findViewById(R.id.lv_downLoad);
        mFileInfoList = new ArrayList<FileInfo>();

        FileInfo fileInfo = null;
        for (int i = 0; i < 1; i++)
		{
        	fileInfo = new FileInfo(i, 
            		"http://192.168.242.1/VIDEO0088.mp4",
            		"imooc" + i + ".mp4", 0, 0);
        	mFileInfoList.add(fileInfo);
		}
        
        mAdapter = new FileListAdapter(this, mFileInfoList);
        mListView.setAdapter(mAdapter);

        IntentFilter filter = new IntentFilter();
        filter.addAction(DownloadService.ACTION_UPDATE);
        filter.addAction(DownloadService.ACTION_FINISHED);
        registerReceiver(mReceiver, filter);
        
        mMainActivity = this;
    }
    
    protected void onDestroy() 
    {
    	super.onDestroy();
    	unregisterReceiver(mReceiver);
    }

    BroadcastReceiver mReceiver = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			if (DownloadService.ACTION_UPDATE.equals(intent.getAction()))
			{
				int finised = intent.getIntExtra("finished", 0);
				int id = intent.getIntExtra("id", 0);
				mAdapter.updateProgress(id, finised);
				Log.i("mReceiver", id + "-finised = " + finised);
			}
			else if (DownloadService.ACTION_FINISHED.equals(intent.getAction()))
			{
				FileInfo fileInfo = (FileInfo) intent.getSerializableExtra("fileInfo");
				mAdapter.updateProgress(fileInfo.getId(), 0);
				Toast.makeText(MainActivity.this,
						mFileInfoList.get(fileInfo.getId()).getFileName() + "tttt",
						Toast.LENGTH_LONG).show();
			}
		}
	};


	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) 
	{
		return super.onKeyUp(keyCode, event);
	}
}
