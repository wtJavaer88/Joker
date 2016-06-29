package com.example.joker.util.app;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

public class RefreshMedia
{
    public static void scanSdCard(Context context)
    {
        IntentFilter intentfilter = new IntentFilter(
                Intent.ACTION_MEDIA_SCANNER_STARTED);
        intentfilter.addAction(Intent.ACTION_MEDIA_SCANNER_FINISHED);
        intentfilter.addDataScheme("file");
        context.registerReceiver(scanSdReceiver, intentfilter);
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri
                .parse("file://"
                        + Environment.getExternalStorageDirectory()
                                .getAbsolutePath())));
        // context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
        // Uri.parse("file://" + "/data")));

    }

    private static BroadcastReceiver scanSdReceiver = new BroadcastReceiver()
    {
        private AlertDialog.Builder builder;
        private AlertDialog ad;

        @Override
        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();
            int count = 0, count2, count1 = 0;
            if (Intent.ACTION_MEDIA_SCANNER_STARTED.equals(action))
            {
                Cursor c1 = context.getContentResolver().query(
                        MediaStore.Audio.Media.INTERNAL_CONTENT_URI,
                        new String[]
                        { MediaStore.Audio.Media.TITLE,
                                MediaStore.Audio.Media.DURATION,
                                MediaStore.Audio.Media.ARTIST,
                                MediaStore.Audio.Media._ID,
                                MediaStore.Audio.Media.DISPLAY_NAME }, null,
                        null, null);
                count1 = c1.getCount();
                this.builder = new AlertDialog.Builder(context);
                this.builder.setMessage("正在扫描存储卡...");
                this.ad = this.builder.create();
                this.ad.show();
                // adapter.notifyDataSetChanged();
            }
            else if (Intent.ACTION_MEDIA_SCANNER_FINISHED.equals(action))
            {
                Cursor c2 = context.getContentResolver().query(
                        MediaStore.Audio.Media.INTERNAL_CONTENT_URI,
                        new String[]
                        { MediaStore.Audio.Media.TITLE,
                                MediaStore.Audio.Media.DURATION,
                                MediaStore.Audio.Media.ARTIST,
                                MediaStore.Audio.Media._ID,
                                MediaStore.Audio.Media.DISPLAY_NAME }, null,
                        null, null);
                count2 = c2.getCount();
                count = count2 - count1;
                this.ad.cancel();
                if (count >= 0)
                {
                    Toast.makeText(context, "共增加" + count + "首歌曲",
                            Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(context, "共减少" + count + "首歌曲",
                            Toast.LENGTH_LONG).show();
                }
            }
        }
    };
}
